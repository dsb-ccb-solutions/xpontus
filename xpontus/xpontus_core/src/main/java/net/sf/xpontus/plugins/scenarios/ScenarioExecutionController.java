/*
 * ScenarioExecutionController.java
 *
 * Created on Aug 26, 2007, 9:34:46 AM
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
package net.sf.xpontus.plugins.scenarios;

import net.sf.xpontus.modules.gui.components.ScenarioExecutionView;
import net.sf.xpontus.utils.XPontusComponentsUtils;


/**
 * Controller to handle scenario profile execution
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class ScenarioExecutionController {
    public static final String CLOSE_METHOD = "closeWindow";
    public static final String EXECUTE_METHOD = "execute";
    private ScenarioExecutionView view;

    /**
     *
     * @param view
     */
    public ScenarioExecutionController(ScenarioExecutionView view) {
        this.view = view;
    }

    /**
     *
     */
    public void closeWindow() {
        view.setVisible(false);
    }

    /**
     *
     */
    public void execute() {
        if (view.getScenarioListModel().size() == 0) {
            XPontusComponentsUtils.showWarningMessage(
                "No transformation profile to run");
        } else if (view.getScenarioList().getSelectedIndex() == -1) {
            XPontusComponentsUtils.showWarningMessage(
                "Please select a transformation profile to run");
        } else {
            XPontusComponentsUtils.showInformationMessage("Not implemented");
        }
    }
}
