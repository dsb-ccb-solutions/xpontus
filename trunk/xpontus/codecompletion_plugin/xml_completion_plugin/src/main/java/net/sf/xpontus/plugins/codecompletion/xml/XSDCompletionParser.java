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
import org.apache.xerces.impl.xs.XSParticleDecl;
import org.apache.xerces.xni.parser.XMLInputSource;
import org.apache.xerces.xs.XSAttributeDeclaration;
import org.apache.xerces.xs.XSAttributeUse;
import org.apache.xerces.xs.XSComplexTypeDefinition;
import org.apache.xerces.xs.XSConstants;
import org.apache.xerces.xs.XSElementDeclaration;
import org.apache.xerces.xs.XSModelGroup;
import org.apache.xerces.xs.XSNamedMap;
import org.apache.xerces.xs.XSObjectList;
import org.apache.xerces.xs.XSParticle;
import org.apache.xerces.xs.XSTerm;
import org.apache.xerces.xs.XSTypeDefinition;
import org.apache.xerces.xs.XSWildcard;

import java.io.InputStream;
import java.io.Reader;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
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

            if (targetNS == null) {
                targetNS = "DEFAULT_PREFIX_MAPPING";
            }

            nsTagListMap.put(targetNS, new ArrayList());

            List tagList = (List) nsTagListMap.get(targetNS);
//            System.out.println("namespace:" + targetNS);

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
        String name = element.getName();

        if (element.getNamespace() != null) {
            name = element.getNamespace() + ":" + name;
        }

        TagInfo tagInfo = new TagInfo(name, true);

        if (tagList.contains(tagInfo)) {
            return;
        }

        tagList.add(tagInfo);
//        System.out.println("Adding:" + tagInfo.getTagName());

        XSTypeDefinition typedef = element.getTypeDefinition();

        if (typedef.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
            XSComplexTypeDefinition complex = (XSComplexTypeDefinition) typedef;

            XSParticle particle = complex.getParticle();

            if (particle != null) {
                XSTerm particleTerm = particle.getTerm();

                if (particleTerm instanceof XSWildcard) {
                } else {
                    xsTermToElementDecl(tagInfo, particleTerm);
                }
            }

            XSObjectList attrs = complex.getAttributeUses();

            for (int i = 0; i < attrs.getLength(); i++) {
                XSAttributeUse attrUse = (XSAttributeUse) attrs.item(i);
                XSAttributeDeclaration attr = attrUse.getAttrDeclaration();

                AttributeInfo attrInfo = new AttributeInfo(attr.getName(),
                        true, AttributeInfo.NONE, attrUse.getRequired());
                tagInfo.addAttributeInfo(attrInfo);
            }
        }
    }

    private void xsElementToElementDecl(TagInfo info,
        XSElementDeclaration element) {
        String name = element.getName();

        if (element.getNamespace() != null) {
//            System.out.println("Namespace:" + element.getNamespace());
        }

        TagInfo elementDecl = new TagInfo(name, true);

        info.addChildTagName(name);
//        System.out.println("Adding child:" + name + " to parent " + info.getTagName());

        XSTypeDefinition typedef = element.getTypeDefinition();

        if (typedef.getTypeCategory() == XSTypeDefinition.COMPLEX_TYPE) {
            XSComplexTypeDefinition complex = (XSComplexTypeDefinition) typedef;

            XSParticle particle = complex.getParticle();

            if (particle != null) {
                XSTerm particleTerm = particle.getTerm();

                if (particleTerm instanceof XSWildcard) {
                } else {
                    xsTermToElementDecl(elementDecl, particleTerm);
                }
            }

            XSObjectList attributes = complex.getAttributeUses();

            for (int i = 0; i < attributes.getLength(); i++) {
                XSAttributeUse attr = (XSAttributeUse) attributes.item(i);
                boolean required = attr.getRequired();

                AttributeInfo attrInfo = new AttributeInfo(attr.getName(),
                        true, AttributeInfo.NONE, required);
                elementDecl.addAttributeInfo(attrInfo);
            }
        }
    } //}}}

    //{{{ xsTermToElementDecl() method
    private void xsTermToElementDecl(TagInfo info, XSTerm term) {
        if (term instanceof XSElementDeclaration) {
            xsElementToElementDecl(info, (XSElementDeclaration) term);
        } else if (term instanceof XSModelGroup) {
            //            XSObjectList content = ((XSModelGroup) term).getParticles();
            //
            //            for (int i = 0; i < content.getLength(); i++) {
            //                XSTerm childTerm = ((XSParticleDecl) content.item(i)).getTerm();
            //                xsTermToElementDecl(info, childTerm);
            //            }
        }
    }
}
