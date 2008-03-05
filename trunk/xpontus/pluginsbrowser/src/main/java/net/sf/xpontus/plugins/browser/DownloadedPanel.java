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

import net.sf.xpontus.plugins.SimplePluginDescriptor;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import java.beans.EventHandler;

import java.io.File;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.html.HTMLEditorKit;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DownloadedPanel extends JComponent {
    private JButton addPluginsButton;
    private JButton installPluginsButton;
    private JSplitPane splitPane;
    private JTable pluginDetailsTable;
    private JEditorPane pluginDescriptionPane;
    private JScrollPane detailsScrollPane;
    private JScrollPane descriptionScrollPane;
    private DefaultTableModel tableModel;
    private InstallDownloadedPluginsController ctrl;
    private PluginsTemplateRenderer ptr;
    private Map<String, SimplePluginDescriptor> pluginsMap;
    private Map<String, File> filesMap;

    public DownloadedPanel() {
        setLayout(new BorderLayout());
        ptr = new PluginsTemplateRenderer();
        pluginsMap = new HashMap<String, SimplePluginDescriptor>();
        filesMap = new HashMap<String, File>();

        ctrl = new InstallDownloadedPluginsController();
        ctrl.setView(this);

        Dimension dim = new Dimension(200, 200);

        addPluginsButton = new JButton("Add plugins...");

        addPluginsButton.addActionListener((ActionListener) EventHandler.create(
                ActionListener.class, ctrl, "addPlugin"));

        installPluginsButton = new JButton("Install");

        installPluginsButton.addActionListener((ActionListener) EventHandler.create(
                ActionListener.class, ctrl, "installPlugin"));

        LayoutManager layout = new FlowLayout(FlowLayout.LEFT);

        JPanel panel = new JPanel(layout);

        panel.add(addPluginsButton);

        add(panel, BorderLayout.NORTH);

        Vector columns = new Vector();
        columns.add("Install");
        columns.add("Identifier");
        columns.add("Category");
        columns.add("Built-in");

        tableModel = new DefaultTableModel(new Vector(), columns) {
                    public Class getColumnClass(int c) {
                        return getValueAt(0, c).getClass();
                    }

                    public boolean isCellEditable(int row, int column) {
                        return (column == 0);
                    }
                };

        pluginDetailsTable = new JTable(tableModel);
        pluginDetailsTable.setMinimumSize(dim);
        pluginDetailsTable.setPreferredSize(dim);

        pluginDetailsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        detailsScrollPane = new JScrollPane(pluginDetailsTable);

        pluginDescriptionPane = new JEditorPane();
        pluginDescriptionPane.setEditable(false);
        pluginDescriptionPane.setEditorKit(new HTMLEditorKit());
        pluginDescriptionPane.setMinimumSize(dim);
        pluginDescriptionPane.setPreferredSize(dim);

        this.descriptionScrollPane = new JScrollPane(pluginDescriptionPane);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                detailsScrollPane, pluginDescriptionPane);
        splitPane.setDividerLocation(0.5);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);

        pluginDetailsTable.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    int row = pluginDetailsTable.getSelectedRow();

                    if (row != -1) {
                        String id = tableModel.getValueAt(row, 1).toString();
                        SimplePluginDescriptor spd = pluginsMap.get(id);
                        pluginDescriptionPane.setText(ptr.renderTemplate(spd));
                    }
                }
            });

        add(splitPane, BorderLayout.CENTER);

        panel = new JPanel(layout);
        panel.add(installPluginsButton);
        add(panel, BorderLayout.SOUTH);
    }

    public Map<String, File> getFilesMap() {
        return filesMap;
    }

    public JTable getPluginDetailsTable() {
        return pluginDetailsTable;
    }

    public Map<String, SimplePluginDescriptor> getPluginsMap() {
        return pluginsMap;
    }

    public JTable getPluginsTable() {
        return pluginDetailsTable;
    }

    public JButton getAddPluginsButton() {
        return addPluginsButton;
    }

    public JButton getInstallPluginsButton() {
        return installPluginsButton;
    }
}
