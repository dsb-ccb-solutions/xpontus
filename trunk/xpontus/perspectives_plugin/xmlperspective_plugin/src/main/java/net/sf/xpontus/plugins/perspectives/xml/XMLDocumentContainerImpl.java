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
package net.sf.xpontus.plugins.perspectives.xml;

import net.sf.xpontus.modules.gui.components.DocumentContainer;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JTree;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.TitledBorder;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XMLDocumentContainerImpl extends DocumentContainer {
    private JTabbedPane tabbedPane;
    private JComponent m_treeViewPanel;
    private JScrollPane treeViewScrollPane;
    private JTree treeViewTree;
    private DefaultMutableTreeNode treeViewRootNode;
    private DefaultTreeModel treeViewModel;
    private JSplitPane verticalSplitPane;
    private JSplitPane horizontalSplitPane;
    private JComponent viewPanel;
    private JComponent elementPanel;
    private JComponent attributesPanel;

    public XMLDocumentContainerImpl() {
        super();
        tabbedPane = new JTabbedPane();
        tabbedPane.setTabPlacement(JTabbedPane.BOTTOM);

        m_treeViewPanel = new JPanel(new BorderLayout());

        viewPanel = new JPanel(new BorderLayout());
        viewPanel.setBorder(createTitledBorder("Tree structure"));

        elementPanel = new JPanel(new BorderLayout());
        elementPanel.setBorder(createTitledBorder("Element"));

        attributesPanel = new JPanel(new BorderLayout());
        attributesPanel.setBorder(createTitledBorder("Attributes"));

        horizontalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true,
                elementPanel, attributesPanel);
        horizontalSplitPane.setDividerLocation(0.5F);
        horizontalSplitPane.setResizeWeight(0.5F);

        String[] headers = {
                "Add element", "Remove element", "Add attribute",
                "Remove attribute"
            };

        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));

        for (String header : headers) {
            topPanel.add(new JButton(header));
        }

        treeViewRootNode = new DefaultMutableTreeNode("Root node");

        treeViewModel = new DefaultTreeModel(treeViewRootNode);

        treeViewTree = new JTree(treeViewModel);

        treeViewScrollPane = new JScrollPane();
        treeViewScrollPane.getViewport().add(treeViewTree);

        viewPanel.add(treeViewScrollPane, BorderLayout.CENTER);

        m_treeViewPanel.add(viewPanel, BorderLayout.CENTER);

        verticalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true,
                horizontalSplitPane, m_treeViewPanel);
        verticalSplitPane.setDividerLocation(0.5F);
        verticalSplitPane.setResizeWeight(0.5F);

        JPanel compPanel = new JPanel(new BorderLayout());
        compPanel.add(topPanel, BorderLayout.NORTH);

        compPanel.add(verticalSplitPane, BorderLayout.CENTER);

        tabbedPane.addTab("Source view", getDocumentPanel());
        tabbedPane.addTab("Tree View", compPanel);
    }

    public Border createTitledBorder(String title) {
        Border outsideBorder = BorderFactory.createEmptyBorder();
        Border insideBorder = new CompoundBorder(new TitledBorder(title),
                new EmptyBorder(1, 1, 1, 1));

        return new CompoundBorder(outsideBorder, insideBorder);
    }

    public Component getComponent() {
        return tabbedPane;
    }
}
