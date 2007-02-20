/*
 * CloseListenerImpl.java
 *
 * Created on February 18, 2007, 10:04 PM
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

import java.awt.event.MouseEvent;
import net.sf.xpontus.controller.actions.SaveAction;
import net.sf.xpontus.view.XPontusWindow;

/**
 *
 * @author Yves Zoundi
 */
public class CloseListenerImpl implements CloseListener{
    
    /** Creates a new instance of CloseListenerImpl */
    public CloseListenerImpl() {
    }
    
    public void closeOperation(MouseEvent e, int overTabIndex) {
          int index = overTabIndex;
      SaveAction  action = (SaveAction) XPontusWindow.getInstance().getApplicationContext()
                                           .getBean("action.save");

        try {
            action.save(index, true);
        } catch (Exception er) {
            er.printStackTrace();
        }

        XPontusWindow.getInstance().getPane().remove(index);
    }

}
