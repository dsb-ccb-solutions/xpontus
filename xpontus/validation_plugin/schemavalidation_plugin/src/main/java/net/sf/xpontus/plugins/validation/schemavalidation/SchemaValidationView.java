/*
 * SchemaValidationView.java
 *
 * Created on December 17, 2007, 2:53 PM
 */

package net.sf.xpontus.plugins.validation.schemavalidation;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import net.sf.xpontus.utils.XPontusComponentsUtils;

/**
 *
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SchemaValidationView extends javax.swing.JDialog {
    
    /** Creates new form SchemaValidationView
     * @param parent
     * @param modal 
     */
    public SchemaValidationView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        model = new SchemaValidationModel();
        adapter = new BeanAdapter(model, true); 
        controller = new SchemaValidationController(this);
        initComponents();
        inputButton.setEnabled(false);
    }
    
    public SchemaValidationView(){
        this( (Frame)XPontusComponentsUtils.getTopComponent().getDisplayComponent(), true);
    }

    /**
     * 
     * @return
     */
    public SchemaValidationModel getModel() {
        return model;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        inputPanel = new javax.swing.JPanel();
        inputButton = new javax.swing.JButton();
        inputTF = new javax.swing.JTextField();
        useCurrentDocumentOption = new javax.swing.JRadioButton();
        useExternalDocumentOption = new javax.swing.JRadioButton();
        Bindings.bind(useExternalDocumentOption, adapter.getValueModel("useCurrentDocument"), Boolean.FALSE);
        schemaPanel = new javax.swing.JPanel();
        schemaButton = new javax.swing.JButton();
        schemaTF = new javax.swing.JTextField();
        bottomPanel = new javax.swing.JPanel();
        validateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Schema validation");

        inputPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Input options"));

        inputButton.setText("Input");
        inputButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                SchemaValidationController.INPUT_METHOD)
        );

        inputTF.setEditable(false);
        Bindings.bind(inputTF, adapter.getValueModel("input"));

        useCurrentDocumentOption.setText("Use current document");
        Bindings.bind(useCurrentDocumentOption, adapter.getValueModel("useCurrentDocument"), Boolean.TRUE);
        useCurrentDocumentOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useCurrentDocumentOptionActionPerformed(evt);
            }
        });

        useExternalDocumentOption.setText("Use external document");
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
                .addContainerGap()
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(inputPanelLayout.createSequentialGroup()
                        .add(inputButton)
                        .add(51, 51, 51)
                        .add(inputTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 321, Short.MAX_VALUE))
                    .add(inputPanelLayout.createSequentialGroup()
                        .add(useCurrentDocumentOption)
                        .add(33, 33, 33)
                        .add(useExternalDocumentOption)))
                .addContainerGap())
        );
        inputPanelLayout.setVerticalGroup(
            inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(inputPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(inputButton)
                    .add(inputTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED, 16, Short.MAX_VALUE)
                .add(inputPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(useCurrentDocumentOption)
                    .add(useExternalDocumentOption))
                .addContainerGap())
        );

        schemaPanel.setBorder(javax.swing.BorderFactory.createTitledBorder("Schema options"));

        schemaButton.setText("Schema file");
        schemaButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                SchemaValidationController.SCHEMA_METHOD)
        );

        schemaTF.setEditable(false);
        Bindings.bind(schemaTF, adapter.getValueModel("schema"));

        org.jdesktop.layout.GroupLayout schemaPanelLayout = new org.jdesktop.layout.GroupLayout(schemaPanel);
        schemaPanel.setLayout(schemaPanelLayout);
        schemaPanelLayout.setHorizontalGroup(
            schemaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(schemaPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(schemaButton)
                .add(51, 51, 51)
                .add(schemaTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 293, Short.MAX_VALUE)
                .addContainerGap())
        );
        schemaPanelLayout.setVerticalGroup(
            schemaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(schemaPanelLayout.createSequentialGroup()
                .add(schemaPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(schemaButton)
                    .add(schemaTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(21, 21, 21))
        );

        validateButton.setText("Validate");
        validateButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                SchemaValidationController.HANDLE_METHOD)
        );
        bottomPanel.add(validateButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                SchemaValidationController.CLOSE_METHOD)
        );
        bottomPanel.add(cancelButton);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(schemaPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(inputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(bottomPanel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 467, Short.MAX_VALUE))
                .add(22, 22, 22))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(inputPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(schemaPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 79, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .add(18, 18, 18)
                .add(bottomPanel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void useCurrentDocumentOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useCurrentDocumentOptionActionPerformed
        inputButton.setEnabled(false);
        model.setInput("");
    }//GEN-LAST:event_useCurrentDocumentOptionActionPerformed

    private void useExternalDocumentOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useExternalDocumentOptionActionPerformed
        inputButton.setEnabled(true);
    }//GEN-LAST:event_useExternalDocumentOptionActionPerformed
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton inputButton;
    private javax.swing.JPanel inputPanel;
    private javax.swing.JTextField inputTF;
    private javax.swing.JButton schemaButton;
    private javax.swing.JPanel schemaPanel;
    private javax.swing.JTextField schemaTF;
    private javax.swing.JRadioButton useCurrentDocumentOption;
    private javax.swing.JRadioButton useExternalDocumentOption;
    private javax.swing.JButton validateButton;
    // End of variables declaration//GEN-END:variables
    private SchemaValidationModel model;
    private BeanAdapter adapter; 
    private SchemaValidationController controller;
}
