/*
 * ImageButton.java
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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
 *
 */
package net.sf.xpontus.modules.gui.components;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JButton;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class ImageButton extends JButton
{
    private static final long serialVersionUID = 3962743587853548916L;
    Dimension fixedSize = null;

    /**
     * @param icon
     */
    public ImageButton(Icon icon)
    {
        super(icon);

        if (icon != null)
        {
            fixedSize = new Dimension(icon.getIconWidth(), icon.getIconHeight());
        }

        setContentAreaFilled(false);
        setBorderPainted(false);
        setFocusable(false);
        setOpaque(false);
        setMargin(new Insets(0, 0, 0, 0));
        setBorder(BorderFactory.createEmptyBorder());
    }

    /**
     * @param action
     */
    public ImageButton(Action action)
    {
        super(action);

        Icon icon = getIcon();

        if (icon != null)
        {
            fixedSize = new Dimension(icon.getIconWidth(), icon.getIconHeight());
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#getMinimumSize()
     */
    public Dimension getMinimumSize()
    {
        if (fixedSize == null)
        {
            return super.getMinimumSize();
        }
        else
        {
            return fixedSize;
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#getMaximumSize()
     */
    public Dimension getMaximumSize()
    {
        if (fixedSize == null)
        {
            return super.getMaximumSize();
        }
        else
        {
            return fixedSize;
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.JComponent#getPreferredSize()
     */
    public Dimension getPreferredSize()
    {
        if (fixedSize == null)
        {
            return super.getPreferredSize();
        }
        else
        {
            return fixedSize;
        }
    }
}
