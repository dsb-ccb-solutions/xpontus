/*
 * AdvancedSettingsView.java
 *
 * Created on 27 janvier 2008, 17:45
 */
package net.sf.xpontus.modules.gui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
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
import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import net.sf.xpontus.modules.gui.components.preferences.PreferencesNode2;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;
import net.sf.xpontus.properties.PropertiesHolder;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.PluginDescriptor;

/**
 *
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class AdvancedSettingsView extends javax.swing.JDialog {

    private Component currentComponent;
    private DefaultTreeModel model;
    private Map<String, PreferencesPluginIF> panelsMap = new HashMap<String, PreferencesPluginIF>();
    private DefaultMutableTreeNode rootNode;
    private Component nullComponent;
    private HashMap<String, List<PreferencesPluginIF>> pluginsMap = new HashMap<String, List<PreferencesPluginIF>>();

    private List<PreferencesPluginIF> createOrGetCategory(String category) {
        List li = new ArrayList();
        if (!pluginsMap.containsKey(category)) {
            pluginsMap.put(category, li);
        } else {
            li = pluginsMap.get(category);
        }
        return li;
    }

    /** Creates new form AdvancedSettingsView
     * @param parent
     * @param modal 
     */
    public AdvancedSettingsView(java.awt.Dialog parent, boolean modal) {
        super(parent, modal);

        nullComponent = new JPanel();

        rootNode = new DefaultMutableTreeNode("Root");
        model = new DefaultTreeModel(rootNode);

        initComponents();

        DefaultMutableTreeNode pluginsNode = new DefaultMutableTreeNode("Plugins");
        model.insertNodeInto(pluginsNode, rootNode, rootNode.getChildCount());
        model.reload(rootNode);

        DefaultMutableTreeNode categoriesNode = new DefaultMutableTreeNode("Categories");
        model.insertNodeInto(categoriesNode, pluginsNode, pluginsNode.getChildCount());
        model.reload(rootNode);

        PluginManager pluginManager = XPontusPluginManager.getPluginManager();
        Object[] pluginDescriptors = pluginManager.getRegistry().getPluginDescriptors().toArray();



        for (Object descriptor : pluginDescriptors) {
            PluginDescriptor d = (PluginDescriptor) descriptor;
            String pluginCategory = d.getAttribute("Category").getValue();
//            String pluginId = d.getAttribute("DisplayName").getValue().toString();

            List li = null;
            if (!pluginsMap.containsKey(pluginCategory)) {
                li = new ArrayList();
                pluginsMap.put(pluginCategory, li);
            } else {
                li = pluginsMap.get(pluginCategory);
            }
        }
        
         Hashtable settingsTable = (Hashtable) PropertiesHolder.getPropertyValue(XPontusConstantsIF.XPONTUS_PREFERENCES_PANELS);

        Iterator<String> settingsIterator = settingsTable.keySet().iterator();
        
        while (settingsIterator.hasNext()) {
            String id = settingsIterator.next();
            Hashtable t = (Hashtable) settingsTable.get(id);
            String m_className = (String) t.get(XPontusConstantsIF.OBJECT_CLASSNAME);
            ClassLoader m_loader = (ClassLoader) t.get(XPontusConstantsIF.CLASS_LOADER);
            String m_category = (String) t.get("category");

            try {
                PreferencesPluginIF prefIF = (PreferencesPluginIF) Class.forName(m_className, true, m_loader).newInstance();
                prefIF.loadSettings();
                panelsMap.put(id, prefIF);
                List m_list = createOrGetCategory(m_category);
                m_list.add(prefIF);
            } catch (Exception err) {

            }
        }

        Iterator<String> it = pluginsMap.keySet().iterator();

        while (it.hasNext()) {
            String m_cat = it.next();
            List<PreferencesPluginIF> a_list = pluginsMap.get(m_cat);
            if (a_list.size() > 0) {
                DefaultMutableTreeNode contextNode = new DefaultMutableTreeNode(m_cat);
                model.insertNodeInto(contextNode, categoriesNode, categoriesNode.getChildCount());
                model.reload(categoriesNode);
                for (PreferencesPluginIF k : a_list) {
                    PreferencesNode2 childNode = new PreferencesNode2(k, k.getPreferencesPanelComponent().getTitle());
                    model.insertNodeInto(childNode, contextNode, contextNode.getChildCount());
                    model.reload(contextNode);
                }
            }
        }

       

//        Hashtable t = new Hashtable();
//            t.put("category", category);
//            t.put("id", id);
//            t.put(XPontusConstantsIF.OBJECT_CLASSNAME, cl.getName());
//            t.put(XPontusConstantsIF.CLASS_LOADER, classLoader);




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

                    if(node instanceof PreferencesNode2){
                       PreferencesPluginIF p = (PreferencesPluginIF) node.getUserObject();
                       show(p.getPreferencesPanelComponent().getJComponent());
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
                TreeNode n = e.nextElement();
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

        setTitle("Plugins settings(not finished)");
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
       
        Iterator<PreferencesPluginIF> m_it = panelsMap.values().iterator();
        while(m_it.hasNext()){
            m_it.next().saveSettings();
        }
        
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

    public Component createSimpleComponent(String title) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel top = new JLabel(title);
        top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
        top.setFont(top.getFont().deriveFont(Font.BOLD));
        top.setOpaque(true);
        top.setBackground(panel.getBackground().brighter());
        panel.add("North", top);
        panel.add(new JLabel(), BorderLayout.CENTER);
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

        AdvancedSettingsView.this.repaint();
    }
    } 
