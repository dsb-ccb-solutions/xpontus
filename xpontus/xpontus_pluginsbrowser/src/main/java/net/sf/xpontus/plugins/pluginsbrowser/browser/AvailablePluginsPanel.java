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
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;
import javax.swing.text.html.HTMLEditorKit;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class AvailablePluginsPanel extends JComponent {
    private JScrollPane scrollPane;
    private JSplitPane splitPane;
    private JComponent buttonsPanel;
    private JTable table;
    private TableModel tableModel;
    private JPanel northPanel;
    private JButton installButton;
    private JButton reloadButton;
    private JTextField searchTextField;
    private JButton searchButton;
    private JEditorPane editorPane;
    private JScrollPane editorScrollPane;
    private Map<String, SimplePluginDescriptor> pluginsMap;
    private PluginsTemplateRenderer ptr;

    /**
     *
     */
    public AvailablePluginsPanel() {
        setLayout(new BorderLayout());
        ptr = new PluginsTemplateRenderer();

        AvailablePluginsResolver resolver = new AvailablePluginsResolver();

        try {
            resolver.resolvePlugins(
                "http://xpontus.sourceforge.net/snapshot/plugins.xml");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pluginsMap = resolver.getPluginDescriptorsMap();
        }

        java.util.Vector columns = new java.util.Vector(3);
        columns.add("Identifier");
        columns.add("Category");
        columns.add("Built-in");

        java.util.Vector rows = new java.util.Vector();

        for (Iterator<String> it = pluginsMap.keySet().iterator();
                it.hasNext();) {
            Vector m_row = new Vector();
            SimplePluginDescriptor spd = pluginsMap.get(it.next());
            m_row.add(spd.getId());
            m_row.add(spd.getCategory());
            m_row.add(spd.getBuiltin());
            rows.add(m_row);
        }

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setEditorKit(new HTMLEditorKit());

        editorScrollPane = new JScrollPane();
        editorScrollPane.getViewport().add(editorPane);

        tableModel = new DefaultTableModel(rows, columns) {
                    @Override
                    public boolean isCellEditable(int row, int column) {
                        return false;
                    }
                };

        table = new JTable(tableModel);

        this.table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    int row = table.getSelectedRow();

                    if (row != -1) {
                        String id = tableModel.getValueAt(row, 0).toString();
                        SimplePluginDescriptor spd = pluginsMap.get(id);
                        editorPane.setText(ptr.renderTemplate(spd));
                    }
                }
            });

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (tableModel.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }

        searchButton = new JButton("Search");
        reloadButton = new JButton("Reload");
        searchTextField = new JTextField(20);
        installButton = new JButton("Install");
        scrollPane = new JScrollPane();
        scrollPane.getViewport().add(table);

        buttonsPanel = new JPanel();
        buttonsPanel.setLayout(new FlowLayout(FlowLayout.LEFT));

        buttonsPanel.add(installButton);

        northPanel = new JPanel();
        northPanel.setLayout(new GridBagLayout());

        // add the reload button
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(3, 3, 3, 3);
        northPanel.add(reloadButton, gbc);

        // add some space
        gbc = new GridBagConstraints();
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(3, 3, 3, 3);
        northPanel.add(Box.createHorizontalGlue(), gbc);

        // add the search button
        gbc = new GridBagConstraints();
        gbc.gridx = 5;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(3, 3, 3, 3);
        northPanel.add(searchButton, gbc);

        // add the search box
        gbc = new GridBagConstraints();
        gbc.gridx = 6;
        gbc.gridy = 0;
        gbc.gridwidth = 4;
        gbc.fill = GridBagConstraints.BOTH;
        gbc.anchor = GridBagConstraints.EAST;
        gbc.insets = new Insets(3, 3, 3, 3);
        northPanel.add(searchTextField, gbc);

        // create the splitpane container 
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                scrollPane, editorScrollPane);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        splitPane.setOneTouchExpandable(true);

        add(northPanel, BorderLayout.NORTH);

        add(splitPane, BorderLayout.CENTER);

        add(buttonsPanel, BorderLayout.SOUTH);
    }

    public String getNbPlugins() {
        return "" + pluginsMap.size();
    }
}
