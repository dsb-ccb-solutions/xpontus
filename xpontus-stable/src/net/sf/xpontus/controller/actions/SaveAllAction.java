/*
 * SaveAllAction.java
 *
 * Created on 5 septembre 2005, 20:20
 *
 * Copyright (C) 2005 Yves Zoundi
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
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.view.PaneForm;
import net.sf.xpontus.view.XPontusWindow;


/**
 * Action to save all documents
 * @author Yves Zoundi
 */
public class SaveAllAction extends BaseAction {
    /** Creates a new instance of SaveAllAction */
    public SaveAllAction() {
    }

    public void execute() {
        // check files to save
        checkFilesToSave();
    }

    public void checkFilesToSave() {
        PaneForm pane = XPontusWindow.getInstance().getPane();
        final int openedFiles = pane.getTabCount();
        SaveAction action;
        action = (SaveAction) XPontusWindow.getInstance().getApplicationContext()
                                           .getBean("action.save");

        try {
            for (int i = 0; i < openedFiles; i++) {
                action.save(i, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
