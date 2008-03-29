/*
 * ConsolePlugin.java
 *
 * Created on 20-Jul-2007, 2:32:15 PM
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
package net.sf.xpontus.plugins.console;

import net.sf.xpontus.plugins.XPontusPlugin;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.awt.Component;

import java.util.Collection;
import java.util.Iterator;


/**
 *
 * @author Yves Zoundi
 */
public class ConsolePlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "consolepluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.console";
    public static final String PLUGIN_CATEGORY = "Console";

    public ConsolePlugin() {
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }

    public void init() throws Exception {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint consoleExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                        .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = consoleExtPoint.getConnectedExtensions();

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);

            String className = ext.getParameter("class").valueAsString();

            Class cl = classLoader.loadClass(className);
            ConsolePluginIF mConsolePlugin = (ConsolePluginIF) cl.newInstance();
            initExtension(mConsolePlugin);
        }
    }

    private void initExtension(ConsolePluginIF mConsolePlugin) {
        String m_name = mConsolePlugin.getName();
        Component m_component = mConsolePlugin.getComponent();
    }
}
