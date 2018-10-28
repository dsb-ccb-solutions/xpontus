/*
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
 * @author Yves Zoundi
 */
public class AttributeInfo implements Comparable<AttributeInfo>
{
    public static final int NONE = 0;
    public static final int ALIGN = 1;
    public static final int VALIGN = 2;
    public static final int INPUT_TYPE = 3;
    public static final int CSS = 4;
    public static final int FILE = 5;
    private String attributeName;
    private boolean hasValue;
    private int attributeType;
    private boolean required = false;
    private List<String> values = new ArrayList<String>();

    /**
     * The constructor.
     *
     * @param attributeName attribute name
     * @param hasValue      this attribute has value or not
     */
    public AttributeInfo(String attributeName, boolean hasValue)
    {
        this(attributeName, hasValue, NONE);
    }

    /**
     * The constructor.
     *
     * @param attributeName attribute name
     * @param hasValue      this attribute has value or not
     * @param attributeType attribute type
     */
    public AttributeInfo(String attributeName, boolean hasValue,
        int attributeType)
    {
        this(attributeName, hasValue, attributeType, false);
    }

    /**
     * The constructor.
     *
     * @param attributeName attribute name
     * @param hasValue      this attribute has value or not
     * @param attributeType attribute type
     * @param required      this attribute is required or not
     */
    public AttributeInfo(String attributeName, boolean hasValue,
        int attributeType, boolean required)
    {
        this.attributeName = attributeName;
        this.hasValue = hasValue;
        this.attributeType = attributeType;
        this.required = required;
    }

    public int getAttributeType()
    {
        return this.attributeType;
    }

    public String getAttributeName()
    {
        return this.attributeName;
    }

    public boolean hasValue()
    {
        return this.hasValue;
    }

    public boolean isRequired()
    {
        return this.required;
    }

    public void addValue(String value)
    {
        this.values.add(value);
    }

    public String toString()
    {
        return this.attributeName;
    }

    public List<String> getValues()
    {
        return Collections.unmodifiableList(values);
    }

    public int compareTo(AttributeInfo attributeInfo)
    {
        return attributeName.compareTo(attributeInfo.getAttributeName());
    }
}
