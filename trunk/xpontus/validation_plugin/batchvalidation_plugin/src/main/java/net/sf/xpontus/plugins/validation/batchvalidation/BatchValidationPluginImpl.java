/*
 * BatchValidationPluginImpl.java
 *
 * Created on 2007-07-26, 14:12:27
 *
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
package net.sf.xpontus.plugins.validation.batchvalidation;

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
public class BatchValidationPluginImpl implements ActionPluginIF {
    private List menunames = new ArrayList();
    private Map actionMap = new HashMap();
    private Map tbMap = new HashMap(1);

    public BatchValidationPluginImpl() {
        menunames.add(XPontusMenuConstantsIF.TOOLS_MENU_ID);

        Action m_action = new BatchValidationAction();

        List actions = new ArrayList();
        actions.add(m_action);
        actionMap.put(XPontusMenuConstantsIF.TOOLS_MENU_ID, actions);

        List tbActions = new Vector(1);
        tbActions.add(m_action);
        tbMap.put(XPontusToolbarConstantsIF.TB_VALIDATION, tbActions);
    }

    public List getMenuNames() {
        return menunames;
    }

    public Map getActionMap() {
        return actionMap;
    }

    public String[] getToolBarNames() {
        return new String[] { XPontusToolbarConstantsIF.TB_VALIDATION };
    }

    public Map getActions() {
        return tbMap;
    }
}
