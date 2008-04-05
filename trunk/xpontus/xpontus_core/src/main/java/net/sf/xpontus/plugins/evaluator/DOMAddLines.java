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
package net.sf.xpontus.plugins.evaluator;

import net.sf.xpontus.utils.NullEntityResolver;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.NamespaceContext;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLLocator;
import org.apache.xerces.xni.XMLString;
import org.apache.xerces.xni.XNIException;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import org.xml.sax.InputSource;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;


/**
 * A sample of Adding lines to the DOM Node. This sample program illustrates:
 * - How to override methods from  DocumentHandler ( XMLDocumentHandler)
 * - How to turn off ignorable white spaces by overriding ignorableWhiteSpace
 * - How to use the SAX Locator to return row position ( line number of DOM element).
 * - How to attach user defined Objects to Nodes using method setUserData
 * This example relies on the following:
 * - Turning off the "fast" DOM so we can use set expansion to FULL
 * @version $Id: DOMAddLines.java,v 1.11 2005/05/09 01:01:40 mrglavas Exp $
 */
public class DOMAddLines extends DOMParser {
    static private boolean NotIncludeIgnorableWhiteSpaces = false;

    /** Print writer. */
    private PrintWriter out;
    private XMLLocator locator;

    public DOMAddLines(InputSource src) {
        //fNodeExpansion = FULL; // faster than: this.setFeature("http://apache.org/xml/features/defer-node-expansion", false);
        try {
            this.setFeature("http://apache.org/xml/features/dom/defer-node-expansion",
                false);
            this.setEntityResolver(NullEntityResolver.createInstance());
            this.parse(src);
            out = new PrintWriter(new OutputStreamWriter(System.out, "UTF8"));
        } catch (IOException e) {
            System.err.println("except" + e);
        } catch (org.xml.sax.SAXException e) {
            System.err.println("except" + e);
        }
    } // constructor

    public String getLineInfo(Node node) {
        String lineRowColumn = (String) ((Node) node).getUserData("startLine");
        String lineRowColumn1 = (String) ((Node) node).getUserData(
                "startColumn");

        return lineRowColumn + ":" + lineRowColumn1;
    }

    /** Prints the specified node, recursively. */
    public void print(Node node) {
        // is there anything to do?
        if (node == null) {
            return;
        }

        String lineRowColumn = (String) ((Node) node).getUserData("startLine");

        int type = node.getNodeType();

        switch (type) {
        // print document
        case Node.DOCUMENT_NODE: {
            out.println(lineRowColumn + ":" +
                "<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
            print(((Document) node).getDocumentElement());
            out.flush();

            break;
        }

        // print element with attributes
        case Node.ELEMENT_NODE: {
            out.print(lineRowColumn + ":" + '<');
            out.print(node.getNodeName());

            Attr[] attrs = sortAttributes(node.getAttributes());

            for (int i = 0; i < attrs.length; i++) {
                Attr attr = attrs[i];
                out.print(' ');
                out.print(attr.getNodeName());
                out.print("=\"");
                out.print(attr.getNodeValue());
                out.print('"');
            }

            out.print('>');

            NodeList children = node.getChildNodes();

            if (children != null) {
                int len = children.getLength();

                for (int i = 0; i < len; i++) {
                    print(children.item(i));
                }
            }

            break;
        }

        // handle entity reference nodes
        case Node.ENTITY_REFERENCE_NODE: {
            out.print('&');
            out.print(node.getNodeName());
            out.print(';');

            break;
        }

        // print cdata sections
        case Node.CDATA_SECTION_NODE: {
            out.print("<![CDATA[");
            out.print(node.getNodeValue());
            out.print("]]>");

            break;
        }

        // print text
        case Node.TEXT_NODE: {
            out.print(node.getNodeValue());

            break;
        }

        // print processing instruction
        case Node.PROCESSING_INSTRUCTION_NODE: {
            out.print("<?");
            out.print(node.getNodeName());

            String data = node.getNodeValue();

            if ((data != null) && (data.length() > 0)) {
                out.print(' ');
                out.print(data);
            }

            out.print("?>");

            break;
        }
        }

        if (type == Node.ELEMENT_NODE) {
            out.print("</");
            out.print(node.getNodeName());
            out.print('>');
        }

        out.flush();
    } // print(Node)

    /** Returns a sorted list of attributes. */
    private Attr[] sortAttributes(NamedNodeMap attrs) {
        int len = (attrs != null) ? attrs.getLength() : 0;
        Attr[] array = new Attr[len];

        for (int i = 0; i < len; i++) {
            array[i] = (Attr) attrs.item(i);
        }

        for (int i = 0; i < (len - 1); i++) {
            String name = array[i].getNodeName();
            int index = i;

            for (int j = i + 1; j < len; j++) {
                String curName = array[j].getNodeName();

                if (curName.compareTo(name) < 0) {
                    name = curName;
                    index = j;
                }
            }

            if (index != i) {
                Attr temp = array[i];
                array[i] = array[index];
                array[index] = temp;
            }
        }

        return (array);
    } // sortAttributes(NamedNodeMap):Attr[]

    /* Methods that we override */
    /*   We override startElement callback  from DocumentHandler */
    public void startElement(QName elementQName, XMLAttributes attrList,
        Augmentations augs) throws XNIException {
        super.startElement(elementQName, attrList, augs);

        Node node = null;

        try {
            node = (Node) this.getProperty(
                    "http://apache.org/xml/properties/dom/current-element-node");

            //System.out.println( "The node = " + node );  TODO JEFF
        } catch (org.xml.sax.SAXException ex) {
            System.err.println("except" + ex);
            ;
        }

        if (node != null) {
            node.setUserData("startLine",
                String.valueOf(locator.getLineNumber()), null); // Save location String into node
            node.setUserData("startColumn",
                String.valueOf(locator.getColumnNumber()), null);
        }
    } //startElement 

    /* We override startDocument callback from DocumentHandler */
    public void startDocument(XMLLocator locator, String encoding,
        NamespaceContext namespaceContext, Augmentations augs)
        throws XNIException {
        super.startDocument(locator, encoding, namespaceContext, augs);
        this.locator = locator;

        Node node = null;

        try {
            node = (Node) this.getProperty(
                    "http://apache.org/xml/properties/dom/current-element-node");
        } catch (org.xml.sax.SAXException ex) {
            System.err.println("except" + ex);
            ;
        }

        if (node != null) {
            node.setUserData("startLine",
                String.valueOf(locator.getLineNumber()), null); // Save location String into node
        }
    } //startDocument 

    public void ignorableWhitespace(XMLString text, Augmentations augs)
        throws XNIException {
        if (!NotIncludeIgnorableWhiteSpaces) {
            super.ignorableWhitespace(text, augs);
        } else {
            ; // Ignore ignorable white spaces
        }
    } // ignorableWhitespace
}
