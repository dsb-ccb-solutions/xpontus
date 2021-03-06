/*
 * IndentationModule.java
 *
 * Created on July 1, 2007, 1:31 PM
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


/**
 *
 * @author Yves Zoundi
 */
public class IndentationPlugin extends XPontusPlugin
{
    public static final String EXTENSION_POINT_NAME = "indentationpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.indentation";
    public static final String PLUGIN_CATEGORY = "Indentation";
    private Hashtable<Object, Object> indenters;

    public IndentationPlugin()
    {
    }

    /**
     * @param plugin
     * @param classLoader
     */
    public void initExtension(IndentationPluginIF plugin,
        ClassLoader classLoader)
    {
        String mimeType = plugin.getMimeType();
        String name = plugin.getName();
        String m_className = plugin.getClass().getName();

        Hashtable<Object, Object> m_map = new Hashtable<Object, Object>();

        m_map.put(XPontusConstantsIF.OBJECT_NAME, name);
        m_map.put(XPontusConstantsIF.CLASS_LOADER, classLoader);
        m_map.put(XPontusConstantsIF.CONTENT_TYPE, mimeType);
        m_map.put(XPontusConstantsIF.OBJECT_CLASSNAME, m_className);
        indenters.put(mimeType, m_map);
    }

    /* (non-Javadoc)
     * @see net.sf.xpontus.plugins.XPontusPlugin#init()
     */
    public void init() throws Exception
    {
        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_INDENTATION_ENGINES,
            indenters);

        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint indentationExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                            .getId(),
                EXTENSION_POINT_NAME);

        Collection<Extension> extensions = indentationExtPoint.getConnectedExtensions();

        for (Extension ext : extensions)
        {
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();

            Class<?> cl = classLoader.loadClass(className);
            IndentationPluginIF mIndentationPlugin = (IndentationPluginIF) cl.newInstance();
            initExtension(mIndentationPlugin, classLoader);
        }
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception
    {
        indenters = new Hashtable<Object, Object>();
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception
    {
        indenters.clear();
    }
}
