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
package net.sf.xpontus.plugins.scenarios;

import net.sf.xpontus.modules.gui.components.ScenarioEditorView;
import net.sf.xpontus.modules.gui.components.ScenarioManagerView;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;


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

    /**
     * Method to delete a scenario
     */
    public void deleteScenario() {
        JList li = view.getScenariosList();

        int index = li.getSelectedIndex();

        if (index == -1) {
            XPontusComponentsUtils.showErrorMessage("Please select a scenario");

            return;
        }

        DefaultComboBoxModel listmodel = (DefaultComboBoxModel) li.getModel();

        listmodel.removeElement(listmodel.getElementAt(index));

        ScenarioListModel _m = new ScenarioListModel();

        List v = new Vector();

        for (int i = 0; i < listmodel.getSize(); i++) {
            v.add(listmodel.getElementAt(i));
        }

        _m.setScenarioList(v);
        _m.save();

        li.setSelectedIndex(li.getModel().getSize() - 1);
    }

    private void initScenarioEditor(ScenarioModel model) {
        if (child == null) {
            child = new ScenarioEditorView(view);
        }

        child.setModel(model);
        child.getController().resetParameters(model.getParameters());
    }

    private void showEditorDialog() {
        child.setLocationRelativeTo(view);
        child.setVisible(true);
    }

    /**
     * Method to add a new scenario
     *
     */
    public void addNewScenario() {
        ScenarioModel scm = new ScenarioModel();
        scm.setProcessor(ScenarioPluginsConfiguration.getInstance()
                                                     .getProcessorList().get(0)
                                                     .toString());
        System.out.println("Processor:" + scm.getProcessor());

        initScenarioEditor(scm);
        child.isnew = true;
        child.setTitle("Create a new transformation profile");
        showEditorDialog();
    }

    /**
     * Method to edit a scenario
     */
    public void editScenario() {
        int index = view.getScenariosList().getSelectedIndex();

        if (index == -1) {
            XPontusComponentsUtils.showErrorMessage("No scenario selected");

            return;
        }

        DetachableScenarioModel scm = (DetachableScenarioModel) view.getScenariosList()
                                                                    .getModel()
                                                                    .getElementAt(index);
        DetachableScenarioModelConverter dsmc = new DetachableScenarioModelConverter(scm);
        initScenarioEditor(dsmc.toScenarioModel());

        child.isnew = false;
        child.setTitle("Editing scenario : " + scm.getAlias());

        showEditorDialog();
    }

    public void closeWindow() {
        view.setVisible(false);
    }
}
