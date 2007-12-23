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

import java.util.Arrays;
import java.util.List;
import java.util.Vector;


/**
 *
 * @author Yves Zoundi <yveszoundit at users dot sf dot net>
 */
public class ScenarioPluginsConfiguration {
    private static ScenarioPluginsConfiguration INSTANCE;
    private List processorList = new Vector();
    private List outputTypes = new Vector();

    private List engines = new Vector();
    
    public List getEngines(){
        return engines;
    }
    
    private ScenarioPluginsConfiguration() {
    }

    public void addEngine(ScenarioPluginIF plugin){
        engines.add(plugin);
        
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
