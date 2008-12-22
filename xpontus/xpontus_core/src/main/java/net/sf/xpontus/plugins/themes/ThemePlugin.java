/*
 * ThemeModule.java
 *
 * Created on 26 avril 2007, 11:05
 *
 * Copyright (C) 2005-2007 Yves Zoundi <yveszoundi at users dot sf dot net>
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
package net.sf.xpontus.plugins.themes;

import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.plugins.XPontusPlugin;
import net.sf.xpontus.properties.PropertiesHolder;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


/**
 * Plugin for themes
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class ThemePlugin extends XPontusPlugin
{
    public static final String EXTENSION_POINT_NAME = "themepluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.themes";
    public static final String PLUGIN_CATEGORY = "Look";

    /* (non-Javadoc)
     * @see net.sf.xpontus.plugins.XPontusPlugin#init()
     */
    public void init() throws Exception
    {
        Map<String, ThemePluginIF> installedThemes = new HashMap<String, ThemePluginIF>();

        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint themePluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                            .getId(),
                EXTENSION_POINT_NAME);

        Collection<Extension> plugins = themePluginExtPoint.getConnectedExtensions();

        ThemePluginIF mPlugin = new DefaultThemeModuleImpl();
        installedThemes.put(mPlugin.getAlias(), mPlugin);

        for (Extension ext : plugins)
        {
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();
            Class<?> cl = classLoader.loadClass(className);
            mPlugin = (ThemePluginIF) cl.newInstance();
            installedThemes.put(mPlugin.getAlias(), mPlugin);
        }

        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_THEMES_PROPERTY,
            installedThemes);
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception
    {
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception
    {
    }
}
