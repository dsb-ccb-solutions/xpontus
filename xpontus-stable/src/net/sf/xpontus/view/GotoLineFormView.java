/*
 * GotoLineFormView.java
 *
 * Created on 30 juillet 2005, 17:09
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


import java.text.NumberFormat;
import net.sf.xpontus.controller.handlers.GotoLineFormController;
import net.sf.xpontus.core.controller.handlers.BaseController;
import net.sf.xpontus.core.utils.L10nHelper;
/**
 * A dialog to move the cursor to a specified line
 * @author Yves Zoundi
 */
public class GotoLineFormView extends javax.swing.JDialog {
    
    private L10nHelper locale;
    
    /**
     * Creates new form GotoLineFormView
     * @param parent The parent window
     * @param modal specify if the window should stay on top
     */
    public GotoLineFormView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        locale = L10nHelper.getInstance();
        initComponents();
        controller = new GotoLineFormController(this); 
        lineTF.setText(new Integer(1) + "");
    }
    
    public int getLine(){
        return Integer.parseInt(lineTF.getText());
    }
    
    public GotoLineFormView(){
        this(XPontusWindow.getInstance().getFrame(), false);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonsPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        mainPanel = new javax.swing.JPanel();
        gotoButton = new javax.swing.JButton();
        lineTF = new javax.swing.JFormattedTextField(NumberFormat.getNumberInstance());

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(locale.getValue("action.gotoline.name"));
        setResizable(false);
        closeButton.setText(locale.getValue("close.key"));
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButton_OnClick(evt);
            }
        });

        buttonsPanel.add(closeButton);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        gotoButton.setText(locale.getValue("action.gotoline.name"));
        gotoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                gotoButton_OnClick(evt);
            }
        });

        mainPanel.add(gotoButton);

        lineTF.setColumns(10);
        mainPanel.add(lineTF);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        pack();
    }
    // </editor-fold>//GEN-END:initComponents

    private void gotoButton_OnClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_gotoButton_OnClick
        controller.invokeAction("gotoline");
    }//GEN-LAST:event_gotoButton_OnClick
    
    private void closeButton_OnClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButton_OnClick
       controller.invokeAction("close");
    }//GEN-LAST:event_closeButton_OnClick
   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JButton gotoButton;
    private javax.swing.JFormattedTextField lineTF;
    private javax.swing.JPanel mainPanel;
    // End of variables declaration//GEN-END:variables
    private BaseController controller;
}