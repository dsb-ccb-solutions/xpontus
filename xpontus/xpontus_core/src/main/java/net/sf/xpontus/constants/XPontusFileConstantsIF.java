/*
 * XPontusFileConstantsIF.java
 *
 * Created on May 28, 2007, 8:25 PM
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
 * constants to use when storing document properties
 * @author Yves Zoundi
 */
public interface XPontusFileConstantsIF {
    /**
     * Constant for file modification's property
     */
    String FILE_MOFIFIED = "FILE_MODIFIED";

    /**
     *
     */
    String FILE_OBJECT = "FILE_OBJECT";

    /**
     * Constant to indicate that a file is new
     */
    String FILE_NEW = "FILE_NEW";

    /**
     * Constant for the date of last modification of a file
     */
    String FILE_LAST_MODIFIED_DATE = "LAST_MODIFIED_DATE";

    /**
     * Constant to indicate that a file is locked
     */
    String FILE_LOCKED = "FILE_LOCKED";

    /**
     * Constant to indicate wether or not a file is writable
     */
    String FILE_WRITABLE = "FILE_WRITABLE";

    /**
     * Constant holding the file size of a document
     */
    String FILE_SIZE = "FILE_SIZE";

    /**
     * Constant for the file name
     */
    String FILE_NAME = "FILE_NAME";

    /**
     * Constant for the absolute path of a file
     */
    String FILE_PATH = "FILE_PATH";

    /**
     * Constant for the lexer class used for syntax highlighting
     */
    String LEXER_FOR_FILE = "LEXER_FOR_FILE";
}
