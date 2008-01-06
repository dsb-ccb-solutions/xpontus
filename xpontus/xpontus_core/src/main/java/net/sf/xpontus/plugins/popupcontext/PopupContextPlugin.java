/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.popupcontext;

import net.sf.xpontus.plugins.XPontusPlugin;


/**
 *
 * @author Propriétaire
 */
public class PopupContextPlugin extends XPontusPlugin {
    /**
    * The plugin id
    */
    public static final String PLUGIN_IDENTIFIER = "plugin.core.popupcontext";

    /**
     * The extension point of this plugin
     */
    public static final String EXTENSION_POINT_NAME = "popupcontextpluginif";

    public void init() throws Exception {
    }

    public void initExtension(PopupContextPluginImpl plugin) {
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }
}
