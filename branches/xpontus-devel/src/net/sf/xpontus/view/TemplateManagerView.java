/*
 * TemplateManagerView.java
 *
 * Created on July 22, 2006, 9:32 PM
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
import java.beans.EventHandler;
import net.sf.xpontus.controller.handlers.TemplateManagerHandler;

/**
 * Controller to manage the template manager dialog
 * @author  Yves Zoundi
 */
public class TemplateManagerView extends javax.swing.JDialog {
    
    /**
     * Creates new form TemplateManagerView
     */
    public TemplateManagerView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);        
        controller = new TemplateManagerHandler(this);
        initComponents();
    }
    
    public TemplateManagerView(){
        this(XPontusWindow.getInstance().getFrame(), true);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        scrollPane = new javax.swing.JScrollPane();
        templateList = new javax.swing.JList();
        moveUpButton = new javax.swing.JButton();
        moveDownButton = new javax.swing.JButton();
        deleteButton = new javax.swing.JButton();
        openButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(XPontusWindow.getInstance().getI18nMessage("manage.template.key"));
        scrollPane.setViewportView(templateList);

        moveUpButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/xpontus/icons/Sun/Up16.gif")));
        moveUpButton.setText(XPontusWindow.getInstance().getI18nMessage("up.key"));
        moveUpButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        moveUpButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, controller, TemplateManagerHandler.MOVE_UP_METHOD));

        moveDownButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/xpontus/icons/Sun/Down16.gif")));
        moveDownButton.setText(XPontusWindow.getInstance().getI18nMessage("down.key"));
        moveDownButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        moveDownButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, controller, TemplateManagerHandler.MOVE_DOWN_METHOD));

        deleteButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/xpontus/icons/Sun/delete16.gif")));
        deleteButton.setText(XPontusWindow.getInstance().getI18nMessage("delete.key"));
        deleteButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        deleteButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, controller, TemplateManagerHandler.DELETE_METHOD));

        openButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/xpontus/icons/Sun/open16.gif")));
        openButton.setText(XPontusWindow.getInstance().getI18nMessage("action.open.description"));
        openButton.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        openButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, controller, TemplateManagerHandler.OPEN_METHOD));

        closeButton.setText(XPontusWindow.getInstance().getI18nMessage("close.key"));
        closeButton.addActionListener((ActionListener)EventHandler.create(ActionListener.class, controller, TemplateManagerHandler.CLOSE_METHOD));

        jLabel1.setText(XPontusWindow.getInstance().getI18nMessage("manage.template.key"));

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                            .add(closeButton)
                            .add(scrollPane, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 216, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                        .add(30, 30, 30)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(openButton)
                            .add(deleteButton)
                            .add(moveDownButton)
                            .add(moveUpButton)))
                    .add(jLabel1))
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {closeButton, deleteButton, moveDownButton, moveUpButton, openButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(9, 9, 9)
                .add(jLabel1)
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                    .add(layout.createSequentialGroup()
                        .add(moveUpButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(moveDownButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(deleteButton)
                        .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                        .add(openButton))
                    .add(scrollPane, 0, 0, Short.MAX_VALUE))
                .add(20, 20, 20)
                .add(closeButton)
                .addContainerGap(org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
  
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.JButton deleteButton;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JButton moveDownButton;
    private javax.swing.JButton moveUpButton;
    private javax.swing.JButton openButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JList templateList;
    // End of variables declaration//GEN-END:variables
    private Object controller;
}