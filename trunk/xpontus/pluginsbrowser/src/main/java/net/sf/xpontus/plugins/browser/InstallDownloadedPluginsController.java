/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import net.sf.xpontus.plugins.SimplePluginDescriptor;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import org.java.plugin.registry.Identity;
import org.java.plugin.registry.PluginRegistry;

import java.io.File;
import java.io.InputStream;

import java.net.URL;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JFileChooser;
import javax.swing.JTable;
import javax.swing.table.DefaultTableModel;
import org.java.plugin.registry.PluginDescriptor;

/**
 * @version 0..0.1
 * @author Yves Zoundi
 */
public class InstallDownloadedPluginsController {

    private JFileChooser chooser = new JFileChooser();
    private DownloadedPanel view;
    private PluginRegistry registry;

    public JFileChooser getFileChooser() {

        if (chooser == null) {
            chooser = new JFileChooser();
            chooser.setDialogTitle("Select XPontus plugin archive");
            chooser.setMultiSelectionEnabled(true);
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setFileFilter(new XPontusModuleFileFilter());
            chooser.setAcceptAllFileFilterUsed(false);
        }

        return chooser;
    }

    public InstallDownloadedPluginsController() {

    }

    public PluginRegistry getRegistry() {
        if (registry == null) {
            registry = XPontusPluginManager.getPluginManager().getRegistry();
        }

        return registry;
    }

    public DownloadedPanel getView() {
        return view;
    }

    public void setView(DownloadedPanel view) {
        this.view = view;
    }

    private void collectManifests(final File file, final Set manifests,
            final Set archives) throws Exception {
        String name = file.getName().toLowerCase();

        if (name.endsWith(".jpa")) {
            archives.add(file.toURL());

            return;
        }

        URL url = PluginsUtils.getInstance().getManifestUrl(file);

        if (url != null) {
            manifests.add(url);

            return;
        }

        if (file.isDirectory()) {
            File[] files = file.listFiles();

            for (int i = 0; i < files.length; i++) {
                collectManifests(files[i], manifests, archives);
            }
        }
    }

    void addManifests(final File[] files) throws Exception {
        if (files.length == 0) {
            return;
        }

        try {
            Set manifests = new HashSet();
            Set archives = new HashSet();

            for (int i = 0; i < files.length; i++) {
                collectManifests(files[i], manifests, archives);
            }

            Map<String, Identity> m_map = getRegistry().register((URL[]) manifests.toArray(
                    new URL[manifests.size()]));

            if (m_map.size() > 0) {
                Iterator<String> it = m_map.keySet().iterator();

                while (it.hasNext()) {
                    String cle = it.next();

                    Identity m_id = m_map.get(cle);
                    String pluginIdentifier = m_id.getId();

                    File archiveFile = view.getFilesMap().get(pluginIdentifier);

                    File destFolder = new File(XPontusConfigurationConstantsIF.XPONTUS_PLUGINS_DIR,
                            pluginIdentifier);

                    if (!destFolder.exists()) {
                        destFolder.mkdirs();
                    }

                    try {
                        PluginsUtils.getInstance().unzip(archiveFile.getAbsolutePath(),
                                destFolder.getAbsolutePath());
                    } catch (Exception err) {
                        throw new Exception("Error extracting plugin archive");
                    }
                }
            }
        } finally {
        }
    }

    public void findRowForPlugin(String id) {
        JTable table = view.getPluginsTable();
        int selected = table.getSelectedRow();

        DefaultTableModel m_model = (DefaultTableModel) table.getModel();
        m_model.removeRow(selected);
    }

    public void installPlugin() {
        JTable table = view.getPluginsTable();
        int selected = table.getSelectedRow();

        if (selected == -1) {
            return;
        }

        DefaultTableModel m_model = (DefaultTableModel) table.getModel();

        String id = (String) m_model.getValueAt(selected, 1);

        File pluginArchive = view.getFilesMap().get(id);

        try {
            addManifests(new File[]{pluginArchive});
            PluginDescriptor pds = XPontusPluginManager.getPluginManager().getRegistry().getPluginDescriptor(id);
            SimplePluginDescriptor spd = PluginsUtils.getInstance().toSimplePluginDescriptor(pds);
            PluginsUtils.getInstance().addToIndex(spd);
        } catch (Exception e) {
            e.printStackTrace();
            XPontusComponentsUtils.showErrorMessage(e.getMessage());
        }
    }

    public void addPlugin() {
        int rep = getFileChooser().showOpenDialog(XPontusComponentsUtils.getTopComponent().getDisplayComponent());


        if (rep == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();

            DefaultTableModel tableModel = (DefaultTableModel) view.getPluginsTable().getModel();

            for (File selectedFile : selectedFiles) {
                try {
                    URL manifestURL = PluginsUtils.getInstance().getManifestUrl(selectedFile);

                    if (manifestURL != null) {
                        InputStream is = manifestURL.openStream();
                        SimplePluginDescriptor spd = net.sf.xpontus.utils.PluginResolverUtils.resolvePlugins(is);

                        if (spd.getId() != null) {
                            view.getFilesMap().put(spd.getId(), selectedFile);
                            view.getPluginsMap().put(spd.getId(), spd);
                            tableModel.addRow(new Object[]{
                                new Boolean(false), spd.getId(),
                                spd.getCategory(), spd.getBuiltin()
                            });
                        }
                    }
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }
    }
}
