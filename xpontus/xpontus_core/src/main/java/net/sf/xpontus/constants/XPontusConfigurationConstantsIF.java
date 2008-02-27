/*
 * XPontusConfigurationConstants.java
 *
 * Created on 4-Aug-2007, 10:18:31 AM
 *
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
 */
package net.sf.xpontus.constants;

import java.io.File;


/**
 * Some file configuration variables
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public interface XPontusConfigurationConstantsIF {
    /**
         * The user home directory string
         */
    String USER_HOME_STRING = System.getProperty("user.home");

    /**
     * The user home directory
     */
    File USER_HOME_DIR = new File(USER_HOME_STRING);

    /**
     * XPontus configuration directory
     */
    File XPONTUS_HOME_DIR = new File(USER_HOME_DIR, ".xpontus");
    File PLUGINS_SEARCHINDEX_DIR = new File(XPONTUS_HOME_DIR, "searchindex");
    File AVAILABLE_PLUGINS_SEARCHINDEX_DIR = new File(PLUGINS_SEARCHINDEX_DIR,
            "installedplugins");
    File INSTALLED_PLUGINS_SEARCHINDEX_DIR = new File(PLUGINS_SEARCHINDEX_DIR,
            "availableplugins");

    /**
     *
     *
     */
    File RECENT_FILES_HISTORY_FILE = new File(XPONTUS_HOME_DIR, "history");

    /**
     * The mimetypes file
     */
    File MIMETYPES_FILE = new File(XPONTUS_HOME_DIR, "mimes.properties");

    /**
     * The virtual file browser bookmarks file
     */
    File FAVORITES_FILE = new File(XPONTUS_HOME_DIR, "favorites");

    /**
     * XPontus plugins directory
     */
    File XPONTUS_PLUGINS_DIR = new File(XPONTUS_HOME_DIR, "plugins");

    /**
     * XPontus preferences directory
     */
    File XPONTUS_PREFERENCES_DIR = new File(XPONTUS_HOME_DIR, "preferences");

    /**
     * The editor preferences file
     */
    File EDITOR_PREFERENCES_FILE = new File(XPONTUS_PREFERENCES_DIR,
            "editorPanel.properties");

    /**
     * Some general properties
     */
    File GENERAL_PREFERENCES_FILE = new File(XPONTUS_PREFERENCES_DIR,
            "general.properties");

    /**
     * User transformations profiles
     */
    File XPONTUS_SCENARIOS_FILE = new File(XPONTUS_PREFERENCES_DIR,
            "scenarios.xml");

    /**
     * Resources cache dir
     */
    File XPONTUS_CACHE_DIR = new File(XPONTUS_HOME_DIR, "cache");

    /**
     * XPontus data directory for plugins
     */
    File XPONTUS_PLUGINS_DATA_DIR = new File(XPONTUS_HOME_DIR, "data");

    /**
     * XPontus embedded database directory
     */
    File XPONTUS_DATABASE_CONFIG_DIR = new File(XPONTUS_HOME_DIR, "config");

    /**
     * XPontus embedded database file - obsolete, db4o is still not used for user settings
     */
    File XPONTUS_DATABASE_FILE = new File(XPONTUS_DATABASE_CONFIG_DIR, "db.yap");
}
