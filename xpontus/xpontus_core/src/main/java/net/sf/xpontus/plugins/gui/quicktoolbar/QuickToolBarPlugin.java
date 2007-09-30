/*
 * QuickToolBarPlugin.java
 * 
 * Created on 21-Aug-2007, 6:49:36 PM
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

package net.sf.xpontus.plugins.gui.quicktoolbar;

import java.util.HashMap;
import java.util.Map;
import net.sf.xpontus.plugins.XPontusPlugin;

/**
 * Quick Toolbar plugin
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class QuickToolBarPlugin extends XPontusPlugin{

    public static final String EXTENSION_POINT_NAME = "quicktoolbarpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.quicktoolbar";
    private Map quicktoolbars;
    
    public QuickToolBarPlugin() {
    }

    public void init() throws Exception {
        
    }

    protected void doStart() throws Exception {
        quicktoolbars = new HashMap();
         
    }

    protected void doStop() throws Exception {
         quicktoolbars.clear();
    }

}
