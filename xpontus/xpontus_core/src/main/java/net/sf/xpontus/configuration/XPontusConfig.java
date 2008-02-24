/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
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
