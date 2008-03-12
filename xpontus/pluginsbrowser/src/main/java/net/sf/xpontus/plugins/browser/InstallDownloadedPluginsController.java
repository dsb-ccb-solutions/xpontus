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

import java.awt.Dimension;
import java.awt.Frame;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import net.sf.xpontus.plugins.SimplePluginDescriptor;
import net.sf.xpontus.utils.XPontusComponentsUtils;


import org.java.plugin.registry.Identity;
import org.java.plugin.registry.PluginRegistry;

import java.io.File;
import java.io.InputStream;

import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.table.DefaultTableModel;
import org.java.plugin.registry.PluginDescriptor;

/**
 * @version 0..0.1
 * @author Yves Zoundi
 */
public class InstallDownloadedPluginsController {

    private JFileChooser chooser;
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

    public Object licenseAccepted(String license) {
        JScrollPane sp = new JScrollPane();
        Dimension dimension = new Dimension(300, 300);
        sp.setMinimumSize(dimension);
        sp.setPreferredSize(dimension);

        JTextArea licenseField = new JTextArea();
        licenseField.setEditable(false);
        licenseField.setLineWrap(true);

        try {
            InputStream is = new URL(license).openStream();
            Reader reader = new InputStreamReader(is);
            licenseField.read(reader, null);
        } catch (Exception e) {
            licenseField.setText("Cannot get license text information");
        }

        sp.getViewport().add(licenseField);

        Frame frame = (Frame) XPontusComponentsUtils.getTopComponent().getDisplayComponent();
        JOptionPane pane = new JOptionPane(sp, JOptionPane.PLAIN_MESSAGE, JOptionPane.YES_NO_OPTION, null, new Object[]{"Yes", "No"});

        JDialog dialog = pane.createDialog(frame, "License agreement");
        dialog.show();

        Object selectedValue = pane.getValue();

        return selectedValue;
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
            Object answer = licenseAccepted(view.getPluginsMap().get(id).getLicense());

            if (answer == null || answer.equals("No")) {
                System.out.println("License not accepted");
                XPontusComponentsUtils.showErrorMessage("You must agree with the license terms");
            } else {
                System.out.println("License accepted");
                addManifests(new File[]{pluginArchive});
                PluginDescriptor pds = XPontusPluginManager.getPluginManager().getRegistry().getPluginDescriptor(id);
                SimplePluginDescriptor spd = PluginsUtils.toSimplePluginDescriptor(pds);
                PluginsUtils.getInstance().addToIndex(spd);
            }
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
