/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenarios;


/**
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class DetachableScenarioModelConverter {
    private DetachableScenarioModel model;

    /**
     * 
     */
    public DetachableScenarioModelConverter() {
    }

    /**
     * @param model
     */
    public DetachableScenarioModelConverter(DetachableScenarioModel model) {
        this.model = model;
    }

    /**
     * @return
     */
    public DetachableScenarioModel getModel() {
        return model;
    }

    /**
     * @param model
     */
    public void setModel(DetachableScenarioModel model) {
        this.model = model;
    }

    /**
     *
     * @param scm
     * @return
     */
    public DetachableScenarioModel toSimpleModel(ScenarioModel scm) {
        DetachableScenarioModel m = new DetachableScenarioModel();
        m.setAlias(scm.getAlias());
        m.setExternalDocument(scm.isExternalDocument());
        m.setParameters(scm.getParameters());
        m.setProcessor(scm.getProcessor());
        m.setInput(scm.getInput());
        m.setOutput(scm.getOutput());
        m.setXsl(scm.getXsl());
        m.setPreview(scm.isPreview());

        return m;
    }

    /**
     *
     * @return
     */
    public ScenarioModel toScenarioModel() {
        ScenarioModel m = new ScenarioModel();
        m.setAlias(model.getAlias());
        m.setExternalDocument(model.isExternalDocument());
        m.setInput(model.getInput());
        m.setPreview(model.isPreview());
        m.setOutput(model.getOutput());
        m.setXsl(model.getXsl());
        m.setProcessor(model.getProcessor());
        m.setParameters(model.getParameters());

        return m;
    }
}
