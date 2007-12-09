/*
 * ScenarioManagerController.java
 * 
 * Created on 19-Aug-2007, 9:17:05 AM
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

import net.sf.xpontus.modules.gui.components.ScenarioManagerView;

/**
 * Class to manage the scenario manager form
 * @author Yves Zoundi
 * @version 0.0.1
 */
public class ScenarioManagerController {

    
    public static final String NEW_SCENARIO_METHOD = "addNewScenario";
    private ScenarioManagerView view;

    public ScenarioManagerView getView() {
        return view;
    }

    public void setView(ScenarioManagerView view) {
        this.view = view;
    }
    
    /**
     * 
     * @param view
     */
    public ScenarioManagerController(ScenarioManagerView view) {
        setView(view);
    }
    
    public ScenarioManagerController() {
    }
    
    /**
     * Method to delete a scenario
     */
    public void deleteScenario(){
        
    }
    
    /**
     * Method to add a new scenario
     * 
     */
    public void addNewScenario(){
        
    }
    
    /**
     * Method to edit a scenario
     */
    public void editScenario(){
        
    }
    
}
