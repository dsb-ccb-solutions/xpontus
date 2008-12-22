/*
 *
 *
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
package net.sf.xpontus.plugins.gendoc;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.plugins.XPontusPlugin;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DocumentationPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "gendocpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.gendoc";
    public static final String PLUGIN_CATEGORY = "Documentation";
    private Map<String, Object> engines;

    public DocumentationPlugin() {
    }

    /**
     *
     *
     * @param m_plugin
     * @param loader
     */
    private void addEngine(IDocumentationPluginIF m_plugin, ClassLoader loader) {
        Hashtable<String, Object> t = new Hashtable<String, Object>();

        t.put(XPontusConstantsIF.CLASS_LOADER, loader);
        t.put(XPontusConstantsIF.OBJECT_CLASSNAME, m_plugin.getClass().getName());

        engines.put(m_plugin.getType(), t);
    }

    public void init() throws Exception {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint themePluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                            .getId(),
                EXTENSION_POINT_NAME);

        Collection<Extension> plugins = themePluginExtPoint.getConnectedExtensions();

        for (Extension ext : plugins) {
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();
            Class<?> cl = classLoader.loadClass(className);
            IDocumentationPluginIF mPlugin = (IDocumentationPluginIF) cl.newInstance();
            addEngine(mPlugin, classLoader);
        }

        DocConfiguration.getInstane().setEngines(engines);
    }

    protected void doStart() throws Exception {
    	 engines = new HashMap<String, Object>();
    }

    protected void doStop() throws Exception {
    	engines.clear();
    }
}
