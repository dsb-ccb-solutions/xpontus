/*
 * XMLUtils2.java
 *
 *
 * Created on 1 août 2005, 17:46
 *
 *  Copyright (C) 2005 Yves Zoundi
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

import net.sf.joost.trax.TransformerFactoryImpl;

import net.sf.saxon.Configuration;
import net.sf.saxon.om.DocumentInfo;
import net.sf.saxon.query.DynamicQueryContext;
import net.sf.saxon.query.StaticQueryContext;
import net.sf.saxon.query.XQueryExpression;

import net.sf.xpontus.core.utils.XMLUtils;
import net.sf.xpontus.model.ScenarioModel;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.avalon.framework.logger.ConsoleLogger;
import org.apache.avalon.framework.logger.Logger;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.fop.apps.Driver;
import org.apache.fop.messaging.MessageHandler;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.lang.reflect.Method;

import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.JOptionPane;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;


/**
 * A class to perform xslt transformations
 *
 * @author Yves Zoundi
 */
public class XMLUtils2 extends XMLUtils
  {
    private static XMLUtils2 _instance;
    private int renderer;
    private Log logger = LogFactory.getLog(XMLUtils2.class);

    public static XMLUtils2 getInstance()
      {
        if (_instance == null)
          {
            _instance = new XMLUtils2();
          }

        return _instance;
      }

    public void runXQuery(ScenarioModel scenario)
      {
        if (!scenario.isExternal())
          {
            JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
                "Using the current document is not yet supported!");

            return;
          }

        InputStream queryStream = null;

        Hashtable params = scenario.getParams();

        // documentul XML ce va fi interogat este reprezentat de
        // fisierul AircraftDealer.xml
        File XMLStream = null;
        String xmlFileName = scenario.getXmlURI();

        // print the result to the console
        OutputStream destStream = null;

        // compile the XQuery expression
        XQueryExpression exp = null;

        // create a Configuration object
        Configuration C = new Configuration();

        // static and dynamic context
        StaticQueryContext SQC = new StaticQueryContext(C);
        DynamicQueryContext DQC = new DynamicQueryContext(C);

        // add the parameters to the query processor
        String queryFileName = null;

        setParameters(DQC, params);

        try
          {
            destStream = new FileOutputStream(scenario.getOutputFile());

            if (!scenario.isExternal())
              {
                javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                            .getCurrentEditor();

                byte[] bt = edit.getText().getBytes();
                queryStream = new java.io.ByteArrayInputStream(bt);
              }
            else
              {
                queryStream = new FileInputStream(scenario.getXmlURI());
              }

            SQC.setBaseURI(new File(queryFileName).toURI().toString());

            // compilation
            exp = SQC.compileQuery(queryStream, null);
            SQC = exp.getStaticContext();

            // get the XML ready
            XMLStream = new File(xmlFileName);

            InputSource XMLSource = new InputSource(XMLStream.toURI().toString());
            SAXSource SAXs = new SAXSource(XMLSource);
            DocumentInfo DI = SQC.buildDocument(SAXs);
            DQC.setContextNode(DI);

            // evaluating
            exp.run(DQC, new StreamResult(destStream), new Properties());

            IOUtils.closeQuietly(destStream);
          }
        catch (Exception e)
          {
            System.err.println(e.getMessage());
          }
      }

    /**
     * XSLT conversion for a user XSLT scenario
     *
     * @param scenario
     *            A XSLT scenario
     */
    public void transform(ScenarioModel scenario)
      {
        Source in = null;
        File out = new File(scenario.getOutputFile());
        Source xsl = null;
        Hashtable params = scenario.getParams();

        try
          {
            if (!scenario.isExternal())
              {
                javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                            .getCurrentEditor();

                if (edit == null)
                  {
                    throw new Exception("Please open a document");
                  }

                byte[] bt = edit.getText().getBytes();
                java.io.InputStream is = new ByteArrayInputStream(bt);
                in = new StreamSource(is);
              }
            else
              {
                in = new StreamSource(scenario.getXmlURI());
              }

            xsl = new StreamSource(scenario.getXslURI());
          }
        catch (Exception e)
          {
            String log = "Error" + e.getMessage();
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
          }

        String output = scenario.getOutputType();

        if (output.equals("HTML") || output.equals("XML"))
          {
            transform(in, out, xsl, params);
          }
        else if (output.equals("STX"))
          {
            transformJoost(in, out, xsl, params);
          }
        else if (output.equals("XQUERY"))
          {
            runXQuery(scenario);
          }
        else
          {
            if (output.equals("PDF"))
              {
                renderer = Driver.RENDER_PDF;
              }
            else if (output.equals("SVG"))
              {
                renderer = Driver.RENDER_SVG;
              }
            else if (output.equals("TEXT"))
              {
                renderer = Driver.RENDER_TXT;
              }

            convertFOP(in, out, xsl, params, renderer);
          }
      }

    /**
     * XSLT transformation using Apache FOP(PDF, SVG, etc.)
     *
     * @param in
     *            The XML file
     * @param out
     *            The output file
     * @param xsl
     *            The xsl stylesheet to use
     * @param params
     *            The xsl stylesheet parameters
     * @param render
     *            The fop renderer to use
     */
    public void convertFOP(Source in, File out, Source xsl, Hashtable params,
        int render)
      {
        try
          {
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

            TransformerFactory _fact = TransformerFactory.newInstance();
            Transformer transformer = _fact.newTransformer(xsl);

            setParameters(transformer, params);

            transformer.transform(in, res);

            String log = "XML/HTML Transformation finished";
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
          }
        catch (Exception e)
          {
            String log = "Error" + e.getMessage();
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
          }
      }

    public void setParameters(Object object, Hashtable params)
      {
        try
          {
            if (params.size() > 0)
              {
                Method mMethod = object.getClass()
                                       .getDeclaredMethod("setParameter",
                        new Class[] { String.class, Object.class });

                for (Iterator it = params.keySet().iterator(); it.hasNext();)
                  {
                    String cle = (String) it.next();
                    String val = params.get(cle).toString();
                    mMethod.invoke(object, new Object[] { cle, val });
                  }
              }
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
      }

    public void transformJoost(Source in, File out, Source xsl, Hashtable params)
      {
        String oldTransformer = System.getProperty(
                "javax.xml.transform.TransformerFactory");

        try
          {
            TransformerFactory _factory = null;
            Transformer tf = null;
            Log logger = LogFactory.getLog(XMLUtils.class);

            _factory = new TransformerFactoryImpl();

            tf = _factory.newTransformer(xsl);

            setParameters(tf, params);

            logger.info("XSL transformation");
            logger.info("Input:" + in.getSystemId());
            logger.info("Output:" + out.getAbsolutePath());

            OutputStream fos = new FileOutputStream(out);
            javax.xml.transform.Result res = new StreamResult(fos);
            tf.transform(in, res);

            IOUtils.closeQuietly(fos);

            String log = "STX Transformation finished";
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
          }
        catch (Exception e)
          {
            String log = "Error" + e.getMessage();
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
          }

        System.setProperty("javax.xml.transform.TransformerFactory",
            oldTransformer);
      }
  }
