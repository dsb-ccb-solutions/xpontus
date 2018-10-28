/*
 * XPontusPluginManager.java
 *
 * Created on May 26, 2007, 8:11 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi <yveszoundi at users dot sf dot net>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.controllers.impl;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.controllers.XPontusControllerIF;
import net.sf.xpontus.plugins.settings.DefaultSettingsModuleImpl;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.java.plugin.ObjectFactory;
import org.java.plugin.PluginLifecycleException;
import org.java.plugin.PluginManager;
import org.java.plugin.PluginManager.PluginLocation;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;
import org.java.plugin.standard.StandardPluginLocation;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.io.InputStream;

import java.net.MalformedURLException;
import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Locale;


/**
 * XPontus plugin manager
 * @author Yves Zoundi
 * @version 0.0.1
 */
public class XPontusPluginManager implements XPontusControllerIF
{
    private static String PLUGINS = "plugins";
    private static PluginManager manager;
    private static final Log LOG = LogFactory.getLog(XPontusPluginManager.class);

    /**
     * The default constructor
     * @throws java.lang.Exception
     */
    public XPontusPluginManager() throws Exception
    {
        manager = ObjectFactory.newInstance().createManager();
        manager.publishPlugins(getBuiltinPluginLocations());
        manager.publishPlugins(getPluginLocations());
    }

    /**
     * @return
     */
    public static synchronized PluginManager getPluginManager()
    {
        if (manager == null)
        {
            throw new RuntimeException("Plugin manager not registered yet!");
        }

        return manager;
    }

    /* (non-Javadoc)
     * @see net.sf.xpontus.controllers.XPontusControllerIF#startApplication()
     */
    public void startApplication()
    {
        PluginRegistry registry = manager.getRegistry();
        Collection<PluginDescriptor> descriptors = registry.getPluginDescriptors();

        for (PluginDescriptor descriptor : descriptors)
        {
            if (!manager.isPluginActivated(descriptor))
            {
                try
                {
                    manager.activatePlugin(descriptor.getId());
                }
                catch (PluginLifecycleException err)
                {
                    LOG.error("PluginLifecycleException", err);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see net.sf.xpontus.controllers.XPontusControllerIF#stopApplication()
     */
    public void stopApplication()
    {
        XPontusComponentsUtils.getTopComponent().deactivateComponent();

        PluginRegistry registry = manager.getRegistry();
        Collection<PluginDescriptor> descriptors = registry.getPluginDescriptors();

        for (PluginDescriptor descriptor : descriptors)
        {
            if (manager.isPluginActivated(descriptor))
            {
                try
                {
                    manager.deactivatePlugin(descriptor.getId());
                }
                catch (Exception err)
                {
                    LOG.error(err.getMessage(), err);
                }
            }
        }

        manager.shutdown();
    }

    /**
     * @param path
     * @return
     */
    public PluginLocation[] getPluginLocations(File path)
    {
        if ((path == null) || !path.exists())
        {
            return new PluginLocation[0];
        }

        File[] files = null;

        if (path.isDirectory() && path.getName().equals(PLUGINS))
        {
            files = path.listFiles(new FileFilter()
                    {
                        public boolean accept(File pathname)
                        {
                            if (pathname.isFile())
                            {
                                String name = pathname.getName()
                                                      .toLowerCase(Locale.US);

                                return name.endsWith(".jar") ||
                                name.endsWith(".zip");
                            }

                            return pathname.isDirectory();
                        }
                    });
        }
        else if (path.isFile())
        {
            files = new File[] { path };
        }

        if (files == null)
        {
            return new PluginLocation[0];
        }

        List<PluginLocation> locations = new ArrayList<PluginLocation>();

        for (File file : files)
        {
            try
            {
                PluginLocation loc = StandardPluginLocation.create(file);

                if (loc != null)
                {
                    locations.add(loc);
                }
            }
            catch (MalformedURLException err)
            {
                LOG.error("MalformedURLException:" + err.getMessage(), err);
            }
        }

        return (PluginLocation[]) locations.toArray(new PluginLocation[0]);
    }

    /**
     *
     * @param context
     * @return
     */
    public PluginLocation getPluginLocation(String context)
    {
        URL u_context = getClass().getResource(context);
        URL u_manifest = getClass().getResource(context + "plugin.xml");

        PluginLocation loc = null;

        try
        {
            loc = new StandardPluginLocation(u_context, u_manifest);
        }
        catch (Exception e)
        {
            LOG.error("Unable to create plugin from context:" + context);
            LOG.error(e.getMessage(), e);
        }

        return loc;
    }

    /**
     * Add built in plugins
     * @return The list of built in plugins locations
     */
    public PluginLocation[] getBuiltinPluginLocations()
    {
        java.lang.String conf = "/net/sf/xpontus/plugins/plugins.txt";

        InputStream is = null;
        LineIterator it = null;

        List<PluginLocation> locations = new ArrayList<PluginLocation>();

        try
        {
            is = getClass().getResourceAsStream(conf);
            it = IOUtils.lineIterator(is, "UTF-8");

            while (it.hasNext())
            {
                String line = it.nextLine();

                PluginLocation loc = getPluginLocation(line);
                locations.add(loc);
            }
        }
        catch (IOException ex)
        {
            LOG.error(ex.getMessage(), ex);
        }
        finally
        {
            if (is != null)
            {
                IOUtils.closeQuietly(is);
            }

            if (it != null)
            {
                LineIterator.closeQuietly(it);
            }
        }

        return (PluginLocation[]) locations.toArray(new PluginLocation[0]);
    }

    /**
     *
     * @return
     */
    public PluginLocation[] getPluginLocations()
    {
        List<PluginLocation> locations = new ArrayList<PluginLocation>();

        String iswebstart = System.getProperty("xpontusisjavawebstart");

        if (iswebstart == null)
        {
            try
            {
                File systemPluginsDir = new File("plugins");

                if (systemPluginsDir.exists())
                {
                    DefaultSettingsModuleImpl.XPONTUS_SYSTEM_PLUGIN_DIR = systemPluginsDir;
                    locations.addAll(Arrays.asList(getPluginLocations(
                                systemPluginsDir)));
                }
            }
            catch (Exception e)
            {
                LOG.warn(e.getMessage(), e);
            }
        }
        else
        {
            // "~/.xpontus/plugins"
            File mainPluginsDir = XPontusConstantsIF.XPONTUS_PLUGINS_DIR;

            locations.addAll(Arrays.asList(getPluginLocations(mainPluginsDir)));
        }

        return (PluginLocation[]) locations.toArray(new PluginLocation[0]);
    }
}
