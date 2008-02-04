/*
 * OutlinePlugin.java
 *
 * Created on 2007-08-08, 14:56:58
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
package net.sf.xpontus.plugins.outline;

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
 * Plugin for outline views
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class OutlinePlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "outlinepluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.outline";
    public static final String PLUGIN_CATEGORY = "Outline";
    private Hashtable outliners;

    public OutlinePlugin() {
    }

    public void init() throws Exception {
        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_OUTLINE_ENGINES,
            outliners);

        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint outlineExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                        .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = outlineExtPoint.getConnectedExtensions();

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();

            Class cl = classLoader.loadClass(className);
            OutlinePluginIF m_outliner = (OutlinePluginIF) cl.newInstance();
            initExtension(m_outliner);
        }
    }

    protected void doStart() throws Exception {
        outliners = new Hashtable();
    }

    protected void doStop() throws Exception {
        outliners.clear();
    }

    private void initExtension(OutlinePluginIF m_outliner) {
        String m_class = m_outliner.getClass().getName();
        String m_mime = m_outliner.getContentType();

        outliners.put(m_mime, m_class);
    }
}
