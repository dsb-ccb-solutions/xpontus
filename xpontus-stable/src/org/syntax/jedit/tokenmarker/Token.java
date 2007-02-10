/*
 * Token.java - Generic token
 * Copyright (C) 1998, 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package org.syntax.jedit.tokenmarker;


/**
 * A linked list of tokens. Each token has three fields - a token
 * identifier, which is a byte value that can be looked up in the
 * array returned by <code>SyntaxDocument.getColors()</code>
 * to get a color value, a length value which is the length of the
 * token in the text, and a pointer to the next token in the list.
 *
 * @author Slava Pestov
 * @version $Id: Token.java,v 1.1.1.1 2005/12/08 01:34:36 yveszoundi Exp $
 */
public class Token {
    /**
     * Normal text token id. This should be used to mark
     * normal text.
     */
    public static final byte NULL = 0;

    /**
     * Comment 1 token id. This can be used to mark a comment.
     */
    public static final byte COMMENT1 = 1;

    /**
     * Comment 2 token id. This can be used to mark a comment.
     */
    public static final byte COMMENT2 = 2;

    /**
     * Comment 2 token id. This can be used to mark a comment.
     */
    public static final byte COMMENT3 = 3;

    /**
     * Literal 1 token id. This can be used to mark a string
     * literal (eg, C mode uses this to mark "..." literals)
     */
    public static final byte LITERAL1 = 4;

    /**
     * Literal 2 token id. This can be used to mark an object
     * literal (eg, Java mode uses this to mark true, false, etc)
     */
    public static final byte PRIMITIVE = 5;

    /**
     * Label token id. This can be used to mark labels
     * (eg, C mode uses this to mark ...: sequences)
     */
    public static final byte LABEL = 6;

    /**
     * Keyword 1 token id. This can be used to mark a
     * keyword. This should be used for general language
     * constructs.
     */
    public static final byte KEYWORD1 = 7;

    /**
     * Keyword 2 token id. This can be used to mark a
     * keyword. This should be used for preprocessor
     * commands, or variables.
     */
    public static final byte KEYWORD2 = 8;

    /**
     * Keyword 3 token id. This can be used to mark a
     * keyword. This should be used for data types.
     */
    public static final byte KEYWORD3 = 9;

    /**
     * Operator token id. This can be used to mark an
     * operator. (eg, SQL mode marks +, -, etc with this
     * token type)
     */
    public static final byte OPERATOR = 10;

    /**
     * Invalid token id. This can be used to mark invalid
     * or incomplete tokens, so the user can easily spot
     * syntax errors.
     */
    public static final byte INVALID = 11;

    /**
     * The total number of defined token ids.
     */
    public static final byte ID_COUNT = 12;

    /**
     * For compatibility with newer TokenMarkers
     * Not needed for our bluej modified Java tokens
     */
    public static final byte LITERAL2 = 13;

    /**
     * The first id that can be used for internal state
     * in a token marker.
     */
    public static final byte INTERNAL_FIRST = 100;

    /**
     * The last id that can be used for internal state
     * in a token marker.
     */
    public static final byte INTERNAL_LAST = 126;

    /**
     * The token type, that along with a length of 0
     * marks the end of the token list.
     */
    public static final byte END = 127;

    /**
     * The length of this token.
     */
    public int length;

    /**
     * The id of this token.
     */
    public byte id;

    /**
     * The next token in the linked list.
     */
    public Token next;

    /**
     * Creates a new token.
     * @param length The length of the token
     * @param id The id of the token
     */
    public Token(int length, byte id) {
        this.length = length;
        this.id = id;
    }

    /**
     * Returns a string representation of this token.
     */
    public String toString() {
        return "[id=" + id + ",length=" + length + "]";
    }
}
