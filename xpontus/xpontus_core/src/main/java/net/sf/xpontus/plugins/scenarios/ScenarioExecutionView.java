/*
 * ScenarioExecutionView.java
 *
 * Created on August 22, 2007, 5:52 PM
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

import java.awt.Frame;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.List;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JList;
import net.sf.xpontus.plugins.scenarios.ScenarioExecutionController;
import net.sf.xpontus.plugins.scenarios.ScenarioListModel;
import net.sf.xpontus.utils.XPontusComponentsUtils;

/**
 * Dialog to execute user defined transformations
 * @version 0.0.1
 * @author  Yves Zoundi
 */
public class ScenarioExecutionView extends javax.swing.JDialog {

    /** Creates new form ScenarioExecutionView 
     * @param parent 
     * @param modal 
     */
    public ScenarioExecutionView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        controller = new ScenarioExecutionController(this);
        scenarioListModel = new DefaultComboBoxModel();
        initComponents();
    }

    public void setVisible(boolean b) {
        if (b) { 
            ScenarioListModel sl = new ScenarioListModel();
            sl.loadScenarios();
            List vector = sl.getScenarioList();
            scenarioListModel = new DefaultComboBoxModel((Vector) vector);
            scenarioList.setModel(scenarioListModel);
            scenarioList.revalidate();
            scenarioList.setSelectedIndex(vector.size() - 1);
            this.runButton.setEnabled(this.scenarioList.getModel().getSize() > 0);
        }
        super.setVisible(b);
    }

    /**
     * 
     * @return 
     */
    public DefaultComboBoxModel getScenarioListModel() {
        return scenarioListModel;
    }

    public JList getScenarioList() {
        return scenarioList;
    }

    /**
     * Default constructor
     */
    public ScenarioExecutionView() {
        this((Frame) XPontusComponentsUtils.getTopComponent().getDisplayComponent(), true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        dialogLabel = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        scenarioList = new javax.swing.JList();
        buttonsPanel = new javax.swing.JPanel();
        runButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Execute a scenario");

        dialogLabel.setText("Execute scenario");

        scenarioList.setModel(scenarioListModel);
        scrollPane.setViewportView(scenarioList);

        runButton.setText("Run");
        runButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioExecutionController.EXECUTE_METHOD)
        );
        buttonsPanel.add(runButton);

        closeButton.setText("Close");
        closeButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioExecutionController.CLOSE_METHOD)
        );
        buttonsPanel.add(closeButton);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(19, 19, 19)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, buttonsPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, scrollPane, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, dialogLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 363, Short.MAX_VALUE))
                .add(20, 20, 20))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(dialogLabel)
                .add(18, 18, 18)
                .add(scrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 235, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(buttonsPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JLabel dialogLabel;
    private javax.swing.JButton runButton;
    private javax.swing.JList scenarioList;
    private javax.swing.JScrollPane scrollPane;
    // End of variables declaration//GEN-END:variables
    private DefaultComboBoxModel scenarioListModel;
    private Object controller;
}
