/*
 * TokenMarker.java
 *
 * Created on January 21, 2007, 3:52 PM
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
package net.sf.xpontus.syntax;

import javax.swing.text.Segment;


/**
 *
 * @author Yves Zoundi
 */
public interface TokenMarker {
    /**
     * Adds a null token to the end of the current linked list of tokens.
     * This should be put at the end of the linked list whenever the last
     * token on the current line is NOT a multiline token.
     */
    public void addNullToken();

    /**
     * Adds the token specified to the current linked list of tokens.
     *
     * @param array The character array from which to get the text.
     * @param start Start offset in <code>segment</code> of token.
     * @param end End offset in <code>segment</code> of token.
     * @param tokenType The token's type.
     * @param startOffset The offset in the document at which this token
     *                    occurs.
     */
    public void addToken(char[] array, int start, int end, int tokenType,
        int startOffset);

    /**
     * Returns the last token on this line's type if the token is "unfinished",
     * or <code>Token.NULL</code> if it was finished.  For example, if C-style
     * syntax highlighting is being implemented, and <code>text</code>
     * contained a line of code that contained the beginning of a comment but
     * no end-comment marker ("*\/"), then this method would return
     * <code>Token.COMMENT_MULTILINE</code> for that line.  This is useful
     * for doing syntax highlighting.
     *
     * @param text The line of tokens to examine.
     * @param initialTokenType The token type to start with (i.e., the value
     *        of <code>getLastTokenTypeOnLine</code> for the line before
     *        <code>text</code>).
     * @return The last token on this line's type, or <code>Token.NULL</code>
     *         if the line was completed.
     */
    public int getLastTokenTypeOnLine(Segment text, int initialTokenType);

    /**
     * Returns the text to place at the beginning and end of a
     * line to "comment" it in a this programming language.
     *
     * @return The start and end strings to add to a line to "comment"
     *         it out.  A <code>null</code> value for either means there
     *         is no string to add for that part.  A value of
     *         <code>null</code> for the array means this language
     *         does not support commenting/uncommenting lines.
     */
    public String[] getLineCommentStartAndEnd();

    /**
     * Returns the first token in the linked list of tokens generated
     * from <code>text</code>.  This method must be implemented by
     * subclasses so they can correctly implement syntax highlighting.
     *
     * @param text The text from which to get tokens.
     * @param initialTokenType The token type we should start with.
     * @param startOffset The offset into the document at which
     *                    <code>text</code> starts.
     * @return The first <code>Token</code> in a linked list representing
     *         the syntax highlighted text.
     */
    public Token getTokenList(Segment text, int initialTokenType,
        int startOffset);
}
