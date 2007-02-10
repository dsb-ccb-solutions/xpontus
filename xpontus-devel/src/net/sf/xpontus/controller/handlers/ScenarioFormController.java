/*
 * ScenarioFormController.java
 *
 *
 * Created on 1 août 2005, 17:46
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
package net.sf.xpontus.controller.handlers;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.model.ScenarioListModel;
import net.sf.xpontus.view.ScenarioFormView;
import net.sf.xpontus.view.ScenarioListView;
import net.sf.xpontus.view.XPontusWindow;


/**
 * Controller for the scenario form dialog
 * @author Yves Zoundi
 */
public class ScenarioFormController
  {
    public static final String OUTPUT_METHOD = "setOutput";
    public static final String ADD_PARAMETER_METHOD = "addScenario";
    public static final String CLOSE_METHOD = "close";
    public static final String VALIDATE_SCENARIO_METHOD = "validateScenario";
    public static final String REMOVE_SCENARIO_METHOD = "removeScenario";
    public static final String XML_INPUT_METHOD = "selectXml";
    public final static String XSL_INPUT_METHOD = "selectXsl";
    private ScenarioListView parent;
    private javax.swing.JFileChooser chooser;
    private ScenarioFormView view;

    /** Creates a new instance of ScenarioFormController */
    public ScenarioFormController(ScenarioFormView view)
      {
        setView(view);
        setParent(view.parent);
        chooser = new javax.swing.JFileChooser();
      }

    public void setParent(ScenarioListView parent)
      {
        this.parent = parent;
      }

    public void setOutput()
      {
        int rep = chooser.showSaveDialog(view);

        if (rep == javax.swing.JFileChooser.APPROVE_OPTION)
          {
            view.getModel()
                .setOutputFile(chooser.getSelectedFile().getAbsolutePath());
            view.getOutputTF()
                .setText(chooser.getSelectedFile().toURI().toString());
          }
      }

    public void addParameter()
      {
        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getTable().getModel();
        model.addRow(new Object[] { "", "" });
      }

    public void close()
      {
        view.setVisible(false);
      }

    public void validateScenario()
      {
        boolean isvalid = true;

        if (view.isnew)
          {
            if (view.parent.scenarioExist(view.getScenarioName()))
              {
                javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                       .getFrame(),
                    XPontusWindow.getInstance()
                                 .getI18nMessage("msg.scenarioExists"),
                    XPontusWindow.getInstance().getI18nMessage("msg.error"),
                    javax.swing.JOptionPane.ERROR_MESSAGE);

                return;
              }
          }

        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getTable().getModel();

        java.util.Hashtable table = new java.util.Hashtable();

        for (int i = 0; i < model.getRowCount(); i++)
          {
            String cle = model.getValueAt(i, 0).toString();
            String value = model.getValueAt(i, 1).toString();

            if (table.contains(cle))
              {
                isvalid = false;
              }
            else
              {
                table.put(cle, value);
              }
          }

        if (view.getModel().isValid() && isvalid)
          {
            view.getModel().setParams(table);

            javax.swing.DefaultComboBoxModel listmodel = (javax.swing.DefaultComboBoxModel) view.parent.getList()
                                                                                                       .getModel();

            if (view.isnew)
              {
                view.parent.getVector().addElement(view.getModel());
                listmodel = new javax.swing.DefaultComboBoxModel(view.parent.getVector());
                view.parent.getList().setModel(listmodel);
                view.parent.getList().revalidate();
                view.parent.getList().setSelectedIndex(listmodel.getSize());

                ScenarioListModel _m = new ScenarioListModel();
                _m.setScenarioList(view.parent.getVector());
                _m.save();
              }
            else
              {
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
          }
        else
          {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                XPontusWindow.getInstance().getI18nMessage("msg.scenarioInvalid"),
                XPontusWindow.getInstance().getI18nMessage("msg.error"),
                javax.swing.JOptionPane.ERROR_MESSAGE);
          }
      }

    public void removeScenario()
      {
        javax.swing.table.DefaultTableModel model;
        model = (javax.swing.table.DefaultTableModel) view.getTable().getModel();

        if (view.getTable().getSelectedRow() != -1)
          {
            model.removeRow(view.getTable().getSelectedRow());
          }
      }

    public void selectXml()
      {
        int rep = chooser.showOpenDialog(view);

        if (rep == javax.swing.JFileChooser.APPROVE_OPTION)
          {
            view.getModel()
                .setXmlURI(chooser.getSelectedFile().getAbsolutePath());
            view.getXmlTF().setText(chooser.getSelectedFile().toURI().toString());
          }
      }

    public void selectXsl()
      {
        int rep = chooser.showOpenDialog(view);

        if (rep == javax.swing.JFileChooser.APPROVE_OPTION)
          {
            view.getModel()
                .setXslURI(chooser.getSelectedFile().getAbsolutePath());
            view.getXslTF().setText(chooser.getSelectedFile().toURI().toString());
          }
      }

    /**
     *
     * @return
     */
    public ScenarioFormView getView()
      {
        return view;
      }

    /**
     *
     * @param view
     */
    public void setView(ScenarioFormView view)
      {
        this.view = view;
      }
  }
