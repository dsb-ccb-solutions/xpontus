/*
 * ScenarioManagerController.java
 *
 * Created on 19-Aug-2007, 9:17:05 AM
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
 */
package net.sf.xpontus.controllers.impl;

import net.sf.xpontus.modules.gui.components.ScenarioManagerView;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import net.sf.xpontus.modules.gui.components.ScenarioEditorView;


/**
 * Class to manage the scenario manager form
 * @author Yves Zoundi
 * @version 0.0.1
 */
public class ScenarioManagerController {
    public static final String NEW_SCENARIO_METHOD = "addNewScenario";
    public static final String CLOSE_WINDOW_METHOD = "closeWindow";
    public static final String EDIT_SCENARIO_METHOD = "editScenario";
    public static final String REMOVE_SCENARIO_METHOD = "deleteScenario";
    private ScenarioEditorView child;
    
    private ScenarioManagerView view;

    /**
     *
     * @param view
     */
    public ScenarioManagerController(ScenarioManagerView view) {
        setView(view);
    }

    public ScenarioManagerController() {
    }

    public ScenarioManagerView getView() {
        return view;
    }

    public void setView(ScenarioManagerView view) {
        this.view = view;
    }

    private void init(){
        if(child == null){
            child = new ScenarioEditorView(view, true);
        }
    }
    /**
     * Method to delete a scenario
     */
    public void deleteScenario() {
        JList li = view.getScenariosList();
        int[] indexes = li.getSelectedIndices();

        if (indexes.length == 0) {
            XPontusComponentsUtils.showErrorMessage("Please select a scenario");
        } else {
            DefaultComboBoxModel m = (DefaultComboBoxModel) li.getModel();
            List rm = new Vector();

            for (int i = 0; i < indexes.length; i++) {
                rm.add(m.getElementAt(i));
            }

            for (int i = 0; i < rm.size(); i++) {
                m.removeElement(m.getElementAt(i));
            }
        }
    }

    /**
     * Method to add a new scenario
     *
     */
    public void addNewScenario() {
       init();
       child.setLocationRelativeTo(view);
       child.setVisible(true);
    }

    /**
     * Method to edit a scenario
     */
    public void editScenario() {
        addNewScenario();
    }
    
    public void closeWindow(){
        view.setVisible(false);
    }
}
