package net.sf.xpontus.core.controller.handlers;

import net.sf.xpontus.constants.XPontusConstants;

import java.io.File;


//import org.java.plugin.boot.Boot;
public class XPontusPluginLoader
  {
    public void boot()
      {
        File pluginDir = new File(XPontusConstants.CONF_DIR, "plugins");

        if (!pluginDir.exists())
          {
            pluginDir.mkdirs();
          }

        File pluginFile = new File(XPontusConstants.CONF_DIR, "boot.properties");

        if (!pluginFile.exists())
          {
            throw new RuntimeException("plugin configuration file not found!");
          }

        System.setProperty("applicationRoot",
            XPontusConstants.CONF_DIR.getAbsolutePath());
        System.setProperty("org.java.plugin.boot.pluginsRepositories",
            XPontusConstants.CONF_DIR.getAbsolutePath());

        //		System.setProperty(Boot.PROP_BOOT_CONFIG, pluginFile.getAbsolutePath());	
        //		
        //		Boot.main(null);
      }
  }
