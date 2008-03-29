/*
 * IndentationModule.java
 *
 * Created on July 1, 2007, 1:31 PM
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
package net.sf.xpontus.plugins.indentation;

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
 *
 * @author Yves Zoundi
 */
public class IndentationPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "indentationpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.indentation";
    public static final String PLUGIN_CATEGORY = "Indentation";
    private Hashtable indenters = new Hashtable();

    /** Creates a new instance of IndentationModule */
    public IndentationPlugin() {
    }

    public void initExtension(IndentationPluginIF plugin,
        ClassLoader classLoader) {
        String mimeType = plugin.getMimeType();
        String name = plugin.getName();
        String m_className = plugin.getClass().getName();

        Hashtable m_map = new Hashtable();

        m_map.put(XPontusConstantsIF.OBJECT_NAME, name);
        m_map.put(XPontusConstantsIF.CLASS_LOADER, classLoader);
        m_map.put(XPontusConstantsIF.CONTENT_TYPE, mimeType);
        m_map.put(XPontusConstantsIF.OBJECT_CLASSNAME, m_className);

        System.out.println("mimetype:" + mimeType);
        System.out.println("indenter:" + name);
        indenters.put(mimeType, m_map);
    }

    public void init() throws Exception {
        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_INDENTATION_ENGINES,
            indenters);

        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint indentationExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                            .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = indentationExtPoint.getConnectedExtensions();

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();

            Class cl = classLoader.loadClass(className);
            IndentationPluginIF mIndentationPlugin = (IndentationPluginIF) cl.newInstance();
            initExtension(mIndentationPlugin, classLoader);
        }
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
        indenters.clear();
    }
}
