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

import net.sf.xpontus.controllers.impl.ScenarioEditorController;

/**
 * Add or edit a scenario
 * @version 0.0.1
 * @author  Yves Zoundi
 */
public class ScenarioEditorView extends javax.swing.JDialog {

    /** 
     * Creates new form ScenarioEditorView 
     * @param parent 
     * @param modal 
     */
    public ScenarioEditorView(javax.swing.JDialog parent, boolean modal) {
        super(parent, modal);
        controller = new ScenarioEditorController(this);
        initComponents();
    }

    public ScenarioEditorView(javax.swing.JDialog parent) {
        this(parent, true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        nameTF = new javax.swing.JTextField();
        bottomPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        parametersPanel = new javax.swing.JPanel();
        paramsScrollPane = new javax.swing.JScrollPane();
        paramsTable = new javax.swing.JTable();
        paramsButtonPanel = new javax.swing.JPanel();
        addParameterButton = new javax.swing.JButton();
        removeParameterButton = new javax.swing.JButton();
        inputPanel = new javax.swing.JPanel();
        useCurrentDocumentOption = new javax.swing.JRadioButton();
        inputButton = new javax.swing.JButton();
        inputTF = new javax.swing.JTextField();
        useExternalDocumentOption = new javax.swing.JRadioButton();
        outputPanel = new javax.swing.JPanel();
        outputTypeLabel = new javax.swing.JLabel();
        outputButton = new javax.swing.JButton();
        outputTF = new javax.swing.JTextField();
        outputTypeList = new javax.swing.JComboBox();
        transPanel = new javax.swing.JPanel();
        scriptButton = new javax.swing.JButton();
        scriptTF = new javax.swing.JTextField();
        processorLabel = new javax.swing.JLabel();
        processorsList = new javax.swing.JComboBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Add a new transformation");

        nameLabel.setText("Name");

        okButton.setText("OK");

        closeButton.setText("Close");

        org.jdesktop.layout.GroupLayout bottomPanelLayout = new org.jdesktop.layout.GroupLayout(bottomPanel);
        bottomPanel.setLayout(bottomPanelLayout);
        bottomPanelLayout.setHorizontalGroup(
            bottomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(bottomPanelLayout.createSequentialGroup()
                .add(216, 216, 216)
                .add(okButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 59, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(5, 5, 5)
                .add(closeButton))
        );

        bottomPanelLayout.linkSize(new java.awt.Component[] {closeButton, okButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        bottomPanelLayout.setVerticalGroup(
            bottomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(bottomPanelLayout.createSequentialGroup()
                .add(5, 5, 5)
                .add(bottomPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(closeButton)
                    .add(okButton))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        parametersPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"), javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        parametersPanel.setLayout(new java.awt.BorderLayout());

        paramsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Name", "Value"
            }
        ));
        paramsScrollPane.setViewportView(paramsTable);

        parametersPanel.add(paramsScrollPane, java.awt.BorderLayout.CENTER);

        addParameterButton.setText("Add");
        paramsButtonPanel.add(addParameterButton);

        removeParameterButton.setText("Remove");
        paramsButtonPanel.add(removeParameterButton);

        parametersPanel.add(paramsButtonPanel, java.awt.BorderLayout.SOUTH);

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input options"));

        useCurrentDocumentOption.setText("Use current document");
        useCurrentDocumentOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        inputButton.setText("Input document...");

        useExternalDocumentOption.setText("Use external document");
        useExternalDocumentOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout inputPanelLayout = new org.jdesktop.layout.GroupLayout(inputPanel);
        inputPanel.setLayout(inputPanelLayout);
        inputPanelLayout.setHorizontalGroup(
            inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(useCurrentDocumentOption)
                    .add(inputButton))
                .add(33, 33, 33)
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(inputTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 338, Short.MAX_VALUE)
                    .add(useExternalDocumentOption))
                .addContainerGap())
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(inputButton)
                    .add(inputTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(29, 29, 29)
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(useCurrentDocumentOption)
                    .add(useExternalDocumentOption))
                .addContainerGap(34, Short.MAX_VALUE))
        );

        outputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Output options"));

        outputTypeLabel.setText("Output type");

        outputButton.setText("Output file ...");

        outputTypeList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "XML", "HTML" }));

        org.jdesktop.layout.GroupLayout outputPanelLayout = new org.jdesktop.layout.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, outputPanelLayout.createSequentialGroup()
                .add(outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(outputPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .add(outputButton))
                    .add(outputPanelLayout.createSequentialGroup()
                        .add(20, 20, 20)
                        .add(outputTypeLabel)))
                .add(37, 37, 37)
                .add(outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(outputTypeList, 0, 405, Short.MAX_VALUE)
                    .add(outputTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 405, Short.MAX_VALUE))
                .addContainerGap())
        );
        outputPanelLayout.setVerticalGroup(
            outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(outputButton)
                    .add(outputTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(18, 18, 18)
                .add(outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(outputTypeLabel)
                    .add(outputTypeList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(35, Short.MAX_VALUE))
        );

        transPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Transformation options"));

        scriptButton.setText("Script/Stylesheet...");

        processorLabel.setText("Processor");

        processorsList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Saxon 6.5.4" }));

        org.jdesktop.layout.GroupLayout transPanelLayout = new org.jdesktop.layout.GroupLayout(transPanel);
        transPanel.setLayout(transPanelLayout);
        transPanelLayout.setHorizontalGroup(
            transPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(transPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(transPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(transPanelLayout.createSequentialGroup()
                        .add(scriptButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scriptTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 397, Short.MAX_VALUE))
                    .add(transPanelLayout.createSequentialGroup()
                        .add(processorLabel)
                        .add(76, 76, 76)
                        .add(processorsList, 0, 397, Short.MAX_VALUE)))
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

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(transPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(parametersPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE)
                    .add(inputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(layout.createSequentialGroup()
                        .add(nameLabel)
                        .add(113, 113, 113)
                        .add(nameTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 420, Short.MAX_VALUE))
                    .add(outputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(bottomPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 572, Short.MAX_VALUE))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(nameLabel)
                    .add(nameTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(inputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(outputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(parametersPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 135, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(transPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 137, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(bottomPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addParameterButton;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton inputButton;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextField inputTF;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTF;
    private javax.swing.JButton okButton;
    private javax.swing.JButton outputButton;
    private javax.swing.JPanel outputPanel;
    private javax.swing.JTextField outputTF;
    private javax.swing.JLabel outputTypeLabel;
    private javax.swing.JComboBox outputTypeList;
    private javax.swing.JPanel parametersPanel;
    private javax.swing.JPanel paramsButtonPanel;
    private javax.swing.JScrollPane paramsScrollPane;
    private javax.swing.JTable paramsTable;
    private javax.swing.JLabel processorLabel;
    private javax.swing.JComboBox processorsList;
    private javax.swing.JButton removeParameterButton;
    private javax.swing.JButton scriptButton;
    private javax.swing.JTextField scriptTF;
    private javax.swing.JPanel transPanel;
    private javax.swing.JRadioButton useCurrentDocumentOption;
    private javax.swing.JRadioButton useExternalDocumentOption;
    // End of variables declaration//GEN-END:variables
    private ScenarioEditorController controller;
}
