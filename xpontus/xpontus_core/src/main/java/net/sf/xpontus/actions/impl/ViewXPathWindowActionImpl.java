/*
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
 *
 */
package net.sf.xpontus.actions.impl;

import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingConstants;
import com.vlsolutions.swing.docking.DockingDesktop;

import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.evaluator.XPathResultsDockable;


/**
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class ViewXPathWindowActionImpl extends AbstractXPontusActionImpl
{
    private static final long serialVersionUID = -3629050624587366376L;
    public static final String BEAN_ALIAS = "action.viewxpathwindow";

    /**
     * @return
     */
    public Dockable getFirstDockedConsole()
    {
        ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                              .getConsole();
        Dockable dc = null;

        for (int i = 0; i < console.getDockables().size(); i++)
        {
            Dockable dockable = (Dockable) console.getDockables().get(i);
            int state = dockable.getDockKey().getDockableState();

            if (state == DockableState.STATE_DOCKED)
            {
                dc = dockable;

                break;
            }
        }

        return dc;
    }

    /* (non-Javadoc)
     * @see net.sf.xpontus.actions.XPontusActionIF#execute()
     */
    public void execute()
    {
        DockingDesktop desktop = DefaultXPontusWindowImpl.getInstance()
                                                         .getDesktop();

        ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                              .getConsole();

        XPathResultsDockable dockable = (XPathResultsDockable) console.getDockableById(XPathResultsDockable.DOCKABLE_ID);

        int state = dockable.getDockKey().getDockableState();

        Dockable rightDockable = DefaultXPontusWindowImpl.getInstance()
                                                         .getDocumentTabContainer()
                                                         .getCurrentDockable();

        if (rightDockable == null)
        {
            rightDockable = DefaultXPontusWindowImpl.getInstance()
                                                    .getDefaultPane();
        }

        if (state == DockableState.STATE_CLOSED)
        {
            setName("Hide XPath Window");

            if (rightDockable.getDockKey().getDockableState() == DockableState.STATE_MAXIMIZED)
            {
                desktop.restore(rightDockable);
            }

            Dockable firstDockable = getFirstDockedConsole();

            if (firstDockable == null)
            {
                desktop.split(rightDockable, dockable,
                    DockingConstants.SPLIT_BOTTOM);
            }
            else
            {
                desktop.createTab(firstDockable, dockable, 1);
            }
        }
        else
        {
            setName("Show XPath Window");
            desktop.close(dockable);
            dockable.getDockKey().setDockableState(DockableState.STATE_CLOSED);
        }

        desktop.revalidate();
        desktop.repaint();
    }
}
