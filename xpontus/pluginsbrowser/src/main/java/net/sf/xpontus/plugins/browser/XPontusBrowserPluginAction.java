/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.actions.impl.XPontusDialogActionImpl;

/**
 *
 * @author mrcheeks
 */
public class XPontusBrowserPluginAction extends XPontusDialogActionImpl{

    public XPontusBrowserPluginAction() {
        setName("Plugins manager");
        setDescription("Plugins manager");
        setWindowClassLoader(XPontusBrowserPlugin.class.getClassLoader());
        setDialogClassName(PluginBrowser.class.getName());
    }

    
}
