/*
 * ScenarioPlugin.java
 *
 * Created on 19-Aug-2007, 9:23:52 AM
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
package net.sf.xpontus.plugins.scenarios;

import java.util.Collection;
import net.sf.xpontus.plugins.XPontusPlugin;

import java.util.Hashtable;
import java.util.Iterator;
import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;


/**
 * User transformations plugin
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class ScenarioPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "scenariopluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.scenarios"; 

    public ScenarioPlugin() {
    }

    public void init() throws Exception {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint scenarioPluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                              .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = scenarioPluginExtPoint.getConnectedExtensions();

        ScenarioPluginsConfiguration.getInstance().addEngine(new DefaultScenarioPluginImpl());
        
        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();
            Class cl = classLoader.loadClass(className);
            ScenarioPluginIF m_plugin = (ScenarioPluginIF) cl.newInstance(); 
            ScenarioPluginsConfiguration.getInstance().addEngine(m_plugin);
        }
        
       
    }

    protected void doStart() throws Exception {
         
    }

    protected void doStop() throws Exception {
         
    }
 
}
