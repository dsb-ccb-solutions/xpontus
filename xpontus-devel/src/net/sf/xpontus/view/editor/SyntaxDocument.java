/*
 * SyntaxDocument.java
 *
 * Created on December 22, 2006, 1:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.editor;

import net.sf.xpontus.utils.DynamicIntArray;
import net.sf.xpontus.view.editor.syntax.ILexer;
import net.sf.xpontus.view.editor.syntax.SyntaxSupport;
import net.sf.xpontus.view.editor.syntax.Token;
import net.sf.xpontus.view.editor.syntax.xml.XMLParser;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.text.SimpleAttributeSet;


/**
 *
 * @author Owner
 */
public class SyntaxDocument extends PlainDocument
{
    // the lexer for the document
    private ILexer lexer;
    private boolean isCodeCompletion = false;

    //element that represents the previous line postion of the cursor
    private DynamicIntArray endTokens;
    private Segment seg;

    //map of styles used to highlight text with
    private Map styleMap; //<TokenType, SimpleStyle>

    //last line postion of cursor - used to highlight - uses pkg access so SyntaxView can read it
    int selected = -1;

    //text component this document is for
    private JEditorPane editor;
    private final MutableAttributeSet DEFAULT_STYLE;
    private boolean xmlCompletion = false;
    
    public SyntaxDocument(JEditorPane editor, SyntaxSupport support)
    {
        this.styleMap = support.getColorProvider().getStyles();
        this.editor = editor;
        seg = new Segment();
        DEFAULT_STYLE = new SimpleAttributeSet();
        this.lexer = support.getLexer();
        isCodeCompletion = (lexer.getClass() == XMLParser.class);
        endTokens = new DynamicIntArray(500);
    }

    public ILexer getLexer(){
        return lexer;
    }
    
    public boolean isCodeCompletion(){
        return xmlCompletion;
    }
    
    public Map getStyleMap()
    {
        return styleMap;
    }

    protected void fireInsertUpdate(DocumentEvent e)
    {
        Element lineMap = getDefaultRootElement();
        DocumentEvent.ElementChange change = e.getChange(lineMap);
        Element[] added = (change == null) ? null : change.getChildrenAdded();

        int numLines = lineMap.getElementCount();
        int line = lineMap.getElementIndex(e.getOffset());
        int previousLine = line - 1;
        int previousTokenType = ((previousLine > -1)
            ? endTokens.get(previousLine) : 0);

        // If entire lines were added...
        if ((added != null) && (added.length > 0))
        {
            Element[] removed = change.getChildrenRemoved();
            int numRemoved = (removed != null) ? removed.length : 0;

            int endBefore = (line + added.length) - numRemoved;

            for (int i = line; i < endBefore; i++)
            {
                setSharedSegment(i); // Loads line i's text into s.

                int tokenType = lexer.getLastTokenTypeOnLine(seg,
                        previousTokenType);
                endTokens.add(i, tokenType);

                previousTokenType = tokenType;
            }

            // Update last tokens for lines below until they stop changing.
            updateLastTokensBelow(endBefore, numLines, previousTokenType);
        }

        // Otherwise, text was inserted on a single line...
        else
        {
            // Update last tokens for lines below until they stop changing.
            updateLastTokensBelow(line, numLines, previousTokenType);
        }

        super.fireInsertUpdate(e);
    }

    protected void fireRemoveUpdate(DocumentEvent chng)
    {
        Element lineMap = getDefaultRootElement();
        int numLines = lineMap.getElementCount();

        DocumentEvent.ElementChange change = chng.getChange(lineMap);
        Element[] removed = (change == null) ? null : change.getChildrenRemoved();

        // If entire lines were removed...
        if ((removed != null) && (removed.length > 0))
        {
            int line = change.getIndex(); // First line entirely removed.
            int previousLine = line - 1; // Line before that.
            int previousTokenType = ((previousLine > -1)
                ? endTokens.get(previousLine) : 0);

            Element[] added = change.getChildrenAdded();
            int numAdded = (added == null) ? 0 : added.length;

            // Remove the cached last-token values for the removed lines.
            int endBefore = (line + removed.length) - numAdded;

            endTokens.removeRange(line, endBefore); // Removing values for
                                                    // lines
                                                    // [line-(endBefore-1)].

            // Update last tokens for lines below until they've stopped changing.
            updateLastTokensBelow(line, numLines, previousTokenType);
        }

        // Otherwise, text was removed from just one line...
        else
        {
            int line = lineMap.getElementIndex(chng.getOffset());

            if (line >= endTokens.getSize())
            {
                return; // If we're editing the
                        // last line in a
                        // document...
            }

            int previousLine = line - 1;
            int previousTokenType = ((previousLine > -1)
                ? endTokens.get(previousLine) : 0);

            // Update last tokens for lines below until they've stopped changing.
            updateLastTokensBelow(line, numLines, previousTokenType);
        }

        super.fireRemoveUpdate(chng);
    }

