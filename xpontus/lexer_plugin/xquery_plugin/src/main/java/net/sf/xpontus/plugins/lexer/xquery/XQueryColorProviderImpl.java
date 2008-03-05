/*
 * XQueryColorProvider.java
 *
 * Created on February 2, 2007, 10,14 PM
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
package net.sf.xpontus.plugins.lexer.xquery;

import net.sf.xpontus.plugins.color.PlainColorProviderImpl;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 *
 * @author Yves Zoundi
 */
public class XQueryColorProviderImpl extends PlainColorProviderImpl {
    /** Creates a new instance of XQueryColorProvider */
    public XQueryColorProviderImpl() {
        MutableAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setForeground(keyword, Color.BLUE);
        StyleConstants.setBold(keyword, true);

        int[] keywords = {
                XQueryParserConstants.DO, XQueryParserConstants.FOR,
                XQueryParserConstants.INSTANCEOF, XQueryParserConstants.RETURNS,
                XQueryParserConstants.NAMESPACE, XQueryParserConstants.DEFAULTT,
                XQueryParserConstants.FUNCTION, XQueryParserConstants.LIST,
                XQueryParserConstants.ELEMENT, XQueryParserConstants.ATTRIBUTE,
                XQueryParserConstants.SORTBY, XQueryParserConstants.OR,
                XQueryParserConstants.BAR, XQueryParserConstants.UNION,
                XQueryParserConstants.EXCEPT, XQueryParserConstants.NOT,
                XQueryParserConstants.BEFORE, XQueryParserConstants.AFTER,
                XQueryParserConstants.AS, XQueryParserConstants.AT,
                XQueryParserConstants.RANGE, XQueryParserConstants.IF,
                XQueryParserConstants.THEN, XQueryParserConstants.ELSE,
                XQueryParserConstants.IN, XQueryParserConstants.LET,
                XQueryParserConstants.WHERE, XQueryParserConstants.RETURN,
                XQueryParserConstants.NODE, XQueryParserConstants.TEXT,
                XQueryParserConstants.COMMENT,
                XQueryParserConstants.PROCESSING_INSTRUCTION,
                XQueryParserConstants.SOME, XQueryParserConstants.EVERY,
                XQueryParserConstants.CAST, XQueryParserConstants.TREAT,
                XQueryParserConstants.ASCENDING,
                XQueryParserConstants.DESCENDING, XQueryParserConstants.TRUE,
                XQueryParserConstants.FALSE, XQueryParserConstants.AND,
                XQueryParserConstants.INTERSECT, XQueryParserConstants.DIV,
                XQueryParserConstants.MOD, XQueryParserConstants.DEREFERENCE,
                XQueryParserConstants.TO, XQueryParserConstants.SATISFIES,
            };

        addAll(keywords, keyword);

        MutableAttributeSet operator = new SimpleAttributeSet();
        StyleConstants.setForeground(operator, new Color(139, 69, 19));

        int[] operators = {
                XQueryParserConstants.L_PAREN, XQueryParserConstants.R_PAREN,
                XQueryParserConstants.L_BRACE, XQueryParserConstants.R_BRACE,
                XQueryParserConstants.L_BRACKET, XQueryParserConstants.R_BRACKET,
                XQueryParserConstants.SEMICOLON, XQueryParserConstants.COLON,
                XQueryParserConstants.COMMA, XQueryParserConstants.DOT,
                XQueryParserConstants.DOTDOT, XQueryParserConstants.APOSTROPH
            };

        addAll(operators, operator);

        MutableAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setForeground(comment, new Color(0, 100, 0));
        StyleConstants.setBold(comment, true);
        StyleConstants.setItalic(comment, true);

        int[] c = {
                XQueryParserConstants.START_MULTILINE_COMMENT,
                XQueryParserConstants.IN_MULTILINE_COMMENT,
                XQueryParserConstants.MULTILINE_COMMENT_CHAR,
                XQueryParserConstants.END_MULTILINE_COMMENT
            };
        addAll(c, comment);
    }
}
