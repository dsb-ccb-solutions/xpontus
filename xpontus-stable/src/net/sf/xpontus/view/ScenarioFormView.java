/*
 * ScenarioFormView.java
 *
 * Created on 1 août 2005, 17:52
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

package net.sf.xpontus.view;


import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import net.sf.xpontus.controller.handlers.ScenarioFormController;
import net.sf.xpontus.core.controller.handlers.BaseController;
import net.sf.xpontus.core.utils.L10nHelper;
import net.sf.xpontus.model.ScenarioModel;


/**
 * A dialog to add/edit a scenario
 * @author  Yves Zoundi
 */
public class ScenarioFormView extends javax.swing.JDialog implements java.awt.event.ActionListener, java.util.Observer{
    
    /**
     * Creates new form ScenarioFormView
     * @param parent The parent window
     * @param modal specify if the window should stay on top
     */
    public ScenarioFormView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        locale = L10nHelper.getInstance();
        initComponents();
        initListeners();
        scenario = new ScenarioModel();
        scenario.addObserver(this);
        
    }
    
    public String getScenarioName(){
        return nameTF.getText();
    }
    
    public void setParent( ScenarioListView parent){
        this.parent = parent;
    }
    
    public ScenarioListView parent;
    
    public javax.swing.JButton getXmlButton(){
        return xmlButton;
    }
    
    public void update(java.util.Observable obs, Object obj){
        String name = scenario.getName();
        java.net.URI xmlURI = null;
        java.net.URI xslURI = null;
        try {
            if(scenario.getXmlURI()!=null){
                xmlURI = new URI(scenario.getXmlURI());
            }
            if(scenario.getXslURI()!=null){
                xslURI = new URI(scenario.getXslURI());
            }
        } catch (URISyntaxException ex) {
            ex.printStackTrace();
        }
        java.util.Hashtable params = scenario.getParams();
        String output = scenario.getOutputType();
        if(xmlTF.getText().equals("")){
            this.nameTF.setText("");
        } else{
            this.nameTF.setText(name);
        }
        if(xmlURI == null){
            this.xmlTF.setText("");
        } else{
            this.xmlTF.setText(xmlURI.toString());
        }
        if(xslURI == null){
            this.xslTF.setText("");
        } else{
            this.xslTF.setText(xslURI.toString());
        }
        xmlButton.setEnabled(scenario.isExternal());
        outputList.setSelectedItem(output);
        resetParameters(params);
        
    }
    
    
    public void resetParameters(java.util.Hashtable params){
        javax.swing.table.DefaultTableModel model = (javax.swing.table.DefaultTableModel)table.getModel();
        int taille = model.getRowCount();
        for(int i=0;i<taille;i++){
            model.removeRow(i);
        }
        if(params.size()>0){
            java.util.Iterator it = params.keySet().iterator();
            while(it.hasNext()){
                String cle = (String)it.next();
                String val = params.get(cle).toString();
                model.addRow(new Object[]{cle,val});
            }
        }
        table.revalidate();
        table.repaint();
    }
    
    /**
     * Creates new form ScenarioFormView
     * @param parent The parent window
     * @param modal specify if the window should stay on top
     */
    public ScenarioFormView(javax.swing.JDialog parent, boolean modal) {
        super(parent, modal);
        locale = L10nHelper.getInstance();
        initComponents();
        initListeners();
        scenario = new ScenarioModel();
        scenario.addObserver(this);
    }
    
    public void actionPerformed(java.awt.event.ActionEvent evt){
        if(evt.getActionCommand().equals("validate")){
            scenario.setName(getScenarioName());
        }
        controller.invokeAction(evt.getActionCommand());
    }
    
    private void initListeners(){
        controller = new ScenarioFormController(this);
        
        addButton.addActionListener(this);
        removeButton.addActionListener(this);
        cancelButton.addActionListener(this);
        okButton.addActionListener(this);
        xmlButton.addActionListener(this);
        xslButton.addActionListener(this);
    }
    
    public void setScenario(ScenarioModel _scenario){
        this.scenario = _scenario;
        String name = scenario.getName();
        String xmlURI = scenario.getXmlURI();
        String xslURI = scenario.getXslURI();
        java.util.Hashtable params = scenario.getParams();
        String output = scenario.getOutputType();
        java.io.File outputFile = null;
        if(scenario.getOutputFile()!= null){
            outputFile = new File(scenario.getOutputFile());
        }
        
        if(scenario.getOutputFile()==null){
            outputTF.setText("");
        } else{
            outputTF.setText(outputFile.toURI().toString());
        }
        if(name==null){
            this.nameTF.setText("");
        } else{
            this.nameTF.setText(name);
        }
        if(xmlURI == null){
            this.xmlTF.setText("");
        } else{
            this.xmlTF.setText(xmlURI.toString());
        }
        if(xslURI == null){
            this.xslTF.setText("");
        } else{
            this.xslTF.setText(xslURI.toString());
        }
        xmlButton.setEnabled(scenario.isExternal());
        this.useExternalDocumentOption.setSelected(scenario.isExternal());
        outputList.setSelectedItem(output);
        resetParameters(params);
    }
    
    public boolean scenarioIsValid(){
        boolean _valid = true;
        StringBuffer errors = new StringBuffer();
        if(nameTF.getText().equals("")){
            _valid = false;
            errors.append("Please type a name");
        }
        if(xmlTF.getText().equals("") && useExternalDocumentOption.isSelected()){
            _valid = false;
            errors.append("Please type a name");
        }
        if(xslTF.getText().equals("")){
            _valid = false;
            errors.append("Select an xsl stylesheet");
        }
        return _valid;
        
    }
    
    public javax.swing.JTable getTable(){
        return table;
    }
    
    public ScenarioModel getModel(){
        return scenario;
    }
    
    public void setNewScenario(boolean b){
        isnew = b;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        buttonGroup = new javax.swing.ButtonGroup();
        buttonsPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        outputLabel = new javax.swing.JLabel();
        outputList = new javax.swing.JComboBox();
        useCurrentDocumentOption = new javax.swing.JRadioButton();
        useExternalDocumentOption = new javax.swing.JRadioButton();
        xslTF = new javax.swing.JTextField();
        xslButton = new javax.swing.JButton();
        nameLabel = new javax.swing.JLabel();
        nameTF = new javax.swing.JTextField();
        paramsPanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        paramsButtonPanel = new javax.swing.JPanel();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        xmlButton = new javax.swing.JButton();
        xmlTF = new javax.swing.JTextField();
        outputButton = new javax.swing.JButton();
        outputTF = new javax.swing.JTextField();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Scenarios");
        okButton.setText(locale.getValue("ok.key"));
        okButton.setActionCommand("validate");
        buttonsPanel.add(okButton);

        cancelButton.setText(locale.getValue("cancel.key"));
        cancelButton.setActionCommand("close");
        buttonsPanel.add(cancelButton);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.CENTER);

        mainPanel.setLayout(new java.awt.GridBagLayout());

        outputLabel.setText(locale.getValue("form.editscenario.output"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(outputLabel, gridBagConstraints);

        outputList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "XML", "HTML", "PDF", "SVG", "TEXT" }));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(outputList, gridBagConstraints);

        buttonGroup.add(useCurrentDocumentOption);
        useCurrentDocumentOption.setSelected(true);
        useCurrentDocumentOption.setText(locale.getValue("form.editscenario.usecurrentdocument"));
        useCurrentDocumentOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useCurrentDocument_Onclick(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(useCurrentDocumentOption, gridBagConstraints);

        buttonGroup.add(useExternalDocumentOption);
        useExternalDocumentOption.setText(locale.getValue("form.editscenario.useexternaldocument"));
        useExternalDocumentOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useExternalDocumentOption_Onclick(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(useExternalDocumentOption, gridBagConstraints);

        xslTF.setColumns(20);
        xslTF.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(xslTF, gridBagConstraints);

        xslButton.setText("XSL ...");
        xslButton.setActionCommand("xsl");
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 1;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(xslButton, gridBagConstraints);

        nameLabel.setText(locale.getValue("form.editscenario.name"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(nameLabel, gridBagConstraints);

        nameTF.setColumns(20);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(nameTF, gridBagConstraints);

        paramsPanel.setLayout(new java.awt.BorderLayout());

        paramsPanel.setBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.CompoundBorder(new javax.swing.border.TitledBorder(locale.getValue("form.editscenario.parameters")), new javax.swing.border.EmptyBorder(new java.awt.Insets(1, 1, 1, 1))), new javax.swing.border.EmptyBorder(new java.awt.Insets(2, 2, 2, 2))));
        scrollPane.setMinimumSize(new java.awt.Dimension(200, 100));
        scrollPane.setPreferredSize(new java.awt.Dimension(200, 100));
        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                locale.getValue("form.editscenario.parameter"),
                locale.getValue("form.editscenario.value")
            }
        ));
        scrollPane.setViewportView(table);

        paramsPanel.add(scrollPane, java.awt.BorderLayout.CENTER);

        addButton.setText(locale.getValue("add.key"));
        addButton.setActionCommand("add");
        paramsButtonPanel.add(addButton);

        removeButton.setText(locale.getValue("delete.key"));
        removeButton.setActionCommand("remove");
        paramsButtonPanel.add(removeButton);

        paramsPanel.add(paramsButtonPanel, java.awt.BorderLayout.SOUTH);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.gridheight = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(paramsPanel, gridBagConstraints);

        xmlButton.setText(locale.getValue("form.editscenario.xmldocument"));
        xmlButton.setActionCommand("xml");
        xmlButton.setEnabled(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(xmlButton, gridBagConstraints);

        xmlTF.setColumns(20);
        xmlTF.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(xmlTF, gridBagConstraints);

        outputButton.setText(locale.getValue("form.editscenario.outputfile"));
        outputButton.setActionCommand("outputButton_Onclick");
        outputButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                outputButtonActionPerformed(evt);
            }
        });

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(outputButton, gridBagConstraints);

        outputTF.setColumns(10);
        outputTF.setEditable(false);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.fill = java.awt.GridBagConstraints.HORIZONTAL;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.WEST;
        gridBagConstraints.insets = new java.awt.Insets(5, 5, 5, 5);
        mainPanel.add(outputTF, gridBagConstraints);

        getContentPane().add(mainPanel, java.awt.BorderLayout.NORTH);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents
    
    private void outputButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_outputButtonActionPerformed
        controller.invokeAction(evt.getActionCommand());
    }//GEN-LAST:event_outputButtonActionPerformed
    
    private void useExternalDocumentOption_Onclick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useExternalDocumentOption_Onclick
