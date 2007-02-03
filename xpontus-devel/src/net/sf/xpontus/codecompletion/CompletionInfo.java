/*
CompletionInfo.java
:tabSize=4:indentSize=4:noTabs=true:
:folding=explicit:collapseFolds=1:

Copyright (C) 2001, 2003 Slava Pestov
Portions Copyright (C) 2002 Ian Lewis (IanLewis@member.fsf.org)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
Optionally, you may find a copy of the GNU General Public License
from http://www.fsf.org/copyleft/gpl.txt
*/
package net.sf.xpontus.codecompletion;

import net.sf.xpontus.utils.MiscUtilities;

import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.xs.StringList;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSWildcard;

import org.xml.sax.*;
import org.xml.sax.helpers.DefaultHandler;

//{{{ Imports
//import gnu.regexp.*;
import java.util.*;


//}}}

/**
 * Encapsulates information about an XML document structure obtained
 * from a DTD or Schema document.
 * @author Slava Pestov
 * @author Ian Lewis (<a href="mailto:IanLewis@member.fsf.org">IanLewis@member.fsf.org</a>)
 * @since jsXe 0.4 pre1
 * @version $Id: CompletionInfo.java 840 2006-04-09 00:33:07Z ian_lewis $
 */
public class CompletionInfo
{
    private static Map m_mappings = new HashMap();
    private static HashMap completionInfoResources = new HashMap();
    private static HashMap completionInfoNamespaces = new HashMap();

    static
    {
        //TODO: Add built-in completion support for ant build files.
        /*
         TODO: Create a better way to support different types of XML documents
               and allow support and completion info for other document types
               to be added via plugins.
        */

        //1999 XML Schema
        completionInfoNamespaces.put("http://www.w3.org/1999/XMLSchema",
            "/net/sourceforge/jsxe/dom/completion/xsd-complete.xml");
        //2001 XML Schema
        completionInfoNamespaces.put("http://www.w3.org/2001/XMLSchema",
            "/net/sourceforge/jsxe/dom/completion/xsd-complete.xml");
        //XSLT
        completionInfoNamespaces.put("http://www.w3.org/1999/XSL/Transform",
            "/net/sourceforge/jsxe/dom/completion/xsl-complete.xml");
        //HTML + XHTML
        completionInfoNamespaces.put("http://www.w3.org/TR/xhtml1/transitional",
            "/net/sourceforge/jsxe/dom/completion/html-complete.xml");
        completionInfoNamespaces.put("http://www.w3.org/1999/xhtml",
            "/net/sourceforge/jsxe/dom/completion/html-complete.xml");
        //XSL:FO
        completionInfoNamespaces.put("http://www.w3.org/1999/XSL/Format",
            "/net/sourceforge/jsxe/dom/completion/fo-complete.xml");
    } //}}}

    protected ArrayList elements;
    private HashMap elementHash;
    protected ArrayList entities;
    private HashMap entityHash;
    protected ArrayList elementsAllowedAnywhere;

    //{{{ CompletionInfo constructor
    public CompletionInfo()
    {
        this(new ArrayList(), new HashMap(), new ArrayList(), new HashMap(),
            new ArrayList());

        addEntity(EntityDecl.INTERNAL, "lt", "<");
        addEntity(EntityDecl.INTERNAL, "gt", ">");
        addEntity(EntityDecl.INTERNAL, "amp", "&");
        addEntity(EntityDecl.INTERNAL, "quot", "\"");
        addEntity(EntityDecl.INTERNAL, "apos", "'");
    } //}}}

    //{{{ CompletionInfo constructor
    public CompletionInfo(ArrayList elements, HashMap elementHash,
        ArrayList entities, HashMap entityHash,
        ArrayList elementsAllowedAnywhere)
    {
        this.elements = elements;
        this.elementHash = elementHash;
        this.entities = entities;
        this.entityHash = entityHash;
        this.elementsAllowedAnywhere = elementsAllowedAnywhere;
    } //}}}

