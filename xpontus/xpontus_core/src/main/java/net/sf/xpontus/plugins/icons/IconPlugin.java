/*
 * IconModule.java
 *
 * Created on 26 avril 2007, 10:59
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
package net.sf.xpontus.plugins.icons;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.plugins.XPontusPlugin;
import net.sf.xpontus.properties.PropertiesHolder;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Icon plugin
 * @author Yves Zoundi
 */
public class IconPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "iconpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugins.core.icons";
    public static final String PLUGIN_CATEGORY = "Look";
    private Map iconsMap = new HashMap();

    /** Creates a new instance of IconModule */
    public IconPlugin() {
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }

    public void initExtension(IconPluginIF m_plugin) {
        String alias = m_plugin.getIconNameAlias();
        String name = m_plugin.getClass().getName();
        ClassLoader cl = m_plugin.getClass().getClassLoader();

        Map m_map = new HashMap();

        m_map.put(XPontusConstantsIF.OBJECT_NAME, name);
        m_map.put(XPontusConstantsIF.CLASS_LOADER, cl);
        m_map.put(XPontusConstantsIF.OBJECT_CLASSNAME, cl);

        iconsMap.put(m_plugin.getIconNameAlias(), m_map);
    }

    public void init() throws Exception {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint iconPluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                           .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = iconPluginExtPoint.getConnectedExtensions();

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();
            Class cl = classLoader.loadClass(className);
            IconPluginIF mPlugin = (IconPluginIF) cl.newInstance();
        }

        PropertiesHolder.registerProperty("XPONTUS_ICONS", iconsMap);
    }
}
