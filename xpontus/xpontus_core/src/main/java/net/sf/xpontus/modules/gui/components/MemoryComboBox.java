/*
 * MemoryComboBox.java
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

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;


/**
 * A drop down list with history support
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class MemoryComboBox extends JComboBox
{
    /**
         *
         */
    private static final long serialVersionUID = -2709128554628262310L;
    public static int NO_MAX = -1;
    private int _maxMemoryCount = NO_MAX;

    /**
     *
     */
    public MemoryComboBox()
    {
        this(NO_MAX);
    }

    /**
     * @param maxMemoryCount
     */
    public MemoryComboBox(int maxMemoryCount)
    {
        super();
        setMaxMemoryCount(maxMemoryCount);
    }

    /**
     * @param value
     */
    public void setMaxMemoryCount(int value)
    {
        _maxMemoryCount = (value > NO_MAX) ? value : NO_MAX;
    }

    /* (non-Javadoc)
     * @see javax.swing.JComboBox#addItem(java.lang.Object)
     */
    public void addItem(Object item)
    {
        if (item != null)
        {
            if (!item.toString().trim().equals(""))
            {
                removeItem(item);
                insertItemAt(item, 0);
                setSelectedIndex(0);

                if ((_maxMemoryCount > NO_MAX) &&
                        (getItemCount() > _maxMemoryCount))
                {
                    removeItemAt(getItemCount() - 1);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.JComboBox#insertItemAt(java.lang.Object, int)
     */
    public void insertItemAt(Object anObject, int index)
    {
        super.insertItemAt(anObject, index);

        SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    while ((_maxMemoryCount > NO_MAX) &&
                            (getItemCount() > _maxMemoryCount))
                    {
                        removeItemAt(0);
                    }
                }
            });
    }
}
