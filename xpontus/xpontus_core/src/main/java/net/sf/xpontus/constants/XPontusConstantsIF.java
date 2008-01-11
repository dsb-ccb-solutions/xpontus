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
    String OUTLINE_INFO = "OUTLINE_INFO";
    String APPLICATION_NAME = "XPontus XML Editor";
    String APPLICATION_VERSION = "1.0.0-pre1";
    String MODIFICATION_HANDLER = "MODIFICATION_HANDLER";
    String OBJECT_NAME = "OBJECT_NAME";
    String OBJECT_CLASSNAME = "OBJECT_CLASSNAME";
    String UNDO_MANAGER = "UNDO_MANAGER";
}
