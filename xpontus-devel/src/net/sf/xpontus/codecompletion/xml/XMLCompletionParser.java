/*
 * XMLCompletionParser.java
 *
 * Created on January 25, 2007, 6:52 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.codecompletion.xml;

import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.IOException;
import java.io.StringReader;

import java.net.URI;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Properties;

import javax.swing.text.Document;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoManager;

import javax.xml.parsers.ParserConfigurationException;


/**
 *
 * @author Yves Zoundi
 */
public class XMLCompletionParser
{
    private Document m_doc;

    /**
     * This flag is set to true if and only if the textual content held in
     * the ContentManager m_content is synced symantically with the DOM
     * and the AdapterNodes held in the tree structure.
     * This flag will be set to false when the tree or content are changed in
     * such a way that they become out of sync.
     */
    private boolean m_syncedWithContent = false;
    private ArrayList m_parseErrors = new ArrayList();
    private ArrayList m_parseFatalErrors = new ArrayList();

    // private ArrayList m_parseWarnings = new ArrayList();
    private EntityResolver m_entityResolver;
    private ArrayList listeners = new ArrayList();
    private Properties props = new Properties();
    private URI m_uri = null;
    private boolean m_formattedLastTime = false;

    /**
     * A namespace uri to CompletionInfo map used to hold completion info
     * for active namespaces
     */
    private HashMap m_mappings;
    private UndoManager m_undoManager = new UndoManager();
    private CompoundEdit m_compoundEdit = null;
    private boolean m_addedToCompoundEdits = false;
    private int m_compoundEditCount = 0;

    /** Creates a new instance of XMLCompletionParser */
    public XMLCompletionParser(Document m_doc, HashMap m_mappings)
    {
        this.m_doc = m_doc;
        this.m_mappings = m_mappings;
    }

    public void parseDocument()
        throws SAXParseException, SAXException, ParserConfigurationException,
            IOException
    {
        // Parse using SAXParser to get Completion Info
        parseWithoutUpdate();
    } //}}}

    //{{{ parseWithoutUpdate()
    /**
     * Parses the document without updating the DOM. This method does, however,
     * update completion info and parse errors.
     */
    public void parseWithoutUpdate()
    {
        //   Log.log(Log.MESSAGE, this, (m_uri != null ? "Validating Document: "+m_uri.toString() : "Validating Document"));

        //     m_parseErrors = new ArrayList();
        //    m_parseFatalErrors = new ArrayList();

        //      Boolean validating = Boolean.valueOf(getProperty(IS_VALIDATING));
        SymbolTable symbolTable = new SymbolTable();
        XMLGrammarPoolImpl grammarPool = new XMLGrammarPoolImpl();

        XMLSchemaHandler handler = new XMLSchemaHandler(grammarPool, m_mappings);

        org.apache.xerces.parsers.SAXParser reader = new org.apache.xerces.parsers.SAXParser(symbolTable,
                grammarPool);

        try
        {
            reader.setFeature("http://xml.org/sax/features/validation", false);
            reader.setFeature("http://apache.org/xml/features/validation/schema",
                false);
            //namespaces needs to be true for resolver to work
            reader.setFeature("http://xml.org/sax/features/namespaces", true);
            //            reader.setErrorHandler(new ParseErrorHandler());
            //            if (m_entityResolver != null) {
            //                reader.setEntityResolver(m_entityResolver);
            //            }
            reader.setContentHandler(handler);
            reader.setProperty("http://xml.org/sax/properties/declaration-handler",
                handler);
        }
        catch (SAXException se)
        {
            //  Log.log(Log.ERROR,this,se);
        }

        try
        {
            //Temporary fix to allow parsing of documnts with multi-byte characters
            // reader.parse(new InputSource(new ContentManagerInputStream(m_content)));
            String text = m_doc.getText(0, m_doc.getLength());
            reader.parse(new InputSource(new StringReader(text)));
        }
        catch (Exception se)
        {
            //validation errors
            //    Log.log(Log.WARNING,this,se.getMessage());
            //    m_parseErrors.add(se);
        }
    } //}}}
}
