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
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Observable;
import java.util.Observer;
import javax.swing.JRadioButton;
import javax.swing.JTable;
import net.sf.xpontus.plugins.scenarios.ScenarioEditorController;
import net.sf.xpontus.plugins.scenarios.ScenarioModel;
import net.sf.xpontus.plugins.scenarios.ParametersPresentationModel;
import net.sf.xpontus.plugins.scenarios.ParametersPresentationModel;
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
        paramModel = new ParametersPresentationModel();

//        originalModel = new ScenarioModel();
//        modifiedModel = new ScenarioModel(); // Create a copy
//        BeanUtils.copyProperties(originalModel, modifiedModel);
//
//        adapter = new BeanAdapter(modifiedModel, true);
//        controller = new ScenarioEditorController(this);
//        
//        paramModel.setParameters(modifiedModel.getParameters());

        ValueModel vm = adapter.getValueModel("processor");
        processorAdapter = new ComboBoxAdapter(ScenarioPluginsConfiguration.getInstance().getProcessorList(), vm);

        initComponents();
    }

    public void setModel(ScenarioModel model) {
//        BeanUtils.copyProperties(model, this.originalModel);       
//        originalModel.setParameters(new ArrayList(model.getParameters()));
//         
//        BeanUtils.copyProperties(originalModel, this.modifiedModel);  
//        modifiedModel.setParameters(new ArrayList(model.getParameters()));
//        
//        paramModel.setParameters(modifiedModel.getParameters()); 
//        
//        this.paramsTable.revalidate();
//        this.paramsTable.repaint(); 

    }

    public JTable getParamsTable() {
        return paramsTable;
    }

    public ScenarioModel getOriginalModel() {
//        return originalModel;
        return null;
    }

    public ScenarioModel getModel() {
        return null;//  return modifiedModel;
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

    public void update(Observable arg0, Object arg1) {
        String name = scenario.getAlias();
        java.net.URI xmlURI = null;
        java.net.URI xslURI = null;
        try {
            if (scenario.getInput() != null) {
                xmlURI = new URI(scenario.getInput());
            }
            if (scenario.getOutput() != null) {
                xslURI = new URI(scenario.getOutput());
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        List params = scenario.getParameters();
        String output = scenario.getProcessor();
        if (scenario.getAlias().equals("")) {
            this.nameTF.setText("");
        } else {
            this.nameTF.setText(name);
        }
        if (xmlURI == null) {
            this.inputTF.setText("");
        } else {
            this.inputTF.setText(xmlURI.toString());
        }
        if (xslURI == null) {
            this.scriptTF.setText("");
        } else {
            this.scriptTF.setText(xslURI.toString());
        }
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
        jPanel1 = new javax.swing.JPanel();
        processorsList = new javax.swing.JComboBox();
        scriptTF = new javax.swing.JTextField();
        scriptButton = new javax.swing.JButton();
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
        Bindings.bind(outputTF, adapter.getValueModel("output"));

        org.jdesktop.layout.GroupLayout outputPanelLayout = new org.jdesktop.layout.GroupLayout(outputPanel);
        outputPanel.setLayout(outputPanelLayout);
        outputPanelLayout.setHorizontalGroup(
            outputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(outputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(outputButton)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(outputTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 381, Short.MAX_VALUE)
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

        Bindings.bind(nameTF, adapter.getValueModel("alias"));

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
                    .add(inputTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 330, Short.MAX_VALUE))
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
                    .add(inputPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(firstPanelLayout.createSequentialGroup()
                        .add(nameLabel)
                        .add(113, 113, 113)
                        .add(nameTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 376, Short.MAX_VALUE))
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
                .addContainerGap(30, Short.MAX_VALUE))
        );

        m_pane.addTab("General options", firstPanel);

        parametersPanel.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createTitledBorder("Parameters"), javax.swing.BorderFactory.createEmptyBorder(2, 2, 2, 2)));
        parametersPanel.setMinimumSize(new java.awt.Dimension(213, 60));
        parametersPanel.setPreferredSize(new java.awt.Dimension(213, 60));
        parametersPanel.setLayout(new java.awt.BorderLayout());

        paramsTable.setModel(paramModel.getTableModel());
        paramsTable.setMinimumSize(new java.awt.Dimension(300, 90));
        paramsTable.setPreferredSize(new java.awt.Dimension(300, 90));
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

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder("Processor options"));

        processorsList.setModel(processorAdapter);

        scriptTF.setColumns(6);
        scriptTF.setEditable(false);
        Bindings.bind(scriptTF, adapter.getValueModel("xsl"));

        scriptButton.setText("Script/Stylesheet...");
        scriptButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                ScenarioEditorController.SCRIPT_METHOD)
        );

        processorLabel.setText("Processor");

        org.jdesktop.layout.GroupLayout jPanel1Layout = new org.jdesktop.layout.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(scriptButton)
                    .add(processorLabel))
                .add(36, 36, 36)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(processorsList, 0, 312, Short.MAX_VALUE)
                    .add(scriptTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(scriptButton)
                    .add(scriptTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(27, 27, 27)
                .add(jPanel1Layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(processorLabel)
                    .add(processorsList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        org.jdesktop.layout.GroupLayout thirdPanelLayout = new org.jdesktop.layout.GroupLayout(thirdPanel);
        thirdPanel.setLayout(thirdPanelLayout);
        thirdPanelLayout.setHorizontalGroup(
            thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, thirdPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, parametersPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 511, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jPanel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(21, 21, 21))
        );
        thirdPanelLayout.setVerticalGroup(
            thirdPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, thirdPanelLayout.createSequentialGroup()
                .add(27, 27, 27)
                .add(jPanel1, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, Short.MAX_VALUE)
                .add(18, 18, 18)
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
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addParameterButton;
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton editButton;
    private javax.swing.JPanel firstPanel;
    private javax.swing.JButton inputButton;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextField inputTF;
    private javax.swing.JPanel jPanel1;
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
    private BeanAdapter adapter;
    private ScenarioModel scenario;
    private ComboBoxAdapter processorAdapter;
    private ParametersPresentationModel paramModel;
    public boolean isnew = false;
}
