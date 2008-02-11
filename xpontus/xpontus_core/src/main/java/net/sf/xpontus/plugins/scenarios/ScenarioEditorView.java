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
package net.sf.xpontus.plugins.scenarios;

import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.beans.EventHandler;
import java.util.Observable;
import java.util.Observer;
import java.util.Vector;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import net.sf.xpontus.plugins.scenarios.ScenarioEditorController;
import net.sf.xpontus.plugins.scenarios.ScenarioModel; 
import net.sf.xpontus.plugins.scenarios.ScenarioPluginsConfiguration;

/**
 * Add or edit a scenario
 * @version 0.0.1
 * @author  Yves Zoundi
 */
public class ScenarioEditorView extends javax.swing.JDialog implements Observer {

    /** 
     * Creates new form ScenarioEditorView 
     * @param parent 
     * @param model 
     */
    public ScenarioEditorView(javax.swing.JDialog parent) {
        super(parent, true);
        //paramModel = new ParametersPresentationModel();

//        originalModel = new ScenarioModel();
//        modifiedModel = new ScenarioModel(); // Create a copy
//        BeanUtils.copyProperties(originalModel, modifiedModel);
//
//        adapter = new BeanAdapter(modifiedModel, true);
        controller = new ScenarioEditorController(this);
//        
//        paramModel.setParameters(modifiedModel.getParameters());

//        ValueModel vm = adapter.getValueModel("processor");
        processorAdapter = new DefaultComboBoxModel(new Vector(ScenarioPluginsConfiguration.getInstance().getProcessorList()));

        initComponents();

        paramsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        scenario = new ScenarioModel();
        scenario.addObserver(this);
    }

    public String getScenarioName() {
        return nameTF.getText();
    }

    public void setModel(ScenarioModel _scenario) {
        String name = _scenario.getAlias();
        String xmlURI = _scenario.getInput();
        String xslURI = _scenario.getXsl();
        String output = _scenario.getProcessor();

        this.scenario.setAlias(name);
        this.scenario.setInput(xmlURI);
        this.scenario.setXsl(xslURI);
        this.scenario.setOutput(_scenario.getOutput());
        this.scenario.setParameters(_scenario.getParameters());
        this.scenario.setProcessor(output);
 
        
        outputTF.setText(scenario.getOutput());

        this.nameTF.setText(name);

        this.inputTF.setText(xmlURI);

        this.scriptTF.setText(xslURI);

        this.inputButton.setEnabled(scenario.isExternalDocument());

        this.useExternalDocumentOption.setSelected(scenario.isExternalDocument());

        this.processorsList.setSelectedItem(output);
    }

    public ScenarioEditorController getController() {
        return controller;
    }

    public JTable getParamsTable() {
        return paramsTable;
    }

    public ScenarioModel getOriginalModel() {
//        return originalModel;
        return null;
    }

