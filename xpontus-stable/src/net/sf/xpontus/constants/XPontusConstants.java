/*
 * XPontusConstants.java
 *
 * Created on 1 août 2005, 17:46
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
package net.sf.xpontus.constants;

import java.io.File;


/**
 * Constants for the application
 * @author Yves Zoundi
 */
public interface XPontusConstants {
    /** The user home directory string  */
    static final String HOMELOC = System.getProperty("user.home");

    /** the user's home directory */
    static final File HOME = new File(HOMELOC);

    /**  The XPontus XML Editor's configuration directory */
    static final File CONF_DIR = new File(new File(HOME, ".xpontus_1"),
            "1.0.0rc2");

    /** the preferences directory */
    static final File PREF_DIR = new File(CONF_DIR, "preferences");

    /** general preferences file */
    static final File GENERAL_PREF = new File(PREF_DIR,
            "general_preferences.xml");

    /** jtidy preferences file */
    static final File JTIDY_PREF = new File(PREF_DIR, "jtidy_preferences.xml");

    /* the editor preferences file */
    static final File EDITOR_PREF = new File(PREF_DIR, "editor_preferences.xml");

    /** the xml preferences file */
    static final File XML_PREF = new File(PREF_DIR, "xml_preferences.xml");

    /**  the color preferences file */
    static public final File COLOR_PREF = new File(PREF_DIR,
            "colors_preferences.xml");

    /** the XPontus XML Editor's file to store users XSLT scenarios */
    static final File SCENARIO_FILE = new File(CONF_DIR, "scenarios.xml");

    /** File menu actions id */
    static final String[] fileActions = new String[] {
            "action.new", "action.open", "-", "action.save", "action.saveas",
            "action.saveall", "-", "action.closetab", "action.closeothers","action.closetaball", "-",
            "action.print", "-", "action.exit"
        };

    /** Edit menu actions id  */
    static final String[] editActions = new String[] {
            "action.undo", "action.redo", "-", "action.cut", "action.copy",
            "action.paste", "action.selectall", "-", "action.find",
            "action.gotoline", "-", "action.insertcomment",
            "action.insertcdatasection"
        };

    /** Help menu actions id  */
    static final String[] helpActions = new String[] {
            "action.about", "action.help"
        };

    /**  Tools menu actions id */
    static final String[] toolsActions = new String[] {
            "action.checkxml", "action.validate", "action.batchvalidation",
            "action.validateschema", "-", "action.generatedtd", "action.xmldiff",
            "-", "action.preferences"
        };

    /**  Format menu actions id */
    static final String[] formatActions = new String[] {
            "action.commentxml", "action.indentxml", "action.tidy"
        };

    /**
     * the id to set messages on the status bar
     */
    public static int MESSAGES_TEXTE = 0;

    /**
     * the id to set "operation" messages on the status bar
     */
    public static int OPERATIONS_TEXT = 1;
}
