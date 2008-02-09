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

import org.java.plugin.registry.PluginDescriptor;

import java.awt.BorderLayout;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
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
public class InstalledPluginsPanel extends JComponent {
    private JTable table;
    private TableModel tableModel;
    private JScrollPane scrollPane;
    private JSplitPane splitPane;
    private JEditorPane editorPane;
    private JScrollPane editorScrollPane;
    private Map<String, SimplePluginDescriptor> pluginsMap = new HashMap<String, SimplePluginDescriptor>();
    private PluginsTemplateRenderer ptr;

    /** Creates new form InstalledPluginsPanel */
    public InstalledPluginsPanel() {
        setLayout(new BorderLayout());

        ptr = new PluginsTemplateRenderer();

        Object[] descriptors = PBPlugin.r.getPluginDescriptors().toArray();

        java.util.Vector columns = new java.util.Vector(3);
        columns.add("Identifier");
        columns.add("Category");
        columns.add("Built-in");

        java.util.Vector rows = new java.util.Vector();

        for (int i = 0; i < descriptors.length; i++) {
            PluginDescriptor pds = (PluginDescriptor) descriptors[i];
            String id = pds.getId().toString();
            String category = pds.getAttribute("Category").getValue().toString();
            String homepage = pds.getAttribute("Homepage").getValue().toString();
            String builtin = pds.getAttribute("Built-in").getValue().toString();
            String displayname = pds.getAttribute("DisplayName").getValue()
                                    .toString();
            String description = pds.getAttribute("Description").getValue()
                                    .toString();
            String version = pds.getVersion().toString();
            SimplePluginDescriptor spd = new SimplePluginDescriptor();

            String vendor = "Yves Zoundi";

            if (pds.getVendor() != null) {
                vendor = pds.getVendor();
            }

            java.util.Vector m_row = new java.util.Vector(3);
            m_row.add(id);
            m_row.add(category);
            m_row.add(builtin);

            spd.setAuthor(vendor);
            spd.setBuiltin(builtin);
            spd.setCategory(category);
            spd.setDescription(description);
            spd.setDisplayname(displayname);
            spd.setHomepage(homepage);
            spd.setId(id);
            spd.setVersion(version);

            rows.add(m_row);

            pluginsMap.put(id, spd);
        }

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorPane.setContentType("text/html");
        editorPane.setEditorKit(new HTMLEditorKit());

        editorScrollPane = new JScrollPane();
        editorScrollPane.getViewport().add(editorPane);

        tableModel = new DefaultTableModel(rows, columns) {
                    public boolean isCellEditable(int row, int column) {
                        return false;
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
                        if (row != -1) {
                            String id = tableModel.getValueAt(row, 0).toString();
                            SimplePluginDescriptor spd = pluginsMap.get(id);
                            editorPane.setText(ptr.renderTemplate(spd));
                        }
                    }
                }
            });
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionInterval(0, 0);

        scrollPane = new JScrollPane();
        scrollPane.getViewport().add(table);

        // create the splitpane container 
        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                scrollPane, editorScrollPane);
        splitPane.setResizeWeight(0.5);
        splitPane.setDividerLocation(0.5);
        splitPane.setOneTouchExpandable(true);

        add(splitPane, BorderLayout.CENTER);
    }

    public int getNbPlugins() {
        return tableModel.getRowCount();
    }
}
