/*
 * ScenarioEditorView.java
 *
 * Created on August 26, 2007, 4:33 PM
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
package net.sf.xpontus.modules.gui.components;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.adapter.SingleListSelectionAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import net.sf.xpontus.controllers.impl.ScenarioEditorController;
import net.sf.xpontus.model.ScenarioModel;
import net.sf.xpontus.plugins.scenarios.ParametersPresentationModel;
import net.sf.xpontus.plugins.scenarios.ParametersPresentationModel;
import net.sf.xpontus.plugins.scenarios.ScenarioPluginsConfiguration;
import org.springframework.beans.BeanUtils;

/**
 * Add or edit a scenario
 * @version 0.0.1
 * @author  Yves Zoundi
 */
public class ScenarioEditorView extends javax.swing.JDialog {

    /** 
     * Creates new form ScenarioEditorView 
     * @param parent 
     * @param model 
     */
    public ScenarioEditorView(javax.swing.JDialog parent, ScenarioModel model) {
        super(parent, true);
        paramModel = new ParametersPresentationModel(model.getParameters());

        originalModel = model;
        modifiedModel = new ScenarioModel(); // Create a copy
        BeanUtils.copyProperties(originalModel, modifiedModel);

        adapter = new BeanAdapter(modifiedModel, true);
        controller = new ScenarioEditorController(this);

        ValueModel vm = adapter.getValueModel("processor");
        processorAdapter = new ComboBoxAdapter(ScenarioPluginsConfiguration.getInstance().getProcessorList(), vm);

        initComponents();
    }

    public JTable getParamsTable() {
        return paramsTable;
    }

    public ScenarioModel getModel() {
        return modifiedModel;
    }

    public JRadioButton getUseCurrentDocumentOption() {
        return useCurrentDocumentOption;
    }

    public void setUseCurrentDocumentOption(JRadioButton useCurrentDocumentOption) {
        this.useCurrentDocumentOption = useCurrentDocumentOption;
    }

    public JRadioButton getUseExternalDocumentOption() {
        return useExternalDocumentOption;
    }

    public void setUseExternalDocumentOption(JRadioButton useExternalDocumentOption) {
        this.useExternalDocumentOption = useExternalDocumentOption;
    }

