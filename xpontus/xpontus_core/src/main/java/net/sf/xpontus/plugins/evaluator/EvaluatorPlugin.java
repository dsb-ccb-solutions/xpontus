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
package net.sf.xpontus.plugins.evaluator;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.plugins.XPontusPlugin;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class EvaluatorPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "evaluatorpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.evaluator";
    public static final String PLUGIN_CATEGORY = "Evaluator";
    private Map engines = new HashMap();

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }

    private void addEngine(EvaluatorPluginIF m_plugin, ClassLoader loader) {
        Hashtable t = new Hashtable();

        t.put(XPontusConstantsIF.CLASS_LOADER, loader);
        t.put(XPontusConstantsIF.OBJECT_CLASSNAME, m_plugin.getClass().getName());

        engines.put(m_plugin.getName(), t);
    }

    public void init() throws Exception {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint scenarioPluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                               .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = scenarioPluginExtPoint.getConnectedExtensions();

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            String className = ext.getParameter("class").valueAsString();

            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);

            Class cl = classLoader.loadClass(className);
            EvaluatorPluginIF m_plugin = (EvaluatorPluginIF) cl.newInstance();
            addEngine(m_plugin, classLoader);
        }

        EvaluatorPluginConfiguration.getInstance().setEngines(engines);
    }
}
