/*
 * SAXTreeViewer.java
 *
 * Created on January 26, 2007, 5:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.view;


/**
 *
 * @author Yves Zoundi
 */
import java.awt.BorderLayout;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.text.BadLocationException;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

// This is an XML book - no need for explicit Swing imports
 
 
 

/**
 *
 * @author Yves Zoundi
 */
public class SAXTreeViewer extends JComponent {

    /** Default parser to use */
    private String vendorParserClass = 
        "org.apache.xerces.parsers.SAXParser";

    /** The base tree to render */
    private JTree jTree;

    /** Tree model to use */
    DefaultTreeModel defaultTreeModel;

    /**
     * <p> This initializes the needed Swing settings. </p>
     */
    public SAXTreeViewer() {
        
    }

    /**
     * <p> This will construct the tree using Swing. </p>
     *
     * @param filename <code>String</code> path to XML document.
     */
    public void init(javax.swing.text.Document xmlURI) throws IOException, SAXException {
        DefaultMutableTreeNode base = 
            new DefaultMutableTreeNode("Document");
        
        // Build the tree model
        defaultTreeModel = new DefaultTreeModel(base);
        jTree = new JTree(defaultTreeModel);

        // Construct the tree hierarchy
        buildTree(defaultTreeModel, base, xmlURI);

        // Display the results
         add(new JScrollPane(jTree), 
            BorderLayout.CENTER);
    }

    /**
     * <p>This handles building the Swing UI tree.</p>
     *
     * @param treeModel Swing component to build upon.
     * @param base tree node to build on.
     * @param xmlURI URI to build XML document from.
     * @throws <code>IOException</code> - when reading the XML URI fails.
     * @throws <code>SAXException</code> - when errors in parsing occur.
     */
    public void buildTree(DefaultTreeModel treeModel, 
                          DefaultMutableTreeNode base, javax.swing.text.Document textDocument) 
        throws IOException, SAXException {

        // Create instances needed for parsing
        XMLReader reader = 
            XMLReaderFactory.createXMLReader(vendorParserClass);
        ContentHandler jTreeContentHandler = 
            new JTreeContentHandler(treeModel, base);
        ErrorHandler jTreeErrorHandler = new JTreeErrorHandler();

        // Register content handler
        reader.setContentHandler(jTreeContentHandler);

        // Register error handler
        reader.setErrorHandler(jTreeErrorHandler);
        InputSource inputSource = null;
        try {
            inputSource = new InputSource(new StringReader(textDocument.getText(0, textDocument.getLength())));
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
        if(inputSource == null)
            return;
        reader.parse(inputSource);
    }
 
}

/**
 * <b><code>JTreeContentHandler</code></b> implements the SAX
 *   <code>ContentHandler</code> interface and defines callback
 *   behavior for the SAX callbacks associated with an XML
 *   document's content, bulding up JTree nodes.
 */
class JTreeContentHandler implements ContentHandler {

    /** Hold onto the locator for location information */
    private Locator locator;

    /** Store URI to prefix mappings */
    private Map namespaceMappings;

    /** Tree Model to add nodes to */
    private DefaultTreeModel treeModel;

    /** Current node to add sub-nodes to */
    private DefaultMutableTreeNode current;

    /**
     * <p> Set up for working with the JTree. </p>
     *
     * @param treeModel tree to add nodes to.
     * @param base node to start adding sub-nodes to.
     */
    public JTreeContentHandler(DefaultTreeModel treeModel, 
                               DefaultMutableTreeNode base) {
        this.treeModel = treeModel;
        this.current = base;
        this.namespaceMappings = new HashMap();
    }

    /**
     * <p>
     *  Provide reference to <code>Locator</code> which provides
     *    information about where in a document callbacks occur.
     * </p>
     *
     * @param locator <code>Locator</code> object tied to callback
     *        process
     */
    public void setDocumentLocator(Locator locator) {
        // Save this for later use
        this.locator = locator;
    }

    /**
     * <p>
     *  This indicates the start of a Document parse-this precedes
     *    all callbacks in all SAX Handlers with the sole exception
     *    of <code>{@link #setDocumentLocator}</code>.
     * </p>
     *
     * @throws <code>SAXException</code> when things go wrong
     */
    public void startDocument() throws SAXException {
        // No visual events occur here
    }

    /**
     * <p>
     *  This indicates the end of a Document parse-this occurs after
     *    all callbacks in all SAX Handlers.</code>.
     * </p>
     *
     * @throws <code>SAXException</code> when things go wrong
     */
    public void endDocument() throws SAXException {
        // No visual events occur here
    }

