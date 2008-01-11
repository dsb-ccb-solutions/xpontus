/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenario.fop.fop_plugin;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.plugins.scenarios.DefaultScenarioPluginImpl;
import net.sf.xpontus.plugins.scenarios.DetachableScenarioModel;
import net.sf.xpontus.plugins.scenarios.TransformationErrorListener;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.local.LocalFile;

import org.apache.fop.apps.Driver;
import org.apache.fop.messaging.MessageHandler;

import org.apache.xalan.processor.TransformerFactoryImpl;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.OutputStream;
import java.io.Reader;

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
public class FOPTransformationPluginImpl extends DefaultScenarioPluginImpl {
    public static final String FOP_TRANSFORMATION = "Output type (PDF, SVG, XML, Text)";
    private Logger logger;

    public FOPTransformationPluginImpl(){
        logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
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

            logger.info("Getting ready for transformation");

            // set the processor properties for JAXP
            setSystemProperties();

            // initialize the xslt processor
            TransformerFactory tFactory = new TransformerFactoryImpl();

            FileObject fo = VFS.getManager().resolveFile(model.getXsl());

            CharsetDetector detector = new CharsetDetector();

            detector.setText(new BufferedInputStream(fo.getContent()
                                                       .getInputStream()));

            Source sSource = new StreamSource(detector.detect().getReader());

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
