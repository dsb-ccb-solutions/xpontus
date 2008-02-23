/*
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
 *
 */
package net.sf.xpontus.plugins.preferences;

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
 * The preferences plugin
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PreferencesPlugin extends XPontusPlugin {
    /**
     *
     */
    public static final String EXTENSION_POINT_NAME = "preferencespluginif";
    public static final String PLUGIN_CATEGORY = "Tools";

    /**
     *
     */
    public static final String PLUGIN_IDENTIFIER = "plugin.core.preferences";
    private Hashtable settings;

    @Override
    public void init() throws Exception {
        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_PREFERENCES_PANELS,
            settings);

        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint outlineExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                        .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = outlineExtPoint.getConnectedExtensions();

        System.out.println("prefs:" + plugins.size());

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();

            Class cl = classLoader.loadClass(className);
            PreferencesPluginIF pref = (PreferencesPluginIF) cl.newInstance();
            String id = pref.getPreferencesPanelComponent().getId();
            String category = pref.getPluginCategory();
            Hashtable t = new Hashtable();
            t.put("category", category);
            t.put("id", id);
            t.put(XPontusConstantsIF.OBJECT_CLASSNAME, cl.getName());
            t.put(XPontusConstantsIF.CLASS_LOADER, classLoader);
            settings.put(id, t);
        }
    }

    @Override
    protected void doStart() throws Exception {
        settings = new Hashtable();
    }

    @Override
    protected void doStop() throws Exception {
        settings.clear();
    }
}