    //{{{ addEntity() method
    public void addEntity(int type, String name, String value)
    {
        addEntity(new EntityDecl(type, name, value));
    } //}}}

    //{{{ addEntity() method
    public void addEntity(int type, String name, String publicId,
        String systemId)
    {
        addEntity(new EntityDecl(type, name, publicId, systemId));
    } //}}}

    //{{{ addEntity() method
    public void addEntity(EntityDecl entity)
    {
        entities.add(entity);

        if ((entity.type == EntityDecl.INTERNAL) &&
                (entity.value.length() == 1))
        {
            Character ch = new Character(entity.value.charAt(0));
            entityHash.put(entity.name, ch);
            entityHash.put(ch, entity.name);
        }
    } //}}}

    //{{{ getEntity()
    /**
     * Gets an entity with the given Name
     * @param name the name of the entity
     */
    public EntityDecl getEntity(String name)
    {
        Iterator itr = entities.iterator();

        while (itr.hasNext())
        {
            EntityDecl decl = (EntityDecl) itr.next();

            if (decl.name.equals(name))
            {
                return decl;
            }
        }

        return null;
    } //}}}

    //{{{ getEntities()
    /**
     * Gets the entities for this completion info
     * @return a list of EntityDecl objects
     */
    public List getEntities()
    {
        return entities;
    } //}}}

    //{{{ getEntityHash()
    /**
     * Gets a map containing entity name to character and character to
     * entity name mappings.
     */
    public Map getEntityHash()
    {
        return entityHash;
    } //}}}

    //{{{ addElement() method
    public void addElement(ElementDecl element)
    {
        elementHash.put(element.name, element);
        elements.add(element);
    } //}}}

    //{{{ getElement()
    /**
     * Gets the element declaration for the element with the given
     * local name.
     */
    public ElementDecl getElement(String localName)
    {
        return (ElementDecl) elementHash.get(localName);
    } //}}}

    //{{{ getAllElements() method
    public void getAllElements(String prefix, List out)
    {
        for (int i = 0; i < elements.size(); i++)
        {
            out.add(((ElementDecl) elements.get(i)).withPrefix(prefix));
        }
    } //}}}

    //{{{ toString() method
    public String toString()
    {
        StringBuffer buf = new StringBuffer();

        buf.append("<element-list>\n\n");

        for (int i = 0; i < elements.size(); i++)
        {
            buf.append(elements.get(i));
            buf.append('\n');
        }

        buf.append("\n</element-list>\n\n<entity-list>\n\n");

        buf.append("<!-- not implemented yet -->\n");
        /* for(int i = 0; i < entities.size(); i++)
        {
                buf.append(entities.get(i));
                buf.append('\n');
        } */
        buf.append("\n</entity-list>");

        return buf.toString();
    } //}}}

    //{{{ getCompletionInfoForNamespace() method
    public static CompletionInfo getCompletionInfoForNamespace(String namespace)
    {
        Object obj = completionInfoNamespaces.get(namespace);

        if (obj instanceof String)
        {
            CompletionInfo info = getCompletionInfoFromResource((String) obj);
            completionInfoNamespaces.put(namespace, info);

            return info;
        }
        else
        {
            return (CompletionInfo) obj;
        }
    } //}}}

    //{{{ getCompletionInfoFromResource() method
    public static CompletionInfo getCompletionInfoFromResource(String resource)
    {
        CompletionInfo info = (CompletionInfo) completionInfoResources.get(resource);

        if (info != null)
        {
            return info;
        }

        // Log.log(Log.NOTICE,CompletionInfo.class,"Loading " + resource);
        CompletionInfoHandler handler = new CompletionInfoHandler();

        try
        {
            XMLReader parser = new org.apache.xerces.parsers.SAXParser();
            parser.setFeature("http://apache.org/xml/features/validation/dynamic",
                false);
            parser.setFeature("http://xml.org/sax/features/validation", false);
            parser.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd",
                false);
            parser.setErrorHandler(handler);
            parser.setEntityResolver(handler);
            parser.setContentHandler(handler);

            java.net.URL doc = null; // jsXe.class.getResource(resource);
            InputSource source = new InputSource(doc.openStream());
            source.setSystemId(doc.toString());

            parser.parse(source);
        }
        catch (SAXException se)
        {
            Throwable e = se.getException();

            if (e == null)
            {
                e = se;
            }

            // Log.log(Log.ERROR,CompletionInfo.class,e);
        }
        catch (Exception e)
        {
            //            Log.log(Log.ERROR,CompletionInfo.class,e);
        }

