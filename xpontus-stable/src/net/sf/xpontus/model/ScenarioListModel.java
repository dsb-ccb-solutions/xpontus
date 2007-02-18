/*
 * ScenarioListModel.java
 *
 * Created on March 4, 2006, 2:44 PM
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.model;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;

import java.io.File;

import java.util.Vector;


/**
 * the scenario list model
 * @author Yves Zoundi
 */
public class ScenarioListModel extends ConfigurationModel {
    private Vector scenarioList;

    /** Creates a new instance of ScenarioListModel */
    public ScenarioListModel() {
        scenarioList = new Vector();
    }

    public File getFileToSaveTo() {
        return XPontusConstants.SCENARIO_FILE;
    }

    public String getMappingURL() {
        return "/net/sf/xpontus/model/mappings/ScenarioListModel.xml";
    }

    public Vector getScenarioList() {
        return scenarioList;
    }

    public void setScenarioList(Vector scenarioList) {
        this.scenarioList = scenarioList;
    }
}
