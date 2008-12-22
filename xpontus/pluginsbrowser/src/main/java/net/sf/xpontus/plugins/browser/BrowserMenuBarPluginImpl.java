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

import net.sf.xpontus.constants.XPontusMenuConstantsIF;
import net.sf.xpontus.plugins.menubar.MenuBarPluginIF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Action;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class BrowserMenuBarPluginImpl implements MenuBarPluginIF {
    private List<String> menuNamesList;
    private Map<String, List<Action>> actionsMap;
    private List<Action> actionsList;

    public List<String> getMenuNames() {
        if (menuNamesList == null) {
            menuNamesList = new ArrayList<String>();
            menuNamesList.add(XPontusMenuConstantsIF.PLUGINS_MENU_ID);
        }

        return menuNamesList;
    }

    public Map<String, List<Action>> getActionMap() {
        if (actionsMap == null) {
            actionsMap = new HashMap<String, List<Action>>();
            actionsList = new ArrayList<Action>();
            actionsList.add(new XPontusBrowserPluginAction());
            actionsMap.put(getMenuNames().get(0), actionsList);
        }

        return actionsMap;
    }
}
