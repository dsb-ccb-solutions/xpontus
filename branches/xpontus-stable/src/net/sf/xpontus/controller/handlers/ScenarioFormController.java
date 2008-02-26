/*
 * ScenarioFormController.java
 *
 *
 * Created on 1 août 2005, 17:46
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
package net.sf.xpontus.controller.handlers;

import net.sf.xpontus.core.controller.handlers.BaseController;
import net.sf.xpontus.model.ScenarioListModel;
import net.sf.xpontus.utils.MsgUtils;
import net.sf.xpontus.view.ScenarioFormView;
import net.sf.xpontus.view.ScenarioListView;
import net.sf.xpontus.view.XPontusWindow;


/**
 * Controller for the scenario form dialog
 * @author Yves Zoundi
 */
public class ScenarioFormController extends BaseController {
    private ScenarioListView parent;
    private javax.swing.JFileChooser chooser;
    private ScenarioFormView view;
    private MsgUtils _msg;

    /** Creates a new instance of ScenarioFormController
     * @param view the view of this controller
     */
    public ScenarioFormController(ScenarioFormView view) {
        setView(view);
        setParent(view.parent);
        _msg = MsgUtils.getInstance();
        chooser = new javax.swing.JFileChooser();
    }

    /**
     * set the view's parent of this controller
     * @param parent the view's parent of this controller
     */
    public void setParent(ScenarioListView parent) {
        this.parent = parent;
    }

    /**
     * select an output file
     */
    public void outputButton_Onclick() {
        int rep = chooser.showSaveDialog(view);

        if (rep == javax.swing.JFileChooser.APPROVE_OPTION) {
            view.getModel()
                .setOutputFile(chooser.getSelectedFile().getAbsolutePath());
            view.getOutputTF()
                .setText(chooser.getSelectedFile().toURI().toString());
        }
    }

    /**
     * add a parameter
     */
    public void add() {
        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getTable().getModel();
        model.addRow(new Object[] { "", "" });
    }

    /**
     * close the view of this controller
     */
    public void close() {
        view.setVisible(false);
    }

    /**
     * validate the scenario
     */
    public void validate() {
        boolean isvalid = true;

        if (view.isnew) {
            if (view.parent.scenarioExist(view.getScenarioName())) {
                javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                       .getFrame(),
                    _msg.getString("msg.scenarioExists"),
                    _msg.getString("msg.error"),
                    javax.swing.JOptionPane.ERROR_MESSAGE);

                return;
            }
        }

        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getTable().getModel();

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

        if (view.getModel().isValid() && isvalid) {
            view.getModel().setParams(table);

            javax.swing.DefaultComboBoxModel listmodel = (javax.swing.DefaultComboBoxModel) view.parent.getList()
                                                                                                       .getModel();

            if (view.isnew) {
                view.parent.getVector().addElement(view.getModel());
                listmodel = new javax.swing.DefaultComboBoxModel(view.parent.getVector());
                view.parent.getList().setModel(listmodel);
                view.parent.getList().revalidate();
                view.parent.getList().setSelectedIndex(listmodel.getSize());

                ScenarioListModel _m = new ScenarioListModel();
                _m.setScenarioList(view.parent.getVector());
                _m.save();
            } else {
                int selected = view.parent.getList().getSelectedIndex();
                view.parent.getVector().setElementAt(view.getModel(), selected);
                listmodel = new javax.swing.DefaultComboBoxModel(view.parent.getVector());
                view.parent.getList().setModel(listmodel);
                view.parent.getList().revalidate();

                ScenarioListModel _m = new ScenarioListModel();
                _m.setScenarioList(view.parent.getVector());
                _m.save();
            }

            view.setVisible(false);
        } else {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                _msg.getString("msg.scenarioInvalid"),
                _msg.getString("msg.error"),
                javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }

    /**
     * remove a parameter
     */
    public void remove() {
        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getTable().getModel();

        if (view.getTable().getSelectedRow() != -1) {
            model.removeRow(view.getTable().getSelectedRow());
        }
    }

    /**
     * select a xml input file
     */
    public void xml() {
        int rep = chooser.showOpenDialog(view);

        if (rep == javax.swing.JFileChooser.APPROVE_OPTION) {
            view.getModel()
                .setXmlURI(chooser.getSelectedFile().getAbsolutePath());
            view.getXmlTF().setText(chooser.getSelectedFile().toURI().toString());
        }
    }

    /**
     * select a xsl stylesheet
     */
    public void xsl() {
        int rep = chooser.showOpenDialog(view);

        if (rep == javax.swing.JFileChooser.APPROVE_OPTION) {
            view.getModel()
                .setXslURI(chooser.getSelectedFile().getAbsolutePath());
            view.getXslTF().setText(chooser.getSelectedFile().toURI().toString());
        }
    }

    /**
     * return the view   of this controller
     * @return the view   of this controller
     */
    public ScenarioFormView getView() {
        return view;
    }

    /**
     * set the view   of this controller
     * @param view the view of this controller
     */
    public void setView(ScenarioFormView view) {
        this.view = view;
    }
}
