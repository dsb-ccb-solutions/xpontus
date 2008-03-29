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
package net.sf.xpontus.plugins.scenarios;

import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JTable;


/**
 * Controller class for the user transformation editor dialog
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.1
 */
public class ScenarioEditorController {
    /**
     * Method name to close the scenario editing dialog
     */
    public static final String CLOSE_WINDOW_METHOD = "closeWindow";

    /**
     * Method name to add a xslt parameter
     */
    public static final String ADD_PARAMETER_METHOD = "addParameter";

    /**
     * Method name to edit a xslt parameter
     */
    public static final String EDIT_PARAMETER_METHOD = "editParameter";

    /**
     * Method name to remove a xslt parameter
     */
    public static final String REMOVE_PARAMETER_METHOD = "removeParameter";

    /**
     * Method name to select an input file
     */
    public static final String INPUT_METHOD = "selectInput";

    /**
     * Method name to select an output file
     */
    public static final String OUTPUT_METHOD = "selectOutput";

    /**
     * Method name to select an xsl stylesheet
     */
    public static final String SCRIPT_METHOD = "selectScript";

    /**
     * Save a transformation profile to persistent storage
     */
    public static final String SAVE_METHOD = "updateScenario";
    private Log log = LogFactory.getLog(ScenarioEditorController.class);
    private ScenarioEditorView view;
    private JFileChooser chooser;

