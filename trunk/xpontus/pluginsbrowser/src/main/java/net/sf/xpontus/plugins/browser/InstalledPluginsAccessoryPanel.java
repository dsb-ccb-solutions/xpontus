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

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;


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
                    StrBuilder tempMessage = new StrBuilder();
                    tempMessage.append("This feature has been disabled for now");
                    tempMessage.appendNewLine();
                    tempMessage.append(
                        "The uninstallation cannot delete the plugin directory");
                    tempMessage.appendNewLine();
                    tempMessage.append("This will be hopefully solved soon");

                    if (true) {
                        XPontusComponentsUtils.showInformationMessage(tempMessage.toString());

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

                        PluginRegistry registry = XPontusPluginManager.getPluginManager()
                                                                      .getRegistry();
                        PluginDescriptor m_pluginDescriptor = registry.getPluginDescriptor(pluginId);
                        URL m_pluginDescriptorLocation = m_pluginDescriptor.getLocation();
                        File m_pluginDescriptorFile = IoUtil.url2file(m_pluginDescriptorLocation);
                        File m_pluginInstallationDirectory = m_pluginDescriptorFile.getParentFile();

                        try {
                            try {
                                registry.unregister(new String[] { pluginId });
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
                                FileUtils.forceDelete(m_pluginInstallationDirectory);
                            } catch (Exception err) {
                                StrBuilder sb = new StrBuilder();
                                sb.append(
                                    "Unable to delete plugin installation");
                                sb.append(" directory for plugin with id:");
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
