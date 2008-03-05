/*
 * XMLLexerPlugin.java
 *
 * Created on 29-Jul-2007, 5:49:38 PM
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
package net.sf.xpontus.plugins.lexer.xml;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.utils.ColorUtils;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.java.plugin.Plugin;

import java.awt.Color;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Iterator;
import java.util.Properties;


/**
 * XML Lexer plugin
 * @author Yves Zoundi
 */
public class XMLLexerPlugin extends Plugin {
    static File configfile = null;
    String packageName = getClass().getPackage().getName();
    File confdir = new File(XPontusConfigurationConstantsIF.XPONTUS_PREFERENCES_DIR,
            packageName);

    public XMLLexerPlugin() {
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

                props.put(XMLLexerPreferencesConstantsIF.class.getName() + "$" +
                    XMLLexerPreferencesConstantsIF.STRING_PROPERTY, ColorUtils.colorToString(Color.red));

                props.put(XMLLexerPreferencesConstantsIF.class.getName() + "$" +
                    XMLLexerPreferencesConstantsIF.ATTRIBUTES_PROPERTY,
                    ColorUtils.colorToString(Color.RED));

                props.put(XMLLexerPreferencesConstantsIF.class.getName() + "$" +
                    XMLLexerPreferencesConstantsIF.COMMENT_PROPERTY,
                    ColorUtils.colorToString(new Color(0, 139, 0)));

                props.put(XMLLexerPreferencesConstantsIF.class.getName() + "$" +
                    XMLLexerPreferencesConstantsIF.TAGS_PROPERTY, ColorUtils.colorToString(Color.BLUE));

                props.put(XMLLexerPreferencesConstantsIF.class.getName() + "$" +
                    XMLLexerPreferencesConstantsIF.DECLARATION_PROPERTY,
                    ColorUtils.colorToString(Color.MAGENTA));

                PropertiesConfigurationLoader.save(configfile, props);

                Iterator it = props.keySet().iterator();

                while (it.hasNext()) {
                    Object m_key = it.next();
                    String m_value = (String) props.get(m_key);
                    Color c = ColorUtils.stringToColor(m_value);
                    XPontusConfig.put(m_key, c);
                }
            } else {
                InputStream fis = FileUtils.openInputStream(configfile);
                props.load(fis);
                IOUtils.closeQuietly(fis);

                Iterator it = props.keySet().iterator();

                while (it.hasNext()) {
                    Object m_key = it.next();
                    String m_value = (String) props.get(m_key); 

                    Color c = ColorUtils.stringToColor(m_value);
                    XPontusConfig.put(m_key, c);
                }
            }
        } catch (Exception err) {
            log.error("Error loading HTML lexer settings");
            log.error(err.getMessage());
        }
    }

    protected void doStop() throws Exception {
    }
}
