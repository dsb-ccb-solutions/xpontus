/*
 * OutlineViewDockable.java
 *
 * Created on July 1, 2007, 11:52 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
 */
package net.sf.xpontus.modules.gui.components;

import com.vlsolutions.swing.docking.DockGroup;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;

import net.sf.xpontus.parsers.XmlNode;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;

/**
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class OutlineViewDockable extends JScrollPane implements Dockable {

    private DockKey key = new DockKey("Outline ");
    private DefaultTreeModel model;
    private DefaultMutableTreeNode root;
    private JScrollPane scrollPane;
    private JTree mTree;

    /**
     *
     */
    public OutlineViewDockable() {
        root = new DefaultMutableTreeNode("Document");
        model = new DefaultTreeModel(root);

        mTree = new JTree(model);
        mTree.setRootVisible(false);
        scrollPane = new JScrollPane(mTree);

        key.setResizeWeight(0.2f);
        key.setDockGroup(new DockGroup("Outline"));

        Dimension dim = new Dimension(250, 200);
        scrollPane.setPreferredSize(dim);
        scrollPane.setMinimumSize(dim);
        this.getViewport().add(scrollPane);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();

        URL m_url = getClass().getResource("/net/sf/xpontus/icons/Element.png");

        final ImageIcon leafIcon = new ImageIcon(m_url);

        renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(leafIcon);
        renderer.setClosedIcon(leafIcon);
        renderer.setIcon(leafIcon);

        mTree.setCellRenderer(renderer);

        // tree selection listener
        mTree.addMouseListener(new MouseAdapter() {

            public void mouseClicked(MouseEvent e) {

                if (e.getClickCount() == 2) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) mTree.getLastSelectedPathComponent();

                    if (node == null) {
                        return;
                    }

                    if (node instanceof XmlNode) {
                        XmlNode nodeInfo = (XmlNode) node;
                        gotoLine(nodeInfo.line, nodeInfo.column);
                    }
                }
            }
            });
    }

    /**
     *
     * @return
     */
    public DefaultMutableTreeNode getRootNode() {
        return root;
    }

    public void updateAll(DefaultMutableTreeNode root) {
        this.root = root;
        model = new DefaultTreeModel(root);
        mTree.setModel(model);
        mTree.revalidate();
        mTree.repaint();
        expandAllNodes();
    }

    private void gotoLine(int line, int column) {
        JTextComponent jtc = DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentEditor();

        Element element = jtc.getDocument().getDefaultRootElement();

        if (element.getElement(line) == null) {
            XPontusComponentsUtils.showErrorMessage(
                    "element location not found");

            return;
        }

        int lineOffset = element.getElement(line).getStartOffset();

        int tokenOffset = lineOffset + column;

        jtc.grabFocus();

        jtc.setCaretPosition(tokenOffset);
    }

    /**
     *
     * @return
     */
    public DefaultTreeModel getTreeModel() {
        return model;
    }

    public void expandAllNodes() {
        for (int i = 0; i < mTree.getRowCount(); i++) {
            mTree.expandRow(i);
        }
    }

    /** implement Dockable
     * @return
     */
    public DockKey getDockKey() {
        return key;
    }

    /** implement Dockable
     * @return
     */
    public Component getComponent() {
        return scrollPane;
    }
}