        info = handler.getCompletionInfo();
        completionInfoResources.put(resource, info);

        return info;
    } //}}}

    //{{{ clone() method
    //marked final since this violates standard contract for clone()
    public final Object clone()
    {
        return new CompletionInfo((ArrayList) elements.clone(),
            (HashMap) elementHash.clone(), (ArrayList) entities.clone(),
            (HashMap) entityHash.clone(),
            (ArrayList) elementsAllowedAnywhere.clone());
    } //}}}

    public static ElementDecl getElementDecl(String name)
    {
        String prefix = MiscUtilities.getNSPrefixFromQualifiedName(name);

        if (prefix == null)
        {
            prefix = "";
        }

        CompletionInfo info = (CompletionInfo) m_mappings.get(prefix);

        if (info == null)
        {
            return null;
        }
        else
        {
            String lName = MiscUtilities.getLocalNameFromQualifiedName(name);
            ElementDecl decl = info.getElement(lName);

            if (decl == null)
            {
                return null;
            }
            else
            {
                return decl.withPrefix(prefix);
            }
        }
    } //}}}

    private static void xsTermToElementDecl(CompletionInfo info, XSTerm term,
        ElementDecl parent)
    {
        if (term instanceof XSElementDeclaration)
        {
            xsElementToElementDecl(info, (XSElementDeclaration) term, parent);
        }
        else
        {
            if (term instanceof XSModelGroup)
            {
                XSObjectList content = ((XSModelGroup) term).getParticles();

                for (int i = 0; i < content.getLength(); i++)
                {
                    XSTerm childTerm = ((XSParticleDecl) content.item(i)).getTerm();
                    xsTermToElementDecl(info, childTerm, parent);
                }
            }
        }
    }

    public static void xsElementToElementDecl(CompletionInfo info,
        XSElementDeclaration element, ElementDecl parent)
    {
        String name = element.getName();

        if (parent != null)
        {
            if (parent.content == null)
            {
                parent.content = new HashSet();
            }

            parent.content.add(name);
        }

        if (info.getElement(name) != null)
        {
            return;
        }

        ElementDecl elementDecl = new ElementDecl(info, name, null);
        info.addElement(elementDecl);

        XSTypeDefinition typedef = element.getTypeDefinition();

        if (typedef.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE)
        {
            XSComplexTypeDefinition complex = (XSComplexTypeDefinition) typedef;

            XSParticle particle = complex.getParticle();

            if (particle != null)
            {
                XSTerm particleTerm = particle.getTerm();

                if (particleTerm instanceof XSWildcard)
                {
                    elementDecl.any = true;
                }
                else
                {
                    xsTermToElementDecl(info, particleTerm, elementDecl);
                }
            }

            XSObjectList attributes = complex.getAttributeUses();

            for (int i = 0; i < attributes.getLength(); i++)
            {
                XSAttributeUse attr = (XSAttributeUse) attributes.item(i);
                boolean required = attr.getRequired();
                XSAttributeDeclaration decl = attr.getAttrDeclaration();
                String attrName = decl.getName();
                String value = decl.getConstraintValue();

                // TODO: possible values
                String type = decl.getTypeDefinition().getName();

                if (type == null)
                {
                    type = "CDATA";
                }

                StringList enumList = decl.getTypeDefinition()
                                          .getLexicalEnumeration();
                ArrayList values = null;

                if (enumList.getLength() > 0)
                {
                    values = new ArrayList();

                    for (int j = 0; j < enumList.getLength(); j++)
                    {
                        values.add(enumList.item(j));
                    }
                }

                elementDecl.addAttribute(new ElementDecl.AttributeDecl(
                        attrName, value, values, type, required));
            }
        }
    } //}}}

    //{{{ getNoNamespaceCompletionInfo() method
    /**
     * Gets the completion info for the null namespace.
     * @since jsXe 0.4 pre1
     */
    public static CompletionInfo getNoNamespaceCompletionInfo()
    {
        CompletionInfo info = (CompletionInfo) m_mappings.get("");

        if (info == null)
        {
            info = new CompletionInfo();
            m_mappings.put("", info);
        }

        return info;
    } //}}}

    //{{{ CompletionInfoHandler class
    /**
     * CompletionInfoHandler is used to create CompletionInfo objects from
     * stored XML documents. These are used to add built in completion info
     * for specific document types.
     * @since jsXe 0.4 pre4
     * @author Slava Pestov
     * @author Ian Lewis (<a href="mailto:IanLewis@member.fsf.org">IanLewis@member.fsf.org</a>)
     * @version $Id: CompletionInfo.java 840 2006-04-09 00:33:07Z ian_lewis $
     */
    private static class CompletionInfoHandler extends DefaultHandler
    {
        //{{{ Private Members
        private CompletionInfo m_m_info;
        private Locator m_m_loc;
        private ElementDecl m_m_element;

        //{{{ CompletionInfoHandler constructor
        public CompletionInfoHandler()
        {
            m_m_info = new CompletionInfo();
            m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "lt", "<"));
            m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "gt", ">"));
            m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "amp", "&"));
            m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "quot", "\""));
            m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "apos", "'"));
        } //}}}

        //{{{ getCompletionInfo() method
        public CompletionInfo getCompletionInfo()
        {
            return m_m_info;
        } //}}}

        //{{{ setDocumentLocator() method
        public void setDocumentLocator(Locator loc)
        {
            m_m_loc = loc;
        } //}}}

        //{{{ resolveEntity() method
        /*
        public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
            try {
                return CatalogManager.resolve(loc.getSystemId(),publicId,systemId);
            } catch(Exception e) {
                throw new SAXException(e);
            }
        }*/

        //}}}

        //{{{ startElement() method
        public void startElement(String namespaceURI, String sName, // simple name
            String qName, // qualified name
            Attributes attrs) throws SAXException
        {
            if (sName.equals("dtd"))
            {
                /*String extend = attrs.getValue("extend");
                
                if (extend != null) {
                    String infoURI = jsXe.getProperty("mode."+extend+".xml.completion-info");
                    if (infoURI != null) {
                        CompletionInfo extendInfo = CompletionInfo.getCompletionInfoFromResource(infoURI);
                        if (extendInfo != null)
                            m_m_completionInfo = (CompletionInfo)extendInfo.clone();
                    }
                }*/
            }
            else if (sName.equals("entity"))
            {
                m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL,
                        attrs.getValue("name"), attrs.getValue("value")));
            }
            else if (sName.equals("element"))
            {
                m_m_element = new ElementDecl(m_m_info, attrs.getValue("name"),
                        attrs.getValue("content"));

                m_m_info.addElement(m_m_element);

                if ("true".equals(attrs.getValue("anywhere")))
                {
                    m_m_info.elementsAllowedAnywhere.add(m_m_element);
                }
            }
            else if (sName.equals("attribute"))
            {
                String name = attrs.getValue("name");
                String value = attrs.getValue("value");
                String type = attrs.getValue("type");

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

                boolean required = "true".equals(attrs.getValue("required"));

                m_m_element.addAttribute(new ElementDecl.AttributeDecl(name,
                        value, values, type, required));
            }
        } //}}}

        //}}}
    } //}}}
}
