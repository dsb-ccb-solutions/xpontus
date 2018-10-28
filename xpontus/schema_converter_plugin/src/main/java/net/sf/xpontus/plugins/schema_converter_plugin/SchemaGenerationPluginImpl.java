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

import net.sf.xpontus.constants.XPontusMenuConstantsIF;
import net.sf.xpontus.constants.XPontusToolbarConstantsIF;
import net.sf.xpontus.plugins.actions.ActionPluginIF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Action;


/**
 *
 * @author Yves Zoundi
 */
public class SchemaGenerationPluginImpl implements ActionPluginIF {
    private List menunames = new ArrayList();
    private Map actionMap = new HashMap();
    private Map tbMap = new HashMap(1);

    public SchemaGenerationPluginImpl() {
        menunames.add(XPontusMenuConstantsIF.TOOLS_MENU_ID);

        List actions = new ArrayList();

        Action m_action = new SchemaGenerationAction();
        actions.add(m_action);

        actionMap.put(XPontusMenuConstantsIF.TOOLS_MENU_ID, actions);

        List tbActions = new Vector(1);
        tbActions.add(m_action);
        tbMap.put(XPontusToolbarConstantsIF.TB_TOOLS, tbActions);
    }

    public List getMenuNames() {
        return menunames;
    }

    public Map getActionMap() {
        return actionMap;
    }

    public String[] getToolBarNames() {
        return new String[] { XPontusToolbarConstantsIF.TB_TOOLS };
    }

    public Map getActions() {
        return tbMap;
    }
}
