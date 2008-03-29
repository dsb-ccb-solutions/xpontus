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

import java.awt.BorderLayout;
import java.awt.Dimension;
import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import net.sf.xpontus.plugins.SimplePluginDescriptor;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrBuilder;

import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.util.IoUtil;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.net.URL;

import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTMLEditorKit;
import net.sf.xpontus.controllers.impl.PopupHandler;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class InstalledPluginsAccessoryPanel extends JComponent {

    private JButton uninstallButton;
    private BrowserPanel panel;

    public InstalledPluginsAccessoryPanel(BrowserPanel m_panel) {
        panel = m_panel;
        setLayout(new FlowLayout(FlowLayout.LEFT));
        uninstallButton = new JButton("Uninstall");
        add(uninstallButton);

        uninstallButton.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                 

                if (true) {
                    JDialog fParent = (JDialog)SwingUtilities.getWindowAncestor(InstalledPluginsAccessoryPanel.this);
                    final JDialog f = new JDialog(fParent, true);
                    f.setTitle("Information about that missing feature");
                    JScrollPane m_scroller = new JScrollPane();
                    final JEditorPane m_editorPane = new JEditorPane();
                    m_editorPane.setContentType("text/html");
                    m_editorPane.setEditorKit(new HTMLEditorKit());
                    m_editorPane.setEditable(false);
                    JPopupMenu popup = new JPopupMenu();
                    popup.add(new AbstractAction("Copy") { 
                        public void actionPerformed(ActionEvent e) {
                            m_editorPane.copy();
                        }
                    });
                    m_editorPane.addMouseListener(new PopupHandler(popup));
                    m_editorPane.setText(new PluginsTemplateRenderer().renderMissingFeatureTemplate());
                    Dimension dim = new Dimension(400, 400);
                    m_scroller.setMinimumSize(dim);
                    m_scroller.setPreferredSize(dim);
                    
                    m_scroller.getViewport().add(m_editorPane);
                    
                    
                    JButton fCloseButton = new JButton("Close window");
                    fCloseButton.addActionListener(new ActionListener() {

                        public void actionPerformed(ActionEvent e) {
                            f.setVisible(false);
                        }
                    });
                    JPanel fPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
                    fPanel.add(fCloseButton);
                    
                    f.getContentPane().add(m_scroller, BorderLayout.CENTER);
                    f.getContentPane().add(fPanel, BorderLayout.SOUTH);
                    
                    f.pack();
                    
                    f.setLocationRelativeTo(fParent);
                    f.setVisible(true); 

                    return;
                }

                JTable m_table = panel.getTable();
                int selected = m_table.getSelectedRow();

                if (selected != -1) {
                    DefaultTableModel m_model = (DefaultTableModel) m_table.getModel();
                    String pluginId = (String) m_model.getValueAt(selected,
                            1);
                    Map<String, SimplePluginDescriptor> pluginsMap = panel.getPluginsHashMap();

                    SimplePluginDescriptor spd = pluginsMap.get(pluginId);

                    PluginRegistry registry = XPontusPluginManager.getPluginManager().getRegistry();
                    PluginDescriptor m_pluginDescriptor = registry.getPluginDescriptor(pluginId);
                    URL m_pluginDescriptorLocation = m_pluginDescriptor.getLocation();
                    File m_pluginDescriptorFile = IoUtil.url2file(m_pluginDescriptorLocation); 

                    try {
                        try {
                            registry.unregister(new String[]{pluginId});
                        } catch (Exception unregisterException) {
                            StrBuilder sb = new StrBuilder();
                            sb.append(
                                    "Unable to uninstall plugin with plugin id:" +
                                    spd.getId());
                            sb.appendNewLine();
                            sb.append(unregisterException.getMessage());

                            Exception m_exception = new Exception(sb.toString());
                            throw m_exception;
                        }

                        try {
                            FileUtils.forceDelete(m_pluginDescriptorFile);
                            XPontusComponentsUtils.showInformationMessage("The plugin will be uninstall when xpontus restarts....");
                        } catch (Exception err) {
                            StrBuilder sb = new StrBuilder();
                            sb.append(
                                    "Unable to delete plugin installation");
                            sb.append(" directory for plugin with id:\n It might have plugins requiring it..");
                            sb.append(spd.getId());
                            sb.appendNewLine();
                            sb.append(err.getMessage());
                            throw new Exception(sb.toString());
                        }

                        panel.reloadPluginsTable();
                    } catch (Exception err) {
                        XPontusComponentsUtils.showErrorMessage(err.getMessage());
                    }
                }
            }
            });
    }
}
