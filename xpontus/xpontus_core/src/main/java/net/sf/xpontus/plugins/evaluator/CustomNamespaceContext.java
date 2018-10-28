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
package net.sf.xpontus.plugins.evaluator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.NamespaceContext;


/**
 *
 * @author Yves Zoundi
 */
public class CustomNamespaceContext implements NamespaceContext {
    private Map map;

    public CustomNamespaceContext(Map map) {
        this.map = map;
    }

    public void setNamespace(String prefix, String namespaceURI) {
        map.put(prefix, namespaceURI);
    }

    public String getNamespaceURI(String prefix) {
        return (String) map.get(prefix);
    }

    public String getPrefix(String namespaceURI) {
        Set keys = map.keySet();

        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String prefix = (String) iterator.next();

            String uri = (String) map.get(prefix);

            if (uri.equals(namespaceURI)) {
                return prefix;
            }
        }

        return null;
    }

    public Iterator getPrefixes(String namespaceURI) {
        List prefixes = new ArrayList();

        Set keys = map.keySet();

        for (Iterator iterator = keys.iterator(); iterator.hasNext();) {
            String prefix = (String) iterator.next();

            String uri = (String) map.get(prefix);

            if (uri.equals(namespaceURI)) {
                prefixes.add(prefix);
            }
        }

        return prefixes.iterator();
    }
}
