/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.outline.xml;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.parsers.TokenNode;

import org.apache.commons.lang.text.StrBuilder;

import org.apache.xerces.parsers.XIncludeAwareParserConfiguration;
import org.apache.xerces.parsers.XMLDocumentParser;
import org.apache.xerces.xni.Augmentations;
import org.apache.xerces.xni.QName;
import org.apache.xerces.xni.XMLAttributes;
import org.apache.xerces.xni.XMLResourceIdentifier;
import org.apache.xerces.xni.XNIException;
import org.apache.xerces.xni.parser.XMLEntityResolver;
import org.apache.xerces.xni.parser.XMLErrorHandler;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xni.parser.XMLParseException;
import org.apache.xerces.xni.parser.XMLParserConfiguration;

import java.io.IOException;
import java.io.Reader;
import java.io.StringReader;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author mrcheeks
 */
public class XMLOutlineDocumentParser {
    /** Default parser configuration (org.apache.xerces.parsers.XIncludeAwareParserConfiguration). */
    protected static final String DEFAULT_PARSER_CONFIG = "org.apache.xerces.parsers.XIncludeAwareParserConfiguration";
    protected static final String NAMESPACE_PREFIXES_FEATURE_ID = "http://xml.org/sax/features/namespace-prefixes";
    private org.apache.xerces.xni.XMLLocator m_locator;
    private java.util.Stack stack = new java.util.Stack();
    private DefaultMutableTreeNode current;
    private DefaultMutableTreeNode root;
    private XMLParserConfiguration parser;
    public String dtdLocation = null;
    private String schemaLocation = null;
    int counter = 0;

    public XMLOutlineDocumentParser() {
        root = new DefaultMutableTreeNode();
        this.current = root;

        try {
            parser = new XIncludeAwareParserConfiguration();
            parser.setErrorHandler(new XMLDocErrorHandler());

            parser.setEntityResolver(new XMLEntityResolver() {
                    public XMLInputSource resolveEntity(
                        XMLResourceIdentifier identifier)
                        throws XNIException, IOException {
                        dtdLocation = identifier.getExpandedSystemId();

                        XMLInputSource src = new XMLInputSource(null, null, null);
                        src.setCharacterStream(new StringReader(""));

                        return src;
                    }
                });
            parser.addRecognizedFeatures(new String[] {
                    NAMESPACE_PREFIXES_FEATURE_ID,
                });
            parser.setFeature("http://xml.org/sax/features/validation", false);
        } catch (Exception err) {
            //            err.printStackTrace();
        }
    }

    public String getDtdLocation() {
        return dtdLocation;
    }

    public String getSchemaLocation() {
        return schemaLocation;
    }

    public DefaultMutableTreeNode getRootNode() {
        return root;
    }

    public void parse(Reader m_reader) {
        try {
            XMLInputSource src = new XMLInputSource(null, null, null);
            src.setCharacterStream(m_reader);
            parser.setDocumentHandler(new XMLDocHandler());
            parser.parse(src);
            DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                    .setMessage("Done reading document structure");
        } catch (Exception e) {
            //            e.printStackTrace();
        }
    }

    private class XMLDocErrorHandler implements XMLErrorHandler {
        private void report(XMLParseException e) {
            // for now do nothing
        }

        public void warning(java.lang.String domain, java.lang.String key,
            XMLParseException e) throws XNIException {
            report(e);
        }

        public void error(java.lang.String domain, java.lang.String key,
            XMLParseException e) throws XNIException {
            report(e);
        }

        public void fatalError(java.lang.String domain, java.lang.String key,
            XMLParseException e) throws XNIException {
            report(e);
        }
    }

    private class XMLDocHandler extends XMLDocumentParser {
        public void startDocument(org.apache.xerces.xni.XMLLocator locator,
            java.lang.String encoding,
            org.apache.xerces.xni.NamespaceContext namespaceContext,
            org.apache.xerces.xni.Augmentations augs)
            throws org.apache.xerces.xni.XNIException {
            m_locator = locator;
        }

        public void findParserInfo(XMLAttributes attrs) {
            for (int i = 0; i < attrs.getLength(); i++) {
                String name = attrs.getQName(i);

                if (name.equals("xsi:schemaLocation") ||
                        name.equals("xsi:noNamespaceSchemaLocation")) {
                    schemaLocation = attrs.getValue(i);

                    break;
                }
            }
        }

        /** Start element. */
        public void startElement(QName element, XMLAttributes attrs,
            Augmentations augs) throws XNIException {
            if (counter == 0) {
                findParserInfo(attrs);
            }

            DefaultMutableTreeNode m_element = new TokenNode(element.rawname,
                    m_locator.getLineNumber(), m_locator.getColumnNumber());
            current.add(m_element);
            current = m_element;
            counter++;
        }

        public void startExternalSubset(
            org.apache.xerces.xni.XMLResourceIdentifier identifier,
            org.apache.xerces.xni.Augmentations augmentations)
            throws org.apache.xerces.xni.XNIException {
            //            System.out.println("startExternalSubset");
            //            System.out.println("identifier:" + identifier.getBaseSystemId());
            //            System.out.println("identifier:" +
            //                identifier.getExpandedSystemId());
            //            System.out.println("identifier:" + identifier.getPublicId());
            //            System.out.println("identifier:" + identifier.getLiteralSystemId());
            //            System.out.println("====================");
        }

        public void emptyElement(QName element, XMLAttributes attrs,
            Augmentations augs) throws XNIException {
            if (counter == 0) {
                findParserInfo(attrs);
            }

            DefaultMutableTreeNode m_element = new TokenNode(element.rawname,
                    m_locator.getLineNumber(), m_locator.getColumnNumber());
            current.add(m_element);
            current = m_element;
            current = (DefaultMutableTreeNode) current.getParent();
            counter++;
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
