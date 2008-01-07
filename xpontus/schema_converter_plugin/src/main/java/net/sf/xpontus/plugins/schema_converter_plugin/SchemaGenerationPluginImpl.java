
/*
 *  
 *  
 * 
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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

package net.sf.xpontus.plugins.schema_converter_plugin;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.xpontus.constants.XPontusMenuConstantsIF; 
import net.sf.xpontus.plugins.menubar.MenuBarPluginIF;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SchemaGenerationPluginImpl implements MenuBarPluginIF {
    private List menunames = new ArrayList();
    private Map actionMap = new HashMap();

    public SchemaGenerationPluginImpl() {
        menunames.add(XPontusMenuConstantsIF.TOOLS_MENU_ID);

        List actions = new ArrayList();
        actions.add(new SchemaGenerationAction());
        actionMap.put(XPontusMenuConstantsIF.TOOLS_MENU_ID, actions);
    }

    public List getMenuNames() {
        return menunames;
    }

    public Map getActionMap() {
        return actionMap;
    }
}
