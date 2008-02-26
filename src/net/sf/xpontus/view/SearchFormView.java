/*
 * SearchFormView.java
 *
 * Created on February 19, 2006, 12:09 PM
 *
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
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

import net.sf.xpontus.controller.handlers.FindFormController;
import net.sf.xpontus.core.controller.handlers.BaseController;
import net.sf.xpontus.core.utils.L10nHelper;

/**
 * The search/replace dialog
 * @author  Yves Zoundi
 */
public class SearchFormView extends javax.swing.JDialog implements java.awt.event.ActionListener{
    
    /** Creates new form SearchFormView 
     * @param parent the parent
     * @param modal modal dialog
     */
    public SearchFormView(java.awt.Frame parent, boolean modal) {
       super(parent, modal);
        locale = L10nHelper.getInstance();
        initComponents();
        initListeners();
    }
    
    /** default constructor*/
    public SearchFormView(){
        this(XPontusWindow.getInstance().getFrame(), false);
    }
    
    /**
     * 
     * @param b 
     */
      public void setVisible(boolean b){
        if(b){
            javax.swing.JEditorPane edit = XPontusWindow.getInstance().getCurrentEditor();
            if(edit.getSelectedText()!=null){
                findList.addItem(edit.getSelectedText());
                findList.setSelectedItem(edit.getSelectedText());
            }
        }
        super.setVisible(b);
    }
    /**
     * 
     * @param evt Event
     */
    public void actionPerformed(java.awt.event.ActionEvent evt){
        controller.invokeAction(evt.getActionCommand());
    }
    
    /** init listeners*/
    private void initListeners(){
        controller = new FindFormController(this); 
        
        findButton.addActionListener(this);
        replaceAllButton.addActionListener(this);
        replaceButton.addActionListener(this);
        closeButton.addActionListener(this);
    }
    
    /**
     * 
     * @return The string to look for
     */
    public String getFindString(){
        return (String)findList.getSelectedItem();
    }
    
    /**
     * 
     * @return The find string replacement
     */
    public String getReplaceString(){
        return (String)replaceList.getSelectedItem();
    }
    
    /**
     * 
     * @return case sentitive search
     */
    public boolean isMatchCase(){
        return matchCaseOption.isSelected();
    }
    
    /**
     * 
     * @return search up or down
     */
    public boolean isDownDirection(){
        return downOption.isSelected();
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        directionGroup = new javax.swing.ButtonGroup();
        findLabel = new javax.swing.JLabel();
        findList = new MemoryComboBox();
        findButton = new javax.swing.JButton();
        replaceLabel = new javax.swing.JLabel();
        replaceList = new MemoryComboBox();
        replaceButton = new javax.swing.JButton();
        upOption = new javax.swing.JCheckBox();
        matchCaseOption = new javax.swing.JCheckBox();
        downOption = new javax.swing.JCheckBox();
        wrapOption = new javax.swing.JCheckBox();
        replaceAllButton = new javax.swing.JButton();
        closeButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(locale.getValue("action.search.name"));
        findLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        findLabel.setText(locale.getValue("find.label"));
        findLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        findButton.setText(locale.getValue("find.key"));
        findButton.setActionCommand("find");

        replaceLabel.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        replaceLabel.setText(locale.getValue("replace.label"));
        replaceLabel.setHorizontalTextPosition(javax.swing.SwingConstants.LEFT);

        replaceButton.setText(locale.getValue("replace.key"));
        replaceButton.setActionCommand("replace");

        directionGroup.add(upOption);
        upOption.setText(locale.getValue("up.key"));
        upOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        upOption.setMargin(new java.awt.Insets(0, 0, 0, 0));

        matchCaseOption.setText(locale.getValue("matchcase.key"));
        matchCaseOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        matchCaseOption.setMargin(new java.awt.Insets(0, 0, 0, 0));

        directionGroup.add(downOption);
        downOption.setSelected(true);
        downOption.setText(locale.getValue("down.key"));
        downOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        downOption.setMargin(new java.awt.Insets(0, 0, 0, 0));

        wrapOption.setText(locale.getValue("wrap.label"));
        wrapOption.setBorder(javax.swing.BorderFactory.createEmptyBorder(0, 0, 0, 0));
        wrapOption.setMargin(new java.awt.Insets(0, 0, 0, 0));

        replaceAllButton.setText(locale.getValue("replaceall.key"));
        replaceAllButton.setActionCommand("replaceall");

        closeButton.setText(locale.getValue("close.key"));
        closeButton.setActionCommand("close");

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(20, 20, 20)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(findLabel)
                    .add(replaceLabel)
                    .add(upOption)
                    .add(downOption))
                .add(28, 28, 28)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(replaceList, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .add(findList, 0, 181, Short.MAX_VALUE))
                    .add(matchCaseOption)
                    .add(wrapOption))
                .add(37, 37, 37)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(replaceAllButton)
                    .add(findButton)
                    .add(replaceButton)
                    .add(closeButton))
                .addContainerGap(25, Short.MAX_VALUE))
        );

        layout.linkSize(new java.awt.Component[] {findList, replaceList}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.linkSize(new java.awt.Component[] {closeButton, findButton, replaceAllButton, replaceButton}, org.jdesktop.layout.GroupLayout.HORIZONTAL);

        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(28, 28, 28)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(findLabel)
                    .add(findList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(findButton))
                .add(29, 29, 29)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(replaceLabel)
                    .add(replaceList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                    .add(replaceButton))
                .add(35, 35, 35)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(upOption)
                    .add(matchCaseOption)
                    .add(replaceAllButton))
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(45, 45, 45)
                        .add(downOption))
                    .add(layout.createSequentialGroup()
                        .add(39, 39, 39)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                            .add(wrapOption)
                            .add(closeButton))))
                .addContainerGap(69, Short.MAX_VALUE))
        );
        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton closeButton;
    private javax.swing.ButtonGroup directionGroup;
    private javax.swing.JCheckBox downOption;
    private javax.swing.JButton findButton;
    private javax.swing.JLabel findLabel;
    private javax.swing.JComboBox findList;
    private javax.swing.JCheckBox matchCaseOption;
    private javax.swing.JButton replaceAllButton;
    private javax.swing.JButton replaceButton;
    private javax.swing.JLabel replaceLabel;
    private javax.swing.JComboBox replaceList;
    private javax.swing.JCheckBox upOption;
    private javax.swing.JCheckBox wrapOption;
    // End of variables declaration//GEN-END:variables
    private BaseController controller;
    private L10nHelper locale;
}
