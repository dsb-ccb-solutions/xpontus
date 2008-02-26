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

import org.java.plugin.registry.PluginDescriptor;

import java.util.HashMap;
import java.util.Map;
import net.sf.xpontus.controllers.impl.XPontusPluginManager;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class InstalledPluginsResolver extends AbstractPluginsResolver {
    private Map<String, SimplePluginDescriptor> pluginsMap = new HashMap<String, SimplePluginDescriptor>();

    @Override
    public Map<String, SimplePluginDescriptor> getPluginDescriptorsMap() {
        return pluginsMap;
    }

    @Override
    public void resolvePlugins() {
        Object[] descriptors = XPontusPluginManager.getPluginManager().getRegistry().getPluginDescriptors().toArray();

        for (int i = 0; i < descriptors.length; i++) {
            PluginDescriptor pds = (PluginDescriptor) descriptors[i];
            String id = pds.getId().toString();
            String category = pds.getAttribute("Category").getValue().toString();
            String homepage = pds.getAttribute("Homepage").getValue().toString();
            String builtin = pds.getAttribute("Built-in").getValue().toString();
            String displayname = pds.getAttribute("DisplayName").getValue()
                                    .toString();
            String description = pds.getAttribute("Description").getValue()
                                    .toString();
            String version = pds.getVersion().toString();
            SimplePluginDescriptor spd = new SimplePluginDescriptor();

            String vendor = "Yves Zoundi";

            if (pds.getVendor() != null) {
                vendor = pds.getVendor();
            }

            spd.setAuthor(vendor);
            spd.setBuiltin(builtin);
            spd.setCategory(category);
            spd.setDescription(description);
            spd.setDisplayname(displayname);
            spd.setHomepage(homepage);
            spd.setId(id);
            spd.setVersion(version);
            pluginsMap.put(id, spd);
        }
    }
}
