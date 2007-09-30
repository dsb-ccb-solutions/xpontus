/*
 * XSLScenarioPluginImpl.java
 *
 * Created on Aug 26, 2007, 9:40:17 AM
 * 
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
 * 
 */
package net.sf.xpontus.plugins.scenarios;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;


/**
 *
 * @author Yves Zoundi
 */
public class XSLScenarioPluginImpl implements ScenarioPluginIF
{
    public XSLScenarioPluginImpl()
    {
    }

    public Object getName()
    {
        return "XSL scenario Plugin";
    }

    public Object getScenarioController()
    {
        return null;
    }

    public Object getId()
    {
        return "xslt.scenarip.plugin.impl";
    }

    public List getOutputTypes()
    {
        return new ArrayList();
    }

    public Map getProcessorSettings()
    {
        return null;
    }

    public void addProcessor(Object processor, Properties processorSettings)
    {
    }
}
