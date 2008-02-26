/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.settings;

import net.sf.xpontus.properties.PropertiesHolder;

import java.util.Map;


/**
 *
 * @author Yves Zoundi
 */
public class XPontusSettings {
    public static final String KEY = XPontusSettings.class.getName();

    /**
     *
     * @return
     */
    public static SettingsModuleIF getDefault() {
        return lookup(XPontusSettings.KEY);
    }

    /**
     *
     * @param role
     * @return
     */
    public static SettingsModuleIF lookup(String role) {
        Map map = (Map) PropertiesHolder.getPropertyValue(role);
        SettingsModuleIF settings = (SettingsModuleIF) map.get(DefaultSettingsModuleImpl.ROLE);

        return settings;
    }
}
