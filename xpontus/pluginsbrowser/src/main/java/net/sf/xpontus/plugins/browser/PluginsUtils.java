/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.browser;

import org.java.plugin.util.IoUtil;

import java.io.File;

import java.net.MalformedURLException;
import java.net.URL;


/**
 *
 * @author mrcheeks
 */
public class PluginsUtils {
    private static URL getManifestUrl(final File file) {
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
