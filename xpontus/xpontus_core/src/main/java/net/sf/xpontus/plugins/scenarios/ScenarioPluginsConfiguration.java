/*
 * ScenarioExecutionView.java
 *
 * Created on August 22, 2007, 5:52 PM
 *
 * Copyright (C) 2005-2008 Yves Zoundi
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
package net.sf.xpontus.plugins.scenarios;

import net.sf.xpontus.constants.XPontusConstantsIF;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Yves Zoundi
 */
public class ScenarioPluginsConfiguration {
    private static ScenarioPluginsConfiguration INSTANCE;
    private List processorList = new Vector();
    private List outputTypes = new Vector();
    private Map enginesMap = new HashMap();
    private List engines = new Vector();
    private static final Log LOG = LogFactory.getLog(ScenarioPluginsConfiguration.class);

    private ScenarioPluginsConfiguration() {
    }

    /**
     * @param enginesMap
     */
    public void setEnginesMap(Map enginesMap) {
        this.enginesMap = enginesMap;
    }

    /**
     * @return
     */
    public List getEngines() {
        return engines;
    }

    /**
     * @param name
     * @return
     */
    public ScenarioPluginIF getEngineForName(String name) {
        System.out.println("Looking for plugin called:" + name);

        Iterator < String > names = enginesMap.keySet().iterator();

        while (names.hasNext()) {
            System.out.println("engine:" + names.next());
        }

        try {
            Hashtable t = (Hashtable) enginesMap.get(name);
            String m_name = (String) t.get(XPontusConstantsIF.OBJECT_CLASSNAME);
            ClassLoader m_loader = (ClassLoader) t.get(XPontusConstantsIF.CLASS_LOADER); 

            ScenarioPluginIF plugin = (ScenarioPluginIF) m_loader.loadClass(m_name)
                                                                 .newInstance();

            LOG.debug("retrieved plugin called:" + name); 
            return plugin;
        } catch (Exception e) {
            LOG.error("Exception while retrieving plugin");
            LOG.error(e.getMessage(), e);

            return null;
        }
    }

    /**
     * @param plugin
     * @param loader
     */
    public void addEngine(ScenarioPluginIF plugin, ClassLoader loader) {
        engines.add(plugin);

        Hashtable t = new Hashtable();
        t.put(XPontusConstantsIF.OBJECT_CLASSNAME, plugin.getClass().getName());
        t.put(XPontusConstantsIF.CLASS_LOADER, loader);
        enginesMap.put(plugin.getName(), t);

        processorList.addAll(Arrays.asList(plugin.getProcessors()));
        outputTypes.addAll(Arrays.asList(plugin.getOutputTypes()));
    }

    /**
     *
     * @return
     */
    public static ScenarioPluginsConfiguration getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ScenarioPluginsConfiguration();
        }

        return INSTANCE;
    }

    /**
     *
     * @return
     */
    public List getOutputTypes() {
        return outputTypes;
    }

    /**
     *
     * @param outputTypes
     */
    public void setOutputTypes(List outputTypes) {
        this.outputTypes = outputTypes;
    }

    /**
     *
     * @return
     */
    public List getProcessorList() {
        return processorList;
    }

    /**
     *
     * @param processorList
     */
    public void setProcessorList(List processorList) {
        this.processorList = processorList;
    }
}
