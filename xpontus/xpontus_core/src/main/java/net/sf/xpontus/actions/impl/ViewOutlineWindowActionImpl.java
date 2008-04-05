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

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.OutlineViewDockable;


/**
 * Show or hide the outline window
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class ViewOutlineWindowActionImpl extends AbstractXPontusActionImpl {
    public static final String BEAN_ALIAS = "action.viewoutlinewindow";

    public void execute() {
        OutlineViewDockable dockable = DefaultXPontusWindowImpl.getInstance()
                                                               .getOutline();

        DockingDesktop desktop = DefaultXPontusWindowImpl.getInstance()
                                                         .getDesktop();

        int state = dockable.getDockKey().getDockableState();

        Dockable rightDockable = DefaultXPontusWindowImpl.getInstance()
                                                         .getDocumentTabContainer()
                                                         .getCurrentDockable();

        if (rightDockable == null) {
            rightDockable = DefaultXPontusWindowImpl.getInstance()
                                                    .getDefaultPane();
        }

        if (state == DockableState.STATE_CLOSED) {
            setName("Hide outline");

            if (rightDockable.getDockKey().getDockableState() == DockableState.STATE_MAXIMIZED) {
                desktop.restore(rightDockable);
            }

            desktop.split(rightDockable, dockable, DockingConstants.SPLIT_LEFT);
            dockable.getDockKey().setDockableState(DockableState.STATE_DOCKED);
        } else {
            setName("Show outline");
            desktop.close(dockable);
            dockable.getDockKey().setDockableState(DockableState.STATE_CLOSED);
        }

        desktop.revalidate();
        desktop.repaint();
    }
}
