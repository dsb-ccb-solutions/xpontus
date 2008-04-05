/*
 * QuickToolBarPlugin.java
 *
 * Created on 21-Aug-2007, 6:49:36 PM
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
package net.sf.xpontus.plugins.quicktoolbar;

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
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


/**
 * Quick Toolbar plugin
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class QuickToolBarPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "quicktoolbarpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.quicktoolbar";
    private Map<String, Object> quicktoolbars;

    public QuickToolBarPlugin() {
    }

    /* (non-Javadoc)
     * @see net.sf.xpontus.plugins.XPontusPlugin#init()
     */
    public void init() throws Exception {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint iocPluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                          .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = iocPluginExtPoint.getConnectedExtensions();

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();

            Class cl = classLoader.loadClass(className);
            QuickToolBarPluginIF mPlugin = (QuickToolBarPluginIF) cl.newInstance();

            initExtension(mPlugin);
        }

        initExtension(new DefaultQuickToolbarPluginImpl());
        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_QUICKTOOLBAR_PROPERTY,
            quicktoolbars);
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception {
        quicktoolbars = new HashMap<String, Object>();
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception {
        quicktoolbars.clear();
    }

    private void initExtension(QuickToolBarPluginIF mPlugin) {
        String mm = mPlugin.getMimeType();

        Hashtable<String, Object> t = new Hashtable<String, Object>();
        t.put(XPontusConstantsIF.CLASS_LOADER,
            mPlugin.getClass().getClassLoader());
        t.put(XPontusConstantsIF.OBJECT_CLASSNAME, mPlugin.getClass().getName());
        quicktoolbars.put(mm, t);
    }
}
