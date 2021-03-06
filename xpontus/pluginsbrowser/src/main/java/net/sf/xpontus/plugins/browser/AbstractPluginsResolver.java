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

import net.sf.xpontus.plugins.SimplePluginDescriptor;

import java.util.Map;


/**
 * Plugins resolver
 * @version 0.0.1
 * @author Yves Zoundi
 */
public abstract class AbstractPluginsResolver {
    /**
     *
     * @return The plugins map(id, model descriptor)
     */
    public abstract Map<String, SimplePluginDescriptor> getPluginDescriptorsMap();

    /**
     * load the plugins map
     */
    public abstract void resolvePlugins();

    /**
     * Reload the plugins map
     */
    public abstract void reload();
}
