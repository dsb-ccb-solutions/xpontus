/*
 *
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
package net.sf.xpontus.actions.impl;

import com.vlsolutions.swing.toolbars.ToolBarContainer;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;

import java.awt.BorderLayout;

import javax.swing.JFrame;


/**
 * Action to show/hide the toolbar
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class ViewToolbarActionImpl extends AbstractXPontusActionImpl
{
    private static final long serialVersionUID = 6284202304212738387L;
    public static final String BEAN_ALIAS = "action.viewtoolbar";

    /* (non-Javadoc)
     * @see net.sf.xpontus.actions.XPontusActionIF#execute()
     */
    public void execute()
    {
        JFrame frame = (JFrame) DefaultXPontusWindowImpl.getInstance()
                                                        .getDisplayComponent();

        ToolBarContainer m_toolbar = DefaultXPontusWindowImpl.getInstance()
                                                             .getToolBar();

        if (m_toolbar.isShowing())
        {
            setName("Show toolbar");
            frame.getContentPane().remove(m_toolbar);
        }
        else
        {
            setName("Hide toolbar");
            frame.getContentPane().add(m_toolbar, BorderLayout.NORTH);
        }

        frame.validate();

        frame.repaint();
    }
}
