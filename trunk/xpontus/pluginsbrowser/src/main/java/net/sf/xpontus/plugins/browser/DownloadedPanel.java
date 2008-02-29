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
import java.awt.FlowLayout;
import java.awt.LayoutManager;
import java.awt.event.ActionListener;

import java.beans.EventHandler;

import java.util.Vector;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
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

    public DownloadedPanel() {
        setLayout(new BorderLayout());

        ctrl = new InstallDownloadedPluginsController();
        ctrl.setView(this);

        Dimension dim = new Dimension(200, 200);

        addPluginsButton = new JButton("Add plugins...");

        addPluginsButton.addActionListener((ActionListener) EventHandler.create(
                ActionListener.class, ctrl, "addPlugin"));

        installPluginsButton = new JButton("Install");

        LayoutManager layout = new FlowLayout(FlowLayout.LEFT);

        JPanel panel = new JPanel(layout);

        panel.add(addPluginsButton);

        add(panel, BorderLayout.NORTH);

        Vector columns = new Vector(3);
        columns.add("Identifier");
        columns.add("Category");
        columns.add("Built-in");

        tableModel = new DefaultTableModel(new Vector(), columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        pluginDetailsTable = new JTable(tableModel);
        pluginDetailsTable.setMinimumSize(dim);
        pluginDetailsTable.setPreferredSize(dim);

        detailsScrollPane = new JScrollPane(pluginDetailsTable);

        pluginDescriptionPane = new JEditorPane();
        pluginDescriptionPane.setEditable(false);
        pluginDescriptionPane.setEditorKit(new HTMLEditorKit());
        pluginDescriptionPane.setMinimumSize(dim);
        pluginDescriptionPane.setPreferredSize(dim);

        descriptionScrollPane = new JScrollPane(pluginDescriptionPane);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                detailsScrollPane, pluginDescriptionPane);
        splitPane.setDividerLocation(0.5);
        splitPane.setOneTouchExpandable(true);
        splitPane.setContinuousLayout(true);

        add(splitPane, BorderLayout.CENTER);

        panel = new JPanel(layout);
        panel.add(installPluginsButton);
        add(panel, BorderLayout.SOUTH);
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
