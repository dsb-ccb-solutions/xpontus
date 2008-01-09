/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenarios;


/**
 *
 * @author Propriétaire
 */
public class DetachableScenarioModelConverter {
    private DetachableScenarioModel model;

    public DetachableScenarioModelConverter(){
        
    }
    public DetachableScenarioModelConverter(DetachableScenarioModel model) {
        this.model = model;
    }

    public DetachableScenarioModel getModel() {
        return model;
    }

    public void setModel(DetachableScenarioModel model) {
        this.model = model;
    }

    public DetachableScenarioModel toSimpleModel(ScenarioModel scm){
       DetachableScenarioModel m = new DetachableScenarioModel(); 
       m.setAlias(scm.getAlias());
       m.setExternalDocument(scm.isExternalDocument());
       m.setParameters(scm.getParameters());
       m.setInput(scm.getInput());
       m.setOutput(scm.getOutput());
       m.setXsl(scm.getXsl());
       return m;
    }
    
    public ScenarioModel toScenarioModel() {
        ScenarioModel m = new ScenarioModel();
        m.setAlias(model.getAlias());
        m.setExternalDocument(model.isExternalDocument());
        m.setInput(model.getInput());
        m.setOutput(model.getOutput());
        m.setXsl(model.getXsl());
        m.setProcessor(model.getProcessor());

        return m;
    }
}
