package net.sf.xpontus.view.editor;

import net.sf.xpontus.view.editor.syntax.StyleInfo;
import net.sf.xpontus.view.editor.syntax.TokenInfo;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.text.AttributeSet;
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
public class SyntaxView2 extends PlainView {
    private Segment seg = new Segment();

    public SyntaxView2(Element elem) {
        super(elem);
    }

    protected void drawLine(int lineIndex, Graphics g, int x, int y) {
        try {
            SyntaxDocument2 doc = (SyntaxDocument2) getDocument();
            Graphics2D g2d = (Graphics2D) g;

            //g2d.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_LCD_HRGB);
            AttributeSet atts = doc.getDefaultRootElement().getElement(lineIndex)
                                   .getAttributes();
            
            
            System.out.println("they are :" + atts.getAttributeCount() + " tokens");

            for (int i = 0; i < atts.getAttributeCount(); i++) {
                TokenInfo token = (TokenInfo) atts.getAttribute(String.valueOf(
                            i));
                doc.getText(token.start, token.size, seg);
                setFont(g2d, token.kind);
                x = Utilities.drawTabbedText(seg, x, y, g, this, 0);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFont(Graphics2D g2d, int val) {
        Integer tokenId = new Integer(val);

        SyntaxDocument2 doc = (SyntaxDocument2) getDocument();

        g2d.setFont(g2d.getFont().deriveFont(Font.PLAIN));

        if (doc.getStyles().containsKey(tokenId)) {
            StyleInfo style = (StyleInfo) doc.getStyles().get(tokenId);
            g2d.setColor(style.color);

            if (style.bold && style.italic) {
                g2d.setFont(g2d.getFont().deriveFont(Font.BOLD | Font.ITALIC));
            } else if (style.bold) {
                g2d.setFont(g2d.getFont().deriveFont(Font.BOLD));
            } else if (style.italic) {
                g2d.setFont(g2d.getFont().deriveFont(Font.ITALIC));
            }
        } else {
            g2d.setColor(Color.BLACK);
        }
    }
}