    /**
     * Create a controller for the specified scenario dialog
     * @param view The scenario editor dialog
     */
    public ScenarioEditorController(ScenarioEditorView view) {
        this.view = view;
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    // check if a scenario already exists with the specified name
    private boolean existsScenario(String name) {
        ScenarioManagerView m_parent = (ScenarioManagerView) view.getParent();

        DefaultComboBoxModel dcm = (DefaultComboBoxModel) m_parent.getScenariosList()
                                                                  .getModel();

        for (int i = 0; i < dcm.getSize(); i++) {
            if (dcm.getElementAt(i).toString().equalsIgnoreCase(name)) {
                XPontusComponentsUtils.showErrorMessage("The scenario exists");

                return true;
            }
        }

        return false;
    }

    /**
     * Select an input file
     */
    public void selectInput() {
        Component c = XPontusComponentsUtils.getTopComponent()
                                            .getDisplayComponent();

        int rep = chooser.showOpenDialog(c);

        if (rep == JFileChooser.APPROVE_OPTION) {
            try {
                String path = chooser.getSelectedFile().getAbsolutePath();
                view.getModel().setInput(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Select a output file
     */
    public void selectOutput() {
        initBrowser();

        Component c = XPontusComponentsUtils.getTopComponent()
                                            .getDisplayComponent();

        int rep = chooser.showOpenDialog(c);

        if (rep == JFileChooser.APPROVE_OPTION) {
            try {
                String path = chooser.getSelectedFile().getAbsolutePath();
                view.getModel().setOutput(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Update the parameters table list
     * @param params The XSLT parameters
     */
    public void resetParameters(java.util.Hashtable params) {
        JTable table = view.getParamsTable();
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel) table.getModel();
        int taille = model.getRowCount();

        for (int i = 0; i < taille; i++) {
            model.removeRow(i);
        }

        if (params.size() > 0) {
            java.util.Iterator it = params.keySet().iterator();

            while (it.hasNext()) {
                String cle = (String) it.next();
                String val = params.get(cle).toString();
                model.addRow(new Object[] { cle, val });
            }
        }

        table.revalidate();
        table.repaint();
    }

    /**
     * Select a script or a XSL stylesheet
     */
    public void selectScript() {
        Component c = XPontusComponentsUtils.getTopComponent()
                                            .getDisplayComponent();

        int rep = chooser.showOpenDialog(c);

        if (rep == JFileChooser.APPROVE_OPTION) {
            try {
                String path = chooser.getSelectedFile().getAbsolutePath();
                view.getModel().setXsl(path);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Returns the plugin to use for the XSLT transformation
     * @param proc The xslt processor to use
     * @return The plugin for the XSLT transformation
     */
    public ScenarioPluginIF getEngineForModel(String proc) {
        List engines = ScenarioPluginsConfiguration.getInstance().getEngines();

        for (int i = 0; i < engines.size(); i++) {
            ScenarioPluginIF plugin = (ScenarioPluginIF) engines.get(i);

            if (Arrays.asList(plugin.getProcessors()).contains(proc)) {
                return plugin;
            }
        }

        XPontusComponentsUtils.showErrorMessage(
            "No processor found for your configuration");

        return null;
    }

    /**
     *  Save a scenario to persistent storage
     */
    public void updateScenario() {
        boolean isvalid = true;

        view.getModel().setAlias(view.getScenarioName());

        if (view.isnew) {
            if (existsScenario(view.getScenarioName())) {
                return;
            }
        }

        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getParamsTable()
                                                          .getModel();

        java.util.Hashtable table = new java.util.Hashtable();

        for (int i = 0; i < model.getRowCount(); i++) {
            String cle = model.getValueAt(i, 0).toString();
            String value = model.getValueAt(i, 1).toString();

            if (table.contains(cle)) {
                isvalid = false;
            } else {
                table.put(cle, value);
            }
        }

        ScenarioManagerView parent = (ScenarioManagerView) view.getParent();

        view.getModel().setProcessor(view.getSelectedProcessor());

        ScenarioPluginIF plugin = getEngineForModel(view.getModel()
                                                        .getProcessor());

        if (plugin == null) {
            return;
        }

        DetachableScenarioModelConverter converter = new DetachableScenarioModelConverter();

        if (plugin.isValidModel(converter.toSimpleModel(view.getModel()), false) &&
                isvalid) {
            view.getModel().setParameters(table);

            JList li = parent.getScenariosList();

            javax.swing.DefaultComboBoxModel listmodel = (javax.swing.DefaultComboBoxModel) li.getModel();

            DetachableScenarioModelConverter dsmc = new DetachableScenarioModelConverter();

            if (view.isnew) {
                listmodel.addElement(dsmc.toSimpleModel(view.getModel()));

                li.setSelectedIndex(listmodel.getSize());

                ScenarioListModel _m = new ScenarioListModel();

                List v = new Vector();

                for (int i = 0; i < listmodel.getSize(); i++) {
                    v.add(listmodel.getElementAt(i));
                }

                _m.setScenarioList(v);
                _m.save();
            } else {
                int selected = li.getSelectedIndex();
                DetachableScenarioModel dsm = dsmc.toSimpleModel(view.getModel());

                Vector v = new Vector();

                for (int i = 0; i < listmodel.getSize(); i++) {
                    if (i == selected) {
                        v.add(dsm);
                    } else {
                        v.add(listmodel.getElementAt(i));
                    }
                }

                li.setModel(new DefaultComboBoxModel(v));
                li.revalidate();
                li.repaint();

                ScenarioListModel _m = new ScenarioListModel();
                _m.setScenarioList(v);
                _m.save();
            }

            view.setVisible(false);
        } else {
            XPontusComponentsUtils.showErrorMessage("The scenario is invalid");
        }
    }

    /**
     * Returns the scenario dialog editor
     * @return The scenario dialog editor
     */
    public ScenarioEditorView getView() {
        return view;
    }

    /**
     * Sets the scenario dialog editor
     * @param view The scenario dialog editor
     */
    public void setView(ScenarioEditorView view) {
        this.view = view;
    }

    /**
     * add an xslt parameter
     */
    public void addParameter() {
        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getParamsTable()
                                                          .getModel();
        model.addRow(new Object[] { "", "" });
    }

    /**
     * Edit an xslt parameter
     */
    public void editParameter() {
    }

    /**
     * Remove an xslt parameter
     */
    public void removeParameter() {
        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getParamsTable()
                                                          .getModel();

        if (view.getParamsTable().getSelectedRow() != -1) {
            model.removeRow(view.getParamsTable().getSelectedRow());
        }
    }

    /**
     * Close the editing dialog
     */
    public void closeWindow() {
        view.setVisible(false);
    }

    private void initBrowser() {
        if (chooser == null) {
            chooser = new JFileChooser();
            chooser.setDialogTitle("Select a file");
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }
    }
}
