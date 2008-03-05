/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenario.fop.fop_plugin;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.java.plugin.Plugin;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Iterator;
import java.util.Properties;


/**
 *
 * @author Propriétaire
 */
public class FOPTransformationPlugin extends Plugin {
    static File configfile = null;
    String packageName = getClass().getPackage().getName();
    File confdir = new File(XPontusConfigurationConstantsIF.XPONTUS_PREFERENCES_DIR,
            packageName);

    protected void doStart() throws Exception {
        try {
            if (!confdir.exists()) {
                confdir.mkdirs();
            }

            configfile = new File(confdir, "settings.properties");

            Properties props = new Properties();

            if (!configfile.exists()) {
                OutputStream bos = FileUtils.openOutputStream(configfile);

                PropertiesConfigurationLoader.save(configfile, props);
            } else {
                InputStream fis = FileUtils.openInputStream(configfile);
                props.load(fis);
                IOUtils.closeQuietly(fis);
            }

            Iterator it = props.keySet().iterator();

            while (it.hasNext()) {
                Object m_key = it.next();
                Object m_value = props.get(m_key);
                XPontusConfig.put(m_key, m_value);
            }
        } catch (Exception err) {
            log.error("Error loading FOP transformation plugin settings");
            log.error(err.getMessage());
        }
    }

    protected void doStop() throws Exception {
    }
}
