/*
 * I18nModuleIF.java
 *
 * Created on May 28, 2007, 8:23 PM
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
package net.sf.xpontus.plugins.i18n;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Interface for the localization messages
 * @author Yves Zoundi
 */
public interface I18nPluginIF {
    /** init the i18n modulel */
    public void init();

    /** stop the i18n module */
    public void stop();

    /**
     *
     * @param res
     */
    public void addResource(ResourceBundle res);

    /**
     *
     * @param bundleName
     * @return
     */
    public ResourceBundle getBundle(String bundleName);

    /**
     *
     * @param key
     * @param locale
     * @return
     */
    public String getMessage(String key, Locale locale);
}
