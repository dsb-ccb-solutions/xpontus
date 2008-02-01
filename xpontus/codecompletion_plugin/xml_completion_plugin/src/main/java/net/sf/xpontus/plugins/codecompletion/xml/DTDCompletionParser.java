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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.exolab.castor.xml.dtd.Attribute;
import org.exolab.castor.xml.dtd.DTDdocument;
import org.exolab.castor.xml.dtd.Element;
import org.exolab.castor.xml.dtd.parser.DTDParser;
import org.exolab.castor.xml.dtd.parser.InputCharStream;

import java.io.InputStreamReader;
import java.io.Reader;

import java.net.URL;

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DTDCompletionParser implements ICompletionParser {
    private Log logger = LogFactory.getLog(DTDCompletionParser.class);
    private List tagList = new Vector();
    private Map nsTagListMap = new HashMap();

    /** Creates a new instance of DTDCompletionParser */
    public DTDCompletionParser() {
    }

    public void init(List tagList, Map nsTagListMap) {
        this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }

    protected TagInfo getTagInfo(String name) {
        TagInfo info = null;

        for (int i = 0; i < tagList.size(); i++) {
            info = (TagInfo) tagList.get(i);

            if (info.getTagName().equals(name)) {
                break;
            }
        }

        if (info == null) {
            info = new TagInfo(name, true);
            tagList.add(info);
        }

        return info;
    }

    public void updateCompletionInfo(String pubid, String uri, Reader in) {
        try {
            Reader reader = new InputStreamReader(new URL(uri).openStream());
            DTDParser parser = new DTDParser(new InputCharStream(reader));
            DTDdocument dtd = parser.Input();
            Enumeration<Element> elements = dtd.getElements();

            while (elements.hasMoreElements()) {
                Element element = elements.nextElement();
                String name = element.getName();
                boolean hasBody = element.isEmptyContent();
                TagInfo tagInfo = new TagInfo(name, hasBody);

                Enumeration<Attribute> attributes = element.getAttributes();

                while (attributes.hasMoreElements()) {
                    Attribute attribute = attributes.nextElement();
                    boolean required = attribute.isREQUIRED();
                    String attrName = attribute.getName();
                    AttributeInfo attrInfo = new AttributeInfo(attrName, true,
                            AttributeInfo.NONE, required);

                    Iterator it = attribute.getValues();

                    while (it.hasNext()) {
                        attrInfo.addValue(it.next().toString());
                    }

                    tagInfo.addAttributeInfo(attrInfo);
                }

                tagList.add(tagInfo);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
