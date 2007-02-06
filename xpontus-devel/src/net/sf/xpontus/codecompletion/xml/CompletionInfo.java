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
package net.sf.xpontus.codecompletion.xml;

  
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
        //1999 XML Schema
        completionInfoNamespaces.put("http://www.w3.org/1999/XMLSchema",
            "/net/sf/xpontus/codecompletion/xsd-complete.xml");
        //2001 XML Schema
        completionInfoNamespaces.put("http://www.w3.org/2001/XMLSchema",
            "/net/sf/xpontus/codecompletion/xsd-complete.xml"); 
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
        }
        catch (Exception e)
        { 
        }

        info = handler.getCompletionInfo();
        completionInfoResources.put(resource, info);

        return info;
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

                elementDecl.addAttribute(new AttributeDecl(
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
 
    
}
