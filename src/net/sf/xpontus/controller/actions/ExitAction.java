/*
 * ExitAction.java
 *
 * Created on 18 juillet 2005, 02:54
 *
 *  Copyright (C) 2005 Yves Zoundi
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
import net.sf.xpontus.model.options.GeneralOptionModel;
import net.sf.xpontus.view.XPontusWindow;

import javax.swing.JOptionPane;


/**
 * Action to exit the program
 * @author Yves Zoundi
 */
public class ExitAction extends BaseAction {
    /** Creates a new instance of ExitAction */
    public ExitAction() {
    }

    public void execute() {
        if (!XPontusWindow.getInstance().getPane().isEmpty()) {
            ((BaseAction) XPontusWindow.getInstance().getApplicationContext()
                                       .getBean("action.closetaball")).execute();
        }

        GeneralOptionModel model1 = (GeneralOptionModel) new GeneralOptionModel().load();

        if (model1 == null) {
            model1 = new GeneralOptionModel();
        }

        if (model1.isConfirmOnExitOption()) {
            int answer = JOptionPane.showConfirmDialog(XPontusWindow.getInstance()
                                                                    .getFrame(),
                    "Are you sure you want to quit?", "Exit the program",
                    JOptionPane.YES_NO_OPTION);

            if (answer == JOptionPane.YES_OPTION) {
                System.exit(0);
            }
        } else {
            System.exit(0);
        }
    }
}
