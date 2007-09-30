/*
 * DefaultIconPluginImpl.java
 *
 * Created on 25-Jul-2007, 6:33:54 PM
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
package net.sf.xpontus.plugins.icons;

import java.net.URL;

import javax.swing.ImageIcon;


/**
 * Default icon set implementation
 * @author Yves Zoundi
 */
public class DefaultIconPluginImpl implements IconPluginIF {
    public DefaultIconPluginImpl() {
    }

    public String getIconNameAlias() {
        return "Default";
    }

    public ImageIcon getIcon(String path) {
        URL url = getClass().getResource(path);
        ImageIcon icon = new ImageIcon(url);

        return icon;
    }
}
