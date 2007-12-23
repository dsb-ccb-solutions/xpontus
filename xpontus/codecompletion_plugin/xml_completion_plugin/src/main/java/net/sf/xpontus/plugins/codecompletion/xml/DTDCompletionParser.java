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
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.xni.parser.XMLInputSource;

import java.io.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DTDCompletionParser implements ICompletionParser {
    private Log logger = LogFactory.getLog(DTDCompletionParser.class);
    private List tagList = new ArrayList();
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
            XMLDTDLoader loader = new XMLDTDLoader() {
                    public void element(java.lang.String elementName,
                        org.apache.xerces.xni.Augmentations augs)
                        throws org.apache.xerces.xni.XNIException {
                        super.element(elementName, augs);

                        TagInfo tagInfo = new TagInfo(elementName, true);

                        if (!tagList.contains(tagInfo)) {
                            tagList.add(tagInfo);
                        }
                    }

                    public void attributeDecl(java.lang.String elementName,
                        java.lang.String attributeName, java.lang.String type,
                        java.lang.String[] enumeration,
                        java.lang.String defaultType,
                        org.apache.xerces.xni.XMLString defaultValue,
                        org.apache.xerces.xni.XMLString nonNormalizedDefaultValue,
                        org.apache.xerces.xni.Augmentations augs)
                        throws org.apache.xerces.xni.XNIException {
                        TagInfo tagInfo = getTagInfo(elementName);

                        AttributeInfo attrInfo = new AttributeInfo(attributeName,
                                true, AttributeInfo.NONE, true);

                        tagInfo.addAttributeInfo(attrInfo);

                        if (enumeration != null) {
                            for (int i = 0; i < enumeration.length; i++) {
                                attrInfo.addValue(enumeration[i]);
                            }
                        } else {
                            if (defaultValue != null) {
                                attrInfo.addValue(defaultValue.toString());
                            }
                        }
                    }

                    public void externalEntityDecl(java.lang.String name,
                        org.apache.xerces.xni.XMLResourceIdentifier identifier,
                        org.apache.xerces.xni.Augmentations augs)
                        throws org.apache.xerces.xni.XNIException {
                        super.externalEntityDecl(name, identifier, augs);
                    }
                };

            FileObject fo = VFS.getManager().resolveFile(uri);

            DTDGrammar grammar = (DTDGrammar) loader.loadGrammar((new XMLInputSource(
                        pubid, uri, fo.getURL().toExternalForm(), in, null)));

            //            System.setProperty("xml.catalog.staticCatalog", "true");
            //            System.setProperty("xml.catalog.files",
            //                "C:\\tests\\docbook-xml-4.4\\docbook.cat");
            //
            //            CatalogManager manager = new CatalogManager();
            //
            //            Catalog catalog = manager.getCatalog();
            //            pubid = pubid.substring(1, pubid.length() - 1);
            //
            //            String s = manager.getCatalog().resolvePublic(pubid, uri); 
            //
            //            s = s.substring(5, s.length());
            //
            //            InputSource is = new InputSource(new FileReader(s));
            //
            //            XMLResourceIdentifier identifier = new XMLResourceIdentifierImpl();
            //            identifier.setPublicId(pubid);
            //            identifier.setLiteralSystemId(s);
            //            identifier.setBaseSystemId(s);
            //
            //            DTDGrammar grammar = (DTDGrammar) loader.loadGrammar(new XMLInputSource(
            //                        identifier));
        } catch (Exception ex) {
            ex.printStackTrace();
            ;
        }
    }
}
