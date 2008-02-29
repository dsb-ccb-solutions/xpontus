/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.browser;

import org.java.plugin.util.IoUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;


/**
 *
 * @author mrcheeks
 */
public class PluginsUtils {
    public static void unzip(String zipFilename, String outdir)
        throws IOException {
        System.out.println("In unzip method...");
        ZipFile zipFile = new ZipFile(zipFilename);
        Enumeration entries = zipFile.entries();

        while (entries.hasMoreElements()) {
            ZipEntry entry = (ZipEntry) entries.nextElement();
            System.out.println("Entry name:" + entry.getName());
            boolean isDirectory = entry.isDirectory();
            byte[] buffer = new byte[1024];
            int len;

             File destDir = new File(outdir + File.separator +
                        entry.getName());
             
            if (isDirectory) {
               
                System.out.println("DestDir:" + destDir.getAbsolutePath());

                if (!destDir.exists()) {
                    destDir.mkdirs();
                }
            } else {
                InputStream zipin = zipFile.getInputStream(entry);
                BufferedOutputStream fileout = new BufferedOutputStream(new FileOutputStream(outdir +
                            File.separator + entry.getName()));

                while ((len = zipin.read(buffer)) >= 0)
                    fileout.write(buffer, 0, len);

                zipin.close();
                fileout.flush();
                fileout.close();
            }
        }
    }

    public static URL getManifestUrl(final File file) {
        try {
            if (file.isDirectory()) {
                File result = new File(file, "plugin.xml"); //$NON-NLS-1$

                if (result.isFile()) {
                    return IoUtil.file2url(result);
                }

                result = new File(file, "plugin-fragment.xml"); //$NON-NLS-1$

                if (result.isFile()) {
                    return IoUtil.file2url(result);
                }

                return null;
            }

            if (!file.isFile()) {
                return null;
            }

            URL url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() + "!/plugin.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() +
                    "!/plugin-fragment.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() +
                    "!/META-INF/plugin.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }

            url = new URL("jar:" //$NON-NLS-1$
                     +IoUtil.file2url(file).toExternalForm() +
                    "!/META-INF/plugin-fragment.xml"); //$NON-NLS-1$

            if (IoUtil.isResourceExists(url)) {
                return url;
            }
        } catch (MalformedURLException mue) {
            // ignore
        }

        return null;
    }
}
