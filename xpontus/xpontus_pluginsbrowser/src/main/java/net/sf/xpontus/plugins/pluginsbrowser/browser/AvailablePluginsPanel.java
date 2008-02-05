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

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableModel;

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

    /**
     *
     */
    public AvailablePluginsPanel() {
        setLayout(new BorderLayout());

        editorPane = new JEditorPane();
        editorPane.setEditable(false);
        editorScrollPane = new JScrollPane();
        editorScrollPane.getViewport().add(editorPane);

        tableModel = new DefaultTableModel();
        table = new JTable(tableModel);
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
}
