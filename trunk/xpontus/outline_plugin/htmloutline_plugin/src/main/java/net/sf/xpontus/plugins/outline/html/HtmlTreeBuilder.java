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
package net.sf.xpontus.plugins.outline.html;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.parsers.TokenNode;

import org.apache.xerces.parsers.AbstractSAXParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XNIException;

import org.cyberneko.html.HTMLConfiguration;

import org.xml.sax.InputSource;

import java.io.Reader;

import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;
import net.sf.xpontus.utils.NullEntityResolver;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */

/**
 * danson: A class to build a tree of TreeNodes out of an HTML document.
 */
public class HtmlTreeBuilder {
    private DefaultMutableTreeNode root = null;
    private Stack stack = new Stack();
    private org.apache.xerces.xni.XMLLocator m_locator;
    private DefaultMutableTreeNode current = null;

    public HtmlTreeBuilder() {
        root = new DefaultMutableTreeNode();
        this.current = root;
    }

    public DefaultMutableTreeNode getRootNode() {
        return root;
    }

    public void parse(Reader m_reader) {
        try {
            InputSource src = new InputSource(m_reader);
            DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                    .setMessage("Done reading document structure");

            HTMLTreeParser parser = new HTMLTreeParser();
            parser.setEntityResolver(NullEntityResolver.createInstance());
            parser.setErrorHandler(new ErrorHandler() {

                public void warning(SAXParseException exception) throws SAXException {
                    
                }

                public void error(SAXParseException exception) throws SAXException {
                    
                }

                public void fatalError(SAXParseException exception) throws SAXException {
                     
                }
            });
            parser.parse(src);
        } catch (Exception e) {
            //            e.printStackTrace();
        }
    }

    public class HTMLTreeParser extends AbstractSAXParser {
        public HTMLTreeParser() {
            super(new HTMLConfiguration());
        }

        public void startDocument(org.apache.xerces.xni.XMLLocator locator,
            java.lang.String encoding,
            org.apache.xerces.xni.NamespaceContext namespaceContext,
            org.apache.xerces.xni.Augmentations augs)
            throws org.apache.xerces.xni.XNIException {
            m_locator = locator;
        }

        /** Start element. */
        public void startElement(QName element, XMLAttributes attrs,
            Augmentations augs) throws XNIException {
            DefaultMutableTreeNode m_element = new TokenNode(element.rawname,
                    m_locator.getLineNumber(), m_locator.getColumnNumber());
            current.add(m_element);
            current = m_element;
        }

        public void emptyElement(QName element, XMLAttributes attrs,
            Augmentations augs) throws XNIException {
            DefaultMutableTreeNode m_element = new TokenNode(element.rawname,
                    m_locator.getLineNumber(), m_locator.getColumnNumber());
            current.add(m_element);
            current = m_element;
            current = (DefaultMutableTreeNode) current.getParent();
        }

        public void endElement(org.apache.xerces.xni.QName element,
            org.apache.xerces.xni.Augmentations augs)
            throws org.apache.xerces.xni.XNIException {
            if (current instanceof TokenNode) {
                TokenNode tn = (TokenNode) current;
                tn.endColumn = m_locator.getColumnNumber();
                tn.endLine = m_locator.getLineNumber();
            }

            current = (DefaultMutableTreeNode) current.getParent();
        }
    }
}
