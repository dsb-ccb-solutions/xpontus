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

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.indentation.IndentationPluginIF;
import net.sf.xpontus.utils.NullEntityResolver;

import com.ibm.icu.text.CharsetMatch;
import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.Reader;

import javax.swing.text.JTextComponent;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Class description
 * @author Yves Zoundi
 */
public class XMLIndentationPluginImpl implements IndentationPluginIF {
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
        byte[] buf = jtc.getText().getBytes();
        chd.setText(new ByteArrayInputStream(buf));

       CharsetMatch match = chd.detect();
        Reader reader = match.getReader();

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
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            fact.setValidating(false);

            DocumentBuilder builder = fact.newDocumentBuilder();
            builder.setEntityResolver(NullEntityResolver.getInstance());

            InputSource src = new InputSource(reader);
            Document doc = builder.parse(src);

            OutputFormat formatter = new OutputFormat();

            formatter.setOmitXMLDeclaration(Boolean.getBoolean(
                    omitXmlDeclaration));
            formatter.setOmitDocumentType(Boolean.getBoolean(omitDoctypeOption));
            formatter.setPreserveSpace(Boolean.getBoolean(preserveSpaceOption));
            formatter.setOmitComments(Boolean.getBoolean(omitCommentsOption));

            formatter.setIndenting(true);

            ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            XMLSerializer serializer = new XMLSerializer(out, formatter);
            serializer.serialize(doc);

            byte[] b = out.toByteArray();

            if (b.length > 0) {
                jtc.getDocument().remove(0, jtc.getDocument().getLength());

                InputStream newIs = new ByteArrayInputStream(b);
                chd.setText(newIs);
                match = chd.detect();
                jtc.read(match.getReader(), match.getName());
            } else {
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
