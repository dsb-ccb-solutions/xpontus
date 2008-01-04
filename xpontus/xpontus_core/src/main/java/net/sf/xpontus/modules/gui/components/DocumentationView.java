/*
 * DocumentationView.java
 *
 * Created on February 11, 2007, 9:08 PM
 */
package net.sf.xpontus.modules.gui.components;

import com.jgoodies.binding.adapter.Bindings;
import com.jgoodies.binding.adapter.ComboBoxAdapter;
import com.jgoodies.binding.beans.BeanAdapter;
import com.jgoodies.binding.value.ValueModel;
import java.awt.Frame;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import net.sf.xpontus.controllers.impl.DocumentationControllerImpl;
import net.sf.xpontus.model.DocumentationModel;
import net.sf.xpontus.plugins.gendoc.DocConfiguration;
import net.sf.xpontus.utils.XPontusComponentsUtils;

/**
 *
 * @author  Owner
 */
public class DocumentationView extends javax.swing.JDialog {

    /** Creates new form DocumentationView
     * @param parent
     * @param modal 
     */
    public DocumentationView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        controller = new DocumentationControllerImpl(this);
        model = new DocumentationModel();
        adapter = new BeanAdapter(model, true);
        
        ValueModel vm = adapter.getValueModel("type");
        typeAdapter = new ComboBoxAdapter(DocConfiguration.getInstane().getEnginesNames(), vm);
        initComponents();
        
        if(docTypeList.getModel().getSize() > 0){
            this.docTypeList.setSelectedIndex(0);
        }
    }

    public DocumentationView() {
        this((Frame) XPontusComponentsUtils.getTopComponent().getDisplayComponent(), true);
    }

    public DocumentationModel getModel() {
        return model;
    }

    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        mainPanel = new javax.swing.JPanel();
        docTitleTF = new javax.swing.JTextField();
        docTitleLabel = new javax.swing.JLabel();
        srcButton = new javax.swing.JButton();
        srcTF = new javax.swing.JTextField();
        destButton = new javax.swing.JButton();
        destTF = new javax.swing.JTextField();
        headerTF = new javax.swing.JTextField();
        footerTF = new javax.swing.JTextField();
        cssButton = new javax.swing.JButton();
        cssTF = new javax.swing.JTextField();
        docTypeLabel = new javax.swing.JLabel();
        docTypeList = new javax.swing.JComboBox();
        noticeTF = new javax.swing.JLabel();
        headerLabel = new javax.swing.JLabel();
        footerLabel = new javax.swing.JLabel();
        buttonsPanel = new javax.swing.JPanel();
        generateButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Documentation generator");

        Bindings.bind(docTitleTF, adapter.getValueModel("title"));

        docTitleLabel.setText("Documentation title");

        srcButton.setText("Source directory ...");
        srcButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        srcButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                DocumentationControllerImpl.INPUT_METHOD)
        );

        srcTF.setEditable(false);
        Bindings.bind(srcTF, adapter.getValueModel("input"));

        destButton.setText("Destination directory ...");
        destButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        destButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                DocumentationControllerImpl.OUTPUT_METHOD)
        );

        destTF.setEditable(false);
        Bindings.bind(destTF, adapter.getValueModel("output"));

        Bindings.bind(headerTF, adapter.getValueModel("header"));

        Bindings.bind(footerTF, adapter.getValueModel("footer"));

        cssButton.setText("CSS stylesheet ...");
        cssButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        cssButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                DocumentationControllerImpl.CSS_METHOD)
        );

        cssTF.setEditable(false);
        Bindings.bind(cssTF, adapter.getValueModel("css"));

        docTypeLabel.setText("Documentation type");

        docTypeList.setModel(typeAdapter);

        noticeTF.setFont(new java.awt.Font("Dialog", 1, 10));
        noticeTF.setForeground(new java.awt.Color(0, 0, 204));
        noticeTF.setText("* The parameters header, footer, and css stylesheet are mandatories");

        headerLabel.setText("Header");

        footerLabel.setText("Footer");

        org.jdesktop.layout.GroupLayout mainPanelLayout = new org.jdesktop.layout.GroupLayout(mainPanel);
        mainPanel.setLayout(mainPanelLayout);
        mainPanelLayout.setHorizontalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(mainPanelLayout.createSequentialGroup()
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                                .add(cssButton, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .add(docTypeLabel))
                            .add(destButton)
                            .add(docTitleLabel)
                            .add(srcButton)
                            .add(headerLabel)
                            .add(footerLabel))
                        .add(54, 54, 54)
                        .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(docTitleTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .add(srcTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .add(destTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .add(headerTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .add(footerTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 270, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(cssTF, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                            .add(docTypeList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 133, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                    .add(noticeTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 461, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        mainPanelLayout.linkSize(new java.awt.Component[] {cssButton, destButton, srcButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        mainPanelLayout.linkSize(new java.awt.Component[] {cssTF, destTF, docTitleTF, footerTF, headerTF, srcTF}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        mainPanelLayout.setVerticalGroup(
            mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, mainPanelLayout.createSequentialGroup()
                .addContainerGap()
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(docTypeLabel)
                    .add(docTypeList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(29, 29, 29)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(docTitleLabel)
                    .add(docTitleTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(17, 17, 17)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(srcTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(srcButton))
                .add(27, 27, 27)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(destTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(destButton))
                .add(29, 29, 29)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(headerTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(headerLabel))
                .add(34, 34, 34)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(footerTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(footerLabel))
                .add(24, 24, 24)
                .add(mainPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(cssButton)
                    .add(cssTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(17, 17, 17)
                .add(noticeTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 26, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(26, Short.MAX_VALUE))
        );

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        generateButton.setText("Generate");
        generateButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                DocumentationControllerImpl.HANDLE_METHOD)
        );
        buttonsPanel.add(generateButton);

        cancelButton.setText("Cancel");
        cancelButton.addActionListener(
            (ActionListener)EventHandler.create(
                ActionListener.class,
                controller,
                DocumentationControllerImpl.CLOSE_METHOD)
        );
        buttonsPanel.add(cancelButton);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton cssButton;
    private javax.swing.JTextField cssTF;
    private javax.swing.JButton destButton;
    private javax.swing.JTextField destTF;
    private javax.swing.JLabel docTitleLabel;
    private javax.swing.JTextField docTitleTF;
    private javax.swing.JLabel docTypeLabel;
    private javax.swing.JComboBox docTypeList;
    private javax.swing.JLabel footerLabel;
    private javax.swing.JTextField footerTF;
    private javax.swing.JButton generateButton;
    private javax.swing.JLabel headerLabel;
    private javax.swing.JTextField headerTF;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JLabel noticeTF;
    private javax.swing.JButton srcButton;
    private javax.swing.JTextField srcTF;
    // End of variables declaration//GEN-END:variables
    private ComboBoxAdapter typeAdapter;
    private DocumentationControllerImpl controller;
    private DocumentationModel model;
    private BeanAdapter adapter;
}
