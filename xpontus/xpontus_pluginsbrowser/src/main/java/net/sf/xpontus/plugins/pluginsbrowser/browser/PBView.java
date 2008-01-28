/*
 * PBView.java
 *
 * Created on December 26, 2007, 11:08 PM
 */
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import java.awt.Frame;
import net.sf.xpontus.utils.XPontusComponentsUtils;

/**
 *
 * @author  mrcheeks
 */
public class PBView extends javax.swing.JDialog {

    /** Creates new form PBView */
    public PBView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
//        pane.addTab("Installed", new InstalledPluginsPanel());
        //  this.validate();
        InstalledPluginsPanel ipp = (InstalledPluginsPanel) pane.getComponentAt(0);
        int nb = ipp.getNbPlugins();
        pane.setTitleAt(0, "Installed(" + nb + ")");
        this.pack();
    }

    public PBView() {
        this((Frame) XPontusComponentsUtils.getTopComponent().getDisplayComponent(), true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        pane = new javax.swing.JTabbedPane();
        jPanel1 = new InstalledPluginsPanel();
        bottomPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Plugins browser (Under development)");

        jPanel1.setLayout(new java.awt.BorderLayout());
        pane.addTab("tab1", jPanel1);

        getContentPane().add(pane, java.awt.BorderLayout.CENTER);

        closeButton.setText("Close"); // NOI18N
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        bottomPanel.add(closeButton);

        getContentPane().add(bottomPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        setVisible(false);
    }//GEN-LAST:event_closeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel bottomPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JTabbedPane pane;
    // End of variables declaration//GEN-END:variables
}