    public ScenarioModel getModel() {
        return scenario;//  return modifiedModel;
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
 

    public void update(Observable arg0, Object arg1) {
        String name = scenario.getAlias();
        String xmlURI = null;
        String xslURI = null;

         if (scenario.getInput() != null) {
            xmlURI = scenario.getInput();
        }
        if (scenario.getOutput() != null) {
            xslURI = scenario.getOutput();
        }

        scriptTF.setText(scenario.getXsl());

//        List params = scenario.getParameters();
        String output = scenario.getProcessor();

        outputTF.setText(scenario.getOutput());
        
        processorsList.setSelectedItem(scenario.getProcessor());
        
        if (scenario.getAlias().equals("")) {
            this.nameTF.setText(getScenarioName());
        } else {
            this.nameTF.setText(name);
        }
        if (xmlURI == null) {
            this.inputTF.setText("");
        } else {
            this.inputTF.setText(xmlURI.toString());
        }

    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
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
        jPanel2 = new javax.swing.JPanel();
        scriptTF = new javax.swing.JTextField();
        scriptButton = new javax.swing.JButton();
        thirdPanel = new javax.swing.JPanel();
        parametersPanel = new javax.swing.JPanel();
        paramsScrollPane = new javax.swing.JScrollPane();
        paramsTable = new javax.swing.JTable();
        paramsButtonPanel = new javax.swing.JPanel();
        addParameterButton = new javax.swing.JButton();
        removeParameterButton = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        processorsList = new javax.swing.JComboBox();
        processorLabel = new javax.swing.JLabel();

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

        org.jdesktop.layout.GroupLayout outputPanelLayout = new org.jdesktop.layout.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(outputButton)
                .add(60, 60, 60)
                .add(outputTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 319, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(6, Short.MAX_VALUE))
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(outputButton)
                    .add(outputTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(4, Short.MAX_VALUE))
        );

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input options"));

        buttonGroup1.add(useCurrentDocumentOption);
        useCurrentDocumentOption.setSelected(true);
        useCurrentDocumentOption.setText("Use current document");
        useCurrentDocumentOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        useCurrentDocumentOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useCurrentDocumentOptionActionPerformed(evt);
            }
        });

        inputButton.setText("Input document...");
        inputButton.setEnabled(false);
        inputButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.INPUT_METHOD)
        );

        inputTF.setEditable(false);

        buttonGroup1.add(useExternalDocumentOption);
        useExternalDocumentOption.setText("Use external document");
        useExternalDocumentOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        useExternalDocumentOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useExternalDocumentOptionActionPerformed(evt);
            }
        });

        org.jdesktop.layout.GroupLayout inputPanelLayout = new org.jdesktop.layout.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inputPanelLayout.createSequentialGroup()
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(inputButton)
                    .add(useCurrentDocumentOption))
                .add(33, 33, 33)
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(useExternalDocumentOption)
                    .add(inputTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 328, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
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

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder("Stylesheet"));

        scriptTF.setColumns(6);
        scriptTF.setEditable(false);

        scriptButton.setText("Script/Stylesheet...");
        scriptButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.SCRIPT_METHOD)
        );

        org.jdesktop.layout.GroupLayout jPanel2Layout = new org.jdesktop.layout.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(scriptButton)
                .add(36, 36, 36)
                .add(scriptTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel2Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(scriptButton)
                    .add(scriptTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout firstPanelLayout = new org.jdesktop.layout.GroupLayout(firstPanel);
        firstPanel.setLayout(firstPanelLayout);
        firstPanelLayout.setHorizontalGroup(
            firstPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(firstPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(firstPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(firstPanelLayout.createSequentialGroup()
                        .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(57, 57, 57))
                    .add(firstPanelLayout.createSequentialGroup()
                        .add(nameLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 113, Short.MAX_VALUE)
                        .add(nameTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 356, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(66, 66, 66))
                    .add(firstPanelLayout.createSequentialGroup()
                        .add(firstPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, outputPanel, 0, 510, Short.MAX_VALUE)
                            .add(org.jdesktop.layout.GroupLayout.LEADING, inputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addContainerGap())))
        );
        firstPanelLayout.setVerticalGroup(
            firstPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(firstPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(firstPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameLabel)
                    .add(nameTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(inputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.UNRELATED)
                .add(outputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 68, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(jPanel2, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        m_pane.addTab("General options", firstPanel);

        parametersPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"), javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        parametersPanel.setMinimumSize(new java.awt.Dimension(213, 60));
        parametersPanel.setPreferredSize(new java.awt.Dimension(213, 60));
        parametersPanel.setLayout(new java.awt.BorderLayout());

        paramsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Value"
            }
        ));
        paramsTable.setMinimumSize(new java.awt.Dimension(300, 90));
        paramsTable.setPreferredSize(new java.awt.Dimension(300, 90));
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

        removeParameterButton.setText("Remove");
        removeParameterButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.REMOVE_PARAMETER_METHOD)
        );
        paramsButtonPanel.add(removeParameterButton);

        parametersPanel.add(paramsButtonPanel, java.awt.BorderLayout.SOUTH);

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Processor options"));

        processorsList.setModel(processorAdapter);
        processorsList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                processorsListItemStateChanged(evt);
            }
        });

        processorLabel.setText("Processor");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(processorLabel)
                .add(116, 116, 116)
                .add(processorsList, 0, 312, Short.MAX_VALUE)
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(processorLabel)
                    .add(processorsList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(22, Short.MAX_VALUE))
        );

        org.jdesktop.layout.GroupLayout thirdPanelLayout = new org.jdesktop.layout.GroupLayout(thirdPanel);
        thirdPanel.setLayout(thirdPanelLayout);
        thirdPanelLayout.setHorizontalGroup(
            thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, thirdPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, parametersPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE))
                .add(21, 21, 21))
        );
        thirdPanelLayout.setVerticalGroup(
            thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, thirdPanelLayout.createSequentialGroup()
                .add(27, 27, 27)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(parametersPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 154, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(129, 129, 129))
        );

        m_pane.addTab("Transformation options", thirdPanel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(m_pane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 547, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(bottomPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 547, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                .addContainerGap()
                .add(m_pane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 346, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(bottomPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void useCurrentDocumentOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useCurrentDocumentOptionActionPerformed
        scenario.setExternalDocument(false);
        inputButton.setEnabled(false);
        scenario.setInput("");
        inputTF.setText("");
    }//GEN-LAST:event_useCurrentDocumentOptionActionPerformed

    private void useExternalDocumentOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useExternalDocumentOptionActionPerformed
        scenario.setExternalDocument(true);
        inputButton.setEnabled(true);
    }//GEN-LAST:event_useExternalDocumentOptionActionPerformed

    private void processorsListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_processorsListItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            scenario.setProcessor(this.processorsList.getSelectedItem().toString());
        }
    }//GEN-LAST:event_processorsListItemStateChanged

    public String getSelectedProcessor(){
        return this.processorsList.getSelectedItem().toString();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addParameterButton;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel firstPanel;
    private javax.swing.JButton inputButton;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextField inputTF;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
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
    private javax.swing.JRadioButton useCurrentDocumentOption;
    private javax.swing.JRadioButton useExternalDocumentOption;
    // End of variables declaration//GEN-END:variables
    private ScenarioEditorController controller;
    private ScenarioModel scenario;
    private DefaultComboBoxModel processorAdapter;
    public boolean isnew = false;
}
