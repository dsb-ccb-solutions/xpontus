/*
 * To change this template, choose Tools | Templates
 * /ScenarioExecutionView.java
 *
 * Created on August 22, 2007, 5:52 PM
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
 */
package net.sf.xpontus.plugins.scenarios;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.model.ScenarioModel;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;

import org.apache.commons.io.FileUtils;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import net.sf.xpontus.utils.XPontusComponentsUtils;
import org.apache.commons.io.IOUtils;


/**
 *
 * @author Yves Zoundi <yveszoundit at users dot sf dot net>
 */
public class DefaultScenarioPluginImpl implements ScenarioPluginIF {
    
    /**
     * 
     */
    public static final String SIMPLE_JAXP_TRANSFORMATION = "Output type (XML, HTML, Text)";
    
    public String getName() {
        return SIMPLE_JAXP_TRANSFORMATION;
    }

    public String[] getProcessors() {
        return new String[] { "Xalan 2.7.0:" + SIMPLE_JAXP_TRANSFORMATION };
    }

    public String[] getTransformationTypes() {
        return new String[] { "Simple (XML, HTML, Text)" };
    }

    public String[] getOutputTypes() {
        return new String[] { "XML", "HTML", "Text" };
    }

    /**
     * 
     * @param tf
     * @param parameters
     */
    public void setParameters(Transformer tf, Hashtable parameters) {
        if (parameters != null) {
            // Create an iterator for the parameters keys
            Iterator it = parameters.keySet().iterator();

            while (it.hasNext()) {
                String m_key = it.next().toString();
                Object m_value = parameters.get(m_key);
                tf.setParameter(m_key, m_value);
            }
        }
    }

    /**
     * 
     * @param model
     * @return
     * @throws java.lang.Exception
     */
    public Reader getReader(ScenarioModel model) throws Exception {
        InputStream bis = null;
        CharsetDetector detector = new CharsetDetector();

        if (model.isExternalDocument()) {
            bis = FileUtils.openInputStream(new File(model.getInput()));
        } else {
            String txt = DefaultXPontusWindowImpl.getInstance()
                                                 .getDocumentTabContainer()
                                                 .getCurrentEditor().getText();
            bis = new BufferedInputStream(new ByteArrayInputStream(
                        txt.getBytes()));
        }

        detector.setText(bis);

        return detector.detect().getReader();
    }

    public void handleScenario(ScenarioModel model) throws Exception {
        // set the processor properties for JAXP
        setSystemProperties();
        
        // initialize the xslt processor
        TransformerFactory tff = TransformerFactory.newInstance();

        Transformer tf = tff.newTransformer();

        // create an input source to process
        Reader m_reader = getReader(model);

        Source src = new StreamSource(m_reader);

        // create the output result
        File output = new File(model.getOutput());

        OutputStream bos = FileUtils.openOutputStream(output);

        Writer m_writer = new BufferedWriter(new OutputStreamWriter(bos));

        Result res = new StreamResult(m_writer);

        // add the xsl parameters
        setParameters(tf, model.getParameters());

        // transform the input file
        tf.transform(src, res);
        
        // close any open streams
        IOUtils.closeQuietly(m_writer);
        
        IOUtils.closeQuietly(bos);
    }

    /**
     * 
     * @param type
     * @param table
     * @return
     */
    public boolean isValidType(String type, String[] table) {
        for (int i = 0; i < table.length; i++) {
            if (table[i].equals(type)) {
                return true;
            }
        }

        return false;
    }

    /**
     * 
     * @param table
     * @return
     */
    public String getSupportedTypesAsString(String[] table) {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < table.length; i++) {
            sb.append(table[i]);

            if (i != (table.length - 1)) {
                sb.append(", ");
            }
        }

        return sb.toString();
    }

    public boolean isValidModel(ScenarioModel model)  { 
         
        StringBuffer errors = new StringBuffer();
 
        if(model.getOutput().trim().equals("")){
            errors.append("The output file has not been specified\n");
        }

        if (model.isExternalDocument()) {
            if (model.getInput().trim().equals("") ||
                    !new File(model.getInput()).exists()) {
                errors.append("The input document is invalid\n");
            }
        } else {
            if (DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer()
                                            .getCurrentEditor() == null) {
                errors.append(
                    "You want to run the scenario against the current\n");
                errors.append(
                    "opened document and theres is no document opened");
            }
        }
        
        if(errors.length() > 0){
             XPontusComponentsUtils.showErrorMessage(errors.toString());
             return false;
        }

        return true;
    }

    public void setSystemProperties() {
        // the default processor to use is xalan        
        System.setProperty("org.xml.sax.parser",
            "org.apache.xerces.parsers.SAXParser");
        System.setProperty("javax.xml.parsers.SAXParserFactory",
            "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
            "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
        System.setProperty("javax.xml.transform.TransformerFactory",
            "org.apache.xalan.processor.TransformerFactoryImpl");
    }
}
