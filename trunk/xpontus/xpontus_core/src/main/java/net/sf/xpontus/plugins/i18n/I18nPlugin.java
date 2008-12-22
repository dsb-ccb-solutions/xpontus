/*
 * I18nModule.java
 *
 * Created on 26 avril 2007, 11:15
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
package net.sf.xpontus.plugins.i18n;

import net.sf.xpontus.plugins.XPontusPlugin;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.util.Collection;


/**
 * The i18n plugin
 * @author Yves Zoundi
 */
public class I18nPlugin extends XPontusPlugin
{
    public static final String EXTENSION_POINT_NAME = "i18npluginif";
    public static final String PLUGIN_IDENTIFIER = "plugins.core.i18n";
    public static final String PLUGIN_CATEGORY = "I18n";

    /** Creates a new instance of I18nModule */
    public I18nPlugin()
    {
    }

    protected void doStart() throws Exception
    {
    }

    protected void doStop() throws Exception
    {
    }

    public void init() throws Exception
    {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint i18nPluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                           .getId(),
                EXTENSION_POINT_NAME);

        Collection<Extension> plugins = i18nPluginExtPoint.getConnectedExtensions();

        for (Extension ext : plugins)
        {
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();
            Class<?> cl = classLoader.loadClass(className);
            I18nPluginIF mPlugin = (I18nPluginIF) cl.newInstance();
        }
    }
}