// TODO add your handling code here:
        scenario.setExternal(true);
        xmlButton.setEnabled(true);
        
    }//GEN-LAST:event_useExternalDocumentOption_Onclick
    
    private void useCurrentDocument_Onclick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useCurrentDocument_Onclick
// TODO add your handling code here:
        scenario.setExternal(false);
        xmlButton.setEnabled(false);
        scenario.setXmlURI(null);
        xmlTF.setText("");
    }//GEN-LAST:event_useCurrentDocument_Onclick
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.ButtonGroup buttonGroup;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTF;
    private javax.swing.JButton okButton;
    private javax.swing.JButton outputButton;
    private javax.swing.JLabel outputLabel;
    private javax.swing.JComboBox outputList;
    private javax.swing.JTextField outputTF;
    private javax.swing.JPanel paramsButtonPanel;
    private javax.swing.JPanel paramsPanel;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JTable table;
    private javax.swing.JRadioButton useCurrentDocumentOption;
    private javax.swing.JRadioButton useExternalDocumentOption;
    private javax.swing.JButton xmlButton;
    private javax.swing.JTextField xmlTF;
    private javax.swing.JButton xslButton;
    private javax.swing.JTextField xslTF;
    // End of variables declaration//GEN-END:variables
    private BaseController controller;
    private ScenarioModel scenario;
    public boolean isnew = false;
    private L10nHelper locale;
    
    public javax.swing.JTextField getNameTF() {
        return nameTF;
    }
    
    public void setNameTF(javax.swing.JTextField nameTF) {
        this.nameTF = nameTF;
    }
    
    public javax.swing.JTextField getXmlTF() {
        return xmlTF;
    }
    
    public void setXmlTF(javax.swing.JTextField xmlTF) {
        this.xmlTF = xmlTF;
    }
    
    public javax.swing.JTextField getXslTF() {
        return xslTF;
    }
    
    public void setXslTF(javax.swing.JTextField xslTF) {
        this.xslTF = xslTF;
    }
    
    public javax.swing.JTextField getOutputTF() {
        return outputTF;
    }
    
    public void setOutputTF(javax.swing.JTextField outputTF) {
        this.outputTF = outputTF;
    }
}
