/*
 * MenuBarModule.java
 *
 * Created on 26 avril 2007, 11:25
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.plugins.menubar;

import net.sf.xpontus.constants.XPontusMenuConstantsIF;
import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
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
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JMenu;
import javax.swing.JMenuBar;


/**
 * Plugin for the menubar entries
 * @author Yves Zoundi
 */
public class MenuBarPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "menubarpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.menubar";
    boolean newmenu = false;
    private Map menuMap = new HashMap();

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }

    /**
     * Get a menu by key
     * @param menu_key  The menu key
     * @return A menu
     */
    private JMenu getOrCreateMenu(String menu_key) {
        newmenu = false;

        JMenu menu = null;

        if (!menuMap.containsKey(menu_key)) {
            menu = new JMenu();

            JMenuBar menubar = DefaultXPontusWindowImpl.getInstance()
                                                       .getMenuBar();
            menubar.add(menu);
            newmenu = true;
            menuMap.put(menu_key, menu);
        } else {
            menu = (JMenu) menuMap.get(menu_key);
        }

        return menu;
    }

    /**
     *
     * @param ext
     */
    public void initExtension(MenuBarPluginIF ext) {
        JMenu menu = null;

        Map map = ext.getActionMap();

        Iterator m_iterator = map.keySet().iterator();

        for (int pos = 0; m_iterator.hasNext(); pos++) {
            String m_MenuKey = m_iterator.next().toString();
            menu = getOrCreateMenu(m_MenuKey);

            if (newmenu) {
                menu.setText(ext.getMenuNames().get(pos).toString());
            }

            List actions = (List) map.get(m_MenuKey);

            for (int j = 0; j < actions.size(); j++) {
                menu.add((Action) actions.get(j));
            }
        }
    }

    public void init() throws Exception {
        JMenu menu = null;

        for (int i = 0; i < XPontusMenuConstantsIF.MENU_IDS.length; i++) {
            menu = getOrCreateMenu(XPontusMenuConstantsIF.MENU_IDS[i]);
            menu.setText(XPontusMenuConstantsIF.MENU_TITLES[i]);
        }

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
            MenuBarPluginIF mPlugin = (MenuBarPluginIF) cl.newInstance();

            initExtension(mPlugin);
        }

        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_MENUS_PROPERTY,
            menuMap);
    }
}
