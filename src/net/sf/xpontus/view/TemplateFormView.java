/*
 * TemplateFormView.java
 *
 * Created on 1 ao�t 2005, 17:57
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

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import net.sf.xpontus.core.utils.L10nHelper;
import org.apache.commons.io.FilenameUtils;
//import org.syntax.jedit.tokenmarker.TokenMarker;

/**
 * The template dialog to create new files
 * @author  Yves Zoundi
 */
public class TemplateFormView extends javax.swing.JDialog {
    
    private L10nHelper locale;
    /** Creates new form TemplateFormView */
    public TemplateFormView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        locale = L10nHelper.getInstance();
        initComponents();
        templateList.setSelectedIndex(0);
        templateList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e){
                if(e.getClickCount() == 2){
                    okButton_Onclick(null);
                }
            }
        });
        
        
        templateList.addKeyListener(new KeyAdapter() {
            public void keyReleased(KeyEvent e){
                if(e.getKeyCode() == KeyEvent.VK_ENTER){
                    okButton_Onclick(null);
                }
            }
        });
    }
    
    public TemplateFormView(){
        this(XPontusWindow.getInstance().getFrame(), true);
    }
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        buttonsPanel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();
        cancelButton = new javax.swing.JButton();
        titleLabel = new javax.swing.JLabel();
        scrollPane = new javax.swing.JScrollPane();
        templateList = new javax.swing.JList();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(locale.getValue("form.template.title"));
        okButton.setText(locale.getValue("ok.key"));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButton_Onclick(evt);
            }
        });

        buttonsPanel.add(okButton);

        cancelButton.setText(locale.getValue("cancel.key"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButton_Onclick(evt);
            }
        });

        buttonsPanel.add(cancelButton);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        titleLabel.setText(locale.getValue("form.template.title"));
        getContentPane().add(titleLabel, java.awt.BorderLayout.NORTH);

        scrollPane.setBorder(javax.swing.BorderFactory.createEmptyBorder(4, 4, 4, 4));
        scrollPane.setMinimumSize(new java.awt.Dimension(460, 160));
        scrollPane.setPreferredSize(new java.awt.Dimension(460, 160));
        templateList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "XML", "XSL", "HTML", "Docbook XML", "XSL FO", "XML Schema", "Relax NG", "Apache Ant", "Other" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        scrollPane.setViewportView(templateList);

        getContentPane().add(scrollPane, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private final String templates[] = {
        "template.xml", "template.xsl", "template.html", "docbook.xml", "template.fo", "template.xsd", "template.rng", "ant-template.xml", ""
    };
    
    
    private void cancelButton_Onclick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButton_Onclick
        setVisible(false);
    }//GEN-LAST:event_cancelButton_Onclick
    
    private void okButton_Onclick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButton_Onclick
        try{
            setVisible(false);
            java.io.InputStream is;
            int index = templateList.getSelectedIndex();
            String tmpl = templates[index];
            if(tmpl.equals("")){
                XPontusWindow.getInstance().getPane().createEditorFromFile();
            } else{
                is = getClass().getResourceAsStream("/net/sf/xpontus/templates/" + tmpl);
                String ext = FilenameUtils.getExtension(templates[templateList.getSelectedIndex()]);
                XPontusWindow.getInstance().getPane().createEditorFromStream(is, ext);
            }
            
        } catch(Exception e){
            javax.swing.JOptionPane.showMessageDialog(
                    XPontusWindow.getInstance().getFrame(),
                    e.getLocalizedMessage(),
                    "Error",
                    javax.swing.JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_okButton_Onclick
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton okButton;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JList templateList;
    private javax.swing.JLabel titleLabel;
    // End of variables declaration//GEN-END:variables
    
}