    public ParametersPresentationModel getParamModel() {
        return paramModel;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        bottomPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        m_pane = new javax.swing.JTabbedPane();
        firstPanel = new javax.swing.JPanel();
        nameLabel = new javax.swing.JLabel();
        outputPanel = new javax.swing.JPanel();
        outputButton = new javax.swing.JButton();
        outputTF = new javax.swing.JTextField();
        nameTF = new javax.swing.JTextField();
        inputPanel = new javax.swing.JPanel();
        useCurrentDocumentOption = new javax.swing.JRadioButton();
        inputButton = new javax.swing.JButton();
        inputTF = new javax.swing.JTextField();
        useExternalDocumentOption = new javax.swing.JRadioButton();
        thirdPanel = new javax.swing.JPanel();
        parametersPanel = new javax.swing.JPanel();
        paramsScrollPane = new javax.swing.JScrollPane();
        paramsTable = new javax.swing.JTable();
        paramsButtonPanel = new javax.swing.JPanel();
        addParameterButton = new javax.swing.JButton();
        editButton = new javax.swing.JButton();
        removeParameterButton = new javax.swing.JButton();
        transPanel = new javax.swing.JPanel();
        scriptButton = new javax.swing.JButton();
        scriptTF = new javax.swing.JTextField();
        processorLabel = new javax.swing.JLabel();
        processorsList = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add a new transformation");

        okButton.setText("OK");
        okButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.SAVE_METHOD)
        );
        bottomPanel.add(okButton);

        closeButton.setText("Close");
        closeButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.CLOSE_WINDOW_METHOD)
        );
        bottomPanel.add(closeButton);

        nameLabel.setText("  Name");

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output options"));

        outputButton.setText("Output file ...");
        outputButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.OUTPUT_METHOD)
        );

        outputTF.setEditable(false);
        Bindings.bind(outputTF, adapter.getValueModel("output"));

        org.jdesktop.layout.GroupLayout outputPanelLayout = new org.jdesktop.layout.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(outputButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 516, Short.MAX_VALUE)
                .addContainerGap())
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(outputButton)
                    .add(outputTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(47, Short.MAX_VALUE))
        );

        Bindings.bind(nameTF, adapter.getValueModel("name"));

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input options"));

        useCurrentDocumentOption.setText("Use current document");
        useCurrentDocumentOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Bindings.bind(useCurrentDocumentOption, adapter.getValueModel("externalDocument"), Boolean.FALSE);

        inputButton.setText("Input document...");
        inputButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.INPUT_METHOD)
        );

        inputTF.setEditable(false);
        Bindings.bind(inputTF, adapter.getValueModel("input"));

        useExternalDocumentOption.setText("Use external document");
        useExternalDocumentOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        Bindings.bind(useExternalDocumentOption, adapter.getValueModel("externalDocument"), Boolean.TRUE);

        org.jdesktop.layout.GroupLayout inputPanelLayout = new org.jdesktop.layout.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(inputButton)
                    .add(useCurrentDocumentOption))
                .add(33, 33, 33)
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(useExternalDocumentOption)
                    .add(inputTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 465, Short.MAX_VALUE))
                .addContainerGap())
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inputPanelLayout.createSequentialGroup()
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(inputButton)
                    .add(inputTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(20, 20, 20)
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(useCurrentDocumentOption)
                    .add(useExternalDocumentOption))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout firstPanelLayout = new org.jdesktop.layout.GroupLayout(firstPanel);
        firstPanel.setLayout(firstPanelLayout);
        firstPanelLayout.setHorizontalGroup(
            firstPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(firstPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(firstPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(firstPanelLayout.createSequentialGroup()
                        .add(nameLabel)
                        .add(113, 113, 113)
                        .add(nameTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE))
                    .add(inputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(outputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap())
        );
        firstPanelLayout.setVerticalGroup(
            firstPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(firstPanelLayout.createSequentialGroup()
                .add(22, 22, 22)
                .add(firstPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameLabel)
                    .add(nameTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(inputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(outputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(104, Short.MAX_VALUE))
        );

        m_pane.addTab("General options", firstPanel);

        parametersPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"), javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        parametersPanel.setLayout(new java.awt.BorderLayout());

        paramsTable.setModel(paramModel.getTableModel());
        paramsTable.setSelectionModel(new SingleListSelectionAdapter(paramModel.getParameterSelectionInList().getSelectionIndexHolder()));
        paramsScrollPane.setViewportView(paramsTable);

        parametersPanel.add(paramsScrollPane, java.awt.BorderLayout.CENTER);

        addParameterButton.setText("Add");
        addParameterButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.ADD_PARAMETER_METHOD)
        );
        paramsButtonPanel.add(addParameterButton);

        editButton.setText("Edit");
        editButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.EDIT_PARAMETER_METHOD)
        );
        paramsButtonPanel.add(editButton);

        removeParameterButton.setText("Remove");
        removeParameterButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.REMOVE_PARAMETER_METHOD)
        );
        paramsButtonPanel.add(removeParameterButton);

        parametersPanel.add(paramsButtonPanel, java.awt.BorderLayout.SOUTH);

        transPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Transformation options"));

        scriptButton.setText("Script/Stylesheet...");
        scriptButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.SCRIPT_METHOD)
        );

        Bindings.bind(scriptTF, adapter.getValueModel("xsl"));

        processorLabel.setText("Processor");

        processorsList.setModel(processorAdapter);

        org.jdesktop.layout.GroupLayout transPanelLayout = new org.jdesktop.layout.GroupLayout(transPanel);
        transPanel.setLayout(transPanelLayout);
        transPanelLayout.setHorizontalGroup(
            transPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(transPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(transPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(processorLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(scriptButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(transPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(processorsList, 0, 488, Short.MAX_VALUE)
                    .add(scriptTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 488, Short.MAX_VALUE))
                .addContainerGap())
        );
        transPanelLayout.setVerticalGroup(
            transPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(transPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(transPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(scriptButton)
                    .add(scriptTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(28, 28, 28)
                .add(transPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(processorLabel)
                    .add(processorsList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout thirdPanelLayout = new org.jdesktop.layout.GroupLayout(thirdPanel);
        thirdPanel.setLayout(thirdPanelLayout);
        thirdPanelLayout.setHorizontalGroup(
            thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(thirdPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(parametersPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 657, Short.MAX_VALUE)
                .addContainerGap())
            .add(thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(thirdPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(transPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        thirdPanelLayout.setVerticalGroup(
            thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, thirdPanelLayout.createSequentialGroup()
                .addContainerGap(211, Short.MAX_VALUE)
                .add(parametersPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 170, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
            .add(thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                .add(thirdPanelLayout.createSequentialGroup()
                    .addContainerGap()
                    .add(transPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
        );

        m_pane.addTab("Transformation options", thirdPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_pane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(bottomPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 684, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(m_pane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(bottomPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addParameterButton;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel firstPanel;
    private javax.swing.JButton inputButton;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextField inputTF;
    private javax.swing.JTabbedPane m_pane;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTF;
    private javax.swing.JButton okButton;
    private javax.swing.JButton outputButton;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JTextField outputTF;
    private javax.swing.JPanel parametersPanel;
    private javax.swing.JPanel paramsButtonPanel;
    private javax.swing.JScrollPane paramsScrollPane;
    private javax.swing.JTable paramsTable;
    private javax.swing.JLabel processorLabel;
    private javax.swing.JComboBox processorsList;
    private javax.swing.JButton removeParameterButton;
    private javax.swing.JButton scriptButton;
    private javax.swing.JTextField scriptTF;
    private javax.swing.JPanel thirdPanel;
    private javax.swing.JPanel transPanel;
    private javax.swing.JRadioButton useCurrentDocumentOption;
    private javax.swing.JRadioButton useExternalDocumentOption;
    // End of variables declaration//GEN-END:variables
    private ScenarioEditorController controller;
    private BeanAdapter adapter;
    private ScenarioModel originalModel,  modifiedModel;
    private ComboBoxAdapter processorAdapter;
    private ParametersPresentationModel paramModel;
    public String original;
}
