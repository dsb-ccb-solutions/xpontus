/*
 * XPontusPropertiesConstantsIF.java
 *
 * Created on Jul 28, 2007, 8:41:25 PM
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
package net.sf.xpontus.constants;


/**
 * <p>Constants for properties than can be retrieved by using the
 * class </code>PropertiesHolder</code></p>
 * @author Yves Zoundi
 */
public interface XPontusPropertiesConstantsIF {
    /**
     * Property holding menus by keys
     */
    String XPONTUS_MENUS_PROPERTY = "XPONTUS_MENUS_PROPERTY";

    /**
     * Property holding toolbar components by keys
     */
    String XPONTUS_TOOLBAR_PROPERTY = "XPONTUS_TOOLBAR_PROPERTY";

    /**
     * Property holding the transformation preview plugins 
     */
    String XPONTUS_PREVIEW_PROPERTY = "XPONTUS_PREVIEW_PROPERTY";

    /**
     * Property for themes supported by XPontus XML Editor
     */
    String XPONTUS_THEMES_PROPERTY = "XPONTUS_THEMES_PROPERTY";

    /**
     * Property for lexers
     */
    String XPONTUS_LEXER_PROPERTY = "XPONTUS_LEXER_PROPERTY";

    /**
     * Property for icons
     */
    String XPONTUS_ICONS_PROPERTY = "XPONTUS_ICONS_PROPERTY";

    /**
     * Property for indentation engines
     */
    String XPONTUS_INDENTATION_ENGINES = "XPONTUS_INDENTATION_ENGINES";

    /**
     * Property for completion engines
     */
    String XPONTUS_COMPLETION_ENGINES = "XPONTUS_COMPLETION_ENGINES";

    /**
     * Property for outline engines
     */
    String XPONTUS_OUTLINE_ENGINES = "XPONTUS_OUTLINE_ENGINES";
    
    /**
     * Property holding the "registered" preferences dialog components
     */
    String XPONTUS_PREFERENCES_PANELS = "XPONTUS_PREFERENCES_PANELS";

    /**
     * Property for user scenario plugins
     */
    String SCENARIO_ENGINES = "SCENARIO_ENGINES";

    /**
     * Property for the editor container plugins
     */
    String XPONTUS_TEXTEDITORS_PLUGINS_PROPERTY = "XPONTUS_TEXTEDITORS_PLUGINS_PROPERTY";

    /**
     * Property for the quick toolbars
     */
    String XPONTUS_QUICKTOOLBAR_PROPERTY = "XPONTUS_QUICKTOOLBAR_PROPERTY";
}
