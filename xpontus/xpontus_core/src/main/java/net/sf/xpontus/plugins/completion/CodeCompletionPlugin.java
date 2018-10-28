/*
 * CodeCompletionPlugin.java
 *
 * Created on 2007-08-08, 14:06:41
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
package net.sf.xpontus.plugins.completion;

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
 * Plugin for code completion
 * @version 0.0.2
 * @author Yves Zoundi
 */
public class CodeCompletionPlugin extends XPontusPlugin
{
    public static final String EXTENSION_POINT_NAME = "completionpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.completion";
    public static final String PLUGIN_CATEGORY = "Completion";
    private Hashtable<String, Hashtable<String, Object>> engines;

    /**
     *
     */
    public CodeCompletionPlugin()
    {
    }

    /**
     * @param plugin
     */
    public void initExtension(CodeCompletionIF plugin)
    {
        String mimeType = plugin.getMimeType();
        String m_className = plugin.getClass().getName();

        Hashtable<String, Object> m_map = new Hashtable<String, Object>();

        m_map.put(XPontusConstantsIF.CONTENT_TYPE, mimeType);
        m_map.put(XPontusConstantsIF.OBJECT_CLASSNAME, m_className);
        m_map.put(XPontusConstantsIF.CLASS_LOADER,
            plugin.getClass().getClassLoader());

        engines.put(mimeType, m_map);
    }

    /* (non-Javadoc)
     * @see net.sf.xpontus.plugins.XPontusPlugin#init()
     */
    public void init() throws Exception
    {
        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_COMPLETION_ENGINES,
            engines);

        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint completionExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                           .getId(),
                EXTENSION_POINT_NAME);

        Collection<Extension> plugins = completionExtPoint.getConnectedExtensions();

        for (Extension ext : plugins)
        {
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();

            Class<?> cl = classLoader.loadClass(className);
            CodeCompletionIF completer = (CodeCompletionIF) cl.newInstance();
            initExtension(completer);
        }
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception
    {
        engines = new Hashtable<String, Hashtable<String, Object>>();
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception
    {
        engines.clear();
    }
}
