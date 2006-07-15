// Copyright (c) 2000 BlueJ Group, Monash University
//
// This software is made available under the terms of the "MIT License"
// A copy of this license is included with this source distribution
// in "license.txt" and is also available at:
// http://www.opensource.org/licenses/mit-license.html
// Any queries should be directed to Michael Kolling: mik@mip.sdu.dk
package net.sf.xpontus.view.editor;

import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.TokenMarker;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.Utilities;


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
 * @version $Id: XPontusEditorView.java,v 1.1.1.1 2005/12/08 01:34:34 yveszoundi Exp $
 */
public class XPontusEditorView extends XPontusSyntaxView {
    // Attributes for lines and document
    public static final String BREAKPOINT = "break";
    public static final String STEPMARK = "step";

    /**
     * Creates a new XPontusEditorView for painting the specified element.
     * @param elem The element
     */
    public XPontusEditorView(Element elem) {
        super(elem);
    }

    /**
     * Draw a line for the moe editor.
     */
    public void paintTaggedLine(Segment lineText, int lineIndex, Graphics g,
        int x, int y, SyntaxDocument document, TokenMarker tokenMarker,
        Color def, Element line) {
        if (tokenMarker == null) {
            Utilities.drawTabbedText(lineText, x + BREAKPOINT_OFFSET, y, g,
                this, 0);
        } else {
            paintSyntaxLine(lineText, lineIndex, x + BREAKPOINT_OFFSET, y, g,
                document, tokenMarker, def);
        }
    }
}
