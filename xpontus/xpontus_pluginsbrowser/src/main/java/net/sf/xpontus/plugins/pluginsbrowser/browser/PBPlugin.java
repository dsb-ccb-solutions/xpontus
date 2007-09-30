// Placed in public domain by Dmitry Olshansky, 2006
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import org.java.plugin.Plugin;
import org.java.plugin.registry.PluginRegistry;
/**
 * @version $Id: PBPlugin.java,v 1.7 2006/02/23 16:44:05 ddimon Exp $
 */
public final class PBPlugin extends Plugin {
    /**
     * This plug-in ID.
     */
    public static final String PLUGIN_ID = "plugin.browser";
    
    public static PluginRegistry r = null;
 
    
    /**
     * @see org.java.plugin.Plugin#doStart()
     */
    protected void doStart() throws Exception { 
r = getManager().getRegistry();
    }

    /**
     * @see org.java.plugin.Plugin#doStop()
     */
    protected void doStop() throws Exception { 
    }
     
}
