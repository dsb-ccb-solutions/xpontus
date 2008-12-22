/*
 * HTMLIndentationPlugin.java
 *
 * Created on Aug 13, 2007, 7:21:39 PM
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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
 */
package net.sf.xpontus.plugins.indentation.html;

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
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class HTMLIndentationPlugin extends Plugin {
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

                props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
                    HTMLIndenterPluginConstantsIF.ATTRIBUTES_PROPERTY, "lower");

                props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
                    HTMLIndenterPluginConstantsIF.ELEMENTS_PROPERTY, "lower");

                props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
                    HTMLIndenterPluginConstantsIF.FIX_WINDOW_ENTITIES_PROPERTY,
                    "true");

                props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
                    HTMLIndenterPluginConstantsIF.NORMALIZE_ATTRIBUTES_PROPERTY,
                    "true");

                props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
                    HTMLIndenterPluginConstantsIF.ENCODING_PROPERTY,
                    "AUTODETECT");

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
            log.error("Error loading HTML indentation settings");
            log.error(err.getMessage());
        }
    }

    protected void doStop() throws Exception {
    }
}
