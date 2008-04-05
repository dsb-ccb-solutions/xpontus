/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.popupcontext;

import net.sf.xpontus.plugins.XPontusPlugin;


/**
 *
 * @author Yves Zoundi
 */
public class PopupContextPlugin extends XPontusPlugin {
    /**
    * The plugin id
    */
    public static final String PLUGIN_IDENTIFIER = "plugin.core.popupcontext";
    /**
     * 
     */
    public static final String PLUGIN_CATEGORY = "Editor";

    /**
     * The extension point of this plugin
     */
    public static final String EXTENSION_POINT_NAME = "popupcontextpluginif";

    /* (non-Javadoc)
     * @see net.sf.xpontus.plugins.XPontusPlugin#init()
     */
    public void init() throws Exception {
    }

    /**
     * @param plugin
     */
    public void initExtension(PopupContextPluginImpl plugin) {
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception {
    }

    /* (non-Javadoc)
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception {
    }
}
