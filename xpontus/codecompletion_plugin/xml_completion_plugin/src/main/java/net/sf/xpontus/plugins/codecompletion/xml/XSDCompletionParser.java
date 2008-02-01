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

import org.exolab.castor.xml.schema.ElementDecl;
import org.exolab.castor.xml.schema.Schema;
import org.exolab.castor.xml.schema.reader.SchemaReader;

import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.Reader;

import java.net.URL;

import java.util.Enumeration;
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
            InputSource src = new InputSource(is);
            SchemaReader sr = new SchemaReader(src);
            sr.setValidation(false);
            sr.setCacheIncludedSchemas(true);

            Schema schema = sr.read();
            
            logger.info("Parsing schema done!!!");
            
            logger.info("Creating completion database");

            Enumeration<ElementDecl> elementDecls = schema.getElementDecls();

            // iterate over the elements
            while (elementDecls.hasMoreElements()) {
                ElementDecl element = elementDecls.nextElement();
                String name = element.getName();
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
