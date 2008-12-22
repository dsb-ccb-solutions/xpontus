/**
 * SyntaxView.java
 *
 * Created on 4-Aug-2007, 10:18:31 AM
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */
package net.sf.xpontus.syntax;

import java.awt.*;
import java.awt.font.TextAttribute;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.text.*;


/**
 * A Swing view implementation that colorizes lines of a SyntaxDocument using a
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
    private final SyntaxDocument doc;

    public SyntaxView(Element elem)
    {
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
    protected void drawLine(int lineIndex, Graphics g, int x, int y)
    {
        JTextComponent editor = (JTextComponent) getContainer(); 
        List<Token> tokens = doc.getTokenListForLine(lineIndex);
        float nextX = x;

        for (Token token : tokens)
        {
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
        JTextComponent editor, TabExpander e)
    {
        MutableAttributeSet style = doc.getStyleForType(token.kind);
        g.setColor(StyleConstants.getForeground(style));

        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.setFont(editor.getFont().deriveFont(getAttributes(style)));

        Segment seg = new Segment(token.image.toCharArray(), 0,
                token.image.length());

        return Utilities.drawTabbedText(seg, (int) x, (int) y, g, e, 0);
    }

    protected Map<TextAttribute, Object> getAttributes(AttributeSet attrSet)
    {
        Map<TextAttribute, Object> attrMap = new HashMap<TextAttribute, Object>();

        if (attrSet.isDefined(StyleConstants.FontFamily))
        {
            attrMap.put(TextAttribute.FAMILY,
                StyleConstants.getFontFamily(attrSet));
        }

        if (attrSet.isDefined(StyleConstants.Bold))
        {
            attrMap.put(TextAttribute.WEIGHT,
                StyleConstants.isBold(attrSet) ? TextAttribute.WEIGHT_BOLD
                                               : TextAttribute.WEIGHT_REGULAR);
        }

        if (attrSet.isDefined(StyleConstants.Italic))
        {
            attrMap.put(TextAttribute.POSTURE,
                StyleConstants.isItalic(attrSet)
                ? TextAttribute.POSTURE_OBLIQUE : TextAttribute.POSTURE_REGULAR);
        }

        if (attrSet.isDefined(StyleConstants.Underline))
        {
            attrMap.put(TextAttribute.UNDERLINE,
                StyleConstants.isUnderline(attrSet)
                ? TextAttribute.UNDERLINE_ON : null);
        }

        if (attrSet.isDefined(StyleConstants.StrikeThrough))
        {
            attrMap.put(TextAttribute.STRIKETHROUGH,
                StyleConstants.isStrikeThrough(attrSet)
                ? TextAttribute.STRIKETHROUGH_ON : null);
        }

        if (attrSet.isDefined(StyleConstants.FontSize))
        {
            attrMap.put(TextAttribute.SIZE,
                new Float(StyleConstants.getFontSize(attrSet)));
        }

        if (attrSet.isDefined(StyleConstants.Foreground))
        {
            attrMap.put(TextAttribute.FOREGROUND,
                StyleConstants.getForeground(attrSet));
        }

        if (attrSet.isDefined(StyleConstants.Background))
        {
            attrMap.put(TextAttribute.BACKGROUND,
                StyleConstants.getBackground(attrSet));
        }

        if (attrSet.isDefined(StyleConstants.Superscript) &&
                !StyleConstants.isSubscript(attrSet))
        {
            attrMap.put(TextAttribute.SUPERSCRIPT,
                TextAttribute.SUPERSCRIPT_SUPER);
        }

        if (attrSet.isDefined(StyleConstants.Subscript) &&
                StyleConstants.isSubscript(attrSet))
        {
            attrMap.put(TextAttribute.SUPERSCRIPT, TextAttribute.SUPERSCRIPT_SUB);
        }

        return attrMap;
    }
}
