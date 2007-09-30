/*
 * PluginBrowserView.java
 *
 * Created on 2007-08-07, 16:07:34
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JDialog;


/**
 *
 * @author YVZOU
 */
public class PluginBrowserView extends JDialog
{
    private javax.swing.JButton jButton1;
    private javax.swing.JPanel jPanel2;

    /** Creates new form PluginBrowserView
     * @param parent
     * @param modal
     */
    public PluginBrowserView(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);
        initComponents();

        javax.swing.JPanel panel = new MainPanel();

        getContentPane().add(panel, java.awt.BorderLayout.CENTER);

        jButton1.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent arg0)
                {
                    PluginBrowserView.this.setVisible(false);
                }
            });
    }

    public PluginBrowserView()
    {
        this((Frame) XPontusComponentsUtils.getTopComponent()
                                           .getDisplayComponent(), true);

        this.setTitle("Plugin Browser");
    }

    private void initComponents()
    {
        jPanel2 = new javax.swing.JPanel();
        jButton1 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("PluginBrowser");

        jButton1.setText("Fermer");
        jPanel2.add(jButton1);

        getContentPane().add(jPanel2, java.awt.BorderLayout.SOUTH);

        pack();
    }
}
