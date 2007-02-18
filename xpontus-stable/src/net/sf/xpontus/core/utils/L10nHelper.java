/*
 * EnvironmentModel.java
 *
 * Created on 2 octobre 2005, 16:25
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.core.utils;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * a helper  class for localized messages
 * @author Yves Zoundi
 */
public class L10nHelper {
    private static ResourceBundle res;
    private static L10nHelper _instance;

    private L10nHelper() {
    }

    /**
     * singleton pattern
     * @return the single instance of this class
     */
    public static L10nHelper getInstance() {
        if (_instance == null) {
            _instance = new L10nHelper();
        }

        return _instance;
    }

    /**
     * get a i18n message value
     * @param key the i18n key
     * @return the i18n key translation
     */
    public String getValue(String key) {
        return res.getObject(key).toString();
    }

    /**
     * register a i18n catalog
     * @param i18nfile add a i18n file
     */
    public static void registerLocalizationFile(String i18nfile) {
        Locale locale = Locale.getDefault();

        try {
            res = ResourceBundle.getBundle(i18nfile, locale);
        } catch (Exception e) {
            locale = Locale.US;
            res = ResourceBundle.getBundle(i18nfile, locale);
        }
    }
}
