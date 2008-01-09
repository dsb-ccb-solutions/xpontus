/*
 * ScenarioConfigurationController.java
 * 
 * Created on Aug 26, 2007, 4:29:48 PM
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

package net.sf.xpontus.controllers.impl;

import net.sf.xpontus.plugins.scenarios.ScenarioModel;

/**
 *
 * Controller for a user scenario according to the output type
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class ScenarioConfigurationController {

    private ScenarioModel scenario;

    /**
     * 
     * @return 
     */
    public ScenarioModel getScenario() {
        return scenario;
    }

    /**
     * 
     * @param scenario 
     */
    public void setScenario(ScenarioModel scenario) {
        this.scenario = scenario;
    }
    
    /**
     * 
     * @param scenario 
     */
    public ScenarioConfigurationController(ScenarioModel scenario) {
        setScenario(scenario);
    }
    
    /**
     * 
     */
    public ScenarioConfigurationController() {
    }

}
