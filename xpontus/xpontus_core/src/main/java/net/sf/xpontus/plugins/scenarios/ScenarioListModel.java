/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.plugins.scenarios;

import java.io.File;
import java.util.List;
import java.util.Vector;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.model.ConfigurationModel;

/**
 * the scenario list model
 * @author Yves Zoundi
 */
public class ScenarioListModel extends ConfigurationModel {
    private List scenarioList;

    /** Creates a new instance of ScenarioListModel */
    public ScenarioListModel() {
        scenarioList = new Vector();
    }

    /**
     * @return the destination file
     */
    public File getFileToSaveTo() {
        return XPontusConfigurationConstantsIF.XPONTUS_SCENARIOS_FILE;
    }

    

    /**
     *  get the scenario list
     * @return the scenario list
     */
    public List getScenarioList() {
        return scenarioList;
    }

    /**
     * set the scenario list
     * @param scenarioList
     */
    public void setScenarioList(List scenarioList) {
        this.scenarioList = scenarioList;
    }
}
