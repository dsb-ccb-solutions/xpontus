/*
 * XSLScenarioControllerImpl.java
 * 
 * Created on Aug 26, 2007, 4:23:41 PM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.plugins.scenarios;

import java.util.Hashtable;
import java.util.Iterator;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.xpontus.controllers.impl.ScenarioConfigurationController;
import net.sf.xpontus.model.ScenarioModel;

/**
 *
 * @author mrcheeks
 */
public class XSLScenarioControllerImpl {

    public XSLScenarioControllerImpl() {
    }
    
    public void run(ScenarioConfigurationController controller) throws Exception{
        
        // the user transformation configuration object
        ScenarioModel scenario = controller.getScenario();
        
        // the processor to use with the given scenario
        String processor = scenario.getProcessor();
        
        // The scenario input file or string
        String input = scenario.getInput();
        
        // the scenario output
        String output = scenario.getOutput();
        
        
        TransformerFactory f = TransformerFactory.newInstance();
        Transformer t = f.newTransformer();
        
        Source src = new StreamSource();
        Result result = new StreamResult();
        
        // set the parameters for the xslt processor
        setParameters(t, scenario);
        
        // transform
        t.transform(src, result);
        
    }
    
    /**
     * 
     * @param t 
     * @param scenario 
     */
    public void setParameters(Transformer t, ScenarioModel scenario){
        Hashtable parameters = scenario.getParameters();
        if(parameters == null || parameters.size() < 0){
            return;
        }
        
        Iterator it = parameters.keySet().iterator();
        while(it.hasNext()){
            String param_key = it.next().toString();
            String param_value = parameters.get(param_key).toString();
            t.setParameter(param_key, param_value);
        }
    }

}
