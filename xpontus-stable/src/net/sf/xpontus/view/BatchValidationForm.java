/*
 * BatchValidationView.java
 *
 * Created on February 16, 2006, 11:39 PM
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

import java.awt.event.ActionListener;
import javax.swing.JDialog;
import net.sf.xpontus.controller.handlers.BatchValidationFormController;
import net.sf.xpontus.core.controller.handlers.BaseController;
import net.sf.xpontus.core.utils.L10nHelper;

/**
 *
 * @author  Yves Zoundi
 */
public class BatchValidationForm extends JDialog implements ActionListener{
    
    
    /** Creates new form BatchValidationView */
    public BatchValidationForm(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        locale = L10nHelper.getInstance();
        model = new javax.swing.DefaultComboBoxModel();
        model.addElement(".xml");
        initComponents();
        controller = new BatchValidationFormController(this);
    }
    
    public BatchValidationForm(){
        this(XPontusWindow.getInstance().getFrame(), true);
    }
    
    /**
     *
     * @return
     */
    public javax.swing.DefaultComboBoxModel getModel(){
        return model;
    }
    
    
    /**
     *
     * @param evt
     */
    public void actionPerformed(java.awt.event.ActionEvent evt){
        controller.invokeAction(evt.getActionCommand());
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        selectButton = new javax.swing.JButton();
        pathTF = new javax.swing.JTextField();
        validateButton = new javax.swing.JButton();
        validateButton.addActionListener(this);
        cancelButton = new javax.swing.JButton();
        extensionLabel = new javax.swing.JLabel();
        extensionTF = new javax.swing.JTextField();
        addButton = new javax.swing.JButton();
        removeButton = new javax.swing.JButton();
        listTitleLabel = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        extensionsList = new javax.swing.JList();
        recurseOption = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(locale.getValue("form.batchvalidation.title"));
        selectButton.setText(locale.getValue("form.batchvalidation.browse"));
        selectButton.setActionCommand("selectPath");
        selectButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        selectButton.addActionListener(this);

        pathTF.setEditable(false);

        validateButton.setText(locale.getValue("form.batchvalidation.validate"));
        validateButton.setActionCommand("validate");

        cancelButton.setText(locale.getValue("cancel.key"));
        cancelButton.setActionCommand("cancel");
        cancelButton.addActionListener(this);

        extensionLabel.setText(locale.getValue("form.batchvalidation.extension"));

        extensionTF.setText(".xml");

        addButton.setText(locale.getValue("add.key"));
        addButton.setActionCommand("addExtension");
        addButton.addActionListener(this);

        removeButton.setText(locale.getValue("delete.key"));
        removeButton.setActionCommand("removeExtension");
        removeButton.addActionListener(this);

        listTitleLabel.setText(locale.getValue("form.batchvalidation.xmlextensions"));

        extensionsList.setModel(model);
        scrollPane.setViewportView(extensionsList);

        recurseOption.setSelected(true);
        recurseOption.setText(locale.getValue("form.batchvalidation.recurse"));
        recurseOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        recurseOption.setMargin(new java.awt.Insets(0, 0, 0, 0));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, listTitleLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, scrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 147, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                                .add(79, 79, 79))
                            .add(org.jdesktop.layout.GroupLayout.TRAILING, layout.createSequentialGroup()
                                .add(validateButton)
                                .add(41, 41, 41))
                            .add(selectButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 272, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(cancelButton))
                    .add(recurseOption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 358, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(removeButton)
                    .add(addButton)
                    .add(layout.createSequentialGroup()
                        .add(extensionLabel)
                        .add(21, 21, 21)
                        .add(extensionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 77, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                    .add(pathTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 201, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        layout.linkSize(new java.awt.Component[] {cancelButton, validateButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.linkSize(new java.awt.Component[] {addButton, removeButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(19, 19, 19)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(39, 39, 39)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(extensionLabel)
                            .add(extensionTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(17, 17, 17)
                        .add(addButton)
                        .add(14, 14, 14)
                        .add(removeButton))
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(pathTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(selectButton))
                        .add(20, 20, 20)
                        .add(listTitleLabel)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(scrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 99, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(23, 23, 23)
                        .add(recurseOption)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 29, Short.MAX_VALUE)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(validateButton)
                            .add(cancelButton))
                        .add(29, 29, 29))))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    /**
     *
     * @return
     */
    public javax.swing.JTextField getExtensionTF() {
        return extensionTF;
    }
    
    /**
     *
     * @param extensionTF
     */
    public void setExtensionTF(javax.swing.JTextField extensionTF) {
        this.extensionTF = extensionTF;
    }
    
    /**
     *
     * @return
     */
    public javax.swing.JList getExtensionsList() {
        return extensionsList;
    }
    
    /**
     *
     * @param extensionsList
     */
    public void setExtensionsList(javax.swing.JList extensionsList) {
        this.extensionsList = extensionsList;
    }
    
    /**
     *
     * @return
     */
    public javax.swing.JTextField getPathTF() {
        return pathTF;
    }
    
    /**
     *
     * @param pathTF
     */
    public void setPathTF(javax.swing.JTextField pathTF) {
        this.pathTF = pathTF;
    }
    
    /**
     *
     * @return
     */
    public boolean isRecursive(){
        return this.recurseOption.isSelected();
    }
    
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton addButton;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel extensionLabel;
    private javax.swing.JTextField extensionTF;
    private javax.swing.JList extensionsList;
    private javax.swing.JLabel listTitleLabel;
    private javax.swing.JTextField pathTF;
    private javax.swing.JCheckBox recurseOption;
    private javax.swing.JButton removeButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JButton selectButton;
    private javax.swing.JButton validateButton;
    // End of variables declaration//GEN-END:variables
    private BaseController controller;
    private javax.swing.DefaultComboBoxModel model;
    private L10nHelper locale;
    
}
