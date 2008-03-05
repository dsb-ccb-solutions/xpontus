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
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
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
        Object[] descriptors = XPontusPluginManager.getPluginManager()
                                                   .getRegistry()
                                                   .getPluginDescriptors()
                                                   .toArray();

        for (int i = 0; i < descriptors.length; i++) {
            PluginDescriptor pds = (PluginDescriptor) descriptors[i];
            System.out.println("PLUGIN_LOCATION:" +
                pds.getLocation().toExternalForm());

            String id = pds.getId().toString();
            String category = pds.getAttribute("Category").getValue();
            String homepage = pds.getAttribute("Homepage").getValue();
            String builtin = pds.getAttribute("Built-in").getValue();
            String displayname = pds.getAttribute("DisplayName").getValue();
            String description = pds.getAttribute("Description").getValue();
            String license = pds.getAttribute("License").getValue();
            String date = pds.getAttribute("date").getValue();
            String version = pds.getVersion().toString();
            SimplePluginDescriptor spd = new SimplePluginDescriptor();

            String vendor = "Yves Zoundi";

            spd.setAuthor(vendor);
            spd.setDate(date);
            spd.setBuiltin(builtin);
            spd.setCategory(category);
            spd.setDescription(description);
            spd.setDisplayname(displayname);
            spd.setHomepage(homepage);
            spd.setId(id);
            spd.setLicense(license);
            spd.setVersion(version);

            if (pds.getVendor() != null) {
                vendor = pds.getVendor();
            }

            if (spd.getAuthor() == null) {
                spd.setAuthor("Yves Zoundi");
            }

            if (spd.getLicense() == null) {
                spd.setLicense("UNKNOWN");
            }

            if (!spd.getBuiltin().equals("true")) {
                pluginsMap.put(id, spd);
            }
        }

        PluginsUtils.getInstance().initInstalledPluginsIndex();
        PluginsUtils.getInstance().shouldAddToIndex(pluginsMap);
    }
}
