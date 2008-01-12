/*
 * ToolBarModule.java
 *
 * Created on 26 avril 2007, 11:26
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
package net.sf.xpontus.plugins.toolbar;

import com.vlsolutions.swing.toolbars.ToolBarConstraints;
import com.vlsolutions.swing.toolbars.ToolBarContainer;
import com.vlsolutions.swing.toolbars.ToolBarPanel;
import com.vlsolutions.swing.toolbars.VLToolBar;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.XPontusPlugin;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.awt.BorderLayout;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.Action;
import javax.swing.JButton;


/**
 * The toolbar plugin
 * @author Yves Zoundi
 */
public class ToolBarPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "toolbarpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.toolbar";
    public static final int MAX_TOOLBAR_PER_LINE = 2;
    private int x_pos = 0;
    private int y_pos = 0;

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }

    /**
     * Try to retrieve the toolbar by its name
     * @param name The name of the toolbar
     * @return The toolbar found or <code>null</code>
     */
    protected VLToolBar lookupToolBar(String name) {
        ToolBarContainer toolbar = DefaultXPontusWindowImpl.getInstance()
                                                           .getToolBar();

        List tbs = toolbar.getRegisteredToolBars();

        for (int j = 0; j < tbs.size(); j++) {
            VLToolBar tb = (VLToolBar) tbs.get(j);

            if (tb.getName().equals(name)) {
                return tb;
            }
        }

        return null;
    }

    /**
     * Return the toolbar if it exists or create a new one
     * @param name The name of the toolbar
     * @return The toolbar
     */
    public VLToolBar getOrCreateToolBar(String name) {
        ToolBarContainer toolbar = DefaultXPontusWindowImpl.getInstance()
                                                           .getToolBar();

        Object m_obj = lookupToolBar(name);

        VLToolBar tb = null;

        if (m_obj == null) {
            tb = new VLToolBar(name);

            tb.setRolloverBorderPainted(true);
            tb.setRolloverContentAreaFilled(true);
            toolbar.registerToolBar(tb);

            ToolBarPanel tbPanel = toolbar.getToolBarPanelAt(BorderLayout.NORTH);

            if (y_pos == MAX_TOOLBAR_PER_LINE) {
                resetToolBarMarkers();
            }

            ToolBarConstraints tbc = new ToolBarConstraints(x_pos, y_pos);
            tbPanel.add(tb, tbc);

            incrementPositions();
        } else {
            tb = (VLToolBar) m_obj;
        }

        return tb;
    }

    /**
     * Configure an extension
     * @param mPlugin  The extension to configure
     */
    public void initExtension(ToolBarPluginIF mPlugin) {
        String[] tbNames = mPlugin.getToolBarNames();
        Map actions = mPlugin.getActions();

        for (int i = 0; i < tbNames.length; i++) {
            VLToolBar tb = getOrCreateToolBar(tbNames[i]);
            List tbActions = (List) actions.get(tbNames[i]);

            for (int j = 0; j < tbActions.size(); j++) {
                JButton m_button = new JButton((Action) tbActions.get(j));
//                m_button.setText(null);
                tb.add(m_button);
            }
        }
    }

    public void init() throws Exception {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint toolbarPluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                              .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = toolbarPluginExtPoint.getConnectedExtensions();

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();
            Class cl = classLoader.loadClass(className);
            ToolBarPluginIF mPlugin = (ToolBarPluginIF) cl.newInstance();
            initExtension(mPlugin);
        }
        
       
    }

    private void incrementPositions() {
        y_pos++;
    }

    private void resetToolBarMarkers() {
        x_pos++;
        y_pos = 0;
    }
}
