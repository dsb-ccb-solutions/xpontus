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
package net.sf.xpontus.plugins.evaluator.xpath2;

import net.sf.saxon.Configuration;
import net.sf.saxon.dom.NodeOverNodeInfo;
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.xpath.NamespaceContextImpl;
import net.sf.saxon.xpath.StandaloneContext;
import net.sf.saxon.xpath.XPathEvaluator;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.evaluator.DOMAddLines;
import net.sf.xpontus.plugins.evaluator.EvaluatorPluginIF;
import net.sf.xpontus.utils.NamespaceResolverHandler;

import org.apache.xerces.parsers.SAXParser;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.text.JTextComponent;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPath2EvaluatorPluginImpl implements EvaluatorPluginIF {
    public XPath2EvaluatorPluginImpl() {
    }

    public String getName() {
        return "XPATH 2.0";
    }

    public Object[] handle(String expression) {
        try {
            JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                         .getDocumentTabContainer()
                                                         .getCurrentEditor();

            String texte = jtc.getText();
            InputStream is = new BufferedInputStream(new ByteArrayInputStream(
                        texte.getBytes()));

            //            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            //
            //            dbf.setValidating(false);
            final Map nsMap = new HashMap();

            SAXParser parser = new SAXParser();

            parser.setContentHandler(new NamespaceResolverHandler(nsMap));

            InputSource mInputSource = new InputSource(new BufferedInputStream(
                        new ByteArrayInputStream(texte.getBytes())));

            parser.parse(mInputSource);

            Source mSource = new SAXSource(new InputSource(
                        new BufferedInputStream(
                            new ByteArrayInputStream(texte.getBytes()))));

            Configuration config = new Configuration();
            config.setLineNumbering(true);

            XPathEvaluator xpath = new XPathEvaluator(config);

            NodeInfo info = xpath.setSource(mSource);

            StandaloneContext std = new StandaloneContext(info);

            Iterator it = nsMap.keySet().iterator();

            while (it.hasNext()) {
                String key = it.next().toString();
                String val = nsMap.get(key).toString();
                std.declareNamespace(key, val);
            }

            NamespaceContextImpl contextImpl = new NamespaceContextImpl(std);

            xpath.setNamespaceContext(contextImpl);

            final List list = xpath.evaluate(expression);

            NodeList nodeList2 = new NodeList() {
                    public Node item(int arg0) {
                        NodeInfo info = (NodeInfo) list.get(arg0);

                        return NodeOverNodeInfo.wrap(info).getNextSibling();
                    }

                    public int getLength() {
                        return list.size();
                    }
                };

            DOMAddLines da = new DOMAddLines(new InputSource(
                        new BufferedInputStream(
                            new ByteArrayInputStream(texte.getBytes()))));

            return new Object[] { nodeList2, da };
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