    /**
     * <p>
     *   This indicates that a processing instruction (other than
     *     the XML declaration) has been encountered.
     * </p>
     *
     * @param target <code>String</code> target of PI
     * @param data <code>String</code containing all data sent to the PI.
     *               This typically looks like one or more attribute value
     *               pairs.
     * @throws <code>SAXException</code> when things go wrong
     */
    public void processingInstruction(String target, String data)
        throws SAXException {

        DefaultMutableTreeNode pi = 
            new DefaultMutableTreeNode("PI (target = '" + target +
                                       "', data = '" + data + "')");
        current.add(pi);
    }

    /**
     * <p>
     *   This indicates the beginning of an XML Namespace prefix
     *     mapping. Although this typically occurs within the root element
     *     of an XML document, it can occur at any point within the
     *     document. Note that a prefix mapping on an element triggers
     *     this callback <i>before</i> the callback for the actual element
     *     itself (<code>{@link #startElement}</code>) occurs.
     * </p>
     *
     * @param prefix <code>String</code> prefix used for the namespace
     *                being reported
     * @param uri <code>String</code> URI for the namespace
     *               being reported
     * @throws <code>SAXException</code> when things go wrong
     */
    public void startPrefixMapping(String prefix, String uri) {
        // No visual events occur here.
        namespaceMappings.put(uri, prefix);
    }

    /**
     * <p>
     *   This indicates the end of a prefix mapping, when the namespace
     *     reported in a <code>{@link #startPrefixMapping}</code> callback
     *     is no longer available.
     * </p>
     *
     * @param prefix <code>String</code> of namespace being reported
     * @throws <code>SAXException</code> when things go wrong
     */
    public void endPrefixMapping(String prefix) {
        // No visual events occur here.
        for (Iterator i = namespaceMappings.keySet().iterator(); 
             i.hasNext(); ) {

            String uri = (String)i.next();
            String thisPrefix = (String)namespaceMappings.get(uri);
            if (prefix.equals(thisPrefix)) {
                namespaceMappings.remove(uri);
                break;
            }
        }
    }

    /**
     * <p>
     *   This reports the occurrence of an actual element. It includes
     *     the element's attributes, with the exception of XML vocabulary
     *     specific attributes, such as
     *     <code>xmlns:[namespace prefix]</code> and
     *     <code>xsi:schemaLocation</code>.
     * </p>
     *
     * @param namespaceURI <code>String</code> namespace URI this element
     *               is associated with, or an empty <code>String</code>
     * @param localName <code>String</code> name of element (with no
     *               namespace prefix, if one is present)
     * @param qName <code>String</code> XML 1.0 version of element name:
     *                [namespace prefix]:[localName]
     * @param atts <code>Attributes</code> list for this element
     * @throws <code>SAXException</code> when things go wrong
     */
    public void startElement(String namespaceURI, String localName,
                             String qName, Attributes atts)
        throws SAXException {

        DefaultMutableTreeNode element = 
            new DefaultMutableTreeNode("Element: "  + "info:(" + locator.getLineNumber() + "," + locator.getColumnNumber() + ")" + localName);
        current.add(element);
        current = element;

        // Determine namespace
        if (namespaceURI.length() > 0) {
            String prefix = 
                (String)namespaceMappings.get(namespaceURI);
            if (prefix.equals("")) {
                prefix = "[None]";
            }
            DefaultMutableTreeNode namespace =
                new DefaultMutableTreeNode("Namespace: prefix = '" +
                    prefix + "', URI = '" + namespaceURI + "'");
            current.add(namespace);
        }

        // Process attributes
        for (int i=0; i<atts.getLength(); i++) {
            DefaultMutableTreeNode attribute =
                new DefaultMutableTreeNode("Attribute (name = '" +
                                           atts.getLocalName(i) + 
                                           "', value = '" +
                                           atts.getValue(i) + "')");
            String attURI = atts.getURI(i);
            if (attURI.length() > 0) {
                String attPrefix = 
                    (String)namespaceMappings.get(namespaceURI);
                if (attPrefix.equals("")) {
                    attPrefix = "[None]";
                }
                DefaultMutableTreeNode attNamespace =
                    new DefaultMutableTreeNode("Namespace: prefix = '" +
                        attPrefix + "', URI = '" + attURI + "'");
                attribute.add(attNamespace);            
            }
            current.add(attribute);
        }
    }

