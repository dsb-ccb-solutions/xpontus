/*
 * TextEditorPlugin.java
 *
 * Created on Jul 29, 2007, 8:56:53 AM
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
 */
package net.sf.xpontus.plugins.gui.editor;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.plugins.XPontusPlugin;
import net.sf.xpontus.properties.PropertiesHolder;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;


/**
 * Text editor plugin
 * @author Yves Zoundi
 */
public class TextEditorPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "texteditormoduleif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.editor";
    private Hashtable h_plugins;

    /**
     * 
     */
    public TextEditorPlugin() {
    }

    /**
     *
     * @param m_plugin
     */
    public void initExtension(TextEditorPluginIF m_plugin) {
        ClassLoader loader = m_plugin.getClass().getClassLoader();
        String m_plugin_name = m_plugin.getClass().getName();

        Hashtable m_plugin_table = new Hashtable();

        m_plugin_table.put(XPontusConstantsIF.CLASS_LOADER, loader);
        m_plugin_table.put(XPontusConstantsIF.OBJECT_CLASSNAME, m_plugin_name);

        h_plugins.put(m_plugin_name, m_plugin_table);
    }

    public void init() throws Exception {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint iocPluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                          .getId(),
                EXTENSION_POINT_NAME);

        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_TEXTEDITORS_PLUGINS_PROPERTY,
            h_plugins);

        Collection plugins = iocPluginExtPoint.getConnectedExtensions();

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();

            Class cl = classLoader.loadClass(className);
            TextEditorPluginIF mPlugin = (TextEditorPluginIF) cl.newInstance();
        }
    }

    protected void doStart() throws Exception {
        h_plugins = new Hashtable();
    }

    protected void doStop() throws Exception {
        h_plugins.clear();
    }
}
