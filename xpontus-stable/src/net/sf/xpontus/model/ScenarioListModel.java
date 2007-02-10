/*
 * ScenarioListModel.java
 *
 * Created on March 4, 2006, 2:44 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.model;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;

import java.io.File;

import java.util.Vector;


/**
 *
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
