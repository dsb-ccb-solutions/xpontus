/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenarios;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.model.ConfigurationModel;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.List;
import java.util.Vector;


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
        if(!XPontusConfigurationConstantsIF.XPONTUS_SCENARIOS_FILE.getParentFile().exists()){
            XPontusConfigurationConstantsIF.XPONTUS_SCENARIOS_FILE.getParentFile().mkdirs();
        }
        return XPontusConfigurationConstantsIF.XPONTUS_SCENARIOS_FILE;
    }

    /**
     *
     *  save a configuration
     *
     */
    public void save() {
        try {
            XStream xstream = new XStream(new DomDriver());
            OutputStream fos = new FileOutputStream(getFileToSaveTo());
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            xstream.toXML(scenarioList, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     * load a configuration
     * @return the deserialized object loaded from a file
     */
    public void loadScenarios() {
        try {
            XStream xstream = new XStream(new DomDriver());
            InputStream is = new FileInputStream(getFileToSaveTo());
            Reader reader = new InputStreamReader(is, "UTF-8");

            scenarioList = (List) xstream.fromXML(reader);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
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
