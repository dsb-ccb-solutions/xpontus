/*
 * SimpleValidationPluginImpl.java
 *
 * Created on 2007-07-26, 14:12:27
 *
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.validation.simplexmlvalidation;

import net.sf.xpontus.constants.XPontusMenuConstantsIF;
import net.sf.xpontus.plugins.gui.menubar.MenuBarPluginIF;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import net.sf.xpontus.constants.XPontusConstantsIF;


/**
 *
 * @author Yves Zoundi
 */
public class SimpleValidationPluginImpl implements MenuBarPluginIF
{
    private List menunames = new ArrayList();
    private Map actionMap = new HashMap();

    public SimpleValidationPluginImpl()
    {
        menunames.add(XPontusMenuConstantsIF.TOOLS_MENU_ID);
        List actions = new ArrayList();
        actions.add(new SimpleValidationAction());
        actionMap.put(XPontusMenuConstantsIF.TOOLS_MENU_ID,
            actions);
    }

    public List getMenuNames()
    {
        return menunames;
    }

    public Map getActionMap()
    {
        return actionMap;
    }
}
