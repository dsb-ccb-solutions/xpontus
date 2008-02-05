/*
 * HTMLIndentationModuleImpl.java
 *
 * Created on Aug 13, 2007, 7:21:39 PM
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
package net.sf.xpontus.plugins.indentation.html;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.indentation.IndentationPluginIF;

import org.apache.commons.io.IOUtils;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.xni.parser.XMLDocumentFilter;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.cyberneko.html.HTMLConfiguration;
import org.cyberneko.html.filters.Purifier;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

import java.io.ByteArrayOutputStream;
import java.io.Reader;

import javax.swing.text.JTextComponent;


/**
 * HTML indenter engine implementation
 * @author Yves Zoundi
 */
public class HTMLIndentationModuleImpl implements IndentationPluginIF
{ 
    private final String ELEM_PROP = "http://cyberneko.org/html/properties/names/elems";
    private final String ATTRS_PROP = "http://cyberneko.org/html/properties/names/attrs";
    private final String NS_PROP = "http://xml.org/sax/features/namespaces";
    private final String BALANCE_PROP = "http://cyberneko.org/html/features/balance-tags";
    private final String FILTERS_PROP = "http://cyberneko.org/html/properties/filters"; 

    public HTMLIndentationModuleImpl()
    {
    }

    public String getName()
    {
        return "HTML formatter";
    }

    public String getMimeType()
    {
        return "text/html";
    }

    public void run() throws Exception
    {
        try
        {
            HTMLConfiguration config = new HTMLConfiguration();
            config.setProperty(ELEM_PROP, "lower");
            config.setProperty(ATTRS_PROP, "lower");
            config.setFeature(NS_PROP, true);
            config.setFeature(BALANCE_PROP, true);

            JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                         .getDocumentTabContainer()
                                                         .getCurrentEditor();

            CharsetDetector chd = new CharsetDetector();
            chd.setText(jtc.getText().getBytes());

            Reader reader = chd.detect().getReader();

            Purifier purifier = new Purifier();
            XMLDocumentFilter[] filters = new XMLDocumentFilter[] { purifier };

            DOMParser parser = new DOMParser(config);
            parser.setProperty(FILTERS_PROP, filters);

            InputSource source = new InputSource(reader);

            parser.parse(source);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Document htmlD = parser.getDocument();
            OutputFormat format = new OutputFormat(htmlD, "UTF-8", true);
            format.setOmitXMLDeclaration(true);

            XMLSerializer serial = new XMLSerializer(out, format);
            serial.serialize(htmlD);
            IOUtils.closeQuietly(out);

            byte[] b = out.toByteArray();

            if (b.length > 0)
            { 
                jtc.getDocument().remove(0, jtc.getDocument().getLength());
                jtc.getDocument().insertString(0, new String(b), null); 
            }
        }
        catch (Exception ex)
        {
            throw ex;
        }
    }
}
