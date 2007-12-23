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
package net.sf.xpontus.utils;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
import org.xml.sax.SAXException;
import org.xml.sax.ext.DefaultHandler2;

import java.util.Map;


/**
 *
 * @author Yves Zoundi
 */
public class NamespaceResolverHandler extends DefaultHandler2 {
    private Map nsMap;

    /**
     *
     * @param nsMap
     */
    public NamespaceResolverHandler(Map nsMap) {
        this.nsMap = nsMap;
        nsMap.clear();
    }

    /**
     *
     * @param prefix
     * @param uri
     * @throws org.xml.sax.SAXException
     */
    public void startPrefixMapping(String prefix, String uri)
        throws SAXException {
        nsMap.put(prefix, uri);
    }
}
