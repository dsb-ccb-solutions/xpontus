/*
 * NullEntityResolver.java
 *
 * Copyright (C) 2005-2008 Yves Zoundi
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
 *
 *
 */
package net.sf.xpontus.utils;

import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.StringReader;


/**
 * A simple entity resolver which does absolutely nothing
 * It allows a fast verification of an XML Document without resolving entities
 * @author Yves Zoundi
 */
public class NullEntityResolver implements EntityResolver
{
    private static final NullEntityResolver INSTANCE = new NullEntityResolver();
    private final InputSource _source;

    private NullEntityResolver()
    {
        _source = new InputSource();
    }

    /**  @return a new instance of this class  */
    public static NullEntityResolver getInstance()
    {
        return INSTANCE;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.EntityResolver#resolveEntity(java.lang.String, java.lang.String)
     */
    public InputSource resolveEntity(String publicId, String systemId)
        throws SAXException, IOException
    {
        _source.setCharacterStream(new StringReader(""));

        return _source;
    }
}
