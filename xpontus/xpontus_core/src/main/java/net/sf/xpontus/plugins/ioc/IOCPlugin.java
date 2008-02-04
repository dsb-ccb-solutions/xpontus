/*
 * IOCPlugin.java
 *
 * Created on July 1, 2007, 10:35 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
package net.sf.xpontus.plugins.ioc;

import net.sf.xpontus.plugins.XPontusPlugin;
import net.sf.xpontus.properties.PropertiesHolder;


/**
 * XPontus IOC Container plugin
 * @author Yves Zoundi
 */
public class IOCPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "iocpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.ioc";
    public static final String PLUGIN_CATEGORY = "IOC";
    private IOCPluginIF container;

    /** Creates a new instance of IOCModule */
    public IOCPlugin() {
        PropertiesHolder.registerProperty(PLUGIN_IDENTIFIER, this);
    }

    /**
     * Returns the IOC Container
     * @return The IOC Container
     */
    public IOCPluginIF getContainer() {
        return container;
    }

    /**
     *
     * @param container
     */
    public void setContainer(IOCPluginIF container) {
        this.container = container;
    }

    /**
     * Retrieve a bean from the IOC container
     * @param alias The alias of the bean
     * @return A bean
     */
    public Object getBean(String alias) {
        return container.getBean(alias);
    }

    /**
     * Initialize the default IOC Container
     * @throws java.lang.Exception
     */
    public void init() throws Exception {
        //        PluginManager manager = getManager();
        //        PluginRegistry registry = manager.getRegistry();
        //        ExtensionPoint iocPluginExtPoint = registry.getExtensionPoint(getDescriptor()
        //                                                                          .getId(),
        //                EXTENSION_POINT_NAME);
        //
        //        Collection plugins = iocPluginExtPoint.getConnectedExtensions();
        //
        //        for (Iterator it = plugins.iterator(); it.hasNext();) {
        //            Extension ext = (Extension) it.next();
        //            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
        //            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
        //            String className = ext.getParameter("class").valueAsString();
        //            Class cl = classLoader.loadClass(className);
        //            IOCPluginIF plugin = (IOCPluginIF) cl.newInstance();
        //            setContainer(plugin);
        //        }
        //
        //        ClassLoader c = Thread.currentThread().getContextClassLoader();
        //
        //        if (container != null) {
        //            container.initializePropertiesBeans("/net/sf/xpontus/configuration/xpontus.properties",
        //                c);
        //        }
        setContainer(new SpringIOCModuleImpl());
        container.initializePropertiesBeans("/net/sf/xpontus/configuration/xpontus.properties",
            this.getClass().getClassLoader());
    }

    /**
     *
     * @throws java.lang.Exception
     */
    protected void doStart() throws Exception {
    }

    /**
     *
     * @throws java.lang.Exception
     */
    protected void doStop() throws Exception {
    }
}
