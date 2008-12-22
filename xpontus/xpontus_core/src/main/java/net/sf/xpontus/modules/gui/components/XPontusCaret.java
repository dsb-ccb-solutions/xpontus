/*
 * XPontusCaret.java
 *
 * Created on July 1, 2007, 7:03 PM
 *
 * Copyright (C) 2005-2007 Yves Zoundi <yveszoundi at users dot sf dot net>
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
 */
package net.sf.xpontus.modules.gui.components;

import net.sf.xpontus.configuration.XPontusConfig;

import java.awt.Color;
import java.awt.event.FocusEvent;

import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter.HighlightPainter;


/**
 * A caret which always blinks
 * @version 0.0.2
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPontusCaret extends DefaultCaret
{
    private static final long serialVersionUID = -4166779148286944701L;
    private HighlightPainter painter;

    /** Creates a new instance of XPontusCaret */
    public XPontusCaret()
    {
        this.setBlinkRate(Integer.valueOf(XPontusConfig.getValue(
                    "cursorBlinkRate").toString()));
        painter = new DefaultHighlightPainter(Color.LIGHT_GRAY);
    }

    /**
     *
     * @return
     */
    protected HighlightPainter getSelectionPainter()
    {
        return painter;
    }

    /* (non-Javadoc)
     * @see javax.swing.text.DefaultCaret#focusLost(java.awt.event.FocusEvent)
     */
    public void focusLost(FocusEvent evt)
    {
    }
}
