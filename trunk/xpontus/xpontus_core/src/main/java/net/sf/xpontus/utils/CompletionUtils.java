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
package net.sf.xpontus.utils;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.xpath.XPathAPI;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileWriter;
import java.io.OutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class CompletionUtils {
    private static final Log log = LogFactory.getLog(CompletionUtils.class);
    private static Document grammarsDescriptor;

    static {
        Runtime.getRuntime().addShutdownHook(new Thread() {
                public void run() {
                    writeCompletionInformation();
                }
            });
    }

    private static Document getGrammarsDescriptor() {
        if (grammarsDescriptor == null) {
            try {
                File f = XPontusConfigurationConstantsIF.GRAMMAR_CACHING_DESCRIPTOR;

                if (!f.exists()) {
                    FileWriter fw = new FileWriter(f);
                    StrBuilder sb = new StrBuilder();
                    sb.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
                    sb.appendNewLine();
                    sb.append("<grammars></grammars>");
                    fw.write(sb.toString());
                    fw.close();
                }

                DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                grammarsDescriptor = db.parse(f);
            } catch (Exception err) {
                err.printStackTrace();
            }
        }

        return grammarsDescriptor;
    }

    // This method writes a DOM document to a file
    public static void writeCompletionInformation() {
        try {
            // Prepare the DOM document for writing
            Source source = new DOMSource(getGrammarsDescriptor());

            // Prepare the output file
            File f = XPontusConfigurationConstantsIF.GRAMMAR_CACHING_DESCRIPTOR;

            OutputStream bos = FileUtils.openOutputStream(f);

            Result result = new StreamResult(bos);

            // Write the DOM document to the file
            Transformer xformer = TransformerFactory.newInstance()
                                                    .newTransformer();
            xformer.transform(source, result);

            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void addCompletionInfo(String uri, String cachePath) {
        System.out.println("uri:" + uri + ",file:" + cachePath);

        Document doc = getGrammarsDescriptor();

        Element root = doc.getDocumentElement();

        Element grammar = doc.createElement("grammar");

        Element cacheURL = doc.createElement("cacheURL");
        Text cacheURLText = doc.createTextNode(uri);
        cacheURL.appendChild(cacheURLText);

        grammar.appendChild(cacheURL);

        Element cacheFile = doc.createElement("cacheFile");
        Text cacheFileText = doc.createTextNode(cachePath);
        cacheFile.appendChild(cacheFileText);

        grammar.appendChild(cacheFile);

        root.appendChild(grammar);
    }

    public static String getFileCompletionCache(String url) {
        try {
            Document doc = getGrammarsDescriptor();
            String xp = "/grammars/grammar[cacheURL[.='" + url + "']]";
            Node m_node = XPathAPI.selectSingleNode(doc, xp);

            if (m_node == null) {
                return null;
            }

            Element m_element = (Element) m_node;

            Element cacheFileElement = (Element) m_element.getElementsByTagName(
                    "cacheFile").item(0);

            System.out.println("Got element:" + cacheFileElement.getTextContent());
            
            return cacheFileElement.getTextContent();
        } catch (Exception err) {
            return null;
        }
    }
}
