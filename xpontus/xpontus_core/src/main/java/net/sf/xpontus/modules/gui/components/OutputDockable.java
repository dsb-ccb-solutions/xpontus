/*
 * OutputDockable.java
 *
 * Created on July 1, 2007, 12:24 PM
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
 */
package net.sf.xpontus.modules.gui.components;

import com.vlsolutions.swing.docking.Dockable;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 *
 * @author Yves Zoundi
 */
public abstract class OutputDockable implements Dockable {
    public static final int BLUE_STYLE = 1;
    public static final int RED_STYLE = 2;
    public static final int BLACK_STYLE = 0;
    private MutableAttributeSet redStyle;
    private MutableAttributeSet blackStyle;
    private MutableAttributeSet blueStyle;

    public OutputDockable() {
        redStyle = new SimpleAttributeSet();
        blackStyle = new SimpleAttributeSet();
        blueStyle = new SimpleAttributeSet();

        StyleConstants.setForeground(redStyle, Color.RED);
        StyleConstants.setForeground(blueStyle, Color.BLUE);
        StyleConstants.setForeground(blackStyle, Color.BLACK);
    }
    
    public abstract String getId();

    public MutableAttributeSet getBlackStyle() {
        return blackStyle;
    }

    public void setBlackStyle(MutableAttributeSet blackStyle) {
        this.blackStyle = blackStyle;
    }

    public MutableAttributeSet getBlueStyle() {
        return blueStyle;
    }

    public void setBlueStyle(MutableAttributeSet blueStyle) {
        this.blueStyle = blueStyle;
    }

    public MutableAttributeSet getRedStyle() {
        return redStyle;
    }

    public void setRedStyle(MutableAttributeSet redStyle) {
        this.redStyle = redStyle;
    }

    public abstract void println(final String message);

    public abstract void println(final String message, final int style);
}
