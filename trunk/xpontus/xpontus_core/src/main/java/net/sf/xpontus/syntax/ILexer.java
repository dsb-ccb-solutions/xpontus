/**
 * ILexer.java
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
package net.sf.xpontus.syntax;

import java.util.List;

import javax.swing.text.Segment;


/**
 * The interface for all lexers
 * @author Yves Zoundi
 * @version 0.0.1
 */
public interface ILexer {
    /** The default lexer state */
    public static final int DEFAULT_LEXER_STATE = 0;

    /**
     * Method getLastTokenTypeOnLine ...
     *
     * @param text of type Segment
     * @param initialTokenType of type int
     * @return int
     */
    public int getLastTokenTypeOnLine(Segment text, int initialTokenType);

    /**
     * Method getTokens ...
     *
     * @param seg of type Segment
     * @param initialTokenType of type int
     * @param offs of type int
     * @return List
     */
    public List<Token> getTokens(Segment seg, int initialTokenType, int offs);
}
