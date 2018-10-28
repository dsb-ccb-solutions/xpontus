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
import java.awt.FlowLayout;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.ListSelectionModel;

/**
 *
 * @author Yves Zoundi
 */
public class PluginsSettingsPanel extends JComponent {
    private JCheckBox checkForUpdatesOption;
    private JScrollPane mirrorsScrollPane;
    private JList mirrorsList;
    private DefaultComboBoxModel sfMirrorsModel;
    private DefaultComboBoxModel updateMirrorsListModel;
    private JSplitPane splitPane;
    private JButton selectMirrorButton;
    private JComponent leftPanel;
    private JComponent rightPanel;
    private JComboBox updateOptionsList;
    private final String[] UPDATE_OPTIONS = {
            "Every week", "Every month", "Never"
        };
    private final String[] MIRRORS_URLS = {
            "http://heanet.dl", "http://xpontus.sf.net",
            "http://yveszoundi.blogspot.com"
        };

    public PluginsSettingsPanel() {
        setLayout(new BorderLayout());

        checkForUpdatesOption = new JCheckBox("Check for updates");
        checkForUpdatesOption.setSelected(true);
        selectMirrorButton = new JButton("Set as default");

        sfMirrorsModel = new DefaultComboBoxModel(MIRRORS_URLS);

        mirrorsList = new JList();
        mirrorsList.setModel(sfMirrorsModel);
        mirrorsList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        updateMirrorsListModel = new DefaultComboBoxModel(UPDATE_OPTIONS);
        updateOptionsList = new JComboBox(updateMirrorsListModel);
        mirrorsScrollPane = new JScrollPane(mirrorsList);
        mirrorsList.setSelectedIndex(0);

        leftPanel = new JPanel(new BorderLayout());

        JPanel leftPanelChild = new JPanel();

        leftPanelChild.setLayout(new FlowLayout(FlowLayout.LEFT));

        leftPanel.add(checkForUpdatesOption, BorderLayout.NORTH);

        leftPanelChild.add(updateOptionsList);

        leftPanel.add(leftPanelChild, BorderLayout.CENTER);

        JPanel p = new JPanel();
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        p.add(new JLabel("Select mirrror"));
        p.add(Box.createVerticalStrut(5));
        p.add(selectMirrorButton);
        p.add(Box.createVerticalStrut(5));

        rightPanel = new JPanel(new BorderLayout());

        rightPanel.add(p, BorderLayout.NORTH);

        rightPanel.add(mirrorsScrollPane, BorderLayout.CENTER);

        splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftPanel,
                rightPanel);

        splitPane.setOneTouchExpandable(true);

        splitPane.setContinuousLayout(true);
        splitPane.setDividerLocation(0.25F);

        splitPane.setResizeWeight(0.25F);

        add(splitPane, BorderLayout.CENTER);
    }
}
