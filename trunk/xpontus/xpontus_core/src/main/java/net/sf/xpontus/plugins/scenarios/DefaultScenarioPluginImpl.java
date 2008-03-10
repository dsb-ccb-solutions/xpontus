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

import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.local.LocalFile;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;

import java.util.Hashtable;
import java.util.Iterator;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 *
 * @author Yves Zoundi <yveszoundit at users dot sf dot net>
 */
public class DefaultScenarioPluginImpl implements ScenarioPluginIF {
    /**
     *
     */
    public static final String SIMPLE_JAXP_TRANSFORMATION = "Output type (XML, HTML, Text)";
    private Log log = LogFactory.getLog(DefaultScenarioPluginImpl.class);

    public String getName() {
        return "Xalan 2.7.0:" + SIMPLE_JAXP_TRANSFORMATION;
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
    public Reader getReader(DetachableScenarioModel model)
        throws Exception {
        InputStream bis = null;
        CharsetDetector detector = new CharsetDetector();

        if (model.isExternalDocument()) {
            FileObject fo = VFS.getManager().resolveFile(model.getInput());
            bis = new BufferedInputStream(fo.getContent().getInputStream());
        } else {
            String txt = DefaultXPontusWindowImpl.getInstance()
                                                 .getDocumentTabContainer()
                                                 .getCurrentEditor().getText();
            bis = new BufferedInputStream(new ByteArrayInputStream(
                        txt.getBytes()));
        }

        detector.setText(new BufferedInputStream(bis));

        return detector.detect().getReader();
    }

    public void handleScenario(DetachableScenarioModel model) {
        try {
            if (!isValidModel(model, true)) {
                return;
            }

            DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("Transformation in progress...");
            
            log.info("Getting ready for transformation");

            // set the processor properties for JAXP
            setSystemProperties();

            // initialize the xslt processor
            TransformerFactory tFactory = TransformerFactory.newInstance();

            FileObject fo = VFS.getManager().resolveFile(model.getXsl());

            CharsetDetector detector = new CharsetDetector();

            detector.setText(new BufferedInputStream(fo.getContent()
                                                       .getInputStream()));

            Source sSource = new StreamSource(fo.getURL().toExternalForm());

            Templates translet = null;

            try {
                translet = tFactory.newTemplates(sSource);
            } catch (TransformerConfigurationException tce) {
                tce.printStackTrace();
                throw new Exception("Error compiling stylesheet:" +
                    tce.getMessage());
            }

            Transformer tf = translet.newTransformer();

            // create an input source to process
            Reader m_reader = getReader(model);

            Source src = new StreamSource(m_reader);

            // create the output result
            FileObject outputFo = VFS.getManager().resolveFile(model.getOutput());

            OutputStream bos = null;

            if (outputFo instanceof LocalFile) {
                bos = new FileOutputStream(outputFo.getName().getPath());
            } else {
                bos = outputFo.getContent().getOutputStream();
            }

            Result res = new StreamResult(bos);

            // add the xsl parameters
            setParameters(tf, model.getParameters());

            TransformationErrorListener tel = new TransformationErrorListener();

            tf.setErrorListener(tel);

            // transform the input file
            tf.transform(src, res);

            // close any open streams 
            IOUtils.closeQuietly(bos);

            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();

            OutputDockable odk = (OutputDockable) console.getDockableById(MessagesWindowDockable.DOCKABLE_ID);

            if (!tel.getErrors().equals("")) {
                odk.println("Some errors occured...", OutputDockable.RED_STYLE);

                odk.println(tel.getErrors(), OutputDockable.RED_STYLE);
            } else {
                DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("Transformation finished!");
                odk.println("Transformation finished!");

                if (tel.warningsFound()) {
                    odk.println("Warnings:" + tel.getWarnings());
                }
            }
        } catch (Exception e) {
            DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("Transformation failed! - See messages window");
            e.printStackTrace();
            throw new RuntimeException("Transformation failed:\n" +
                e.getMessage());
        } 
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

    public boolean isValidModel(DetachableScenarioModel model,
        boolean transformationMode) {
        log.info("Validating the model before execution");

        StringBuffer errors = new StringBuffer();

        if (model.getOutput().trim().equals("")) {
            errors.append("The output file has not been specified\n");
        }

        if (model.getXsl().trim().equals("")) {
            errors.append("The script/stylesheet file has not been specified\n");
        }

        if (model.isExternalDocument()) {
            if (model.getInput().trim().equals("")) {
                errors.append("The input document is invalid\n");
            }
        } else {
            if (transformationMode) {
                if (DefaultXPontusWindowImpl.getInstance()
                                                .getDocumentTabContainer()
                                                .getCurrentEditor() == null) {
                    errors.append(
                        "You want to run the scenario against the current\n");
                    errors.append(
                        "opened document and theres is no document opened");
                }
            }
        }

        if (!ScenarioPluginsConfiguration.getInstance().getProcessorList()
                                             .contains(model.getProcessor())) {
            errors.append(
                "The processor for this transformation is not found\n");
        }

        if (errors.length() > 0) {
            XPontusComponentsUtils.showErrorMessage(errors.toString());

            return false;
        }

        return true;
    }

    public void setSystemProperties() {
        System.setProperty("javax.xml.transform.TransformerFactory",
            "org.apache.xalan.processor.TransformerFactoryImpl");
        // the default processor to use is xalan        
        System.setProperty("org.xml.sax.parser",
            "org.apache.xerces.parsers.SAXParser");
        System.setProperty("javax.xml.parsers.SAXParserFactory",
            "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
            "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    }
}
