/*
 * PreferencesView.java
 *
 * Created on 27 janvier 2008, 17:45
 */
package net.sf.xpontus.modules.gui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import net.sf.xpontus.modules.gui.components.preferences.EditorPanel;
import net.sf.xpontus.modules.gui.components.preferences.GeneralPanel;
import net.sf.xpontus.modules.gui.components.preferences.IPreferencesPanel;
import net.sf.xpontus.modules.gui.components.preferences.PreferencesNode;
import net.sf.xpontus.utils.XPontusComponentsUtils;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.PluginDescriptor;

/**
 *
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PreferencesView extends javax.swing.JDialog {

    private Component currentComponent;
    private DefaultTreeModel model;
    private DefaultMutableTreeNode rootNode;

    private Component nullComponent;
    
    /** Creates new form PreferencesView
     * @param parent
     * @param modal 
     */
    public PreferencesView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        nullComponent = new JPanel();
         
        rootNode = new DefaultMutableTreeNode("Root");
        model = new DefaultTreeModel(rootNode);

        Class componentsClass[] = {GeneralPanel.class, EditorPanel.class};

        initComponents();

        for (Class c : componentsClass) {
            try {
                IPreferencesPanel panel = (IPreferencesPanel) c.newInstance();
                PreferencesNode node = new PreferencesNode(panel);
                model.insertNodeInto(node, rootNode, rootNode.getChildCount());
                model.reload(rootNode);
                TreePath path = new TreePath(node.getPath());
                preferencesTree.expandPath(path);

            } catch (Exception err) { 
                err.printStackTrace();
            }
        } 

        DefaultMutableTreeNode pluginsNode = new DefaultMutableTreeNode("Plugins");
        model.insertNodeInto(pluginsNode, rootNode, rootNode.getChildCount());
        model.reload(rootNode);

        DefaultMutableTreeNode categoriesNode = new DefaultMutableTreeNode("Categories");
        model.insertNodeInto(categoriesNode, pluginsNode, pluginsNode.getChildCount());
        model.reload(rootNode);

        PluginManager pluginManager = XPontusPluginManager.getPluginManager();
        Object[] pluginDescriptors =   pluginManager.getRegistry().getPluginDescriptors().toArray();

        HashMap<String, List> pluginsMap = new HashMap<String, List>();

        for (Object descriptor : pluginDescriptors) {
            PluginDescriptor d = (PluginDescriptor) descriptor;
            String pluginCategory = d.getAttribute("Category").getValue();
            String pluginId = d.getId(); 
            
            List li = null;
            if (!pluginsMap.containsKey(pluginCategory)) {
                li = new ArrayList();
                pluginsMap.put(pluginCategory, li);
            } else {
                li = pluginsMap.get(pluginCategory);
            }
            li.add(pluginId);
        }

        Iterator<String> it = pluginsMap.keySet().iterator();

        while (it.hasNext()) {
            String m_cat = it.next();
            DefaultMutableTreeNode contextNode = new DefaultMutableTreeNode(m_cat);
            model.insertNodeInto(contextNode, categoriesNode, categoriesNode.getChildCount());
            model.reload(categoriesNode);

            List<String> li = pluginsMap.get(m_cat);
            for (String s : li) {
                DefaultMutableTreeNode childNode = new DefaultMutableTreeNode(s);
                model.insertNodeInto(childNode, contextNode, contextNode.getChildCount());
                model.reload(contextNode);
            }
        }

        jSplitPane1.setLeftComponent(treeScrollPane);
        jSplitPane1.setRightComponent(panePanel);

        Dimension dim = new Dimension(300, 200);
        treeScrollPane.setMinimumSize(dim);
        treeScrollPane.setPreferredSize(dim);
        
        nullComponent.setMinimumSize(dim);
        nullComponent.setPreferredSize(dim);

        preferencesTree.addTreeSelectionListener(new TreeSelectionListener() {

            public void valueChanged(TreeSelectionEvent e) {
                TreePath path = e.getNewLeadSelectionPath();


                // if there is no path, then there is nothing selected, so we need
                // to clear the table model... that's it!
                if (path != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) path.getLastPathComponent();

                    if (node instanceof PreferencesNode) {
                        Component c = (Component) ((PreferencesNode) node).getUserObject();
                        show(c);
                    }
                    else{
                        show(nullComponent);
                    }
                }
            }
        });

        expandAll(preferencesTree, true);

        preferencesTree.setSelectionPath(new TreePath(((DefaultMutableTreeNode) rootNode.getFirstChild()).getPath()));

        pack();

    }

    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode) tree.getModel().getRoot();

        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }

    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode) parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration<TreeNode> e = node.children(); e.hasMoreElements();) {
                TreeNode n =   e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }

        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }

    public PreferencesView() {
        this((Frame) XPontusComponentsUtils.getTopComponent().getDisplayComponent(), true);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panePanel = new javax.swing.JPanel();
        treeScrollPane = new javax.swing.JScrollPane();
        preferencesTree = new javax.swing.JTree();
        buttonsPanel = new javax.swing.JPanel();
        closeButton = new javax.swing.JButton();
        jSplitPane1 = new javax.swing.JSplitPane();

        panePanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(3, 3, 3, 3));
        panePanel.setMinimumSize(new java.awt.Dimension(500, 300));
        panePanel.setPreferredSize(new java.awt.Dimension(500, 300));
        panePanel.setLayout(new java.awt.BorderLayout());

        preferencesTree.setModel(model);
        preferencesTree.setRootVisible(false);
        treeScrollPane.setViewportView(preferencesTree);

        setTitle("Preferences");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                closeDialog(evt);
            }
        });

        closeButton.setText("Close");
        closeButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                closeButtonActionPerformed(evt);
            }
        });
        buttonsPanel.add(closeButton);

        getContentPane().add(buttonsPanel, java.awt.BorderLayout.SOUTH);

        jSplitPane1.setContinuousLayout(true);
        jSplitPane1.setOneTouchExpandable(true);
        getContentPane().add(jSplitPane1, java.awt.BorderLayout.CENTER);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    /** Closes the dialog */
    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog
        setVisible(false); 
    }//GEN-LAST:event_closeDialog

    private void closeButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_closeButtonActionPerformed
        setVisible(false);
}//GEN-LAST:event_closeButtonActionPerformed
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JButton closeButton;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JPanel panePanel;
    private javax.swing.JTree preferencesTree;
    private javax.swing.JScrollPane treeScrollPane;
    // End of variables declaration//GEN-END:variables
    private JPanel makePanel(String title, Component c) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel top = new JLabel(title);
        top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        top.setFont(top.getFont().deriveFont(Font.BOLD));
        top.setOpaque(true);
        top.setBackground(panel.getBackground().brighter());
        panel.add("North", top);
        panel.add(c, BorderLayout.CENTER);
        panel.setPreferredSize(new Dimension(500, 300));
        panel.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        return panel;
    }

    private void show(Component component) {
        if (currentComponent != null) {
            panePanel.remove(currentComponent);
        }

        currentComponent = component;

        panePanel.add(currentComponent, BorderLayout.CENTER);

        panePanel.revalidate();
        panePanel.repaint();

        PreferencesView.this.repaint();
    }
    } 
