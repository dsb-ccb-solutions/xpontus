/*
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
 *
 */
package net.sf.xpontus.plugins.lexer.xml;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public interface XMLLexerPreferencesConstantsIF {
    String STRING_PROPERTY = "STRING";
    String DECLARATION_PROPERTY = "XML_DECLARATIONS";
    String COMMENT_PROPERTY = "COMMENT";
    String TAGS_PROPERTY = "TAGS";
    String ATTRIBUTES_PROPERTY = "ATTRIBUTES";
    String[] AVAILABLE_PROPERTIES = {
            STRING_PROPERTY, DECLARATION_PROPERTY, COMMENT_PROPERTY,
            TAGS_PROPERTY, ATTRIBUTES_PROPERTY
        };
}
