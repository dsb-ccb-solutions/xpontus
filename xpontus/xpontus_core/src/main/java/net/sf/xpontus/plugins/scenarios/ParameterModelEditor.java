/*
 * ParameterModelEditor.java
 *
 * Created on 21 d�cembre 2007, 00:31
 */
package net.sf.xpontus.plugins.scenarios;

import com.jgoodies.binding.adapter.BasicComponentFactory;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.list.LinkedListModel;
import javax.swing.JDialog;
import net.sf.xpontus.modules.gui.components.ScenarioEditorView;
import net.sf.xpontus.utils.XPontusComponentsUtils;
import org.springframework.beans.BeanUtils;

/**
 *
 * @author  Yves Zoundi <yveszoundit at users dot sf dot net>
 */
public class ParameterModelEditor extends javax.swing.JDialog {

    /** 
     * Creates new form ParameterModelEditor
     * @param parent 
     * @param model 
     */
    public ParameterModelEditor(JDialog parent, ParameterModel model) {
        super(parent, true);
        buttonPressed = 0;

        originalParameter = model;
        modifiedParameter = new ParameterModel(); // Create a copy
        BeanUtils.copyProperties(originalParameter, modifiedParameter);

        parameterModelBeanAdapter = new BeanAdapter(modifiedParameter, true);

        initComponents();

        setLocationRelativeTo(parent);

        setVisible(true);
    }

    public byte getButtonPressed() {
        return buttonPressed;
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        nameLabel = new javax.swing.JLabel();
        nameTF = BasicComponentFactory.createTextField(parameterModelBeanAdapter.getValueModel(ParameterModel.PROPERTY_NAME));
        valueLabel = new javax.swing.JLabel();
        valueTF = BasicComponentFactory.createTextField(parameterModelBeanAdapter.getValueModel(ParameterModel.PROPERTY_VALUE));
        buttonsPanel = new javax.swing.JPanel();
        saveButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Parameter editor");
        setResizable(false);

        nameLabel.setText("Name");

        valueLabel.setText("Value");

        saveButton.setText("Save");
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(saveButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(cancelButton);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(buttonsPanel, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(nameLabel)
                            .addComponent(valueLabel))
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(valueTF, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE)
                            .addComponent(nameTF, javax.swing.GroupLayout.DEFAULT_SIZE, 284, Short.MAX_VALUE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(nameLabel)
                    .addComponent(nameTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(valueLabel)
                    .addComponent(valueTF, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(buttonsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        buttonPressed = BUTTON_CANCEL;
        setVisible(false);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void saveButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveButtonActionPerformed

        StringBuffer errors = new StringBuffer();

        if (modifiedParameter.getName() ==null || modifiedParameter.getName().trim().equals("")) {
            errors.append("The parameter name is empty cannot save it! \n");
        }

        ScenarioEditorView m_view = (ScenarioEditorView) getParent();

        LinkedListModel ll = m_view.getParamModel().getParametersListModel();

        boolean alreadyExists = false;

        for (int i = 0; i < ll.size(); i++) {
            ParameterModel pm = (ParameterModel) ll.get(i);

            if (pm.getName().equals(modifiedParameter.getName())) {
                if(!pm.getName().equals(originalParameter.getName())){
                    alreadyExists = true;
                }
                break;
            }
        }

        if (alreadyExists) {
            errors.append("The parameter already exists!");
        }

        if (errors.length() > 0) {
            XPontusComponentsUtils.showErrorMessage(errors.toString());
            return;
        } 
        
        BeanUtils.copyProperties(modifiedParameter, originalParameter);
        buttonPressed = BUTTON_OK;
        setVisible(false);

    }//GEN-LAST:event_saveButtonActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JLabel nameLabel;
    private javax.swing.JTextField nameTF;
    private javax.swing.JButton saveButton;
    private javax.swing.JLabel valueLabel;
    private javax.swing.JTextField valueTF;
    // End of variables declaration//GEN-END:variables
    private BeanAdapter parameterModelBeanAdapter;
    private ParameterModel originalParameter,  modifiedParameter;
    public static final byte BUTTON_OK = 1;
    public static final byte BUTTON_CANCEL = 2;
    private byte buttonPressed;
}