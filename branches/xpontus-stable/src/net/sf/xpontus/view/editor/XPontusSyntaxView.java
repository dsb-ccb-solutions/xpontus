// Copyright (c) 2000 BlueJ Group, Monash University
//
// This software is made available under the terms of the "MIT License"
// A copy of this license is included with this source distribution
// in "license.txt" and is also available at:
// http://www.opensource.org/licenses/mit-license.html
// Any queries should be directed to Michael Kolling: mik@mip.sdu.dk
package net.sf.xpontus.view.editor;

import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.Token;
import org.syntax.jedit.tokenmarker.TokenMarker;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainView;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;


/**
 * MoeSyntaxView.java - adapted from
 * SyntaxView.java - jEdit's own Swing view implementation
 * to add Syntax highlighting to the BlueJ programming environment.
 */

/**
 * A Swing view implementation that colorizes lines of a
 * SyntaxDocument using a TokenMarker.
 *
 * This class should not be used directly; a SyntaxEditorKit
 * should be used instead.
 *
 * @author Slava Pestov
 * @author Bruce Quig
 * @author Michael Kolling
 *
 * @version $Id: XPontusSyntaxView.java,v 1.1.1.1 2005/12/08 01:34:34 yveszoundi Exp $
 */
public abstract class XPontusSyntaxView extends PlainView {
    /**  width of tag area for setting breakpoints */
    public static final short TAG_WIDTH = 14;
    protected static final int BREAKPOINT_OFFSET = 0; //TAG_WIDTH + 2;
    private Segment line;
    private Font defaultFont;

    // protected FontMetrics metrics;  is inherited from PlainView
    private Font lineNumberFont;
    private Font smallLineNumberFont;
    FontMetrics lineNumberMetrics;
    private boolean initialised = false;

    /**
     * Creates a new XPontusSyntaxView.
     * @param elem The element
     */
    public XPontusSyntaxView(Element elem) {
        super(elem);
        line = new Segment();
    }

    /**
     * Paints the specified line.
     *
     * This method performs the following:
     *
     *  - Gets the token marker and color table from the current document,
     *    typecast to a SyntaxDocument.
     *  - Tokenizes the required line by calling the
     *    markTokens() method of the token marker.
     *  - Paints each token, obtaining the color by looking up the
     *    the Token.id value in the color table.
     *
     * If either the document doesn't implement
     * SyntaxDocument, or if the returned token marker is
     * null, the line will be painted with no colorization.
     *
     * Currently, we assume that the whole document uses the same font.
     * To support font changes, some of the code from "initilise" needs
     * to be here to be done repeatedly for each line.
     *
     * @param lineIndex The line number
     * @param g The graphics context
     * @param x The x co-ordinate where the line should be painted
     * @param y The y co-ordinate where the line should be painted
     */
    protected void drawLine(int lineIndex, Graphics g, int x, int y) {
        if (!initialised) {
            initialise(g);
        }

        SyntaxDocument document = (SyntaxDocument) getDocument();
        TokenMarker tokenMarker = document.getTokenMarker();

        Color def = getDefaultColor();

        try {
            Element lineElement = getElement()
                                      .getElement(lineIndex);
            int start = lineElement.getStartOffset();
            int end = lineElement.getEndOffset();

            document.getText(start, end - (start + 1), line);
            g.setColor(def);

            paintTaggedLine(line, lineIndex, g, x, y, document, tokenMarker,
                def, lineElement);
        } catch (BadLocationException bl) {
            // shouldn't happen
            bl.printStackTrace();
        }
    }

    /**
     * Draw a line for this view, including the tag mark.
     */
    public abstract void paintTaggedLine(Segment line, int lineIndex,
        Graphics g, int x, int y, SyntaxDocument document,
        TokenMarker tokenMarker, Color def, Element lineElement);

    /**
     * Draw the line number in front of the line
     */

    //    protected void drawLineNumber(Graphics g, int lineNumber, int x, int y) {
    //        g.setColor(Color.darkGray);
    //        
    //        String number = Integer.toString(lineNumber);
    //        int stringWidth = lineNumberMetrics.stringWidth(number);
    ////        int xoffset = BREAKPOINT_OFFSET - stringWidth - 4;
    //        int xoffset = BREAKPOINT_OFFSET - stringWidth - 4;
    //        if(xoffset < -2)      // if it doesn't fit, shift one pixel over.
    //            xoffset++;
    //        
    //        if(xoffset < -2) {    // if it still doesn't fit...
    //            g.setFont(smallLineNumberFont);
    //            g.drawString(number, x-3, y);
    //        } else {
    //            g.setFont(lineNumberFont);
    //            g.drawString(number, x + xoffset, y);
    //        }
    //        g.setFont(defaultFont);
    //    }
    protected void paintSyntaxLine(Segment line, int lineIndex, int x, int y,
        Graphics g, SyntaxDocument document, TokenMarker tokenMarker, Color def) {
        Color[] colors = document.getColors();
        Token tokens = tokenMarker.markTokens(line, lineIndex);
        int offset = 0;

        for (;;) {
            byte id = tokens.id;

            if (id == Token.END) {
                break;
            }

            int length = tokens.length;
            Color color;

            if (id == Token.NULL) {
                color = def;
            } else {
                // check we are within the array bounds
                // safeguard for updated syntax package
                if (id < colors.length) {
                    color = colors[id];
                } else {
                    color = def;
                }
            }

            g.setColor((color == null) ? def : color);

            line.count = length;
            x = Utilities.drawTabbedText(line, x, y, g, this, offset);
            line.offset += length;
            offset += length;

            tokens = tokens.next;
        }
    }

    /**
     * Check whether a given line is tagged with a given tag.
     * @param line The line to check
     * @param tag  The name of the tag
     * @return     True, if the tag is set
     */
    protected final boolean hasTag(Element line, String tag) {
        return Boolean.TRUE.equals(line.getAttributes().getAttribute(tag));
    }

    /**
     * Initialise some fields after we get a graphics context for the first time
     */
    private void initialise(Graphics g) {
        defaultFont = g.getFont();
        lineNumberFont = defaultFont.deriveFont(9.0f);
        smallLineNumberFont = defaultFont.deriveFont(7.0f);

        Component c = getContainer();
        lineNumberMetrics = c.getFontMetrics(lineNumberFont);
        initialised = true;
    }

    /**
     * Return default foreground colour
     */
    protected Color getDefaultColor() {
        return getContainer().getForeground();
    }

    /**
     * redefined from PlainView private method to allow for redefinition of
     * modelToView method
     */

    //    public Rectangle lineToRect(Shape a, int line) {
    //        Rectangle r = null;
    //        if (metrics != null) {
    //            Rectangle alloc = a.getBounds();
    //            r = new Rectangle(alloc.x, alloc.y + (line * metrics.getHeight()),
    //                    alloc.width, metrics.getHeight());
    //        }
    //        return r;
    //    }
}
