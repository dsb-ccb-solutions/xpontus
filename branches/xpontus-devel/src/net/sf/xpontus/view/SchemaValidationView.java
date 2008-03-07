/*
 * SchemaValidationView.java
 *
 * Created on February 19, 2006, 12:53 PM
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

import com.sun.java.help.impl.SwingWorker;
import com.sun.msv.verifier.ValidityViolation;
import java.io.StringReader;
import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierFactory;
import org.xml.sax.InputSource;

/**
 *
 * @author  Yves Zoundi
 */
public class SchemaValidationView extends javax.swing.JDialog {
    
    /** Creates new form SchemaValidationView */
    public SchemaValidationView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
    }
    
    
    public SchemaValidationView(){
        this(XPontusWindow.getInstance().getFrame(), true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        chooser = new javax.swing.JFileChooser();
        group = new javax.swing.ButtonGroup();
        schemaButton = new javax.swing.JButton();
        schemaTF = new javax.swing.JTextField();
        currentDocumentOption = new javax.swing.JRadioButton();
        useExternalDocumentOption = new javax.swing.JRadioButton();
        xmlButton = new javax.swing.JButton();
        xmlTF = new javax.swing.JTextField();
        validateButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(XPontusWindow.getInstance().getI18nMessage("form.relaxng.title"));
        schemaButton.setText(XPontusWindow.getInstance().getI18nMessage("form.relaxng.schema") + " ...");
        schemaButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                schemaButtonActionPerformed(evt);
            }
        });

        schemaTF.setEditable(false);

        group.add(currentDocumentOption);
        currentDocumentOption.setSelected(true);
        currentDocumentOption.setText(XPontusWindow.getInstance().getI18nMessage("form.editscenario.usecurrentdocument"));
        currentDocumentOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        currentDocumentOption.setMargin(new java.awt.Insets(0, 0, 0, 0));
        currentDocumentOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                currentDocumentOptionActionPerformed(evt);
            }
        });

        group.add(useExternalDocumentOption);
        useExternalDocumentOption.setText(XPontusWindow.getInstance().getI18nMessage("form.editscenario.useexternaldocument"));
        useExternalDocumentOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        useExternalDocumentOption.setMargin(new java.awt.Insets(0, 0, 0, 0));
        useExternalDocumentOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                useExternalDocumentOptionActionPerformed(evt);
            }
        });

        xmlButton.setText(XPontusWindow.getInstance().getI18nMessage("form.editscenario.xmldocument"));
        xmlButton.setEnabled(false);
        xmlButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                xmlButtonActionPerformed(evt);
            }
        });

        xmlTF.setEditable(false);

        validateButton.setText(XPontusWindow.getInstance().getI18nMessage("form.batchvalidation.validate"));
        validateButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                validateButton_Onclick(evt);
            }
        });

        closeButton.setText(XPontusWindow.getInstance().getI18nMessage("close.key"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButton_Onclick(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .addContainerGap()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(layout.createSequentialGroup()
                                .add(schemaButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                .add(36, 36, 36)
                                .add(schemaTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 403, Short.MAX_VALUE))
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                    .add(xmlButton, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 176, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(67, 67, 67)
                                    .add(xmlTF))
                                .add(org.jdesktop.layout.GroupLayout.LEADING, layout.createSequentialGroup()
                                    .add(currentDocumentOption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 272, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                                    .add(62, 62, 62)
                                    .add(useExternalDocumentOption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 226, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))))
                    .add(layout.createSequentialGroup()
                        .add(171, 171, 171)
                        .add(validateButton)
                        .add(52, 52, 52)
                        .add(closeButton)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(33, 33, 33)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(schemaButton)
                    .add(schemaTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(43, 43, 43)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(currentDocumentOption)
                    .add(useExternalDocumentOption))
                .add(36, 36, 36)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(xmlButton)
                    .add(xmlTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(44, 44, 44)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(validateButton)
                    .add(closeButton))
                .addContainerGap(54, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void useExternalDocumentOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_useExternalDocumentOptionActionPerformed
        xmlButton.setEnabled(true);
    }//GEN-LAST:event_useExternalDocumentOptionActionPerformed
    
    private void closeButton_Onclick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButton_Onclick
        setVisible(false);
    }//GEN-LAST:event_closeButton_Onclick
    
    private void validateButton_Onclick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_validateButton_Onclick
        if(isValid()){
            setVisible(false);
            final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    validateSchema();
                    return null;
                }
            };
            worker.start();
        } else{
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
                    sb.toString(), "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_validateButton_Onclick
    
    private void xmlButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_xmlButtonActionPerformed
        int answer = chooser.showOpenDialog(XPontusWindow.getInstance().getFrame());
        if(answer == javax.swing.JFileChooser.APPROVE_OPTION){
            xmlFile = chooser.getSelectedFile();
            xmlTF.setText(xmlFile.toURI().toString());
        }
    }//GEN-LAST:event_xmlButtonActionPerformed
    
    private void currentDocumentOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_currentDocumentOptionActionPerformed
        xmlButton.setEnabled(false);
    }//GEN-LAST:event_currentDocumentOptionActionPerformed
    
    private void schemaButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_schemaButtonActionPerformed
        int answer = chooser.showOpenDialog(XPontusWindow.getInstance().getFrame());
        if(answer == javax.swing.JFileChooser.APPROVE_OPTION){
            schemaFile = chooser.getSelectedFile();
            schemaTF.setText(schemaFile.toURI().toString());
        }
    }//GEN-LAST:event_schemaButtonActionPerformed
    
    public boolean isValid(){
        boolean valid = true;
        sb = new StringBuffer();
        if(schemaFile == null){
            sb.append("* Please select a schema\n");
            valid = false;
        }
        if(xmlFile == null){
            if(useExternalDocumentOption.isSelected()){
                sb.append("* Please select a XML document\n");
                valid = false;
            }
        }
        
        return valid;
    }
    public void validateSchema(){
        // create a RELAX NG validator
        try {
            XPontusWindow.getInstance().append(ConsoleOutputWindow.MESSAGES_WINDOW, "---------------------");
            XPontusWindow.getInstance().append(ConsoleOutputWindow.MESSAGES_WINDOW, "Schema validation");
            
            VerifierFactory factory = new com.sun.msv.verifier.jarv.TheFactoryImpl();
            
            // compile a schema
            Schema schema = factory.compileSchema(schemaFile.toURL().toExternalForm());
            
            InputSource src = null;
            
            // parse the document
            
            if(currentDocumentOption.isSelected()){
                String texte = XPontusWindow.getInstance().getCurrentEditor().getText();
                StringReader sr = new StringReader(texte);
                src=new InputSource(sr);
            } else{
                src = new InputSource(new java.io.FileInputStream(xmlFile));
            }
            
            Verifier verifier = schema.newVerifier(); 
            boolean b = verifier.verify(src);
             
            
            if(b){                
                XPontusWindow.getInstance().getStatusBar().setOperationMessage("The document is valid!");
            } 
        } 
        catch(ValidityViolation e){
            XPontusWindow.getInstance().getStatusBar().setOperationMessage("The document is invalid!");
            StringBuffer msg = new StringBuffer();
            msg.append("The document is invalid!");
            msg.append("\n Error at line : " + e.getLineNumber() + ",column:" + e.getColumnNumber());
            msg.append("\n" + e.getMessage()); 
            XPontusWindow.getInstance().getConsole().setFocus(ConsoleOutputWindow.ERRORS_WINDOW);
              XPontusWindow.getInstance().append(ConsoleOutputWindow.ERRORS_WINDOW, msg.toString());
            
            
        }
        catch (Exception e) {
            XPontusWindow.getInstance().getConsole().setFocus(ConsoleOutputWindow.ERRORS_WINDOW);
           XPontusWindow.getInstance().append(ConsoleOutputWindow.ERRORS_WINDOW, e.getMessage());
        }
    }
     
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JFileChooser chooser;
    private javax.swing.JButton closeButton;
    private javax.swing.JRadioButton currentDocumentOption;
    private javax.swing.ButtonGroup group;
    private javax.swing.JButton schemaButton;
    private javax.swing.JTextField schemaTF;
    private javax.swing.JRadioButton useExternalDocumentOption;
    private javax.swing.JButton validateButton;
    private javax.swing.JButton xmlButton;
    private javax.swing.JTextField xmlTF;
    // End of variables declaration//GEN-END:variables
    
    private java.io.File xmlFile, schemaFile;
    private StringBuffer sb;
}