    /**
     * <p>
     *   Indicates the end of an element
     *     (<code>&lt;/[element name]&gt;</code>) is reached. Note that
     *     the parser does not distinguish between empty
     *     elements and non-empty elements, so this occurs uniformly.
     * </p>
     *
     * @param namespaceURI <code>String</code> URI of namespace this
     *                element is associated with
     * @param localName <code>String</code> name of element without prefix
     * @param qName <code>String</code> name of element in XML 1.0 form
     * @throws <code>SAXException</code> when things go wrong
     */
    public void endElement(String namespaceURI, String localName,
                           String qName)
        throws SAXException {

        // Walk back up the tree
        current = (DefaultMutableTreeNode)current.getParent();
    }

    /**
     * <p>
     *   This reports character data (within an element).
     * </p>
     *
     * @param ch <code>char[]</code> character array with character data
     * @param start <code>int</code> index in array where data starts.
     * @param length <code>int</code> index in array where data ends.
     * @throws <code>SAXException</code> when things go wrong
     */
    public void characters(char[] ch, int start, int length)
        throws SAXException {

        String s = new String(ch, start, length);
        DefaultMutableTreeNode data =
            new DefaultMutableTreeNode("Character Data: '" + s + "'");
        current.add(data);
    }

    /**
     * <p>
     * This reports whitespace that can be ignored in the
     * originating document. This is typically invoked only when
     * validation is ocurring in the parsing process.
     * </p>
     *
     * @param ch <code>char[]</code> character array with character data
     * @param start <code>int</code> index in array where data starts.
     * @param end <code>int</code> index in array where data ends.
     * @throws <code>SAXException</code> when things go wrong
     */
    public void ignorableWhitespace(char[] ch, int start, int length)
        throws SAXException {
        
        // This is ignorable, so don't display it
    }

    /**
     * <p>
     *   This reports an entity that is skipped by the parser. This
     *     should only occur for non-validating parsers, and then is still
     *     implementation-dependent behavior.
     * </p>
     *
     * @param name <code>String</code> name of entity being skipped
     * @throws <code>SAXException</code> when things go wrong
     */
    public void skippedEntity(String name) throws SAXException {
        DefaultMutableTreeNode skipped =
            new DefaultMutableTreeNode("Skipped Entity: '" + name + "'");
        current.add(skipped);
    }
}

/**
 * <b><code>JTreeErrorHandler</code></b> implements the SAX
 *   <code>ErrorHandler</code> interface and defines callback
 *   behavior for the SAX callbacks associated with an XML
 *   document's warnings and errors.
 */
class JTreeErrorHandler implements ErrorHandler {

    /**
     * <p>
     * This will report a warning that has occurred; this indicates
     *   that while no XML rules were "broken", something appears
     *   to be incorrect or missing.
     * </p>
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws <code>SAXException</code> when things go wrong 
     */
    public void warning(SAXParseException exception)
        throws SAXException {
            
        System.out.println("**Parsing Warning**\n" +
                           "  Line:    " + 
                              exception.getLineNumber() + "\n" +
                           "  URI:     " + 
                              exception.getSystemId() + "\n" +
                           "  Message: " + 
                              exception.getMessage());        
        throw new SAXException("Warning encountered");
    }

    /**
     * <p>
     * This will report an error that has occurred; this indicates
     *   that a rule was broken, typically in validation, but that
     *   parsing can reasonably continue.
     * </p>
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws <code>SAXException</code> when things go wrong 
     */
    public void error(SAXParseException exception)
        throws SAXException {
        
        System.out.println("**Parsing Error**\n" +
                           "  Line:    " + 
                              exception.getLineNumber() + "\n" +
                           "  URI:     " + 
                              exception.getSystemId() + "\n" +
                           "  Message: " + 
                              exception.getMessage());
        throw new SAXException("Error encountered");
    }

    /**
     * <p>
     * This will report a fatal error that has occurred; this indicates
     *   that a rule has been broken that makes continued parsing either
     *   impossible or an almost certain waste of time.
     * </p>
     *
     * @param exception <code>SAXParseException</code> that occurred.
     * @throws <code>SAXException</code> when things go wrong 
     */
    public void fatalError(SAXParseException exception)
        throws SAXException {
    
        System.out.println("**Parsing Fatal Error**\n" +
                           "  Line:    " + 
                              exception.getLineNumber() + "\n" +
                           "  URI:     " + 
                              exception.getSystemId() + "\n" +
                           "  Message: " + 
                              exception.getMessage());        
        throw new SAXException("Fatal Error encountered");
    }
}

