/*
 * QuickToolBarPluginIF.java
 *
 * Created on 21-Aug-2007, 6:49:55 PM
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
package net.sf.xpontus.plugins.quicktoolbar;

import java.awt.Component;


/**
 * Plugin interface for quick toolbar functions
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public interface QuickToolBarPluginIF {
    /**
     * The name of the module
     * @return The name of the module
     */
    String getName();

    /**
     *
     * @return The description of the module
     */
    String getDescription();

    /**
     * The supported content type
     * @return The supported content type
     */
    String getMimeType();

    /**
     *
     * @return
     */
    Component getComponent();

    /**
     * The file context mode (ant, docbook, etc.)
     * @return The file context mode (ant, docbook, etc.)
     */
    String getFileContextMode();
}
