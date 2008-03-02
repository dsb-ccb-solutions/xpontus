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
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.HashMap;
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
import javax.swing.text.html.HTMLEditorKit;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class BrowserPanel extends JComponent {
    private JScrollPane scrollPane;
    private JSplitPane splitPane;
    private JTable table;
    private DefaultTableModel tableModel;
    private JPanel northPanel;
    private JButton reloadButton;
    private JTextField searchTextField;
    private JButton searchButton;
    private JEditorPane editorPane;
    private JScrollPane editorScrollPane;
    private   Map<String, SimplePluginDescriptor> pluginsMap;
    private transient Map<String, SimplePluginDescriptor> currentMap;
    private PluginsTemplateRenderer ptr;
    private String indexFile;
    private AbstractPluginsResolver resolver;

    public BrowserPanel(AbstractPluginsResolver m_resolver, String title,
        String m_indexFile) {
        setLayout(new BorderLayout());
        resolver = m_resolver;
        ptr = new PluginsTemplateRenderer();
        this.indexFile = m_indexFile;

        resolver.resolvePlugins();

        pluginsMap = resolver.getPluginDescriptorsMap();
        currentMap = new HashMap(pluginsMap);

        java.util.Vector columns = new java.util.Vector(3);

        columns.add(title);
        columns.add("Identifier");
        columns.add("Category");
        columns.add("Built-in");

        java.util.Vector rows = new java.util.Vector();

        for (Iterator<String> it = pluginsMap.keySet().iterator();
                it.hasNext();) {
            Vector m_row = new Vector();
            m_row.add(new Boolean(false));

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
                        return (column == 0);
                    }

                    public Class getColumnClass(int c) {
                        return getValueAt(0, c).getClass();
                    }
                };

        table = new JTable(tableModel);

        table.getSelectionModel().addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent e) {
                    if (e.getValueIsAdjusting()) {
                        return;
                    }

                    int row = table.getSelectedRow();

                    if (row != -1) {
                        String id = tableModel.getValueAt(row, 1).toString();
                        SimplePluginDescriptor spd = currentMap.get(id);
                        editorPane.setText(ptr.renderTemplate(spd));
                    }
                }
            });

        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        if (tableModel.getRowCount() > 0) {
            table.setRowSelectionInterval(0, 0);
        }

        searchTextField = new JTextField(20);

        searchButton = new JButton("Search");
        searchButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    Map<String, SimplePluginDescriptor> searchResults = PluginsUtils.getInstance().searchIndex(searchTextField.getText(),
                            indexFile);

                    boolean updateNeeded = false;

                    System.out.println("Search results :" +
                        searchResults.size());

                    if (searchResults.size() > 0) {
                        updateNeeded = true;
                        currentMap = new HashMap<String, SimplePluginDescriptor>(searchResults);
                    } else {
                        if (!currentMap.equals(pluginsMap)) {
                            currentMap = new HashMap<String, SimplePluginDescriptor>(pluginsMap);
                            updateNeeded = true;
                        }
                    }

                    if (updateNeeded) {
                        reloadPluginsTable();
                    }
                }
            });

        reloadButton = new JButton("Reload");
        reloadButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    resolver.reload();
                    pluginsMap = new HashMap(resolver.getPluginDescriptorsMap());
                    currentMap = new HashMap(pluginsMap);
                    reloadPluginsTable();
                    
                }
            });

        scrollPane = new JScrollPane();
        scrollPane.getViewport().add(table);

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
    }

    void reloadPluginsTable() {
        while (tableModel.getRowCount() > 0) {
            tableModel.removeRow(tableModel.getRowCount() - 1);

            tableModel.fireTableDataChanged();
        }

        for (Iterator<String> it = currentMap.keySet().iterator();
                it.hasNext();) {
            Vector m_row = new Vector();
            m_row.add(new Boolean(false));

            SimplePluginDescriptor spd = currentMap.get(it.next());
            m_row.add(spd.getId());
            m_row.add(spd.getCategory());
            m_row.add(spd.getBuiltin());
            tableModel.addRow(m_row);
        }

        tableModel.fireTableDataChanged();
    }

    public String getNbPlugins() {
        return "" + pluginsMap.size();
    }
}
