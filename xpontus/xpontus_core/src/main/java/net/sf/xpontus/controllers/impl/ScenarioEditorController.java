/*
 *
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
 */
package net.sf.xpontus.controllers.impl;

import net.sf.xpontus.model.ScenarioModel;
import net.sf.xpontus.modules.gui.components.ScenarioEditorView;
import net.sf.xpontus.modules.gui.components.ScenarioManagerView;
import net.sf.xpontus.plugins.scenarios.ParameterModel;
import net.sf.xpontus.plugins.scenarios.ParameterModelEditor;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.springframework.beans.BeanUtils;

import java.awt.Component;


import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;


/**
 * Controller class for the user transformation editor dialog
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.1
 */
public class ScenarioEditorController {
    /**
     *
     */
    public static final String CLOSE_WINDOW_METHOD = "closeWindow";

    /**
     *
     */
    public static final String ADD_PARAMETER_METHOD = "addParameter";

    /**
    *
    */
    public static final String EDIT_PARAMETER_METHOD = "editParameter";

    /**
     *
     */
    public static final String REMOVE_PARAMETER_METHOD = "removeParameter";

    /**
     *
     */
    public static final String INPUT_METHOD = "selectInput";

    /**
     *
     */
    public static final String OUTPUT_METHOD = "selectOutput";

    /**
     *
     */
    public static final String SCRIPT_METHOD = "selectScript";

    /**
     *
     */
    public static final String SAVE_METHOD = "updateScenario";
    private ScenarioEditorView view;
    private JFileChooser chooser;

    /**
     *
     * @param view
     */
    public ScenarioEditorController(ScenarioEditorView view) {
        this.view = view;
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    private boolean existsScenario(String name) {
        ScenarioManagerView m_parent = (ScenarioManagerView) view.getParent();
        DefaultComboBoxModel dcm = (DefaultComboBoxModel) m_parent.getScenariosList()
                                                                  .getModel();

        for (int i = 0; i < dcm.getSize(); i++) {
            if (dcm.getElementAt(i).toString().equalsIgnoreCase(name)) {
                return true;
            }
        }

        return false;
    }

    /**
     *
     */
    public void selectInput() {
        Component c = XPontusComponentsUtils.getTopComponent()
                                            .getDisplayComponent();

        int rep = chooser.showOpenDialog(c);

        if (rep == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            view.getModel().setInput(path);
        }
    }

    /**
     *
     */
    public void selectOutput() {
        Component c = XPontusComponentsUtils.getTopComponent()
                                            .getDisplayComponent();

        int rep = chooser.showOpenDialog(c);

        if (rep == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            view.getModel().setOutput(path);
        }
    }

    /**
     *
     */
    public void selectScript() {
        Component c = XPontusComponentsUtils.getTopComponent()
                                            .getDisplayComponent();

        int rep = chooser.showOpenDialog(c);

        if (rep == JFileChooser.APPROVE_OPTION) {
            String path = chooser.getSelectedFile().getAbsolutePath();
            view.getModel().setXsl(path);
        }
    }

    /**
     *
     */
    public void updateScenario() {
        boolean notNew = false;

        // the user transformation profile
        ScenarioModel model = view.getModel();

        // validate the model

        // if the model is new check if the transformation profile already exist for that name

        // save the scenario
        ScenarioManagerView parent = (ScenarioManagerView) view.getParent();

        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) parent.getScenariosList()
                                                                 .getModel();

        if (notNew) {
            // we are editing a transformation profile
            // if name different than original check if it exists
            if (model.getName() != null) {
                if(true){
                    return;
                }
            }

            ScenarioModel editingModel = (ScenarioModel) dcbm.getSelectedItem();
            BeanUtils.copyProperties(model, editingModel);
        } else {
            // the scenario is new
            
            // check if there is already a scenario with that name
            if(existsScenario(model.getName())){
                XPontusComponentsUtils.showErrorMessage("A scenario with that name already exists.");
                return;
            }
            dcbm.addElement(model);
        }

        view.setVisible(false);
    }

    /**
     *
     * @return
     */
    public ScenarioEditorView getView() {
        return view;
    }

    /**
     *
     * @param view
     */
    public void setView(ScenarioEditorView view) {
        this.view = view;
    }

    /**
     *
     */
    public void addParameter() {
        ParameterModel pm = new ParameterModel();
        ParameterModelEditor editor = new ParameterModelEditor(view, pm);

        if (editor.getButtonPressed() == ParameterModelEditor.BUTTON_OK) {
            view.getParamModel().getParametersListModel().add(pm);
            view.getParamModel().getTableModel().fireTableDataChanged();
        }
    }

    public void editParameter() {
        ParameterModel pm = (ParameterModel) view.getParamModel()
                                                 .getParameterSelectionHolder()
                                                 .getValue();

        if (pm != null) {
            ParameterModelEditor editor = new ParameterModelEditor(view, pm);

            if (editor.getButtonPressed() == ParameterModelEditor.BUTTON_OK) {
                view.getParamModel().getTableModel().fireTableDataChanged();
            }
        } else {
            XPontusComponentsUtils.showErrorMessage("Please select a parameter");
        }
    }

    /**
     *
     */
    public void removeParameter() {
        ParameterModel pm = (ParameterModel) view.getParamModel()
                                                 .getParameterSelectionHolder()
                                                 .getValue();

        if (pm != null) {
            view.getParamModel().getParametersListModel().remove(pm);
            view.getParamModel().getTableModel().fireTableDataChanged();
        } else {
            XPontusComponentsUtils.showErrorMessage("Please select a parameter");
        }
    }

    /**
     *
     */
    public void closeWindow() {
        view.setVisible(false);
    }
}
