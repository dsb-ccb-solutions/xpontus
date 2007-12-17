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

import net.sf.xpontus.modules.gui.components.ScenarioEditorView;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Component;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


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

    private boolean openedDialog() {
        boolean answer = false;

        Component c = XPontusComponentsUtils.getTopComponent()
                                            .getDisplayComponent();

        int rep = chooser.showOpenDialog(c);

        if (rep == JFileChooser.APPROVE_OPTION) {
            answer = true;
        }

        return answer;
    }

    /**
     * 
     */
    public void selectInput() {
        if (openedDialog()) {
            String path = chooser.getSelectedFile().getAbsolutePath();
        }
    }

    /**
     * 
     */
    public void selectOutput() {
        if (openedDialog()) {
            String path = chooser.getSelectedFile().getAbsolutePath();
        }
    }

    /**
     * 
     */
    public void selectScript() {
        if (openedDialog()) {
            String path = chooser.getSelectedFile().getAbsolutePath();
        }
    }

    /**
     * 
     */
    public void updateScenario() {
        XPontusComponentsUtils.showErrorMessage("not implemented");
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
        JTable table = view.getParamsTable();

        DefaultTableModel m = (DefaultTableModel) table.getModel();
        m.addRow(new Object[] { "", "" });
    }

    /**
     * 
     */
    public void removeParameter() {
        JTable table = view.getParamsTable();

        if (!table.getSelectionModel().isSelectionEmpty()) {
            int index = table.getSelectedRow();
            DefaultTableModel m = (DefaultTableModel) table.getModel();
            m.removeRow(index);
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
