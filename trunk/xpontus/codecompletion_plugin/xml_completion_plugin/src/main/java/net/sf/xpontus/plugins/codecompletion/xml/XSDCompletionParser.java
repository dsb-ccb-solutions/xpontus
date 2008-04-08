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
import org.apache.xerces.xs.*;

import java.io.InputStreamReader;
import java.io.Reader;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Yves Zoundi
 */
public class XSDCompletionParser implements ICompletionParser {
    private Log logger = LogFactory.getLog(XSDCompletionParser.class);
    private List tagList = new ArrayList();
    private Map nsTagListMap = new HashMap();

    /** Creates a new instance of XMLSchemaCompletionParser */
    public XSDCompletionParser() {
    }

    public void init(List tagList, Map nsTagListMap) {
        this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }

    public void updateCompletionInfo(String pubid, String uri, Reader in) {
        try {
            String[] schemas = uri.split("[ \t\n\r]+");

            for (String schema : schemas) {
                if (schema.indexOf(".xsd") != -1) {

                    Reader m_reader = new InputStreamReader(new URL(schema).openStream());
                    SchemaGrammar grammer = (SchemaGrammar) new XMLSchemaLoader().loadGrammar(new XMLInputSource(
                                null, null, null, m_reader, null));

                    // clear at first
                    String targetNS = grammer.getTargetNamespace();
                    // System.out.println("Target Namespace:" + targetNS);
                    nsTagListMap.put(targetNS, new ArrayList());

                    List tagList = (List) nsTagListMap.get(targetNS);
                    
                    XSNamedMap map = grammer.getComponents(XSConstants.ELEMENT_DECLARATION);

                    for (int i = 0; i < map.getLength(); i++) {
                        XSElementDeclaration element = (XSElementDeclaration) map.item(i);
                        parseXSDElement(tagList, element);
                    }
                }
            }
        } catch (Exception ex) {
        }
    }

    private void parseXSDElement(List tagList, XSElementDeclaration element) {
        TagInfo tagInfo = new TagInfo(element.getName(), true);

        if (!tagList.contains(tagInfo)) {
            tagList.add(tagInfo);

            // System.out.println("Adding :" + tagInfo.getTagName());
        }

        XSTypeDefinition type = element.getTypeDefinition();

        if (type instanceof XSComplexTypeDefinition) {
            XSParticle particle = ((XSComplexTypeDefinition) type).getParticle();

            if (particle != null) {
                XSTerm term = particle.getTerm();

                if (term instanceof XSElementDeclaration) {
                    parseXSDElement(tagList, (XSElementDeclaration) term);

                    XSElementDeclaration m_declaration = (XSElementDeclaration) term;
                    tagInfo.addChildTagName(m_declaration.getName());

                    // System.out.println("Adding tag:" + m_declaration.getName() + " to parent:" + tagInfo.getTagName() );
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

                    // System.out.println("Adding tag:" + ((XSElementDeclaration) term2).getName() + " to parent:" + tagInfo.getTagName() );
                } else if (term2 instanceof XSModelGroup) {
                    parseXSModelGroup(tagInfo, tagList, (XSModelGroup) term2);
                }
            }
        }
    }
}
