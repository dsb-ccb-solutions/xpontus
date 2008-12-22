/*
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
package net.sf.xpontus.plugins.codecompletion.xml;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class TagInfo implements Comparable<TagInfo>
{
    public static final int NONE = 0;
    public static final int EVENT = 1;
    public static final int FORM = 2;
    private String tagName;
    private boolean hasBody;
    private List<AttributeInfo> attributes = new ArrayList<AttributeInfo>();
    private List<String> children = new ArrayList<String>();

    
    /**
     * @param tagName
     * @param hasBody
     */
    public TagInfo(String tagName, boolean hasBody)
    {
        this.tagName = tagName;
        this.hasBody = hasBody;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return tagName;
    }

     
    /**
     * @return
     */
    public String getTagName()
    {
        return this.tagName;
    }

    
    /**
     * @return
     */
    public boolean hasBody()
    {
        return this.hasBody;
    }

     
    /**
     * @param attribute
     */
    public void addAttributeInfo(AttributeInfo attribute)
    {
        int i = 0;

        for (; i < attributes.size(); i++)
        {
            AttributeInfo info = (AttributeInfo) attributes.get(i);

            if (info.getAttributeName().compareTo(attribute.getAttributeName()) > 0)
            {
                break;
            }
        }

        this.attributes.add(i, attribute);
    }

    /**
     * @return
     */
    public List<AttributeInfo> getAttributeInfo()
    {
        return Collections.unmodifiableList(attributes);
    }

    /**
     * @return
     */
    public List<AttributeInfo> getRequiredAttributeInfo()
    {
        List<AttributeInfo> requiredAttributesList = new ArrayList<AttributeInfo>();

        for (AttributeInfo attributeInfo : attributes)
        {
            if (attributeInfo.isRequired())
            {
                requiredAttributesList.add(attributeInfo);
            }
        }

        return requiredAttributesList;
    }

    /**
     * @param name
     * @return
     */
    public AttributeInfo getAttributeInfo(String name)
    {
        for (AttributeInfo info : attributes)
        {
            if (info.getAttributeName().equals(name))
            {
                return info;
            }
        }

        return null;
    }

    /**
     * @param name
     */
    public void addChildTagName(String name)
    {
        children.add(name);
    }

    /**
     * @return
     */
    public List<String> getChildTagNames()
    {
        return Collections.unmodifiableList(children);
    }

    /* (non-Javadoc)
     * @see java.lang.Comparable#compareTo(java.lang.Object)
     */
    public int compareTo(TagInfo tagInfo)
    {
        return tagName.compareTo(tagInfo.getTagName());
    }
}
