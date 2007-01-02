package net.sf.xpontus.view.editor;

import net.sf.xpontus.view.editor.syntax.CharStream;
import net.sf.xpontus.view.editor.syntax.FastCharStream;
import net.sf.xpontus.view.editor.syntax.StyleInfo;
import net.sf.xpontus.view.editor.syntax.SyntaxSupport;
import net.sf.xpontus.view.editor.syntax.Token;
import net.sf.xpontus.view.editor.syntax.TokenMgrError;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import java.io.Reader;
import java.io.StringReader;

import java.util.Map;

import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;


/**
 * A Swing view implementation that colorizes lines of a SyntaxDocument using a
 * TokenMarker.
 *
 * This class should not be used directly; a SyntaxEditorKit should be used
 * instead.
 *
 *
 * @author Yves Zoundi
 *
 * @version $Id: SyntaxView.java,v 1.1.1.1 2005/12/08 01:34:34 yveszoundi Exp $
 */
public class SyntaxView extends PlainView
  {
    private Segment line;
    private Segment seg = new Segment();
    private ILexer lexer;
    private Color defaultColor = Color.BLACK;
    private Map styleMap;

    /**
     * @param e
     * @param provider
     * @param lexer
     */
    public SyntaxView(Element e, SyntaxSupport syntaxSupport)
      {
        super(e);
        line = new Segment();
        this.styleMap = syntaxSupport.getColorProvider().getStyles();
        this.lexer = syntaxSupport.getLexer();
      }

    /*
     * (non-Javadoc)
     *
     * @see javax.swing.text.PlainView#drawLine(int, java.awt.Graphics, int,
     *      int)
     */
    protected void drawLine(int lineIndex, Graphics g, int x, int y)
      {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.BLACK);

        try
          {
            Element lineElement = getElement()
                                      .getElement(lineIndex);

            int start = lineElement.getStartOffset();

            //int end = lineElement.getEndOffset();
            int end = getElement().getElement(lineIndex).getEndOffset();
            getDocument().getText(start, end - (start + 1), line);

            Reader reader = new StringReader(line.toString());

            CharStream is = new FastCharStream(reader);
            lexer.ReInit(is);

            int last = start;
            int realStart;
            int thisStart;
            boolean gotMatch = false;

            Token token = null;

            while (((token = lexer.getNextToken()) != null) &&
                    (token.kind != 0))
              {
                int tokenId = token.kind;

                gotMatch = true;

                int tokenSize = token.image.length();
                realStart = token.beginColumn + start;

                if (last < realStart)
                  {
                    g2d.setColor(defaultColor);
                    g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN));

                    int middle = Math.abs(realStart - last);
                    getDocument().getText(last, middle, seg);
                    x = Utilities.drawTabbedText(seg, x, y, g2d, this, 0);
                  }

                setFont(g2d, tokenId);

                /* get segement of text for lexer match */
                getDocument().getText(realStart, tokenSize, seg);

                thisStart = x;

                x = Utilities.drawTabbedText(seg, x, y, g2d, this, 0);

                // draw underlines if needed
                if (styleMap.containsKey(new Integer(tokenId)))
                  {
                    StyleInfo m_style;
                    m_style = (StyleInfo) styleMap.get(new Integer(tokenId));

                    if (m_style.underlined)
                      {
                        g2d.drawLine(thisStart, y + 2, x, y + 2);
                      }
                  }

                last = realStart + tokenSize;
              }

            g2d.setColor(defaultColor);
            g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN));

            if ((last < end) && gotMatch)
              {
                // draw text from end of last match to end of segment
                getDocument().getText(last, end - last, seg);
                x = Utilities.drawTabbedText(seg, x, y, g2d, this, 0);
              }
            else if (!gotMatch)
              {
                // no match - draw whole segment
                Utilities.drawTabbedText(line, x, y, g2d, this, 0);
              }
          }
        catch (TokenMgrError e)
          {
            //            Utilities.drawTabbedText(seg, x, y, g2d, this, 0);
          }
        catch (Exception e)
          {
          }
      }

    /**
     * @param g2d
     * @param val
     */
    private void setFont(Graphics2D g2d, int val)
      {
        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN));

        if (styleMap.containsKey(new Integer(val)))
          {
            StyleInfo style = (StyleInfo) styleMap.get(new Integer(val));
            g2d.setColor(style.color);

            if (style.bold && style.italic)
              {
                g2d.setFont(g2d.getFont().deriveFont(Font.BOLD | Font.ITALIC));
              }
            else if (style.bold)
              {
                g2d.setFont(g2d.getFont().deriveFont(Font.BOLD));
              }
            else if (style.italic)
              {
                g2d.setFont(g2d.getFont().deriveFont(Font.ITALIC));
              }
          }
        else
          {
            g2d.setColor(defaultColor);
          }
      }
  }
