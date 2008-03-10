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
import net.sf.xpontus.utils.NamespaceResolverHandler;

import org.apache.xerces.parsers.SAXParser;

import org.apache.xpath.XPathAPI;

import org.w3c.dom.Document;
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
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPath1EvaluatorPluginImpl implements EvaluatorPluginIF {
    public XPath1EvaluatorPluginImpl() {
    }

    public String getName() {
        return "XPATH 1.0";
    }

    public Object[] handle(String expression) throws Exception {
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

        NodeList nodeList = api.eval(doc, expression, resolver).nodelist();

        return new Object[] { nodeList, da };
    }
}
