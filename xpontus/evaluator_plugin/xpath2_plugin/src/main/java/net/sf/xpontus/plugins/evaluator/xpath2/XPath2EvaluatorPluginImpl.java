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
import net.sf.saxon.om.NodeInfo;
import net.sf.saxon.tree.NodeImpl;
import net.sf.saxon.xpath.NamespaceContextImpl;
import net.sf.saxon.xpath.StandaloneContext;
import net.sf.saxon.xpath.XPathEvaluator;
import net.sf.saxon.xpath.XPathExpressionImpl;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.evaluator.EvaluatorPluginIF;
import net.sf.xpontus.plugins.evaluator.XPathResultDescriptor;
import net.sf.xpontus.utils.NamespaceResolverHandler;

import org.apache.xerces.parsers.SAXParser;

import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.swing.text.JTextComponent;

import javax.xml.transform.Source;
import javax.xml.transform.sax.SAXSource;


/**
 * XPath 2.0 engine
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPath2EvaluatorPluginImpl implements EvaluatorPluginIF {
    public XPath2EvaluatorPluginImpl() {
    }

    public String getName() {
        return "XPATH 2.0";
    }

    public XPathResultDescriptor[] handle(String expression)
        throws Exception { 
        
        JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                     .getDocumentTabContainer()
                                                     .getCurrentEditor();

        String texte = jtc.getText();
        InputStream is = new BufferedInputStream(new ByteArrayInputStream(
                    texte.getBytes()));

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

        XPathExpressionImpl m_exp = xpath.createExpression(expression);

        final List list = m_exp.evaluate();

        XPathResultDescriptor[] results = new XPathResultDescriptor[list.size()];

        for (int i = 0; i < list.size(); i++) {
            Object o = list.get(i);
            XPathResultDescriptor res = null;

            if (o instanceof NodeInfo) {
                NodeInfo resultInfo = (NodeInfo) o;
                res = new XPathResultDescriptor(resultInfo.getDisplayName());
                res.setLineInfo(true);
                res.setLine(resultInfo.getLineNumber() + 1);
                res.setColumn(0);
            } else {
                res = new XPathResultDescriptor(o.toString());
            }

            results[i] = res;
        }

        return results;
    }
}
