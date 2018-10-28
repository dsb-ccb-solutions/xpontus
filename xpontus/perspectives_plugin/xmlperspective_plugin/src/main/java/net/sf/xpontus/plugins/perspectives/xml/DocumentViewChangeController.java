/*
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
 *
 */
package net.sf.xpontus.plugins.perspectives.xml;

import com.ibm.icu.text.CharsetDetector;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

import java.io.Reader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;


/**
 *
 * @author Yves Zoundi
 */
public class DocumentViewChangeController {
    public static Document createDocument(byte[] is) throws Exception {
        CharsetDetector detector = new CharsetDetector();
        detector.setText(is);

        Reader m_reader = detector.detect().getReader();

        TransformerFactory factory = TransformerFactory.newInstance();
        Transformer transformer = factory.newTransformer();
        transformer.setOutputProperty(OutputKeys.INDENT, "no");

        DOMResult result = new DOMResult();

        InputSource m_source = new InputSource(m_reader);

        transformer.transform(new SAXSource(m_source), result);

        return (Document) result.getNode();
    }
}
