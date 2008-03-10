/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenario.fop.fop_plugin;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.plugins.scenarios.DetachableScenarioModel;
import net.sf.xpontus.plugins.scenarios.ScenarioPluginIF;
import net.sf.xpontus.plugins.scenarios.ScenarioPluginsConfiguration;
import net.sf.xpontus.plugins.scenarios.TransformationErrorListener;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.avalon.framework.logger.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.local.LocalFile;

import org.apache.fop.apps.Driver;
import org.apache.fop.apps.Options;
import org.apache.fop.messaging.MessageHandler;

import org.apache.xalan.processor.TransformerFactoryImpl;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
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
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;


/**
 *
 * @author Propriétaire
 */
public class FOPTransformationPluginImpl implements ScenarioPluginIF {
    public static final String FOP_CONFIG_FILE_PROPERTY = "FOP_CONFIG_FILE_PROPERTY";
    public static final String FOP_TRANSFORMATION = "Output type (PDF, SVG, XML, Text)";
    private Logger logger;

    public FOPTransformationPluginImpl() {
        logger = new org.apache.avalon.framework.logger.ConsoleLogger();
    }

    public String getName() {
        return "FOP 0.20.5:" + FOP_TRANSFORMATION;
    }

    public String[] getProcessors() {
        return new String[] { "FOP 0.20.5:" + FOP_TRANSFORMATION };
    }

    public String[] getTransformationTypes() {
        return new String[] { "FOP (PDF, SVG, XML, Text)" };
    }

    public String[] getOutputTypes() {
        return new String[] { "PDF", "SVG", "XML", "Text" };
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

    public void handleScenario(DetachableScenarioModel model) {
        try {
            if (!isValidModel(model, true)) {
                return;
            }

            Driver driver = new Driver();

            MessageHandler.setScreenLogger(logger);
            driver.setLogger(logger);

            int renderer = Driver.RENDER_XML;

            String m_ext = FilenameUtils.getExtension(model.getOutput())
                                        .toUpperCase();

            if (m_ext.equals("PDF")) {
                renderer = Driver.RENDER_PDF;
            } else if (m_ext.equals("SVG")) {
                renderer = Driver.RENDER_SVG;
            } else if (m_ext.equals("TXT")) {
                renderer = Driver.RENDER_TXT;
            }

            driver.setRenderer(renderer);

            String m_prop = FOPConfigurationPanel.class.getName() + "$" +
                FOPTransformationPluginImpl.FOP_CONFIG_FILE_PROPERTY;
            Object o = XPontusConfig.getValue(m_prop);

            if (o != null) {
                Options opts = new Options();
                opts.loadUserconfiguration(o.toString());
            }

            logger.info("Getting ready for transformation");

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

            OutputStream bos = null;

            FileObject outputFo = VFS.getManager().resolveFile(model.getOutput());

            if (outputFo instanceof LocalFile) {
                bos = FileUtils.openOutputStream(output);
            } else {
                bos = outputFo.getContent().getOutputStream();
            }

            driver.setOutputStream(bos);

            // add the xsl parameters
            setParameters(tf, model.getParameters());

            Result res = new SAXResult(driver.getContentHandler());

            TransformationErrorListener tel = new TransformationErrorListener();

            tf.setErrorListener(tel);

            // transform the input file
            tf.transform(src, res);

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
        logger.info("Validating the model before execution");

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

    public void setSystemProperties() {
        logger.info("Settings system properties before running the scenario");

        // the default processor to use is xalan        
        System.setProperty("org.xml.sax.parser",
            "org.apache.xerces.parsers.SAXParser");
        System.setProperty("javax.xml.parsers.SAXParserFactory",
            "org.apache.xerces.jaxp.SAXParserFactoryImpl");
        System.setProperty("javax.xml.parsers.DocumentBuilderFactory",
            "org.apache.xerces.jaxp.DocumentBuilderFactoryImpl");
    }
}
