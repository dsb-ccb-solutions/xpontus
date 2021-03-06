/*
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *
 */
package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;


/**
 *
 * @author Yves Zoundi
 */
public class PluginBrowser extends javax.swing.JDialog {
    
	private static final long serialVersionUID = -4870236691256218236L;
	private JTabbedPane tabbedPane;
    private JComponent downloadedPanel;
    private BrowserPanel installedPanel;
    private BrowserPanel availablePanel;
    private JComponent buttonsPanel;
    private JButton closeButton;
    private JComponent noticePanel;
    private JLabel noticeLabel;

    /**
     * Creates new form PBView
     * @param parent
     * @param modal
     */
    public PluginBrowser(java.awt.Frame parent, boolean modal) {
        super(parent, modal);
        initComponents();
        this.pack();
    }

    /**
     * Default constructor
     */
    public PluginBrowser() {
        this((Frame) XPontusComponentsUtils.getTopComponent()
                                           .getDisplayComponent(), true);
    }

    private void initComponents() {
        setLayout(new BorderLayout());

        setTitle("Plugins Manager (Still under heavy development)");

        this.tabbedPane = new JTabbedPane();

        this.buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        this.closeButton = new JButton("Close");
        this.closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    setVisible(false);
                }
            });
        this.buttonsPanel.add(closeButton);

        this.noticeLabel = new JLabel(
                "The plugins browser is under heavy development");
        this.noticePanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        this.downloadedPanel = new DownloadedPanel();
        this.installedPanel = new BrowserPanel(new InstalledPluginsResolver(),
                "Uninstall",
                XPontusConfigurationConstantsIF.INSTALLED_PLUGINS_SEARCHINDEX_DIR.getAbsolutePath());
        this.installedPanel.setAccessory(new InstalledPluginsAccessoryPanel(
                installedPanel));
//        this.availablePanel = new BrowserPanel(new AvailablePluginsResolver(),
//                "Install",
//                XPontusConfigurationConstantsIF.AVAILABLE_PLUGINS_SEARCHINDEX_DIR.getAbsolutePath());
//        tabbedPane.addTab("Available(" + availablePanel.getNbPlugins() + ")",
//            availablePanel);
        tabbedPane.addTab("Downloaded", downloadedPanel);
        tabbedPane.addTab("Installed(" + installedPanel.getNbPlugins() + ")",
            installedPanel);

        add(noticePanel, BorderLayout.NORTH);
        add(tabbedPane, BorderLayout.CENTER);
        add(buttonsPanel, BorderLayout.SOUTH);

        pack();
    }
}
