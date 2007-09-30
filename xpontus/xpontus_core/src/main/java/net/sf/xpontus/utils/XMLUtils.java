/*
 * XMLUtils.java
 *
 * Created on 24 avril 2007, 15:20
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
 */
package net.sf.xpontus.utils;

import com.ibm.icu.text.CharsetDetector;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;
import org.xml.sax.helpers.DefaultHandler;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;


/**
 *
 * @author Yves Zoundi
 */
public class XMLUtils {
    private static XMLUtils INSTANCE;

    /** Creates a new instance of XMLUtils */
    private XMLUtils() {
    }

    /**
     *
     * @param text
     * @return
     * @throws java.lang.Exception
     */
    public static Document createDomDocument(String text)
        throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance()
                                                   .newDocumentBuilder();
        CharsetDetector detector = new CharsetDetector();
        byte[] b = IOUtils.toByteArray(text);
        detector.setText(b);

        Reader reader = detector.detect().getReader();
        Document doc = db.parse(new InputSource(reader));

        return doc;
    }

    /**
     *
     * @param xmlFile
     * @return
     * @throws java.lang.Exception
     */
    public static Document createDomDocument(File xmlFile)
        throws Exception {
        DocumentBuilder db = DocumentBuilderFactory.newInstance()
                                                   .newDocumentBuilder();
        CharsetDetector detector = new CharsetDetector();

        InputStream is = new BufferedInputStream(FileUtils.openInputStream(
                    xmlFile));
        detector.setText(is);

        Reader reader = detector.detect().getReader();
        Document doc = db.parse(new InputSource(reader));

        return doc;
    }

    /**
     *
     * @param text
     * @throws java.lang.Exception
     * @return
     */
    public static boolean isValid(String text) throws Exception {
        InputStream bis = new ByteArrayInputStream(text.getBytes());

        IOUtils.closeQuietly(bis);

        return isValid(bis);
    }

    /**
     *
     * @param is
     * @return
     */
    public static boolean isValid(InputStream is) {
        boolean valid = true;

        try {
            SAXParserFactory spf = SAXParserFactory.newInstance();
            SAXParser saxParser = spf.newSAXParser();
            saxParser.parse(is, new DefaultHandler());
        } catch (Exception e) {
            valid = false;
        }

        return valid;
    }
}
