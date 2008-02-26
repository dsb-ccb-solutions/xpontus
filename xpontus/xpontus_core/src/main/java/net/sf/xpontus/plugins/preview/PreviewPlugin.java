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
package net.sf.xpontus.plugins.preview;

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
import java.util.Iterator;
import java.util.Map;


/**
 * Plugin for transformation results preview
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PreviewPlugin extends XPontusPlugin {
    /**
     *
     */
    public static final String EXTENSION_POINT_NAME = "previewpluginif";
    public static final String PLUGIN_CATEGORY = "Preview";

    /**
     *
     */
    public static final String PLUGIN_IDENTIFIER = "plugin.core.preview";
    private Map previewers;

    public void init() throws Exception {
        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_PREVIEW_PROPERTY,
            previewers);

        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint outlineExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                        .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = outlineExtPoint.getConnectedExtensions();

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();

            Class cl = classLoader.loadClass(className);
            PreviewPluginIF m_preview = (PreviewPluginIF) cl.newInstance();
            initExtension(m_preview);
        }
    }

    protected void doStart() throws Exception {
        previewers = new HashMap();
    }

    protected void doStop() throws Exception {
        previewers.clear();
    }

    private void initExtension(PreviewPluginIF m_preview) {
        m_preview = null;
    }
}
