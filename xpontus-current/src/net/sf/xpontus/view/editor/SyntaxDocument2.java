/*
 * SyntaxDocument2.java
 *
 * Created on December 29, 2006, 5:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.editor;

import java.awt.EventQueue;
import java.io.Reader;
import net.sf.xpontus.view.editor.syntax.*;
import net.sf.xpontus.view.editor.syntax.CharStream;
import net.sf.xpontus.view.editor.syntax.FastCharStream;
import net.sf.xpontus.view.editor.syntax.TokenInfo;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;


/**
 *
 * @author Yves Zoundi
 */
public class SyntaxDocument2 extends PlainDocument {
   private int lastMatch;
    private ILexer lexer;
    private Segment seg = new Segment();
    private Map colorMap;
    private JEditorPane editor;
    private List errors;

    /**
     * 
     * @param editor 
     * @param syntaxSupport 
     */
    public SyntaxDocument2(JEditorPane editor, SyntaxSupport syntaxSupport)
    {
        this.colorMap = syntaxSupport.getColorProvider().getStyles();
        this.editor = editor;
        this.lexer = syntaxSupport.getLexer();
    }

    /**
     * 
     * @return 
     */
    public Map getStyles()
    {
        return colorMap;
    }

    /**
     * 
     * @param e 
     */
    protected void fireRemoveUpdate(final DocumentEvent e)
    {
        super.fireRemoveUpdate(e);
        doLex();
    }

    /**
     * 
     * @param e 
     */
    protected void fireInsertUpdate(DocumentEvent e)
    {
        super.fireInsertUpdate(e);
        doLex();
    }

    private void doLex()
    {
        try
        {
            lex(0, getLength());
        }
        catch (Exception e1)
        {
            e1.printStackTrace();
        }
        catch(Error err){
            
        }

        EventQueue.invokeLater(new Runnable()
            {
                public void run()
                {
                    editor.repaint();
                }
            });
    }

    public void lex(int startOffs, int endOffs) throws Exception, Error
    {
        List  elems = getLineElements(startOffs - 1, endOffs);

        if (elems.isEmpty())
        {
            return;
        }

        clearAtts(elems);
        prepareForLex(Math.max(0, startOffs - 1), endOffs);

        Iterator  it = elems.iterator();
        Element elem = (Element) it.next();
        Token val = null;
        TokenInfo token = null;

        while ( (val = lexer.getNextToken())!=null && val.kind!=0)
        {
            int realOffs = val.beginColumn + startOffs;

            token = new TokenInfo(val, startOffs);
            
            // val.offset+=realOffs;
            if (lastMatch != realOffs)
            {
                System.out.println("lastMatch:" + lastMatch + ",realOffs" +
                    realOffs);

                TokenInfo _default = new TokenInfo();
                //                int getstart = 0;
                //                int getend = 0;

                //                if (lastMatch > realOffs)
                //                {
                //                    getstart = realOffs;
                //                    getend = lastMatch;
                //                }
                //                else
                //                {
                //                    getstart = lastMatch;
                //                    getend = realOffs;
                //                }
                //
                _default.text = getText(lastMatch, realOffs - lastMatch);
                //                               token.
                //fill gap between matches
               
                elem = fillGaps(it, elem, lastMatch, realOffs, _default);
            }

            // ensure im on the correct line , should only be behind one if not
            Element tmp = getDefaultRootElement()
                              .getElement(getDefaultRootElement()
                                              .getElementIndex(realOffs));

            if (elem != tmp)
            {
                elem = (Element) it.next();
            }

            //fill current match
            lastMatch = realOffs + token.size;
            elem = fillGaps(it, elem, realOffs, lastMatch, token);
        }

        // fill from last macth to end
        if (endOffs != lastMatch)
        {
           TokenInfo _default = new TokenInfo();
            //
            //            int getstart = 0;
            //            int getend = 0;

            //            if (lastMatch > endOffs)
            //            {
            //                getstart = endOffs;
            //                getend = lastMatch;
            //            }
            //            else
            //            {
            //                getstart = lastMatch;
            //                getend = endOffs;
            //            }
            _default.text = getText(lastMatch, endOffs - lastMatch);
            
            
            fillGaps(it, elem, lastMatch, endOffs, _default);
            
            System.out.println("lastMatch:" + lastMatch + ",endOffs:" +
                endOffs);

            //			fillGaps(it, elem, lastMatch, endOffs, val);
        }
    }

    private void clearAtts(List  elems)
    {
        final int nbLines = elems.size();
        
        for(int i=0;i<nbLines;i++)
        {
            Element elem = (Element)elems.get(i);
            MutableAttributeSet mas = (MutableAttributeSet) elem.getAttributes();
            mas.removeAttributes(mas);
        }
    }

    private void prepareForLex(int startOffs, int endOffs)
    {
        try
        {
            getText(startOffs, endOffs - startOffs, seg);
            
            Reader reader = new StringReader(seg.toString());
            
            CharStream cs = new FastCharStream(reader);
           
            lexer.ReInit(cs);
            
            lastMatch = startOffs;
        }
        catch (BadLocationException e)
        {
            e.printStackTrace();
        }
    }
    
       private Element fillGaps(Iterator it, Element line, int startOffs,
        int endOffs, TokenInfo fillType)
    {
        while (startOffs < endOffs)
        {
            int end = Math.min(endOffs, line.getEndOffset());
            MutableAttributeSet mas1 = (MutableAttributeSet) line.getAttributes();
           
            System.out.println("offset:" + fillType.start);
            mas1.addAttribute(String.valueOf(mas1.getAttributeCount()), fillType);

            if (end < endOffs)
            {
                try
                {
                    line = (Element) it.next();
                }
                catch (Exception e)
                {
                    e.printStackTrace();

                    //					System.out.println("Token offs + len = " + (token.offset + token.len) + " endOffs = " + endOffs);
                }
            }

            startOffs = end;
        }

        return line;
    }

    private List  getLineElements(int startOffs, int endOffs)
    {
        List returnElements = new ArrayList ();
        Element root = getDefaultRootElement();

        try
        {
            int index;
            Element elem;

            for (int i = startOffs; i < endOffs; i++)
            {
                index = root.getElementIndex(i);
                elem = root.getElement(index);
                i = elem.getEndOffset() - 1; // see javadoc for that pos
                returnElements.add(elem);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return returnElements;
    }
    
    
}
