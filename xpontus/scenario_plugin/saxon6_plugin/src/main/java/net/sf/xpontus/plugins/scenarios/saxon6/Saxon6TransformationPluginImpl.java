/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenarios.saxon6;

import com.ibm.icu.text.CharsetDetector;

import com.icl.saxon.TransformerFactoryImpl;
import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.plugins.scenarios.DetachableScenarioModel;
import net.sf.xpontus.plugins.scenarios.ScenarioPluginIF;
import net.sf.xpontus.plugins.scenarios.ScenarioPluginsConfiguration;
import net.sf.xpontus.plugins.scenarios.TransformationErrorListener;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

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
import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 *
 * @author Proprietary
 */
public class Saxon6TransformationPluginImpl implements ScenarioPluginIF {
    public static final String SAXON6_TRANSFORMATION = "Output type (XML, HTML, Text)";
    private Log log = LogFactory.getLog(Saxon6TransformationPluginImpl.class);

    public Saxon6TransformationPluginImpl() {
    }

    public String getName() {
        return "Saxon6:" + SAXON6_TRANSFORMATION;
    }

    public String[] getProcessors() {
        return new String[] { "Saxon6:" + SAXON6_TRANSFORMATION };
    }

    public String[] getTransformationTypes() {
        return new String[] { "(XML, HTML, Text)" };
    }

    public String[] getOutputTypes() {
        return new String[] { "HTML", "XML", "Text" };
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
            bis = FileUtils.openInputStream(new File(model.getInput()));
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
            if (model.getInput().trim().equals("") ||
                    !new File(model.getInput()).exists()) {
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

    public void handleScenario(DetachableScenarioModel model) {
        try {
            if (!isValidModel(model, true)) {
                return;
            }

            log.info("Getting ready for transformation");

            // set the processor properties for JAXP
            setSystemProperties();

            // initialize the xslt processor
            TransformerFactory tFactory = new TransformerFactoryImpl();

            FileObject fo = VFS.getManager().resolveFile(model.getXsl());

            CharsetDetector detector = new CharsetDetector();

            detector.setText(new BufferedInputStream(fo.getContent()
                                                       .getInputStream()));

            Source sSource = new StreamSource(fo.getURL().toExternalForm());

            Templates translet = null;

            try {
                translet = tFactory.newTemplates(sSource);
            } catch (TransformerConfigurationException tce) {
                throw new Exception("Error compiling stylesheet:" +
                    tce.getMessage());
            }

            Transformer tf = translet.newTransformer();

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

            TransformationErrorListener tel = new TransformationErrorListener();

            tf.setErrorListener(tel);

            // transform the input file
            tf.transform(src, res);

            // close any open streams
            IOUtils.closeQuietly(m_writer);

            IOUtils.closeQuietly(bos);

            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();

            OutputDockable odk = (OutputDockable) console.getDockableById(MessagesWindowDockable.DOCKABLE_ID);

            if (!tel.getErrors().equals("")) {
                odk.println("Some errors occured...", OutputDockable.RED_STYLE);
                odk.println(tel.getErrors(), OutputDockable.RED_STYLE);
            } else {
                odk.println("Transformation finished!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Transformation failed:\n" +
                e.getMessage());
        }
    }

    public void setSystemProperties() {
        log.info("Settings system properties before running the scenario"); 
        // the default processor to use is xalan        
        System.setProperty("org.xml.sax.parser",
            "org.apache.xerces.parsers.SAXParser");
        System.setProperty("javax.xml.parsers.SAXParserFactory",
            "org.apache.xerces.jaxp.SAXParserFactoryImpl"); 
    }
}
