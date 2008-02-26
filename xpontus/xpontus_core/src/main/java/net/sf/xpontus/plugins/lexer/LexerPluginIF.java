/*
 * LexerModuleIF.java
 *
 * Created on 26 avril 2007, 11:07
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
package net.sf.xpontus.plugins.lexer;

import net.sf.xpontus.syntax.IColorProvider;
import net.sf.xpontus.syntax.ILexer;


/**
 * Interface for lexers
 * @author Yves Zoundi
 */
public interface LexerPluginIF extends ILexer {
    /**
     *
     * @return The content type supported by the lexer
     */
    public String getMimeType();

    /**
     *
     * @return
     */
    public String[] getSupportedExtensions();

    /**
     * Returns the lexer's name
     * @return The lexer's name
     */
    public String getName();

    /**
     * The lexer's description
     * @return The lexer description
     */
    public String getDescription();

    /**
     * Returns the lexer full class name
     * @return The lexer full class name
     */
    public String getLexerClassName();

    /**
     * Return the syntax highlighting object of this analyzer
     * @return The syntax colorer of this class
     */
    public IColorProvider getColorer();
}
