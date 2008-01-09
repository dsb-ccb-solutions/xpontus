/*
 * ScenarioPluginIF.java
 *
 * Created on 19-Aug-2007, 9:24:22 AM
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

import net.sf.xpontus.plugins.scenarios.ScenarioModel;


/**
 * Scenario plugin interface
 * @version 0.0.1
 * @author Yves Zoundi
 */
public interface ScenarioPluginIF {
    /**
     *
     * @return
     */
    public String getName();

    /**
     *
     * @return
     */
    public String[] getProcessors();

    /**
     *
     * @return
     */
    public String[] getTransformationTypes();

    /**
     *
     * @return
     */
    public String[] getOutputTypes();

    /**
     *
     */
    public void setSystemProperties();

    /**
     *
     * @param model
     * @throws java.lang.Exception
     */
    void handleScenario(DetachableScenarioModel model) throws Exception;

    /**
     *
     * @param model
     * @return
     * @throws java.lang.Exception
     */
    public boolean isValidModel(DetachableScenarioModel model, boolean b) ;
}
