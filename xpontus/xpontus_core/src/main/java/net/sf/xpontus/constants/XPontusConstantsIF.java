/*
 * XPontusConstantsIF.java
 *
 * Created on 24 avril 2007, 15:20
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
 * The constants of the application
 * @author Yves Zoundi
 */
public interface XPontusConstantsIF extends XPontusConfigurationConstantsIF,
    XPontusFileConstantsIF, LexerPropertiesConstantsIF, XPontusMenuConstantsIF,
    XPontusPropertiesConstantsIF {
    /**
     * The information about a document code structure
     */
    String OUTLINE_INFO = "OUTLINE_INFO";

    /**
     * The software's name
     */
    String APPLICATION_NAME = "XPontus XML Editor";

    /**
     * The software's version
     */
    String APPLICATION_VERSION = "1.0.0.1";

    /**
     *
     */
    String MODIFICATION_HANDLER = "MODIFICATION_HANDLER";

    /**
     * An object name property
     */
    String OBJECT_NAME = "OBJECT_NAME";

    /**
     * An object full qualified name
     */
    String OBJECT_CLASSNAME = "OBJECT_CLASSNAME";

    /**
     * The undo/redo manager
     */
    String UNDO_MANAGER = "UNDO_MANAGER";
    String PARSER_DATA_DTD_COMPLETION_INFO = "PARSER_DATA_DTD_COMPLETION_INFO";
    String PARSER_DATA_SCHEMA_COMPLETION_INFO = "PARSER_DATA_SCHEMA_COMPLETION_INFO";
}
