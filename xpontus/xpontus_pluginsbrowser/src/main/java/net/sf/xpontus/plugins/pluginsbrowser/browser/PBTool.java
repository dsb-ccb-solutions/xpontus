// Placed in public domain by Dmitry Olshansky, 2006
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import net.sf.xpontus.constants.XPontusMenuConstantsIF; 

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import net.sf.xpontus.plugins.menubar.MenuBarPluginIF;


/**
 *
 * @version $Id: PBTool.java,v 1.2 2006/02/23 16:44:05 ddimon Exp $
 */
public class PBTool implements MenuBarPluginIF
{
    private List menusList;
    private Map actionsMap;

    public PBTool()
    {
        menusList = new ArrayList();
        actionsMap = new HashMap();

        menusList.add(XPontusMenuConstantsIF.HELP_MENU_ID);
    }

    public List getMenuNames()
    {
        return menusList;
    }

    public Map getActionMap()
    {
        if (actionsMap.size() == 0)
        {
            List li = new Vector();
            li.add(new PluginBrowserAction());
            actionsMap.put(XPontusMenuConstantsIF.HELP_MENU_ID, li);
        }

        return actionsMap;
    }
}
