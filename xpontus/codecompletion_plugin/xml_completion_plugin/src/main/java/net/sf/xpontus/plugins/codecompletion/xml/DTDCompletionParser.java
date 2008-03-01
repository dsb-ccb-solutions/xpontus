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

import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDAttribute;
import com.wutka.dtd.DTDChoice;
import com.wutka.dtd.DTDDecl;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDEmpty;
import com.wutka.dtd.DTDEnumeration;
import com.wutka.dtd.DTDItem;
import com.wutka.dtd.DTDMixed;
import com.wutka.dtd.DTDName;
import com.wutka.dtd.DTDParser;
import com.wutka.dtd.DTDSequence;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.utils.CompletionUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Reader;
import java.io.Writer;

import java.net.URL;

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

    /**
     *
     * @param tagList
     * @param nsTagListMap
     */
    public void init(List tagList, Map nsTagListMap) {
        this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }

    /**
     *
     * @param name
     * @return
     */
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

    /**
     *
     * @param pubid
     * @param uri
     * @param in
     */
    public void updateCompletionInfo(String pubid, String uri, Reader in) {
        try {
            URL parserURL = null;

            if (new File(uri).exists()) {
                parserURL = new File(uri).toURL();
            } else {
                parserURL = new URL(uri);
            }

            DTDParser parser = null;

            String cacheURL = CompletionUtils.getFileCompletionCache(uri);

            if (cacheURL != null) {
                System.out.println("Using cache for DTD completion parsing...");
                parserURL = new File(cacheURL).toURL();
            }

            parser = new DTDParser(parserURL);

            DTD dtd = parser.parse();

            if (cacheURL == null) {
                try {
                    System.out.println("Caching completion to cache");

                    String str = CachingUtils.getCachedURL(uri);
                    File cacheFile = new File(XPontusConfigurationConstantsIF.GRAMMAR_CACHING_DIR,
                            str);
                    System.out.println("cacheFile:" +
                        cacheFile.getAbsolutePath());

                    Writer m_writer = new OutputStreamWriter(FileUtils.openOutputStream(
                                cacheFile));
                    PrintWriter pw = new PrintWriter(m_writer);
                    dtd.write(pw);
                    IOUtils.closeQuietly(pw);
                    IOUtils.closeQuietly(m_writer);
                    CompletionUtils.addCompletionInfo(uri,
                        cacheFile.getAbsolutePath());
                } catch (Exception cachingException) {
                    cachingException.printStackTrace();
                }
            } else {
                System.out.println("Using code completion from cache...");
            }

            Object[] obj = dtd.getItems();

            System.out.println("nb:items:" + obj.length);

            for (int i = 0; i < obj.length; i++) {
                if (obj[i] instanceof DTDElement) {
                    DTDElement element = (DTDElement) obj[i];
                    String name = element.getName();
                    DTDItem item = element.getContent();
                    boolean hasBody = true;

                    if (item instanceof DTDEmpty) {
                        hasBody = false;
                    }

                    TagInfo tagInfo = new TagInfo(name, hasBody);
                    Iterator ite = element.attributes.keySet().iterator();

                    // set child tags
                    if (item instanceof DTDSequence) {
                        DTDSequence seq = (DTDSequence) item;
                        setChildTagName(tagInfo, seq.getItem());
                    } else if (item instanceof DTDMixed) {
                        // #PCDATA
                    }

                    while (ite.hasNext()) {
                        String attrName = (String) ite.next();
                        DTDAttribute attr = element.getAttribute(attrName);

                        DTDDecl decl = attr.getDecl();
                        boolean required = false;

                        if (decl == DTDDecl.REQUIRED) {
                            required = true;
                        }

                        AttributeInfo attrInfo = new AttributeInfo(attrName,
                                true, AttributeInfo.NONE, required);
                        tagInfo.addAttributeInfo(attrInfo);

                        Object attrType = attr.getType();

                        if (attrType instanceof DTDEnumeration) {
                            DTDEnumeration dtdEnum = (DTDEnumeration) attrType;
                            String[] items = dtdEnum.getItems();

                            for (int j = 0; j < items.length; j++) {
                                attrInfo.addValue(items[j]);
                            }
                        }
                    }

                    tagList.add(tagInfo);

                    // TODO root tag is an element that was found at first.
                }

                System.out.println("TagList:" + tagList.size());
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
          * Sets a child tag name to TagInfo.
          *
          * @param tagInfo TagInfo
          * @param items an array of DTDItem
          */
    private void setChildTagName(TagInfo tagInfo, DTDItem[] items) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] instanceof DTDName) {
                DTDName dtdName = (DTDName) items[i];
                tagInfo.addChildTagName(dtdName.getValue());
            } else if (items[i] instanceof DTDChoice) {
                DTDChoice dtdChoise = (DTDChoice) items[i];
                setChildTagName(tagInfo, dtdChoise.getItem());
            }
        }
    }
}
