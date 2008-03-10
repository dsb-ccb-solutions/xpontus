/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.constants.XPontusMenuConstantsIF;
import net.sf.xpontus.plugins.menubar.MenuBarPluginIF;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.Action;


/**
 *
 * @author mrcheeks
 */
public class BrowserMenuBarPluginImpl implements MenuBarPluginIF {
    private List<String> menuNamesList;
    private Map<String, List<Action>> actionsMap;
    private List<Action> actionsList;

    public List<String> getMenuNames() {
        if (menuNamesList == null) {
            menuNamesList = new Vector<String>();
            menuNamesList.add(XPontusMenuConstantsIF.PLUGINS_MENU_ID);
        }

        return menuNamesList;
    }

    public Map<String, List<Action>> getActionMap() {
        if (actionsMap == null) {
            actionsMap = new HashMap<String, List<Action>>();
            actionsList = new Vector<Action>();
            actionsList.add(new XPontusBrowserPluginAction());
            actionsMap.put(getMenuNames().get(0), actionsList);
        }

        return actionsMap;
    }
}
