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
package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import net.sf.xpontus.plugins.SimplePluginDescriptor;

import org.java.plugin.registry.PluginDescriptor;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author Yves Zoundi
 */
public class InstalledPluginsResolver extends AbstractPluginsResolver {

    private Map<String, SimplePluginDescriptor> pluginsMap = new HashMap<String, SimplePluginDescriptor>();

    public Map<String, SimplePluginDescriptor> getPluginDescriptorsMap() {
        return pluginsMap;
    }

    public void reload() {
        pluginsMap.clear();
        resolvePlugins();
    }

    public void resolvePlugins() {
        Object[] descriptors = XPontusPluginManager.getPluginManager().getRegistry().getPluginDescriptors().toArray();

        for (int i = 0; i < descriptors.length; i++) {
            PluginDescriptor pds = (PluginDescriptor) descriptors[i];

            SimplePluginDescriptor spd = PluginsUtils.toSimplePluginDescriptor(pds);

            if (spd.getAuthor() == null) {
                spd.setAuthor("Yves Zoundi");
            }

            if (spd.getLicense() == null) {
                spd.setLicense("UNKNOWN");
            }

            if (!spd.getBuiltin().equals("true")) {
                pluginsMap.put(spd.getId(), spd);
            }
        }

        PluginsUtils.getInstance().initInstalledPluginsIndex();
        PluginsUtils.getInstance().shouldAddToIndex(pluginsMap);
    }
}
