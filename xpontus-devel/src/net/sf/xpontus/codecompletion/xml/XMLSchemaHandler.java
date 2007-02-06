/*
 * XMLSchemaHandler.java
 *
 * Created on January 25, 2007, 7:07 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.codecompletion.xml;

 


import org.apache.xerces.impl.xs.XSDDescription;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XSGrammar;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModel;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;

import org.xml.sax.SAXException;
import org.xml.sax.ext.DeclHandler;
import org.xml.sax.helpers.DefaultHandler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;


/**
 *
 * @author Yves Zoundi
 */
public class XMLSchemaHandler extends DefaultHandler implements DeclHandler
{
    private Map m_mappings;
    private XMLGrammarPoolImpl m_m_grammarPool;
    private HashMap m_m_activePrefixes = new HashMap();

    //{{{ SchemaHandler constructor
    public XMLSchemaHandler(XMLGrammarPoolImpl grammarPool, HashMap m_mappings)
    {
        this.m_mappings = m_mappings;
        m_m_grammarPool = grammarPool;
    } //}}}

    //{{{ endDocument()
    public void endDocument() throws SAXException
    {
        Grammar grammar = getGrammarForNamespace(null);

        if (grammar != null)
        {
            CompletionInfo info = grammarToCompletionInfo(grammar);

            if (info != null)
            {
                m_mappings.put("", info);
            }
        }
    } //}}}

    //{{{ startPrefixMapping()
    public void startPrefixMapping(String prefix, String uri)
    {
        m_m_activePrefixes.put(prefix, uri);
    } //}}}

    //{{{ endPrefixMapping()
    public void endPrefixMapping(String prefix)
    {
        String uri = (String) m_m_activePrefixes.get(prefix);

        // check for built-in completion info for this URI
        // (eg, XSL, XSD, XHTML has this).
        if (uri != null)
        {
            CompletionInfo info = CompletionInfo.getCompletionInfoForNamespace(uri);

            if (info != null)
            {
                m_mappings.put(prefix, info);

                return;
            }
        }

        Grammar grammar = getGrammarForNamespace(uri);

        if (grammar != null)
        {
            CompletionInfo info = grammarToCompletionInfo(grammar);

            if (info != null)
            {
                m_mappings.put(prefix, info);
            }
        }
    } //}}}

    //{{{ elementDecl()
    public void elementDecl(String name, String model)
    {
        ElementDecl element = CompletionInfo.getElementDecl(name);

        if (element == null)
        {
            CompletionInfo info = CompletionInfo.getNoNamespaceCompletionInfo();
            element = new ElementDecl(info, name, model);
            info.addElement(element);
        }
        else
        {
            element.setContent(model);
        }
    } //}}}

    //{{{ attributeDecl()
    public void attributeDecl(String eName, String aName, String type,
        String valueDefault, String value)
    {
        ElementDecl element = CompletionInfo.getElementDecl(eName);

        if (element == null)
        {
            CompletionInfo info = CompletionInfo.getNoNamespaceCompletionInfo();
            element = new ElementDecl(info, eName, null);
            info.addElement(element);
        }

        // as per the XML spec
        if (element.getAttribute(aName) != null)
        {
            return;
        }

        ArrayList values;

        if (type.startsWith("("))
        {
            values = new ArrayList();

            StringTokenizer st = new StringTokenizer(type.substring(1,
                        type.length() - 1), "|");

            while (st.hasMoreTokens())
            {
                values.add(st.nextToken());
            }
        }
        else
        {
            values = null;
        }

        boolean required = "#REQUIRED".equals(valueDefault);

        element.addAttribute(new AttributeDecl(aName, value,
                values, type, required));
    } //}}}

    //{{{ internalEntityDecl()
    public void internalEntityDecl(String name, String value)
    {
        // this is a bit of a hack
        if (name.startsWith("%"))
        {
            return;
        }

        CompletionInfo.getNoNamespaceCompletionInfo()
                      .addEntity(EntityDecl.INTERNAL, name, value);
    } //}}}

    //{{{ externalEntityDecl()
    public void externalEntityDecl(String name, String publicId, String systemId)
    {
        if (name.startsWith("%"))
        {
            return;
        }

        CompletionInfo.getNoNamespaceCompletionInfo()
                      .addEntity(EntityDecl.EXTERNAL, name, publicId, systemId);
    } //}}}

    //{{{ Private members

    //{{{ grammarToCompletionInfo()
    private CompletionInfo grammarToCompletionInfo(Grammar grammar)
    {
        if (!(grammar instanceof XSGrammar))
        {
            return null;
        }

        CompletionInfo info = new CompletionInfo();

        XSModel model = ((XSGrammar) grammar).toXSModel();

        XSNamedMap elements = model.getComponents(XSConstants.ELEMENT_DECLARATION);

        for (int i = 0; i < elements.getLength(); i++)
        {
            XSElementDeclaration element = (XSElementDeclaration) elements.item(i);
            CompletionInfo.xsElementToElementDecl(info, element, null);
        }

        XSNamedMap attributes = model.getComponents(XSConstants.ATTRIBUTE_DECLARATION);

        for (int i = 0; i < attributes.getLength(); i++)
        {
            XSObject attribute = attributes.item(i);

            //not sure what this means
            //TODO: attribute declarations seem not to be supported for Schema

            //  Log.log(Log.WARNING, this, "look! " + attribute);
            /* String name = element.getName();
            boolean empty = true;
            boolean any = true;
            List attributes = new ArrayList();
            Map attributeHash = new HashMap();
            Set content = new HashSet();
            info.addElement(new ElementDecl(info,name,empty,any,
                attributes,attributeHash,content)); */
        }

        return info;
    } //}}}

    //{{{ getGrammarForNamespace()
    private Grammar getGrammarForNamespace(String uri)
    {
        XSDDescription schemaDesc = new XSDDescription();
        schemaDesc.setTargetNamespace(uri);

        Grammar grammar = m_m_grammarPool.getGrammar(schemaDesc);

        return grammar;
    } //}}}

    //}}}
} //}}}
