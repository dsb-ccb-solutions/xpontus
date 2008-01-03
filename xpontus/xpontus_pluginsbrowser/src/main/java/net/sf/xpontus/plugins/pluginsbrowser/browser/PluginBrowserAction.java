/*
 * PluginBrowserAction.java
 *
 * Created on 19 janvier 2007, 10:17
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import net.sf.xpontus.actions.impl.XPontusThreadedActionImpl;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;

import java.awt.Frame;

import java.net.URL;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JDialog;


/**
 *
 * @author Yves Zoundi
 */
public class PluginBrowserAction extends XPontusThreadedActionImpl
{
    private JDialog dialog;

    /** Creates a new instance of PluginBrowserAction */
    public PluginBrowserAction()
    {
        setName("Plugin Browser");

        URL url = getClass()
                      .getResource("/net/sf/xpontus/plugins/pluginsbrowser/browser/icons/plugin.png");
        ImageIcon icon = new ImageIcon(url);
        this.putValue(Action.SMALL_ICON, icon);
    }

    public void run()
    {
        if (dialog == null)
        {
            dialog = new PBView();
        }

        dialog.setLocationRelativeTo(DefaultXPontusWindowImpl.getInstance()
                                                             .getDisplayComponent());
        dialog.setVisible(true);
    }
}
