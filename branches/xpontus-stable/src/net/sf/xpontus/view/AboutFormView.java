/*
 * AboutForm.java
 *
 * Created on 13 juillet 2005, 18:47
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

import net.sf.xpontus.core.utils.L10nHelper;





/**
 * The about dialog window
 * @author  Yves Zoundi
 */
public class AboutFormView extends javax.swing.JDialog {
    
    /**
     * Creates new form AboutFormView
     * @param parent the parent window
     * @param modal modal dialog
     */
    public AboutFormView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        locale = L10nHelper.getInstance();
        tableModel = new net.sf.xpontus.core.model.EnvironmentModel();
        initComponents();        
    }
    
    /**
     * default constructor
     */
    public AboutFormView(){
        this(XPontusWindow.getInstance().getFrame(), true);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        pane = new javax.swing.JTabbedPane();
        informationPanel = new javax.swing.JPanel();
        logoLabel = new javax.swing.JLabel();
        descriptionLabel = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        environmentPanel = new javax.swing.JPanel();
        scroller = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        licensePanel = new javax.swing.JPanel();
        scrollPane = new javax.swing.JScrollPane();
        licenseField = new javax.swing.JTextArea();
        componentsPanel = new javax.swing.JPanel();
        componentsScrollPane = new javax.swing.JScrollPane();
        componentsTable = new javax.swing.JTable();
        panel = new javax.swing.JPanel();
        okButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle(locale.getValue("form.about.title"));
        setResizable(false);
        pane.setMaximumSize(null);
        logoLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        logoLabel.setIcon(new javax.swing.ImageIcon(getClass().getResource("/net/sf/xpontus/icons/Sun/logo.png")));

        descriptionLabel.setFont(new java.awt.Font("Dialog", 1, 11));
        descriptionLabel.setForeground(new java.awt.Color(0, 102, 204));
        descriptionLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        descriptionLabel.setText(locale.getValue("form.about.description"));

        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("<html>\n<b>Homepage</b> : \n<a href = \"http://xpontus.sourceforge.net\">\nhttp://xpontus.sourceforge.net\n</a>\n</html>");

        jLabel2.setText(locale.getValue("author.key"));

        org.jdesktop.layout.GroupLayout informationPanelLayout = new org.jdesktop.layout.GroupLayout(informationPanel);
        informationPanel.setLayout(informationPanelLayout);
        informationPanelLayout.setHorizontalGroup(
            informationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(descriptionLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 484, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(logoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 484, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
            .add(org.jdesktop.layout.GroupLayout.TRAILING, informationPanelLayout.createSequentialGroup()
                .addContainerGap(107, Short.MAX_VALUE)
                .add(informationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel2, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .add(org.jdesktop.layout.GroupLayout.LEADING, jLabel1, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .add(101, 101, 101))
        );
        informationPanelLayout.setVerticalGroup(
            informationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(informationPanelLayout.createSequentialGroup()
                .add(informationPanelLayout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(descriptionLabel)
                    .add(informationPanelLayout.createSequentialGroup()
                        .add(14, 14, 14)
                        .add(logoLabel, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 129, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(jLabel2)
                .add(16, 16, 16)
                .add(jLabel1)
                .addContainerGap(17, Short.MAX_VALUE))
        );
        pane.addTab(locale.getValue("form.about.title"), informationPanel);

        environmentPanel.setLayout(new java.awt.GridLayout(1, 0));

        environmentPanel.setMaximumSize(new java.awt.Dimension(400, 300));
        environmentPanel.setMinimumSize(new java.awt.Dimension(460, 300));
        environmentPanel.setPreferredSize(new java.awt.Dimension(460, 300));
        scroller.setMinimumSize(new java.awt.Dimension(400, 300));
        scroller.setPreferredSize(new java.awt.Dimension(400, 300));
        table.setBackground(getBackground());
        table.setModel(tableModel);
        scroller.setViewportView(table);

        environmentPanel.add(scroller);

        pane.addTab(locale.getValue("environment.key"), environmentPanel);

        licensePanel.setLayout(new java.awt.GridLayout(1, 0));

        licensePanel.setBackground(getBackground());
        scrollPane.setMinimumSize(new java.awt.Dimension(460, 300));
        scrollPane.setPreferredSize(new java.awt.Dimension(460, 300));
        licenseField.setBackground(getBackground());
        licenseField.setEditable(false);
        licenseField.setLineWrap(true);
        licenseField.setWrapStyleWord(true);
        try{
            String path = "/net/sf/xpontus/info/license.txt";
            java.net.URL url = getClass().getResource(path);
            java.io.InputStream is = url.openStream();
            java.io.InputStreamReader ir = new java.io.InputStreamReader(is);
            java.io.BufferedReader in = new java.io.BufferedReader(ir);
            licenseField.read(in, null);
        } catch(Exception e){
            e.printStackTrace();
        }
        scrollPane.setViewportView(licenseField);

        licensePanel.add(scrollPane);

        pane.addTab("License", licensePanel);

        componentsPanel.setLayout(new java.awt.GridLayout(1, 0));

        componentsScrollPane.setMaximumSize(new java.awt.Dimension(400, 300));
        componentsScrollPane.setPreferredSize(new java.awt.Dimension(400, 300));
        componentsTable.setBackground(getBackground());
        componentsTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {"VLDocking Framework","2.0.5"},
                {"JavaHelp", "2.0.2"},
                {"Saxon", "6.5.4"},
                {"Xalan", "2.7.0"},
                {"Saxon8", "Saxon8B"}
            },
            new String [] {
                locale.getValue("form.about.component"),
                locale.getValue("form.about.version")
            }

        )
        {
            public boolean isCellEditable(int row, int column) {
                return false;
            }

        });
        componentsScrollPane.setViewportView(componentsTable);

        componentsPanel.add(componentsScrollPane);

        pane.addTab(locale.getValue("form.about.components"), componentsPanel);

        getContentPane().add(pane, java.awt.BorderLayout.CENTER);

        okButton.setText(locale.getValue("ok.key"));
        okButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                okButton_OnClick(evt);
            }
        });

        panel.add(okButton);

        getContentPane().add(panel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    
    private void okButton_OnClick(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButton_OnClick
        setVisible(false);
    }//GEN-LAST:event_okButton_OnClick
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel componentsPanel;
    private javax.swing.JScrollPane componentsScrollPane;
    private javax.swing.JTable componentsTable;
    private javax.swing.JLabel descriptionLabel;
    private javax.swing.JPanel environmentPanel;
    private javax.swing.JPanel informationPanel;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JTextArea licenseField;
    private javax.swing.JPanel licensePanel;
    private javax.swing.JLabel logoLabel;
    private javax.swing.JButton okButton;
    private javax.swing.JTabbedPane pane;
    private javax.swing.JPanel panel;
    private javax.swing.JScrollPane scrollPane;
    private javax.swing.JScrollPane scroller;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
    private javax.swing.table.TableModel tableModel;
    private L10nHelper locale;
}