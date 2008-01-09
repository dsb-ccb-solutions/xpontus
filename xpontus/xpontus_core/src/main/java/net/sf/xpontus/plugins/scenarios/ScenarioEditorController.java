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

import net.sf.xpontus.modules.gui.components.ScenarioEditorView;
import net.sf.xpontus.modules.gui.components.ScenarioManagerView;
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
    private Log log = LogFactory.getLog(ScenarioEditorController.class);
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
                XPontusComponentsUtils.showErrorMessage("The scenario exists");

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
     * @param params
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
        
        System.out.println("nb params:" + model.getRowCount());
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

    public ScenarioPluginIF getEngineForModel(String proc) {
        System.out.println("Looking for engine for:" + proc);

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

        if (plugin.isValidModel(view.getModel(), false) && isvalid) {
            view.getModel().setParameters(table);

            JList li = parent.getScenariosList();

            javax.swing.DefaultComboBoxModel listmodel = (javax.swing.DefaultComboBoxModel) li.getModel();

            DetachableScenarioModelConverter dsmc = new DetachableScenarioModelConverter();

            if (view.isnew) {
                listmodel.addElement(dsmc.toSimpleModel(view.getModel()));
                //                listmodel = new javax.swing.DefaultComboBoxModel((Vector) parent.getVector());
                //                li.setModel(listmodel);
                //                li.revalidate();
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
     *
     */

    //    public void updateScenario() {
    //        ScenarioModel model = view.getModel();
    //
    //        ScenarioModel original = view.getOriginalModel();
    //
    //        String originalName = original.getAlias();
    //
    //	String modifiedName = model.getAlias();
    //
    //        ScenarioManagerView parent = (ScenarioManagerView) view.getParent();
    //
    //        DefaultComboBoxModel dcbm = (DefaultComboBoxModel) parent.getScenariosList().getModel();
    //
    //        // scenario validation ensure this
    //        boolean isNew = originalName.equals("");
    //
    //        ScenarioPluginIF plugin = getEngineForModel(model);
    //
    //        if (plugin == null) {
    //            return;
    //        }
    //
    //        // validate the scenario
    //        if (!plugin.isValidModel(model, false)) {
    //            log.info("Invalid model");
    //            return;
    //        }
    //
    //        // new scenario
    //        if (isNew) {
    //            if (!existsScenario(modifiedName)) {
    //                log.info("Adding new scenario");
    //                
    //                view.setVisible(false);
    //                dcbm.addElement(model);
    //            }
    //        } // updated scenario
    //        else {
    //            if (originalName.equals(modifiedName)) {
    //                log.info("Updating model with same name");
    //                ScenarioModel sm = (ScenarioModel) parent.getScenariosList().getSelectedValue();
    //                try{
    //                    BeanUtils.copyProperties(sm, model);
    //                }
    //                catch(Exception err){
    //                    err.printStackTrace();
    //                }
    //                view.setVisible(false);
    //                parent.getScenariosList().revalidate();
    //                parent.getScenariosList().repaint();
    //                
    //            } else {
    //                System.out.println("modified name:" + modifiedName);
    //                if (!existsScenario(modifiedName)) {
    //                    ScenarioModel sm = (ScenarioModel) parent.getScenariosList().getSelectedValue();
    //                    log.info("Updating model with a new name");
    //                    
    //                  try{
    //                    BeanUtils.copyProperties(sm, model);
    //                }
    //                catch(Exception err){
    //                    err.printStackTrace();
    //                }
    //                    view.setVisible(false);
    //                    parent.getScenariosList().revalidate();
    //                    parent.getScenariosList().repaint();
    //                    
    //                }
    //                else{
    //                    System.out.println("The scenario is not new and exists!");
    //                }
    //            }
    //        }
    //
    //
    //    }

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
        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getParamsTable()
                                                          .getModel();
        model.addRow(new Object[] { "", "" });
    }

    public void editParameter() {
        //        ParameterModel pm = (ParameterModel) view.getParamModel().getParameterSelectionHolder().getValue();
        //
        //        if (pm != null) {
        //            ParameterModelEditor editor = new ParameterModelEditor(view, pm);
        //
        //            if (editor.getButtonPressed() == ParameterModelEditor.BUTTON_OK) {
        //                view.getParamModel().getTableModel().fireTableDataChanged();
        //                int row = view.getParamsTable().getSelectedRow();
        //                view.getParamModel().getParameters().set(row, pm);
        //            }
        //        } else {
        //            XPontusComponentsUtils.showErrorMessage("Please select a parameter");
        //        }
    }

    /**
     *
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
     *
     */
    public void closeWindow() {
        view.setVisible(false);
    }
}
