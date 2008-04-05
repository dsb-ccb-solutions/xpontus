/*
 * ActionPlugin.java
 *
 * Created on Jul 15, 2007, 11:31:01 AM
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
package net.sf.xpontus.plugins.actions;

import net.sf.xpontus.plugins.XPontusPlugin;
import net.sf.xpontus.plugins.menubar.MenuBarPlugin;
import net.sf.xpontus.plugins.popupcontext.PopupContextPlugin;
import net.sf.xpontus.plugins.toolbar.ToolBarPlugin;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.util.Collection;
import java.util.Iterator;


/**
 * Plugin to plug actions to the menubar, the toolbar or the editor popup menu
 * @author Yves Zoundi
 */
public class ActionPlugin extends XPontusPlugin {
    /**
     * The plugin id
     */
    public static final String PLUGIN_IDENTIFIER = "plugin.core.actions";
    public static final String PLUGIN_CATEGORY = "Actions";

    /**
     * The extension point of this plugin
     */
    public static final String EXTENSION_POINT_NAME = "actionpluginif";

    public ActionPlugin() {
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception {
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception {
    }

    /**
     *
     * @param plugin
     */
    public void initExtension(ActionPluginIF plugin) {
        try {
            ToolBarPlugin toolbarPlugin = (ToolBarPlugin) getManager()
                                                              .getPlugin(ToolBarPlugin.PLUGIN_IDENTIFIER);
            MenuBarPlugin menubarPlugin = (MenuBarPlugin) getManager()
                                                              .getPlugin(MenuBarPlugin.PLUGIN_IDENTIFIER);
            PopupContextPlugin popupPlugin = (PopupContextPlugin) getManager()
                                                                      .getPlugin(PopupContextPlugin.PLUGIN_IDENTIFIER);

            menubarPlugin.initExtension(plugin);
            popupPlugin.initExtension(plugin);

            toolbarPlugin.initExtension(plugin);
        } catch (Exception e) {
            log.fatal(e.getMessage());
        }
    }

    /**
    * Initialize the actions plugin
    * @throws java.lang.Exception
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
            initExtension((ActionPluginIF) cl.newInstance());
        }
    }
}
