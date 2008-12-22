/*
 * XMLIndentationPlugin.java
 *
 * Created on 8-Aug-2007, 6:01:33 PM
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
 */
package net.sf.xpontus.plugins.indentation.xml;

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
 * Class description
 * @author Yves Zoundi
 */
public class XMLIndentationPlugin extends Plugin {
    static File configfile = null;
    String packageName = getClass().getPackage().getName();
    File confdir = new File(XPontusConfigurationConstantsIF.XPONTUS_PREFERENCES_DIR,
            packageName);

    public XMLIndentationPlugin() {
    }

    protected void doStart() throws Exception {
        try {
            if (!confdir.exists()) {
                confdir.mkdirs();
            }

            configfile = new File(confdir, "settings.properties");

            Properties props = new Properties();

            if (!configfile.exists()) {
                OutputStream bos = FileUtils.openOutputStream(configfile);

                props.put(XMLIndentationPreferencesConstantsIF.class.getName() +
                    "$" +
                    XMLIndentationPreferencesConstantsIF.OMIT_COMMENTS_OPTION,
                    "true");

                props.put(XMLIndentationPreferencesConstantsIF.class.getName() +
                    "$" +
                    XMLIndentationPreferencesConstantsIF.OMIT_DOCTYPE_OPTION,
                    "true");

                props.put(XMLIndentationPreferencesConstantsIF.class.getName() +
                    "$" +
                    XMLIndentationPreferencesConstantsIF.OMIT_XML_DECLARATION_OPTION,
                    "true");

                props.put(XMLIndentationPreferencesConstantsIF.class.getName() +
                    "$" +
                    XMLIndentationPreferencesConstantsIF.PRESERVE_SPACE_OPTION,
                    "true");

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
            err.printStackTrace();
        }
    }

    protected void doStop() throws Exception {
    }
}
