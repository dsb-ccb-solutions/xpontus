/*
 * DoubleClickListenerImpl.java
 *
 * Created on February 18, 2007, 10:09 PM
 *
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.view.tabbedpane;

import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;

import net.sf.xpontus.view.XPontusWindow;

import java.awt.event.MouseEvent;


/**
 *
 * @author Yves Zoundi
 */
public class DoubleClickListenerImpl implements DoubleClickListener {
    /** Creates a new instance of DoubleClickListenerImpl */
    public DoubleClickListenerImpl() {
    }

    public void doubleClickOperation(MouseEvent e) {
        DockingDesktop desktop = XPontusWindow.getInstance().getDesktop();

        XPontusWindow.DockablePaneForm paneForm = (XPontusWindow.DockablePaneForm) XPontusWindow.getInstance()
                                                                                                .getPane();

        int state = paneForm.getDockKey().getDockableState();

        if (state == DockableState.STATE_MAXIMIZED) {
            desktop.restore(paneForm);
        } else {
            desktop.maximize(paneForm);
        }
    }
}
