/*
 * JTreeDockable.java
 *
 * Created on February 4, 2007, 1:28 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view;

import com.vlsolutions.swing.docking.*;

import net.sf.xpontus.parsers.XmlNode;

import java.awt.*;

import java.net.URL;

import javax.swing.*;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.text.Element;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;


/**
 *
 * @author mrcheeks
 */
public class OutlineViewDockable extends JScrollPane implements Dockable {
    private DockKey key = new DockKey("Outline ");
    private DefaultTreeModel model;
    private DefaultMutableTreeNode root;
    private JScrollPane scrollPane;
    private JTree mTree;

    public OutlineViewDockable() {
        root = new DefaultMutableTreeNode("Document");
        model = new DefaultTreeModel(root);

        mTree = new JTree(model);
        mTree.setRootVisible(false);
        scrollPane = new JScrollPane(mTree);
        
        key.setResizeWeight(0.2f);
        key.setDockGroup(new DockGroup("Outline"));

        Dimension dim = new Dimension(250, 200);
        //            mTree.setPreferredSize(dim);
        //            mTree.setMinimumSize(dim);
        // key.setAutoHideEnabled(false);
        // key.setResizeWeight(1.0f); // takes all resizing
        scrollPane.setPreferredSize(dim);
        //            mTree.setPreferredSize(dim);
        //            mTree.setMinimumSize(dim);
        // key.setAutoHideEnabled(false);
        // key.setResizeWeight(1.0f); // takes all resizing
        scrollPane.setMinimumSize(dim);
        //            mTree.setPreferredSize(dim);
        //            mTree.setMinimumSize(dim);
        // key.setAutoHideEnabled(false);
        // key.setResizeWeight(1.0f); // takes all resizing
        this.getViewport().add(scrollPane);

        URL m_url = getClass().getResource("Element.png");
        final ImageIcon leafIcon = new ImageIcon(m_url);

        DefaultTreeCellRenderer renderer = new DefaultTreeCellRenderer();
        renderer.setLeafIcon(leafIcon);
        renderer.setOpenIcon(leafIcon);
        renderer.setClosedIcon(leafIcon);
        renderer.setIcon(leafIcon);

        mTree.setCellRenderer(renderer);

        // tree selection listener
        mTree.addTreeSelectionListener(new TreeSelectionListener() {
                public void valueChanged(TreeSelectionEvent e) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) mTree.getLastSelectedPathComponent();

                    if (node == null) {
                        return;
                    }

                    if (node instanceof XmlNode) {
                        XmlNode nodeInfo = (XmlNode) node;
                        gotoLine(nodeInfo.line, nodeInfo.column);
                    }
                }
            });
    }

    public DefaultMutableTreeNode getRootNode() {
        return root;
    }

    public void updateAll() {
        model.reload(root);
        mTree.revalidate();
        mTree.repaint();
        expandAllNodes();
    }

    public DefaultTreeModel getTreeModel() {
        return model;
    }

    public void expandAllNodes() {
        for (int i = 0; i < mTree.getRowCount(); i++) {
            mTree.expandRow(i);
        }
    }

    /** implement Dockable  */
    public DockKey getDockKey() {
        return key;
    }

    /** implement Dockable  */
    public Component getComponent() {
        return scrollPane;
    }

    private void gotoLine(int line, int column) {
        JEditorPane edit = XPontusWindow.getInstance().getCurrentEditor();
        Element element = edit.getDocument().getDefaultRootElement();

        if (element.getElement(line) == null) {
            JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
                XPontusWindow.getInstance().getI18nMessage("msg.noSuchLine"),
                XPontusWindow.getInstance().getI18nMessage("msg.error"),
                JOptionPane.WARNING_MESSAGE);

            return;
        }

        int lineOffset = element.getElement(line).getStartOffset();

        int tokenOffset = lineOffset + column;

        edit.setCaretPosition(tokenOffset);
    }
}
