/*
 * XPontusPlugin.java
 *
 * Created on 19-Jul-2007, 5:10:23 PM
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
 */
package net.sf.xpontus.plugins;

import org.java.plugin.Plugin;


/**
 * XPontus plugins have all a  method called init
 * @author Yves Zoundi
 */
public abstract class XPontusPlugin extends Plugin {
    /**
     * Default constructor nothing to do for now
     */
    public XPontusPlugin() {
    }

    /**
     * <p>Initialize the plugin. This method exists because the main
     * window must be created before anything happens</p>
     *
     * @throws java.lang.Exception An exception
     */
    public abstract void init() throws Exception;
}
