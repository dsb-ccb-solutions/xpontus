/*
 * XMLIndentationPluginImpl.java
 *
 * Created on 8-Aug-2007, 6:01:57 PM
 *
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
 *
 */
package net.sf.xpontus.plugins.indentation.xml;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.indentation.IndentationPluginIF;
import net.sf.xpontus.utils.NullEntityResolver;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;

import java.io.StringWriter;
import java.util.Vector;
import javax.swing.text.JTextComponent;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import org.apache.xalan.processor.TransformerFactoryImpl;
import org.xml.sax.helpers.DefaultHandler;


/**
 * Class description
 * @author Yves Zoundi
 */
public class XMLIndentationPluginImpl implements IndentationPluginIF {
    private static final IndentingTransformerImpl TRANSFORMER = new IndentingTransformerImpl();
    
    public XMLIndentationPluginImpl() {
    }

    public String getName() {
        return "XML Indentation engine";
    }

    public String getMimeType() {
        return "text/xml";
    }

    public void run() throws Exception {
        JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                     .getDocumentTabContainer()
                                                     .getCurrentEditor();

        CharsetDetector chd = new CharsetDetector();
//        byte[] buf = jtc.getText().getBytes();
//        chd.setText(new ByteArrayInputStream(buf));
//
//        CharsetMatch match = chd.detect();
//        Reader reader = match.getReader();

        String omitCommentsOption = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() +
                "$" +
                XMLIndentationPreferencesConstantsIF.OMIT_COMMENTS_OPTION);

        String omitDoctypeOption = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() +
                "$" + XMLIndentationPreferencesConstantsIF.OMIT_DOCTYPE_OPTION);

        String omitXmlDeclaration = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() +
                "$" +
                XMLIndentationPreferencesConstantsIF.OMIT_XML_DECLARATION_OPTION);

        String preserveSpaceOption = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() +
                "$" +
                XMLIndentationPreferencesConstantsIF.PRESERVE_SPACE_OPTION);

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance(); 
//            Transformer t = transformerFactory.newTransformer() ;
//            t.setOutputProperty(OutputKeys.INDENT, "yes");  
////            XMLReader r = XMLReaderFactory.createXMLReader();
////            r.setFeature("http://xml.org/sax/features/validation", false);
////            r.setEntityResolver(NullEntityResolver.getInstance());  
////            r.setContentHandler(new DefaultHandler());
////            InputSource src = new InputSource(reader); 
//            Source saxSrc = new StreamSource(reader);//new SAXSource(r, src);   
//            ByteArrayOutputStream out = new java.io.ByteArrayOutputStream(); 
            StringWriter m_writer = new StringWriter();
//            Result m_result = new StreamResult(m_writer); 
//            t.transform(saxSrc, m_result);
//            m_writer.close();
//            out.close();
            TRANSFORMER.indentXml(jtc.getText(), m_writer, 4, false, new Vector());
//            TRANSFORMER.indentXml(omitXmlDeclaration, m_writer);

            byte[] b = m_writer.toString().getBytes();

            if (b.length > 0) {
                jtc.getDocument().remove(0, jtc.getDocument().getLength()); 
                chd = new CharsetDetector();
                chd.setText(b); 
                jtc.read(chd.detect().getReader(), null);
                System.out.println("done");
            }  
            else{
                System.out.println("Nothing done");
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