    /**
     * Makes our private <code>Segment s</code> point to the text in our
     * document referenced by the specified element. Note that <code>line</code>
     * MUST be a valid line number in the document.
     *
     * @param line
     *          The line number you want to get.
     */
    private final void setSharedSegment(int line)
    {
        Element map = getDefaultRootElement();
        int numLines = map.getElementCount();

        Element element = map.getElement(line);

        if (element == null)
        {
            throw new InternalError("Invalid line number: " + line);
        }

        int startOffset = element.getStartOffset();
        int endOffset = ((line == (numLines - 1)) ? (element.getEndOffset() -
            1) : (element.getEndOffset() - 1));

        try
        {
            getText(startOffset, endOffset - startOffset, seg);
        }
        catch (BadLocationException ble)
        {
            throw new InternalError("Text range not in document: " +
                startOffset + "-" + endOffset);
        }
    }

    private int updateLastTokensBelow(int line, int numLines,
        int previousTokenType)
    {
        int firstLine = line;
        int end = numLines - 1;

        while (line < end)
        {
            setSharedSegment(line);

            int oldTokenType = endTokens.get(line);
            int newTokenType = lexer.getLastTokenTypeOnLine(seg,
                    previousTokenType);

            // If this line's end-token value didn't change, stop here. Note
            // that we're saying this line needs repainting; this is because
            // the beginning of this line did indeed change color, but the
            // end didn't.
            if (oldTokenType == newTokenType)
            {
                damageRange(firstLine, line);

                return line;
            }

            // If the line's end-token value did change, update it and
            // keep going.
            endTokens.set(line, newTokenType);
            previousTokenType = newTokenType;
            line++;
        }

        if (line > firstLine)
        {
            damageRange(firstLine, line);
        }

        return line;
    }

    private void damageRange(int firstLine, int lastLine)
    {
        Element f = getDefaultRootElement().getElement(firstLine);
        Element e = getDefaultRootElement().getElement(lastLine);
        editor.getUI().damageRange(editor, f.getStartOffset(), e.getEndOffset());
    }

    public final List getTokenListForLine(int line)
    {
        Element map = getDefaultRootElement();
        Element elem = map.getElement(line);
        int startOffset = elem.getStartOffset();
        int endOffset = elem.getEndOffset() - 1;

        try
        {
            getText(startOffset, endOffset - startOffset, seg);
        }
        catch (BadLocationException ble)
        {
            ble.printStackTrace();

            return null;
        }

        int initialTokenType = 0;

        if (line > 0)
        {
            initialTokenType = endTokens.get(line - 1);
        }

        return lexer.getTokens(seg, initialTokenType, startOffset);
    }

    public MutableAttributeSet getStyleForType(int kind)
    {
        Integer tokenId = new Integer(kind);
        Object tokenStyle = styleMap.get(tokenId);

        return (tokenStyle != null) ? (MutableAttributeSet) tokenStyle
                                    : DEFAULT_STYLE;
    }

    public void insertString(int off, String str, AttributeSet set)
        throws BadLocationException
    {
        if (str.equals(">") && isCodeCompletion)
        {
            int dot = editor.getCaret().getDot();

            StringBuffer endTag = new StringBuffer(str);

            String text = getText(0, off);
            int startTag = text.lastIndexOf('<', off);
            int prefEndTag = text.lastIndexOf('>', off);

            // If there was a start tag and if the start tag is not empty
            // and
            // if the start-tag has not got an end-tag already.
            if ((startTag > 0) && (startTag > prefEndTag) &&
                    (startTag < (text.length() - 1)))
            {
                String tag = text.substring(startTag, text.length());
                char first = tag.charAt(1);

                if ((first != '/') && (first != '!') && (first != '?') &&
                        !Character.isWhitespace(first))
                {
                    boolean finished = false;
                    char previous = tag.charAt(tag.length() - 1);

                    if ((previous != '/') && (previous != '-'))
                    {
                        endTag.append("</");

                        for (int i = 1; (i < tag.length()) && !finished; i++)
                        {
                            char ch = tag.charAt(i);

                            if (!Character.isWhitespace(ch))
                            {
                                endTag.append(ch);
                            }
                            else
                            {
                                finished = true;
                            }
                        }

                        endTag.append(">");
                    }
                }
            }

            str = endTag.toString();

            super.insertString(off, str, set);

            editor.getCaret().setDot(dot + 1);
        }
        else
        {
            super.insertString(off, str, set);
        }
    }

    // Tries to find out if the line finishes with an element start
    private boolean isStartElement(String line)
    {
        boolean result = false;

        int first = line.lastIndexOf("<");
        int last = line.lastIndexOf(">");

        if (last < first)
        { // In the Tag
            result = true;
        }
        else
        {
            int firstEnd = line.lastIndexOf("</");
            int lastEnd = line.lastIndexOf("/>");

            // Last Tag is not an End Tag
            if ((firstEnd != first) && ((lastEnd + 1) != last))
            {
                result = true;
            }
        }

        return result;
    }
}
