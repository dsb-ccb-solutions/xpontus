package net.sf.xpontus.syntax;

import java.awt.Graphics;
import java.awt.Graphics2D;

import java.util.List;

import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.StyleConstants;
import javax.swing.text.TabExpander;
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
public class SyntaxView extends PlainView {
    private final SyntaxDocument doc;

    public SyntaxView(Element elem) {
        super(elem);
        doc = (SyntaxDocument) getDocument();
    }

    /**
     * 
     * @param lineIndex
     * @param g
     * @param x
     * @param y
     */
    protected void drawLine(int lineIndex, Graphics g, int x, int y) {
        JTextComponent editor = (JTextComponent) getContainer();
        List tokens = doc.getTokenListForLine(lineIndex);
        float nextX = x;

        for (int i = 0; i < tokens.size(); i++) {
            Token token = (Token) tokens.get(i);
            nextX = paint(token, (Graphics2D) g, nextX, y, editor, this);
        }
    }

    /**
     * .
     * @param token
     * @param g
     * @param x
     * @param y
     * @param editor
     * @param e
     * @return
     */
    public float paint(Token token, Graphics2D g, float x, float y,
        JTextComponent editor, TabExpander e) {
        MutableAttributeSet style = doc.getStyleForType(token.kind);
        g.setColor(StyleConstants.getForeground(style));
        g.setFont(editor.getFont());

        Segment seg = new Segment(token.image.toCharArray(), 0,
                token.image.length());

        return Utilities.drawTabbedText(seg, (int) x, (int) y, g, e, 0);
    }
}
