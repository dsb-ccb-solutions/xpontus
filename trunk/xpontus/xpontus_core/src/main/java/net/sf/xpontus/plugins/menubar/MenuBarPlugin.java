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

import net.sf.xpontus.configuration.XPontusConfig;
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
import javax.swing.JMenuItem;


/**
 * Plugin for the menubar entries
 * @author Yves Zoundi
 */
public class MenuBarPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "menubarpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.menubar";
    public static final String PLUGIN_CATEGORY = "Menubar";
    private boolean newmenu = false;
    private boolean textOnly = false;
    private Map<String, Object> menuMap;

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception {
        menuMap = new HashMap<String, Object>();

        String confValue = XPontusConfig.getValue("xpontus.MenuBarLookAndFeel")
                                        .toString();

        if (confValue.equals("Text only")) {
            textOnly = true;
        }
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception {
        menuMap.clear();
    }

    /**
     * Get a menu by key
     * @param menu_key  The menu key
     * @return A menu
     */
    public JMenu getOrCreateMenu(String menu_key) {
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

        Map<String, List<Action>> map = ext.getActionMap();

        Iterator<String> m_iterator = map.keySet().iterator();

        for (int pos = 0; m_iterator.hasNext(); pos++) {
            String m_MenuKey = m_iterator.next();
            menu = getOrCreateMenu(m_MenuKey);

            if (newmenu) {
                menu.setText(ext.getMenuNames().get(pos).toString());
            }

            List<Action> actions = map.get(m_MenuKey);

            for (int j = 0; j < actions.size(); j++) {
                JMenuItem m_item = menu.add(actions.get(j));

                if (textOnly) {
                    m_item.setIcon(null);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see net.sf.xpontus.plugins.XPontusPlugin#init()
     */
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
