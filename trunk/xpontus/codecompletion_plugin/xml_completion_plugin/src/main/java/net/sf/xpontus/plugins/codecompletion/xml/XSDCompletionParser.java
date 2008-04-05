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
package net.sf.xpontus.plugins.codecompletion.xml;

import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dynvocation.lib.xsd4j.XSDAttribute;
import org.dynvocation.lib.xsd4j.XSDElement;
import org.dynvocation.lib.xsd4j.XSDParser;
import org.dynvocation.lib.xsd4j.XSDSchema;
import org.dynvocation.lib.xsd4j.XSDSequence;
import org.dynvocation.lib.xsd4j.XSDType;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XSDCompletionParser implements ICompletionParser
{
    private Log logger = LogFactory.getLog(XSDCompletionParser.class);
    private List tagList = new Vector();
    private Map nsTagListMap = new HashMap();

    /** Creates a new instance of XSDCompletionParser */
    public XSDCompletionParser()
    {
    }

    public void init(List tagList, Map nsTagListMap)
    {
        this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }

    /**
     *
     * @param pubid
     * @param uri
     * @param in
     */
    public void updateCompletionInfo(String pubid, String uri, Reader in)
    {
        try
        {
            XSDSchema schema = new XSDParser().parseSchemaFile(uri,
                    XSDParser.PARSER_FLAT);

            List<XSDElement> elements = schema.getElements();

            // clear at first
            String targetNS = schema.getTargetNamespace();

            if (targetNS == null)
            {
                targetNS = "DEFAULT_PREFIX_MAPPING";
            }

            List m_list = new ArrayList();

            nsTagListMap.put(targetNS, m_list);

            List<XSDElement> topElements = schema.getElements();

            traverse(m_list, elements, null);
        }
        catch (Exception err)
        {
        }
    }

    public static void traverse(List tags, List<XSDElement> elements,
        TagInfo parent)
    {
        if (elements == null)
        {
            return;
        }

        for (Object o : elements)
        {
            XSDElement element = (XSDElement) o;

            String tagName = element.getName().getLocalPart();
            TagInfo tag = new TagInfo(tagName, true);

            if (!tags.contains(tag))
            {
                tags.add(tag);
                System.out.println("Adding tag:" + tagName);
            }

            if (parent != null)
            {
                parent.addChildTagName(tagName);
            } 

            XSDType m_type = element.getType();

            if (m_type != null)
            {
                if (m_type.getAttributes() != null)
                {
                    List<XSDAttribute> attributes = m_type.getAttributes();
                    traverseAttributes(tag, attributes);
                }

                XSDSequence m_sequence = m_type.getSequence();

                if (m_sequence != null)
                {
                    System.out.println("recursion....");
                    traverse(tags, m_sequence.getElements(), tag);
                }
            }
        }
    }

    public static void traverseAttributes(TagInfo tagInfo,
        List<XSDAttribute> attributes)
    {
        if (attributes == null)
        {
            return;
        }

        for (XSDAttribute attribute : attributes)
        {
            String m_name = attribute.getName().getLocalPart();
            tagInfo.addAttributeInfo(new AttributeInfo(m_name, true));
        }
    }
}
