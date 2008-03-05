/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.actions.impl.XPontusDialogActionImpl;

import java.net.URL;

import javax.swing.Action;
import javax.swing.ImageIcon;


/**
 *
 * @author mrcheeks
 */
public class XPontusBrowserPluginAction extends XPontusDialogActionImpl {
    public XPontusBrowserPluginAction() {
        setName("Plugins manager");
        setDescription("Plugins manager");
        setWindowClassLoader(XPontusBrowserPlugin.class.getClassLoader());
        setDialogClassName(PluginBrowser.class.getName());

        URL sURL = getClass().getResource("plugin.png");
        ImageIcon sIcon = new ImageIcon(sURL);
        putValue(Action.SMALL_ICON, sIcon);
    }
}
