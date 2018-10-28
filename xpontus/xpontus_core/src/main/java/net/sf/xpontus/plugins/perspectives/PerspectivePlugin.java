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
package net.sf.xpontus.plugins.perspectives;

import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.plugins.XPontusPlugin;
import net.sf.xpontus.properties.PropertiesHolder;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;


/**
 *
 * @author Yves Zoundi
 */
public class PerspectivePlugin extends XPontusPlugin
{
    /**
     * The extension point interface of this plugin
     */
    public static final String EXTENSION_POINT_NAME = "perspectivepluginif";

    /**
     * The unique identifier of this plugin
     */
    public static final String PLUGIN_IDENTIFIER = "plugin.core.perspectives";

    /**
     * The category of this plugin
     */
    public static final String PLUGIN_CATEGORY = "Viewer";

    // the list of all registered perspectives to render documents
    private List<PerspectivePluginIF> availablePerspectives;

    /* (non-Javadoc)
     * @see net.sf.xpontus.plugins.XPontusPlugin#init()
     */
    public void init() throws Exception
    {
        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_PERSPECTIVES,
            availablePerspectives);

        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint outlineExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                        .getId(),
                EXTENSION_POINT_NAME);

        Collection<Extension> plugins = outlineExtPoint.getConnectedExtensions();

        // register the default perspective
        initExtension(new DefaultPerspectiveImpl());

        for (Extension ext : plugins)
        {
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();

            Class<?> cl = classLoader.loadClass(className);
            PerspectivePluginIF m_perspective = (PerspectivePluginIF) cl.newInstance();
            initExtension(m_perspective);
        }
    }

    // register a perspective
    private void initExtension(PerspectivePluginIF m_perspective)
    {
        availablePerspectives.add(m_perspective);
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception
    {
        // create a new hashtable to store registered perspectives 
        availablePerspectives = new ArrayList<PerspectivePluginIF>();
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception
    {
        // clear the registered perspectives and free resources
        availablePerspectives.clear();
    }
}
