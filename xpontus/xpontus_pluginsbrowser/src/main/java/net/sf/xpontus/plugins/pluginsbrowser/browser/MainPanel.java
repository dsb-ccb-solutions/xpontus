// Placed in public domain by Dmitry Olshansky, 2006
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import org.java.plugin.*;
import org.java.plugin.boot.*;
import org.java.plugin.registry.*;
import org.java.plugin.registry.PluginRegistry.*;
import org.java.plugin.tools.*;
import org.java.plugin.util.*;

import org.onemind.jxp.*;

import java.awt.*;
import java.awt.event.*;

import java.io.*;

import java.net.*;

import java.util.*;

import javax.swing.*;
import javax.swing.event.*;
import javax.swing.tree.*;


/**
 * @version $Id: MainPanel.java,v 1.8 2007/01/08 10:34:07 ddimon Exp $
 */
final class MainPanel extends JPanel
{
    private static final long serialVersionUID = 8531058675733981722L;
    JSplitPane jSplitPane = null;
    private JPanel leftPanel = null;
    private JPanel jPanel = null;
    private JButton addManifestButton = null;
    private JScrollPane jScrollPane = null;
    JTree jTree = null;
    private JButton removeManifestButton = null;
    final PluginRegistry registry = PBPlugin.r;
    private final JxpProcessor processor;
    private JScrollPane jScrollPane1 = null;
    private JTextPane htmlTextPane = null;
    private JButton checkIntegrityButton = null;
    private JButton showGraphButton = null;
    JFileChooser fcM = new JFileChooser();

    /**
         * This is the default constructor
         */
    MainPanel()
    {
        super();
        Collection descriptors = registry.getPluginDescriptors();
        Object m_d[] = descriptors.toArray();
        
        for(int i=0;i<m_d.length;i++){
            PluginDescriptor pds = (PluginDescriptor)m_d[i];
            String id = pds.getId();
            String proto = pds.getLocation().getProtocol();
            pds.getVersion();
             
            System.out.println("id:" + id + ",proto:" + proto);
        }
       
        initialize();

        String templatesPath = this.getClass().getName()
                                   .substring(0,
                this.getClass().getName().lastIndexOf('.')).replace('.', '/') +
            "/templates/";
        processor = new JxpProcessor(new ClassPathPageSource(templatesPath,
                    "UTF-8"));

        try
        {
            //            Configuration config = ((PBPlugin) PluginManager.lookup(this)
            //                                                            .getPlugin(PBPlugin.PLUGIN_ID)).getConfiguration();
            //            int treePaneSize = config.getTreePaneSize();
            //
            //            if (treePaneSize > 0) {
            //                jSplitPane.setDividerLocation(treePaneSize);
            //            }
        }
        catch (Exception ex)
        {
            // ignore
        }
    }

