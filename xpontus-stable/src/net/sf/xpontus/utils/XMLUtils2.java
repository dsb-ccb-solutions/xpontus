/*
 * XMLUtils2.java
 *
  *
 * Created on 1 août 2005, 17:46
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.utils;

import net.sf.xpontus.core.utils.XMLUtils;
import net.sf.xpontus.model.ScenarioModel;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.fop.apps.Driver;
import org.apache.fop.messaging.MessageHandler;

import java.io.File;

import java.util.Hashtable;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;


/**
 * A class to perform xslt transformations
 * @author Yves Zoundi
 */
public class XMLUtils2 extends XMLUtils {
    private static XMLUtils2 _instance;
    private int renderer;
    private Log logger = LogFactory.getLog(XMLUtils2.class);

    /**
     *
     * @return
     */
    public static XMLUtils2 getInstance() {
        if (_instance == null) {
            _instance = new XMLUtils2();
        }

        return _instance;
    }

    /**
     * XSLT conversion for a user XSLT scenario
     * @param scenario A XSLT scenario
     */
    public void transform(ScenarioModel scenario) {
        Source in = null;
        File out = new File(scenario.getOutputFile());
        Source xsl = null;
        Hashtable params = scenario.getParams();

        try {
            if (!scenario.isExternal()) {
                javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                            .getCurrentEditor();

                if (edit == null) {
                    throw new Exception("Please open a document");
                }

                byte[] bt = edit.getText().getBytes();
                java.io.InputStream is = new java.io.ByteArrayInputStream(bt);
                in = new StreamSource(is);
            } else {
                in = new StreamSource(scenario.getXmlURI());
            }

            xsl = new StreamSource(scenario.getXslURI());
        } catch (Exception e) {
            String log = "Error" + e.getMessage();
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
        }

        String output = scenario.getOutputType();

        if (output.equals("HTML") || output.equals("XML")) {
            transform(in, out, xsl, params);
        } else {
            if (output.equals("PDF")) {
                renderer = Driver.RENDER_PDF;
            } else if (output.equals("SVG")) {
                renderer = Driver.RENDER_SVG;
            } else if (output.equals("TEXT")) {
                renderer = Driver.RENDER_TXT;
            }

            convertFOP(in, out, xsl, params, renderer);
        }
    }

    /**
     * XSLT transformation using Apache FOP(PDF, SVG, etc.)
     * @param in The XML file
     * @param out The output file
     * @param xsl The xsl stylesheet to use
     * @param params The xsl stylesheet parameters
     * @param render The fop renderer to use
     */
    public void convertFOP(Source in, File out, Source xsl, Hashtable params,
        int render) {
        try {
            Driver driver = new Driver();
            Logger logger = new ConsoleLogger(ConsoleLogger.LEVEL_INFO);
            MessageHandler.setScreenLogger(logger);
            driver.setLogger(logger);
            driver.setRenderer(render);

            logger.info("XSL transformation");
            logger.info("Input:" + in.getSystemId());
            logger.info("Output:" + out.getAbsolutePath());
            driver.setOutputStream(new java.io.FileOutputStream(out));

            Result res = new SAXResult(driver.getContentHandler());

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer(xsl);

            if (params.size() > 0) {
                java.util.Iterator it = params.keySet().iterator();

                while (it.hasNext()) {
                    String cle = (String) it.next();
                    String val = params.get(cle).toString();
                    logger.info("Parameter:" + cle + ",Value:" + val);
                    transformer.setParameter(cle, val);
                }
            }

            transformer.transform(in, res);

            String log = "XML/HTML Transformation finished";
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
        } catch (Exception e) {
            String log = "Error" + e.getMessage();
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
        }
    }
}
