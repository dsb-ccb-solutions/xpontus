/*
 * XPontusMenuConstantsIF.java
 *
 * Created on May 28, 2007, 8:26 PM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
package net.sf.xpontus.constants;


/**
 *
 * Interface for the menu's constants
 * @author Yves Zoundi
 */
public interface XPontusMenuConstantsIF {
    /**
     * The file menu id
     */
    String FILE_MENU_ID = "FILE_MENU_ID";
    
    /**
     * 
     */
    String OPTIONS_MENU_ID = "OPTIONS_MENU_ID";

    /**
     * The help menu id
     */
    String HELP_MENU_ID = "HELP_MENU_ID";

    /**
     * The tools menu id
     */
    String TOOLS_MENU_ID = "TOOLS_MENU_ID";

    /**
     * The edit menu id
     */
    String EDIT_MENU_ID = "EDIT_MENU_ID";

    /**
     * The window menu id
     */
    String WINDOW_MENU_ID = "WINDOW_MENU_ID";

    /**
     * The menu ids array
     */
    String[] MENU_IDS = {
            FILE_MENU_ID, EDIT_MENU_ID, TOOLS_MENU_ID, OPTIONS_MENU_ID, WINDOW_MENU_ID,
            HELP_MENU_ID
        };

    /**
     * The menu titles (to be replace with i18n keys)
     */
    String[] MENU_TITLES = { "File", "Edit", "Tools", "Options", "Window", "Help" };
}
