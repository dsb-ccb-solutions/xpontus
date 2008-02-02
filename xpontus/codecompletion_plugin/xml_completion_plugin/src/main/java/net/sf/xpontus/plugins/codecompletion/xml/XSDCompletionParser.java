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

import org.apache.xerces.impl.xs.SchemaGrammar;
import org.apache.xerces.impl.xs.XMLSchemaLoader;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObject;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;

import java.io.InputStream;
import java.io.Reader;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XSDCompletionParser implements ICompletionParser {
    private Log logger = LogFactory.getLog(XSDCompletionParser.class);
    private List tagList = new Vector();
    private Map nsTagListMap = new HashMap();

    /** Creates a new instance of XSDCompletionParser */
    public XSDCompletionParser() {
    }

    public void init(List tagList, Map nsTagListMap) {
        this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }

    /**
    *
    * @param pubid
    * @param uri
    * @param in
    */
    public void updateCompletionInfo(String pubid, String uri, Reader in) {
        try {
            InputStream is = new URL(uri).openStream();
            SchemaGrammar grammer = (SchemaGrammar) new XMLSchemaLoader().loadGrammar(new XMLInputSource(
                        null, null, null, is, null));

            // clear at first
            String targetNS = grammer.getTargetNamespace();
            nsTagListMap.put(targetNS, new ArrayList());

            List tagList = (List) nsTagListMap.get(targetNS);
            System.out.println("namespace:" + targetNS);
            XSNamedMap map = grammer.getComponents(XSConstants.ELEMENT_DECLARATION);

            for (int i = 0; i < map.getLength(); i++) {
                XSElementDeclaration element = (XSElementDeclaration) map.item(i);
                parseXSDElement(tagList, element);
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void parseXSDElement(List tagList, XSElementDeclaration element) {
        TagInfo tagInfo = new TagInfo(element.getName(), true);

        if (tagList.contains(tagInfo)) {
            return;
        }

        tagList.add(tagInfo);

        XSTypeDefinition type = element.getTypeDefinition();

        if (type instanceof XSComplexTypeDefinition) {
            XSParticle particle = ((XSComplexTypeDefinition) type).getParticle();

            if (particle != null) {
                XSTerm term = particle.getTerm();

                if (term instanceof XSElementDeclaration) {
                    parseXSDElement(tagList, (XSElementDeclaration) term);
                    tagInfo.addChildTagName(((XSElementDeclaration) term).getName());
                }

                if (term instanceof XSModelGroup) {
                    parseXSModelGroup(tagInfo, tagList, (XSModelGroup) term);
                }
            }

            XSObjectList attrs = ((XSComplexTypeDefinition) type).getAttributeUses();

            for (int i = 0; i < attrs.getLength(); i++) {
                XSAttributeUse attrUse = (XSAttributeUse) attrs.item(i);
                XSAttributeDeclaration attr = attrUse.getAttrDeclaration();
                AttributeInfo attrInfo = new AttributeInfo(attr.getName(),
                        true, AttributeInfo.NONE, attrUse.getRequired());
                tagInfo.addAttributeInfo(attrInfo);
            }
        }
    }

    private void parseXSModelGroup(TagInfo tagInfo, List tagList,
        XSModelGroup term) {
        XSObjectList list = ((XSModelGroup) term).getParticles();

        for (int i = 0; i < list.getLength(); i++) {
            XSObject obj = list.item(i);

            if (obj instanceof XSParticle) {
                XSTerm term2 = ((XSParticle) obj).getTerm();

                if (term2 instanceof XSElementDeclaration) {
                    parseXSDElement(tagList, (XSElementDeclaration) term2);
                    tagInfo.addChildTagName(((XSElementDeclaration) term2).getName());
                }

                if (term2 instanceof XSModelGroup) {
                    parseXSModelGroup(tagInfo, tagList, (XSModelGroup) term2);
                }
            }
        }
    }
}
