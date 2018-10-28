/*
 * XMLUtils.java
 *
 * Created on 24 avril 2007, 15:20
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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
package net.sf.xpontus.utils;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.commons.io.FileUtils;
import org.apache.xerces.util.XMLCatalogResolver;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import com.ibm.icu.text.CharsetDetector;


/**
 * Utility class for XML documents
 * @author Yves Zoundi
 */
public class XMLUtils
{
    private XMLCatalogResolver xmlCatalogResolver;

    /** Creates a new instance of XMLUtils */
    private XMLUtils()
    {
    }

    public InputSource resolveURI(String uri)
    {
        try
        {
            InputSource src = new InputSource(xmlCatalogResolver.resolveURI(uri));

            if (src == null)
            {
            }

            return src;
        }
        catch (Exception e)
        {
            return null;
        }
    }

    /**
     * Create a DOM document from a string
     * @param text The document's text
     * @return a DOM Document
     * @throws java.lang.Exception parsing exception
     */
    public static Document createDomDocument(String text)
        throws Exception
    {
        DocumentBuilder db = DocumentBuilderFactory.newInstance()
                                                   .newDocumentBuilder();
        CharsetDetector detector = new CharsetDetector();
        detector.setText(text.getBytes());

        return db.parse(new InputSource(detector.detect().getReader()));
    }

    /**
     * Create a DOM document from a file
     * @param xmlFile The input file
     * @return a DOM document
     * @throws java.lang.Exception a parsing exception
     */
    public static Document createDomDocument(File xmlFile)
        throws Exception
    {
        DocumentBuilder db = DocumentBuilderFactory.newInstance()
                                                   .newDocumentBuilder();
        CharsetDetector detector = new CharsetDetector();

        InputStream is = new BufferedInputStream(FileUtils.openInputStream(
                    xmlFile));
        detector.setText(is);

        return db.parse(new InputSource(detector.detect().getReader()));
    }

    /**
     *
     * @param text
     * @throws java.lang.Exception
     * @return
     */
    public static boolean isValid(String text) throws Exception
    {
        InputStream bis = new ByteArrayInputStream(text.getBytes());

        return isValid(bis);
    }

    /**
     * Validate a stream
     * @param is The input stream
     * @return whether or not the input stream is valid
     */
    public static boolean isValid(InputStream is)
    {
        boolean valid = true;

        try
        {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();

            saxParser.parse(is, new DefaultHandler());
        }
        catch (Exception e)
        {
            valid = false;
        }

        return valid;
    }
}
