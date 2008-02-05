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

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.indentation.IndentationPluginIF;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.Reader;

import javax.swing.text.JTextComponent;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.xpontus.utils.NullEntityResolver;


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

    public void run() throws Exception{
        JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                     .getDocumentTabContainer()
                                                     .getCurrentEditor();

        CharsetDetector chd = new CharsetDetector();
        chd.setText(jtc.getText().getBytes());

        Reader reader = chd.detect().getReader();

        try {
            DocumentBuilderFactory fact = DocumentBuilderFactory.newInstance();
            fact.setValidating(false);

            DocumentBuilder builder = fact.newDocumentBuilder();
            builder.setEntityResolver(NullEntityResolver.getInstance());

            InputSource src = new InputSource(reader);
            Document doc = builder.parse(src);

            OutputFormat formatter = new OutputFormat();
            formatter.setIndenting(true); 
            formatter.setEncoding("UTF-8");

            ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            XMLSerializer serializer = new XMLSerializer(out, formatter);
            serializer.serialize(doc);

            byte[] b = out.toByteArray();

            if (b.length > 0) {
                jtc.getDocument().remove(0, jtc.getDocument().getLength());
                jtc.getDocument().insertString(0, new String(b), null);
            } else {
            }
        } catch (Exception e) {
           throw e;
        }
    }
}

