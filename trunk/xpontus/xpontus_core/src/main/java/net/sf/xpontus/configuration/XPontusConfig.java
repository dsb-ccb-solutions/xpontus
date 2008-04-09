/*
 * XPontusConfig.java
 *
 * Created on July 1, 2007, 10:51 AM
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
 */
package net.sf.xpontus.configuration;

import java.util.HashMap;
import java.util.Map;


/**
 *
 * @author mrcheeks
 */
public class XPontusConfig {
    private static final Map<Object, Object> configMap = new HashMap<Object, Object>();

    public static void put(Object key, Object value) {
        configMap.put(key, value);
    }

    public static Object getValue(Object key) {
        return configMap.get(key);
    }
}
