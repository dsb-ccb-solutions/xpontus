/*
 * ScenarioListController.java
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
import net.sf.xpontus.model.ScenarioModel;
import net.sf.xpontus.utils.XMLUtils2;
import net.sf.xpontus.view.ScenarioFormView;
import net.sf.xpontus.view.ScenarioListView;
import net.sf.xpontus.view.XPontusWindow;

import javax.swing.ListModel;


/**
 * Controller for the scenario list dialog actions
 * @author Yves Zoundi
 */
public class ScenarioListController
  {
    public static final String EDIT_SCENARIO_METHOD = "editScenario";
    public static final String ADD_SCENARIO_METHOD = "addScenario";
    public static final String REMOVE_SCENARIO_METHOD = "removeScenario";
    public static final String CLOSE_METHOD = "close";
    public static final String TRANSFORM_SCENARIO_METHOD = "transformScenario";
    private ScenarioListView view;
    private ScenarioFormView scenarioForm;

    /** Creates a new instance of ScenarioListController */
    public ScenarioListController()
      {
      }

    /**
     *
     * @param view
     */
    public ScenarioListController(ScenarioListView view)
      {
        this.view = view;
        initComponents();
      }

    private void initComponents()
      {
        scenarioForm = new ScenarioFormView(view, true);
        scenarioForm.setParent(view);
      }

    public void edit()
      {
        ListModel model = view.getList().getModel();
        int index = view.getList().getSelectedIndex();

        if (index == -1)
          {
            javax.swing.JOptionPane.showMessageDialog(view,
                XPontusWindow.getInstance().getI18nMessage("msg.selectScenario"),
                XPontusWindow.getInstance().getI18nMessage("msg.error"),
                javax.swing.JOptionPane.ERROR_MESSAGE);
          }
        else
          {
            ScenarioModel scenario = (ScenarioModel) model.getElementAt(index);
            scenarioForm.setScenario(scenario);
            scenarioForm.isnew = false;
            scenarioForm.setTitle("Editing scenario : " + scenario.getName());
            scenarioForm.setLocationRelativeTo(view);
            scenarioForm.setVisible(true);
          }
      }

    public void addScenario()
      {
        ScenarioModel scenario = new ScenarioModel();
        scenarioForm.setScenario(scenario);
        scenarioForm.isnew = true;
        scenarioForm.setTitle("Create a new XSLT scenario");
        scenarioForm.setLocationRelativeTo(view);
        scenarioForm.setVisible(true);
      }

    public void removeScenario()
      {
        int index = view.getList().getSelectedIndex();

        if (index == -1)
          {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                XPontusWindow.getInstance().getI18nMessage("msg.selectScenario"),
                XPontusWindow.getInstance().getI18nMessage("msg.error"),
                javax.swing.JOptionPane.ERROR_MESSAGE);

            return;
          }

        javax.swing.DefaultComboBoxModel m;

        m = (javax.swing.DefaultComboBoxModel) view.getList().getModel();
        m.removeElementAt(index);

        ScenarioListModel _m = new ScenarioListModel();
        _m.setScenarioList(view.getVector());
        _m.save();
      }

    public void close()
      {
        view.setVisible(false);
      }

    public void transformScenario()
      {
        ListModel model = view.getList().getModel();
        int index = view.getList().getSelectedIndex();

        if (index == -1)
          {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                XPontusWindow.getInstance().getI18nMessage("msg.selectScenario"));

            return;
          }

        final ScenarioModel scenario = (ScenarioModel) model.getElementAt(index);
        Thread t = new Thread()
              {
                public void run()
                  {
                    XMLUtils2.getInstance().transform(scenario);
                  }
              };

        view.setVisible(false);
        t.start();
      }
  }
