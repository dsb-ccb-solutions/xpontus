/*
 * IconModuleIF.java
 *
 * Created on May 28, 2007, 8:18 PM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
package net.sf.xpontus.plugins.icons;

import javax.swing.ImageIcon;


/**
 * Interface to provide icon themes
 * @author Yves Zoundi
 */
public interface IconPluginIF {
    /**
     * Get the name of the icon set
     * @return The alias of the icon
     */
    public String getIconNameAlias();

    /**
     * Load an icon
     * @param path The path of the icon to load
     * @return An icon
     */
    public ImageIcon getIcon(String path);
}
