/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.utils.PluginResolverUtils;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;

import java.net.URL;

import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.rmi.CORBA.Util;

import javax.swing.JFileChooser;
import javax.swing.table.DefaultTableModel;


/**
 *
 * @author mrcheeks
 */
public class InstallDownloadedPluginsController {
    private JFileChooser chooser = new JFileChooser();
    private DownloadedPanel view;

    public InstallDownloadedPluginsController() {
        chooser = new JFileChooser();
        chooser.setDialogTitle("Select archive");
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    public DownloadedPanel getView() {
        return view;
    }

    public void setView(DownloadedPanel view) {
        this.view = view;
    }

    public InputStream readDescriptor(final URL archiveFile)
        throws IOException {
        ZipInputStream zipStrm = new ZipInputStream(new BufferedInputStream(
                    archiveFile.openStream()));

        try {
            ZipEntry entry = zipStrm.getNextEntry(); 
            System.out.println("Entry:" + entry.getName());
            if (entry == null) { 
                throw new IOException(
                    "invalid plug-ins archive, no entries found");
            }

            return zipStrm;
        } catch (Exception e) {
            System.out.println("Exception");
            e.printStackTrace();
            return null;
        }
    }

    public void addPlugin() {
        int rep = chooser.showOpenDialog(XPontusComponentsUtils.getTopComponent()
                                                               .getDisplayComponent());

        if (rep == JFileChooser.APPROVE_OPTION) {
            File[] selectedFiles = chooser.getSelectedFiles();
            DefaultTableModel tableModel = (DefaultTableModel) view.getPluginsTable()
                                                                   .getModel();

            for (File selectedFile : selectedFiles) {
                try {
                    FileObject fo = VFS.getManager().toFileObject(selectedFile);
                    InputStream is = readDescriptor(fo.getURL());
                    String s = IOUtils.toString(is); 
                    SimplePluginDescriptor spd = PluginResolverUtils.resolvePlugins(new ByteArrayInputStream(s.getBytes()));
                    tableModel.addRow(new String[] {
                            spd.getId(), spd.getCategory(), spd.getBuiltin()
                        });
                } catch (Exception err) {
                    err.printStackTrace();
                }
            }
        }
    }
}
