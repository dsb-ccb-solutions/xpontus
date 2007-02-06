/*
ElementDecl.java
:tabSize=4:indentSize=4:noTabs=true:
:folding=explicit:collapseFolds=1:

Copyright (C) 2001 Slava Pestov
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

import java.util.*;


public class ElementDecl
{
    public CompletionInfo completionInfo;
    public String name;
    public boolean empty;
    public boolean any;
    private List attributes;
    private Map attributeHash;
    public Set content;

    //{{{ ElementDecl constructor
    public ElementDecl(CompletionInfo completionInfo, String name,
        String content)
    {
        this.completionInfo = completionInfo;

        this.name = name;

        if (content != null)
        {
            setContent(content);
        }

        attributes = new ArrayList();
        attributeHash = new HashMap();
    } //}}}

    //{{{ ElementDecl constructor
    public ElementDecl(CompletionInfo completionInfo, String name,
        boolean empty, boolean any, List attributes, Map attributeHash,
        Set content)
    {
        this.completionInfo = completionInfo;
        this.name = name;
        this.empty = empty;
        this.any = any;
        this.attributes = attributes;
        this.attributeHash = attributeHash;
        this.content = content;
    } //}}}

    //{{{ setContent()
    public void setContent(String content)
    {
        if (content.equals("EMPTY"))
        {
            empty = true;
        }
        else
        {
            if (content.equals("ANY"))
            {
                any = true;
            }
            else
            {
                this.content = new HashSet();

                StringTokenizer st = new StringTokenizer(content, "(?*+|,) \t\n");

                while (st.hasMoreTokens())
                {
                    String element = st.nextToken();

                    if (element.equals("#PCDATA"))
                    {
                        continue;
                    }

                    this.content.add(element);
                }
            }
        }
    } //}}}

    //{{{ withPrefix()
    public ElementDecl withPrefix(String prefix)
    {
        if ((prefix == null) || prefix.equals(""))
        {
            return this;
        }
        else
        {
            return new ElementDecl(completionInfo, prefix + ':' + name, empty,
                any, attributes, attributeHash, content);
        }
    } //}}}

    //{{{ getChildElements()
    public List getChildElements(String prefix)
    {
        ArrayList children = new ArrayList(100);

        if (any)
        {
            for (int i = 0; i < completionInfo.elements.size(); i++)
            {
                children.add(((ElementDecl) completionInfo.elements.get(i)).withPrefix(
                        prefix));
            }
        }
        else
        {
            for (int i = 0; i < completionInfo.elementsAllowedAnywhere.size();
                    i++)
            {
                children.add(((ElementDecl) completionInfo.elementsAllowedAnywhere.get(
                        i)).withPrefix(prefix));
            }

            if (content != null)
            {
                Iterator iter = content.iterator();

                while (iter.hasNext())
                {
                    ElementDecl decl = completionInfo.getElement(iter.next()
                                                                     .toString());

                    if (decl != null)
                    {
                        children.add(decl.withPrefix(prefix));
                    }
                }
            }
        }

        return children;
    } //}}}

    //{{{ getAttribute()
    public AttributeDecl getAttribute(String name)
    {
        return (AttributeDecl) attributeHash.get(name);
    } //}}}

    //{{{ addAttribute()
    public void addAttribute(AttributeDecl attribute)
    {
        attributeHash.put(attribute.name, attribute);

        for (int i = 0; i < attributes.size(); i++)
        {
            AttributeDecl attr = (AttributeDecl) attributes.get(i);

            if (attr.name.compareTo(attribute.name) > 0)
            {
                attributes.add(i, attribute);

                return;
            }
        }

        attributes.add(attribute);
    } //}}}

    //{{{ getAttributes()
    /**
     * Gets all the attribute declarations for this element
     * @return a list of AttributeDecl objects
     */
    public List getAttributes()
    {
        return attributes;
    } //}}}

    //{{{ getRequiredAttributesString()
    public String getRequiredAttributesString()
    {
        StringBuffer buf = new StringBuffer();

        for (int i = 0; i < attributes.size(); i++)
        {
            AttributeDecl attr = (AttributeDecl) attributes.get(i);

            if (attr.required)
            {
                buf.append(' ');
                buf.append(attr.name);
                buf.append("=\"");

                if (attr.value != null)
                {
                    buf.append(attr.value);
                }

                buf.append('"');
            }
        }

        return buf.toString();
    } //}}}

    //{{{ toString() method
    public String toString()
    {
        StringBuffer buf = new StringBuffer();
        buf.append("<element name=\"");
        buf.append(name);
        buf.append('"');

        buf.append("\ncontent=\"");

        if (empty)
        {
            buf.append("EMPTY");
        }
        else
        {
            if (content != null)
            {
                buf.append('(');

                Iterator iter = content.iterator();

                while (iter.hasNext())
                {
                    buf.append(iter.next());

                    if (iter.hasNext())
                    {
                        buf.append('|');
                    }
                }

                buf.append(')');
            }
        }

        buf.append('"');

        if (attributes.size() == 0)
        {
            buf.append(" />");
        }
        else
        {
            buf.append(">\n");

            for (int i = 0; i < attributes.size(); i++)
            {
                buf.append(attributes.get(i));
                buf.append('\n');
            }

            buf.append("</element>");
        }

        return buf.toString();
    } //}}}

    //{{{ AttributeDecl class
    

    //{{{ Compare class
    public static class Compare implements Comparator
    {
        public int compare(Object obj1, Object obj2)
        {
            return MiscUtilities.compareStrings(((ElementDecl) obj1).name,
                ((ElementDecl) obj2).name, true);
        }
    } //}}}
}
