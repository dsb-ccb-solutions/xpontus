/*
 *
 *
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
package net.sf.xpontus.plugins.evaluator.xpath1;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.evaluator.CustomNamespaceContext;
import net.sf.xpontus.plugins.evaluator.DOMAddLines;
import net.sf.xpontus.plugins.evaluator.EvaluatorPluginIF;
import net.sf.xpontus.plugins.evaluator.XPathResultDescriptor;
import net.sf.xpontus.utils.NamespaceResolverHandler;

import org.apache.commons.lang.text.StrBuilder;

import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.parsers.SAXParser;

import org.apache.xpath.XPathAPI;
import org.apache.xpath.objects.XObject;

import org.w3c.dom.CharacterData;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.JTextComponent;

import javax.xml.namespace.NamespaceContext;


/**
 *
 * @author Yves Zoundi
 */
public class XPath1EvaluatorPluginImpl implements EvaluatorPluginIF {
    public XPath1EvaluatorPluginImpl() {
    }

    public String getName() {
        return "XPATH 1.0";
    }

    public XPathResultDescriptor[] handle(String expression)
        throws Exception {
        JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                     .getDocumentTabContainer()
                                                     .getCurrentEditor();

        String texte = jtc.getText();
        byte[] b = texte.getBytes();
        InputStream is = new ByteArrayInputStream(texte.getBytes());

        final Map nsMap = new HashMap();

        SAXParser parser = new SAXParser();

        parser.setFeature("http://xml.org/sax/features/validation", false);

        parser.setEntityResolver(null);

        parser.setContentHandler(new NamespaceResolverHandler(nsMap));

        CharsetDetector detector = new CharsetDetector();
        detector.setText(is);

        Reader m_reader = detector.detect().getReader();
        InputSource mInputSource = new InputSource(m_reader);

        parser.parse(mInputSource);

        detector.setText(new ByteArrayInputStream(texte.getBytes()));
        m_reader = detector.detect().getReader();

        InputSource src = new InputSource(m_reader);
        DOMAddLines da = new DOMAddLines(src); //db.parse(new BufferedInputStream(new ByteArrayInputStream(texte.getBytes())));

        Document doc = da.getDocument();

        XPathAPI api = new XPathAPI();

        NamespaceContext nsc = new CustomNamespaceContext(nsMap);

        JAXPPrefixResolver resolver = new JAXPPrefixResolver(nsc);

        XObject xObject = api.eval(doc, expression, resolver);

        if (xObject == null) {
            return new XPathResultDescriptor[0];
        }

        if (xObject.getType() == XObject.CLASS_NODESET) {
            NodeList nodeList = xObject.nodelist();

            XPathResultDescriptor[] results = new XPathResultDescriptor[nodeList.getLength()];

            for (int i = 0; i < results.length; i++) {
                Node m_node = nodeList.item(i);

                String m_text = null;

                if (m_node instanceof ElementImpl) {
                    m_text = m_node.getNodeName();
                } else if (m_node instanceof CharacterData) {
                    CharacterData ti = (CharacterData) m_node;
                    StrBuilder buff = new StrBuilder();
                    buff.append(ti.getData());

                    int taille = buff.length();
                    m_text = buff.toString();

                    if (taille > 15) {
                        m_text = m_text.substring(0, 15) +
                            " [REST OF THE TEXT OMITTED]";
                    }
                } else {
                    m_text = m_node.getNodeValue();
                }

                XPathResultDescriptor desc = new XPathResultDescriptor(m_text);
                int m_line = da.getLineNumber(m_node);
                int m_column = da.getColumnNumber(m_node);

                if (m_line != -1) {
                    desc.setLineInfo(true);
                    desc.setLine(m_line);
                    desc.setColumn(m_column);
                }

                results[i] = desc;
            }

            return results;
        } else {
            XPathResultDescriptor desc = new XPathResultDescriptor(xObject.xstr()
                                                                          .toString());

            return new XPathResultDescriptor[] { desc };
        }
    }
}