    private static URL getManifestUrl(final File file)
    {
        try
        {
            if (file.isDirectory())
            {
                File result = new File(file, "plugin.xml"); //$NON-NLS-1$

                if (result.isFile())
                {
                    return IoUtil.file2url(result);
                }

                result = new File(file, "plugin-fragment.xml"); //$NON-NLS-1$

                if (result.isFile())
                {
                    return IoUtil.file2url(result);
                }

                return null;
            }

            if (!file.isFile())
            {
                return null;
            }

            URL url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() + "!/plugin.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url))
            {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() +
                    "!/plugin-fragment.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url))
            {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() +
                    "!/META-INF/plugin.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url))
            {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() +
                    "!/META-INF/plugin-fragment.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url))
            {
                return url;
            }
        }
        catch (MalformedURLException mue)
        {
            // ignore
        }

        return null;
    }

    private void initialize()
    {
        this.setLayout(new BorderLayout());
         Dimension dimension = new Dimension(650,500);        
        this.setPreferredSize(dimension);
        this.setMinimumSize(dimension);
        this.add(getJSplitPane(), java.awt.BorderLayout.CENTER);
    }

    void addManifests(final File[] files) throws Exception
    {
        if (files.length == 0)
        {
            return;
        }

        RegistryChangeListenerImpl listener = new RegistryChangeListenerImpl();
        registry.registerListener(listener);

        try
        {
            Set manifests = new HashSet();
            Set archives = new HashSet();

            for (int i = 0; i < files.length; i++)
            {
                collectManifests(files[i], manifests, archives);
            }

            registry.register((URL[]) manifests.toArray(
                    new URL[manifests.size()]));

            for (Iterator it = archives.iterator(); it.hasNext();)
            {
                PluginArchiver.readDescriptor((URL) it.next(), registry);
            }
        }
        finally
        {
            registry.unregisterListener(listener);
        }

        updateTree();
        showRegistryChanges(listener);
    }

    void showRegistryChanges(final RegistryChangeListenerImpl listener)
    {
        Map ctx = new HashMap();
        ctx.put("addedPlugins", listener.addedPlugins());
        ctx.put("removedPlugins", listener.removedPlugins());
        ctx.put("modifiedPlugins", listener.modifiedPlugins());
        ctx.put("addedExtensions", listener.addedExtensions());
        ctx.put("removedExtensions", listener.removedExtensions());
        ctx.put("modifiedExtensions", listener.modifiedExtensions());
        renderTemplate("registryChangeReport.jxp", ctx);
    }

    void updateTree()
    {
        jTree.clearSelection();

        DefaultTreeModel model = new DefaultTreeModel(new DefaultMutableTreeNode(
                    "Root"));
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) model.getRoot();
        DefaultMutableTreeNode pluginsNode = new DefaultMutableTreeNode(
                "Plug-ins");
        root.add(pluginsNode);

        for (Iterator it = registry.getPluginDescriptors().iterator();
                it.hasNext();)
        {
            PluginDescriptor descr = (PluginDescriptor) it.next();
            TreeNodeImpl node = new TreeNodeImpl(descr);
            populateDescriptor(node, descr);
            pluginsNode.add(node);
        }

        DefaultMutableTreeNode fragmentsNode = new DefaultMutableTreeNode(
                "Plug-in fragments");
        root.add(fragmentsNode);

        for (Iterator it = registry.getPluginFragments().iterator();
                it.hasNext();)
        {
            fragmentsNode.add(new TreeNodeImpl((PluginFragment) it.next()));
        }

        jTree.setModel(model);
        jTree.scrollPathToVisible(new TreePath(
                ((DefaultMutableTreeNode) root.getFirstChild()).getPath()));
    }

    private void populateDescriptor(final TreeNodeImpl parentNode,
        final PluginDescriptor descr)
    {
        DefaultMutableTreeNode extPointsNode = new DefaultMutableTreeNode(
                "Extension points");

        for (Iterator it = descr.getExtensionPoints().iterator(); it.hasNext();)
        {
            extPointsNode.add(new TreeNodeImpl((ExtensionPoint) it.next()));
        }

        parentNode.add(extPointsNode);

        DefaultMutableTreeNode extensionsNode = new DefaultMutableTreeNode(
                "Extensions");

        for (Iterator it = descr.getExtensions().iterator(); it.hasNext();)
        {
            extensionsNode.add(new TreeNodeImpl((Extension) it.next()));
        }

        parentNode.add(extensionsNode);
    }

    void renderTemplate(final String name, final Map ctx)
    {
        StringWriter writer = new StringWriter();

        try
        {
            processor.process(name, new JxpProcessingContext(writer, ctx));
        }
        catch (Throwable t)
        {
            ErrorDialog.showError(this, "Error", t);
        }

        htmlTextPane.setText(writer.getBuffer().toString());
        htmlTextPane.setCaretPosition(0);
    }

    private void collectManifests(final File file, final Set manifests,
        final Set archives) throws Exception
    {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".jpa"))
        {
            archives.add(file.toURL());

            return;
        }

        URL url = getManifestUrl(file);

        if (url != null)
        {
            manifests.add(url);

            return;
        }

        if (file.isDirectory())
        {
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++)
            {
                collectManifests(files[i], manifests, archives);
            }
        }
    }

    /**
         * This method initializes jSplitPane
         * @return javax.swing.JSplitPane
         */
    private JSplitPane getJSplitPane()
    {
        if (jSplitPane == null)
        {
            jSplitPane = new JSplitPane();
            jSplitPane.setLeftComponent(getLeftPanel());
            jSplitPane.setRightComponent(getJScrollPane1());
            jSplitPane.setDividerLocation(290);
        }

        return jSplitPane;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getLeftPanel()
    {
        if (leftPanel == null)
        {
            leftPanel = new JPanel();
            leftPanel.setLayout(new BorderLayout());
            leftPanel.add(getJPanel(), java.awt.BorderLayout.NORTH);
            leftPanel.add(getJScrollPane(), java.awt.BorderLayout.CENTER);
        }

        return leftPanel;
    }

    /**
     * This method initializes jPanel
     *
     * @return javax.swing.JPanel
     */
    private JPanel getJPanel()
    {
        if (jPanel == null)
        {
            jPanel = new JPanel();
            jPanel.add(getAddManifestButton(), null);
            jPanel.add(getRemoveManifestButton(), null);
            jPanel.add(getCheckIntegrityButton(), null);
            jPanel.add(getShowGraphButton(), null);
        }

        return jPanel;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getAddManifestButton()
    {
        if (addManifestButton == null)
        {
            addManifestButton = new JButton();
            addManifestButton.setIcon(new ImageIcon(getClass()
                                                        .getResource("icons/plus.gif")));
            //                        addManifestButton.setText("Register plug-in(s)");
            addManifestButton.setToolTipText("Ajouter un ou des plugin(s)");
            addManifestButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(final ActionEvent evt)
                    {
                        try
                        {
                            fcM.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                            fcM.setMultiSelectionEnabled(true);
                            fcM.showOpenDialog(MainPanel.this);
                            addManifests(fcM.getSelectedFiles());
                        }
                        catch (Throwable t)
                        {
                            ErrorDialog.showError(MainPanel.this, "Error", t);
                        }
                    }
                });
        }

        return addManifestButton;
    }

    /**
     * This method initializes jScrollPane
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane()
    {
        if (jScrollPane == null)
        {
            jScrollPane = new JScrollPane();
            jScrollPane.setViewportView(getJTree());
        }

        return jScrollPane;
    }

    /**
     * This method initializes jTree
     *
     * @return javax.swing.JTree
     */
    private JTree getJTree()
    {
        if (jTree == null)
        {
            jTree = new JTree(new DefaultTreeModel(
                        new DefaultMutableTreeNode("Root")));
            jTree.setRootVisible(false);
            jTree.setEditable(false);
            jTree.setShowsRootHandles(true);
            jTree.addTreeSelectionListener(new TreeSelectionListener()
                {
                    public void valueChanged(final TreeSelectionEvent evt)
                    {
                        Object node = jTree.getLastSelectedPathComponent();

                        if (!(node instanceof TreeNodeImpl))
                        {
                            return;
                        }

                        Identity idt = ((TreeNodeImpl) node).getIdentity();
                        Map ctx = new HashMap();
                        String template;

                        if (idt instanceof PluginDescriptor)
                        {
                            ctx.put("descriptor", idt);
                            template = "plugin.jxp";
                        }
                        else if (idt instanceof PluginFragment)
                        {
                            ctx.put("fragment", idt);
                            template = "fragment.jxp";
                        }
                        else if (idt instanceof ExtensionPoint)
                        {
                            ctx.put("extPoint", idt);
                            template = "extPoint.jxp";
                        }
                        else if (idt instanceof Extension)
                        {
                            ctx.put("ext", idt);
                            template = "ext.jxp";
                        }
                        else
                        {
                            JOptionPane.showMessageDialog(MainPanel.this,
                                "Unknown identity: " + idt);

                            return;
                        }

                        renderTemplate(template, ctx);
                    }
                });
        }

        return jTree;
    }

    /**
     * This method initializes jButton1
     *
     * @return javax.swing.JButton
     */
    private JButton getRemoveManifestButton()
    {
        if (removeManifestButton == null)
        {
            removeManifestButton = new JButton();
            removeManifestButton.setIcon(new ImageIcon(getClass()
                                                           .getResource("icons/minus.gif")));
            removeManifestButton.setToolTipText("Supprimer un plug-in");
            removeManifestButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(final ActionEvent evt)
                    {
                        Object node = jTree.getLastSelectedPathComponent();

                        if (!(node instanceof TreeNodeImpl))
                        {
                            return;
                        }

                        Identity idt = ((TreeNodeImpl) node).getIdentity();

                        if (!(idt instanceof PluginDescriptor) &&
                                !(idt instanceof PluginFragment))
                        {
                            return;
                        }

                        RegistryChangeListenerImpl listener = new RegistryChangeListenerImpl();
                        registry.registerListener(listener);

                        try
                        {
                            registry.unregister(new String[] { idt.getId() });
                        }
                        finally
                        {
                            registry.unregisterListener(listener);
                        }

                        updateTree();
                        showRegistryChanges(listener);
                    }
                });
        }

        return removeManifestButton;
    }

    /**
     * This method initializes jScrollPane1
     *
     * @return javax.swing.JScrollPane
     */
    private JScrollPane getJScrollPane1()
    {
        if (jScrollPane1 == null)
        {
            jScrollPane1 = new JScrollPane();
            jScrollPane1.setViewportView(getHtmlTextPane());
        }

        return jScrollPane1;
    }

    /**
     * This method initializes jTextPane
     *
     * @return javax.swing.JTextPane
     */
    private JTextPane getHtmlTextPane()
    {
        if (htmlTextPane == null)
        {
            htmlTextPane = new JTextPane();
            htmlTextPane.setContentType("text/html");
            htmlTextPane.setEditable(false);
            htmlTextPane.addHyperlinkListener(new HyperlinkListener()
                {
                    public void hyperlinkUpdate(final HyperlinkEvent evt)
                    {
                        if (!evt.getEventType()
                                    .equals(HyperlinkEvent.EventType.ACTIVATED))
                        {
                            return;
                        }

                        String descr = evt.getDescription();
                        int p = descr.indexOf(':');

                        if (p == -1)
                        {
                            JOptionPane.showMessageDialog(MainPanel.this,
                                "Unsupported link: " + descr);

                            return;
                        }

                        activateTreeNode(descr.substring(0, p),
                            descr.substring(p + 1));
                    }
                });
        }

        return htmlTextPane;
    }

    void activateTreeNode(final String type, final String id)
    {
        TreeNodeImpl node = null;

        if ("plugin".equalsIgnoreCase(type))
        {
            node = findPluginNode(id);
        }
        else if ("fragment".equalsIgnoreCase(type))
        {
            node = findFragmentNode(id);
        }
        else if ("extp".equalsIgnoreCase(type))
        {
            node = findExtPointNode(findPluginNode(registry.extractPluginId(id)),
                    registry.extractId(id));
        }
        else if ("ext".equalsIgnoreCase(type))
        {
            node = findExtNode(findPluginNode(registry.extractPluginId(id)),
                    registry.extractId(id));
        }
        else
        {
            JOptionPane.showMessageDialog(this,
                "Unknown link type [" + type +
                "] given for manifest element with ID " + id);

            return;
        }

        if (node == null)
        {
            JOptionPane.showMessageDialog(this,
                "Unable to find node for link of type [" + type + "] and ID " +
                id);

            return;
        }

        jTree.setSelectionPath(new TreePath(node.getPath()));
    }

    private TreeNodeImpl findPluginNode(final String id)
    {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) jTree.getModel()
                                                                    .getRoot();
        DefaultMutableTreeNode pluginsNode = null;

        for (Enumeration en = root.children(); en.hasMoreElements();)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();

            if (node.getUserObject().equals("Plug-ins"))
            {
                pluginsNode = node;

                break;
            }
        }

        if (pluginsNode == null)
        {
            return null;
        }

        for (Enumeration en = pluginsNode.children(); en.hasMoreElements();)
        {
            TreeNodeImpl node = (TreeNodeImpl) en.nextElement();

            if (node.getIdentity().getId().equals(id))
            {
                return node;
            }
        }

        return null;
    }

    private TreeNodeImpl findFragmentNode(final String id)
    {
        DefaultMutableTreeNode root = (DefaultMutableTreeNode) jTree.getModel()
                                                                    .getRoot();
        DefaultMutableTreeNode fragmentsNode = null;

        for (Enumeration en = root.children(); en.hasMoreElements();)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();

            if (node.getUserObject().equals("Plug-in fragments"))
            {
                fragmentsNode = node;

                break;
            }
        }

        if (fragmentsNode == null)
        {
            return null;
        }

        for (Enumeration en = fragmentsNode.children(); en.hasMoreElements();)
        {
            TreeNodeImpl node = (TreeNodeImpl) en.nextElement();

            if (node.getIdentity().getId().equals(id))
            {
                return node;
            }
        }

        return null;
    }

    private TreeNodeImpl findExtPointNode(final TreeNodeImpl pluginNode,
        final String id)
    {
        if (pluginNode == null)
        {
            return null;
        }

        DefaultMutableTreeNode extPointsNode = null;

        for (Enumeration en = pluginNode.children(); en.hasMoreElements();)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();

            if (node.getUserObject().equals("Extension points"))
            {
                extPointsNode = node;

                break;
            }
        }

        if (extPointsNode == null)
        {
            return null;
        }

        for (Enumeration en = extPointsNode.children(); en.hasMoreElements();)
        {
            TreeNodeImpl node = (TreeNodeImpl) en.nextElement();

            if (node.getIdentity().getId().equals(id))
            {
                return node;
            }
        }

        return null;
    }

    private TreeNodeImpl findExtNode(final TreeNodeImpl pluginNode,
        final String id)
    {
        if (pluginNode == null)
        {
            return null;
        }

        DefaultMutableTreeNode extsNode = null;

        for (Enumeration en = pluginNode.children(); en.hasMoreElements();)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) en.nextElement();

            if (node.getUserObject().equals("Extensions"))
            {
                extsNode = node;

                break;
            }
        }

        if (extsNode == null)
        {
            return null;
        }

        for (Enumeration en = extsNode.children(); en.hasMoreElements();)
        {
            TreeNodeImpl node = (TreeNodeImpl) en.nextElement();

            if (node.getIdentity().getId().equals(id))
            {
                return node;
            }
        }

        return null;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getCheckIntegrityButton()
    {
        if (checkIntegrityButton == null)
        {
            checkIntegrityButton = new JButton();
            checkIntegrityButton.setIcon(new ImageIcon(getClass()
                                                           .getResource("icons/check.gif")));
            checkIntegrityButton.setToolTipText(
                "Vérifier l'intégrité des plug-ins");
            checkIntegrityButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(final ActionEvent e)
                    {
                        if (registry == null)
                        {
                            return;
                        }

                        jTree.clearSelection();

                        Map ctx = new HashMap();
                        ctx.put("report", registry.checkIntegrity(null));
                        renderTemplate("integrityCheckReport.jxp", ctx);
                    }
                });
        }

        return checkIntegrityButton;
    }

    /**
     * This method initializes jButton
     *
     * @return javax.swing.JButton
     */
    private JButton getShowGraphButton()
    {
        if (showGraphButton == null)
        {
            showGraphButton = new JButton();
            showGraphButton.setIcon(new ImageIcon(getClass()
                                                      .getResource("icons/diagram.gif")));
            showGraphButton.setToolTipText(
                "Visualiser le diagramme de plugin-in(s)");
            showGraphButton.addActionListener(new ActionListener()
                {
                    public void actionPerformed(final ActionEvent evt)
                    {
                        if (registry.getPluginDescriptors().isEmpty())
                        {
                            JOptionPane.showMessageDialog(MainPanel.this,
                                "No plug-ins registered. Register at least one plug-in first.",
                                "Data Missing", JOptionPane.WARNING_MESSAGE);

                            return;
                        }

                        GraphWindow window = new GraphWindow(null);

                        try
                        {
                            try
                            {
                                window.setRegistry(registry);
                            }
                            catch (Exception e)
                            {
                                ErrorDialog.showError(MainPanel.this, "Erreur",
                                    e);

                                return;
                            }

                            window.setLocationRelativeTo(MainPanel.this);
                            window.show();
                        }
                        finally
                        {
                            window.dispose();
                        }
                    }
                });
        }

        return showGraphButton;
    }

    private static class TreeNodeImpl extends DefaultMutableTreeNode
    {
        private static final long serialVersionUID = -9170454575995595543L;
        private Identity idt;

        TreeNodeImpl(final Identity identity)
        {
            idt = identity;
        }

        /**
         * @see javax.swing.tree.DefaultMutableTreeNode#toString()
         */
        public String toString()
        {
            return idt.getId();
        }

        Identity getIdentity()
        {
            return idt;
        }
    }

    private static class RegistryChangeListenerImpl
        implements RegistryChangeListener
    {
        private Set addedPlugins = new HashSet();
        private Set removedPlugins = new HashSet();
        private Set modifiedPlugins = new HashSet();
        private Set addedExtensions = new HashSet();
        private Set removedExtensions = new HashSet();
        private Set modifiedExtensions = new HashSet();

        RegistryChangeListenerImpl()
        {
            // no-op
        }

        /**
         * @see RegistryChangeListener#registryChanged(
         *      org.java.plugin.registry.PluginRegistry.RegistryChangeData)
         */
        public void registryChanged(final RegistryChangeData data)
        {
            addedPlugins.addAll(data.addedPlugins());
            removedPlugins.addAll(data.removedPlugins());
            modifiedPlugins.addAll(data.modifiedPlugins());
            addedExtensions.addAll(data.addedExtensions());
            removedExtensions.addAll(data.removedExtensions());
            modifiedExtensions.addAll(data.modifiedExtensions());
        }

        Set addedExtensions()
        {
            return addedExtensions;
        }

        Set addedPlugins()
        {
            return addedPlugins;
        }

        Set modifiedExtensions()
        {
            return modifiedExtensions;
        }

        Set modifiedPlugins()
        {
            return modifiedPlugins;
        }

        Set removedExtensions()
        {
            return removedExtensions;
        }

        Set removedPlugins()
        {
            return removedPlugins;
        }
    }
} //  @jve:decl-index=0:visual-constraint="10,10"
