/*
 * XSLTokenMarker.java - XSL token marker
 * Copyright (C) 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package org.syntax.jedit.tokenmarker;

import org.syntax.jedit.*;

import javax.swing.text.Segment;


/**
 * XSL token marker.
 *
 * @author Rob Rohan
 * @version $Id: XSLTokenMarker.java,v 1.1.1.1 2005/12/08 01:34:37 yveszoundi Exp $
 */
public class XSLTokenMarker extends TokenMarker {
    //public static final byte JAVASCRIPT = Token.INTERNAL_FIRST;
    // private members
    private static KeywordMap keywords;
    private static KeywordMap XSLKeywords;

    //private boolean js = true;
    private int lastOffset;
    private int lastKeyword;

    public XSLTokenMarker() {
        this.keywords = getKeywords();
    }

    public byte markTokensImpl(byte token, Segment line, int lineIndex) {
        char[] array = line.array;
        int offset = line.offset;
        lastOffset = offset;
        lastKeyword = offset;

        int length = line.count + offset;
        boolean backslash = false;
loop: 
        for (int i = offset; i < length; i++) {
            int i1 = (i + 1);

            char c = array[i];

            if (c == '\\') {
                backslash = !backslash;

                continue;
            }

            switch (token) {
            ///////////////////////////////////////////////////////////////////
            case Token.NULL:

                switch (c) {
                case '<':

                    if (SyntaxUtilities.regionMatches(false, line, i1, "!--")) {
                        addToken(i - lastOffset, token);
                        lastOffset = lastKeyword = i;
                        i += 3;
                        token = Token.COMMENT1;
                    } else {
                        addToken(i1 - lastOffset, token);
                        token = Token.NULL;
                        lastOffset = lastKeyword = i1;
                    }

                    break;

                case '/':
                    addToken(i1 - lastOffset, token);
                    token = Token.NULL;
                    lastOffset = lastKeyword = i1;

                    break;

                case '&':
                    addToken(i - lastOffset, token);
                    lastOffset = lastKeyword = i;
                    token = Token.COMMENT2;

                    break;

                /*case '#':
                        if(backslash) backslash = false;
                        else if(cpp)
                        {
                                if(doKeyword(line,i,c))
                                        break;
                                addToken(i - lastOffset,token);
                                addToken(length - i,Token.KEYWORD2);
                                lastOffset = lastKeyword = length;
                                break loop;
                        }
                        break; */
                case '@':
                    doKeyword(line, i, c);

                    if (backslash) {
                        backslash = false;
                    } else {
                        addToken(i - lastOffset, token);
                        token = Token.OPERATOR;
                        lastOffset = lastKeyword = i;
                    }

                    break;

                case '$':
                    doKeyword(line, i, c);

                    if (backslash) {
                        backslash = false;
                    } else {
                        addToken(i - lastOffset, token);
                        token = Token.OPERATOR;
                        lastOffset = lastKeyword = i;
                    }

                    break;

                case '?':
                    doKeyword(line, i, c);

                    if (backslash) {
                        backslash = false;
                    } else {
                        addToken(i - lastOffset, token);
                        token = Token.OPERATOR;
                        lastOffset = lastKeyword = i;
                    }

                    break;

                case '\'':
                    doKeyword(line, i, c);

                    if (backslash) {
                        backslash = false;
                    } else {
                        addToken(i - lastOffset, token);
                        token = Token.LITERAL2;
                        lastOffset = lastKeyword = i;
                    }

                    break;

                /* case ':':
                        doKeyword(line,i,c);
                        if(backslash)
                                backslash = false;
                        else{
                                addToken(i - lastOffset,token);
                                token = Token.OPERATOR;
                                lastOffset = lastKeyword = i;
                        }
                        break; */

                /* case '/':
                        backslash = false;
                        doKeyword(line,i,c);
                        if(length - i > 1){
                                switch(array[i1]){
                                case '*':
                                        addToken(i - lastOffset,token);
                                        lastOffset = lastKeyword = i;
                                        if(length - i > 2 && array[i+2] == '*')
                                                token = Token.COMMENT2;
                                        else
                                                token = Token.COMMENT1;
                                        break;
                                case '/':
                                        addToken(i - lastOffset,token);
                                        addToken(length - i,Token.COMMENT1);
                                        lastOffset = lastKeyword = length;
                                        break loop;
                                }
                        }
                        break; */
                default:
                    backslash = false;

                    if (!Character.isLetterOrDigit(c) && (c != '_') &&
                            (c != '-')) { //&& c != '<')
                        doKeyword(line, i, c);
                    }

                    break;
                }

                break;

            ///////////////////////////////////////////////////////////////////
            case Token.COMMENT1:

                if (SyntaxUtilities.regionMatches(false, line, i, "-->")) {
                    addToken((i + 3) - lastOffset, token);
                    lastOffset = lastKeyword = i + 3;
                    token = Token.NULL;
                }

                break;

            ///////////////////////////////////////////////////////////////////
            case Token.COMMENT2:

                /*backslash = false;
                if(c == '*' && length - i > 1){
                        if(array[i1] == '/'){
                                i++;
                                addToken((i+1) - lastOffset,token);
                                token = Token.NULL;
                                lastOffset = lastKeyword = i+1;
                        }
                }
                break;*/
                if (c == ';') {
                    addToken(i1 - lastOffset, token);
                    lastOffset = lastKeyword = i1;
                    token = Token.NULL;

                    break;
                }

            ///////////////////////////////////////////////////////////////////
            case Token.LITERAL1:

                if (backslash) {
                    backslash = false;
                } else if (c == '"') {
                    addToken(i1 - lastOffset, token);
                    token = Token.NULL;
                    lastOffset = lastKeyword = i1;
                }

                break;

            ///////////////////////////////////////////////////////////////////
            case Token.LITERAL2:

                if (backslash) {
                    backslash = false;
                } else if (c == '\'') {
                    addToken(i1 - lastOffset, Token.LITERAL1);
                    token = Token.NULL;
                    lastOffset = lastKeyword = i1;
                }

                break;

            ///////////////////////////////////////////////////////////////////  
            case Token.OPERATOR:
                addToken(i - lastOffset, Token.OPERATOR);
                token = Token.NULL;
                lastOffset = lastKeyword = i;

                break;

            default:
                throw new InternalError("Invalid state: " + token);
            }
        }

        //if the token is still null check keyword?
        if (token == Token.NULL) {
            doKeyword(line, length, '\0');
        }

        switch (token) {
        case Token.LITERAL1:
        case Token.LITERAL2:
            addToken(length - lastOffset, Token.INVALID);
            token = Token.NULL;

            break;

        case Token.KEYWORD2:
            addToken(length - lastOffset, token);

            if (!backslash) {
                token = Token.NULL;
            }

        default:
            addToken(length - lastOffset, token);

            break;
        }

        return token;
    }

    public static KeywordMap getKeywords() {
        //get all of the keywords with style types
        //(maybe this should be in a file?)
        if (XSLKeywords == null) {
            XSLKeywords = new org.syntax.jedit.KeywordMap(false);

            //this will get populated when the xslt is analysed
            //but it will look like this:
            //XSLKeywords.add("xsl",Token.LABEL);
            //operators  //Axis
            XSLKeywords.add("self", Token.OPERATOR);
            XSLKeywords.add("child", Token.OPERATOR);
            XSLKeywords.add("parent", Token.OPERATOR);
            XSLKeywords.add("attribute", Token.OPERATOR);
            XSLKeywords.add("descendant", Token.OPERATOR);
            XSLKeywords.add("descendant-or-self", Token.OPERATOR);
            XSLKeywords.add("ancestor", Token.OPERATOR);
            XSLKeywords.add("ancestor-or-self", Token.OPERATOR);
            XSLKeywords.add("following-sibling", Token.OPERATOR);
            XSLKeywords.add("preceding-sibling", Token.OPERATOR);
            XSLKeywords.add("following", Token.OPERATOR);
            XSLKeywords.add("preceding", Token.OPERATOR);
            XSLKeywords.add("namespace", Token.OPERATOR);

            //////////////////////////////////////
            XSLKeywords.add(".", Token.OPERATOR);
            XSLKeywords.add("..", Token.OPERATOR);

            //XSLKeywords.add("//",Token.OPERATOR);
            XSLKeywords.add("::", Token.OPERATOR);
            XSLKeywords.add("or", Token.OPERATOR);
            XSLKeywords.add("and", Token.OPERATOR);
            XSLKeywords.add("=", Token.OPERATOR);
            XSLKeywords.add("!=", Token.OPERATOR);
            XSLKeywords.add("*", Token.OPERATOR);
            XSLKeywords.add("div", Token.OPERATOR);
            XSLKeywords.add("mod", Token.OPERATOR);
            XSLKeywords.add("true", Token.OPERATOR);
            XSLKeywords.add("false", Token.OPERATOR);
            XSLKeywords.add("|", Token.OPERATOR);
            XSLKeywords.add("+", Token.OPERATOR);
            XSLKeywords.add("-", Token.OPERATOR);

            //XSLKeywords.add("?",Token.OPERATOR);
            //XSLKeywords.add("$",Token.OPERATOR);
            //xpath functions
            XSLKeywords.add("boolean", Token.KEYWORD3);
            XSLKeywords.add("ceiling", Token.KEYWORD3);
            XSLKeywords.add("concat", Token.KEYWORD3);
            XSLKeywords.add("contains", Token.KEYWORD3);
            XSLKeywords.add("count", Token.KEYWORD3);
            XSLKeywords.add("current", Token.KEYWORD3);
            XSLKeywords.add("document", Token.KEYWORD3);
            XSLKeywords.add("element-available", Token.KEYWORD3);
            XSLKeywords.add("false", Token.KEYWORD3);
            XSLKeywords.add("floor", Token.KEYWORD3);
            XSLKeywords.add("format-number", Token.KEYWORD3);
            XSLKeywords.add("function-available", Token.KEYWORD3);
            XSLKeywords.add("generate-id", Token.KEYWORD3);
            XSLKeywords.add("id", Token.KEYWORD3);
            XSLKeywords.add("key", Token.KEYWORD3);
            XSLKeywords.add("lang", Token.KEYWORD3);
            XSLKeywords.add("last", Token.KEYWORD3);
            XSLKeywords.add("local-name", Token.KEYWORD3);
            XSLKeywords.add("name", Token.KEYWORD3);
            XSLKeywords.add("namespace-uri", Token.KEYWORD3);
            XSLKeywords.add("normalize-space", Token.KEYWORD3);
            XSLKeywords.add("not", Token.KEYWORD3);
            XSLKeywords.add("number", Token.KEYWORD3);
            XSLKeywords.add("position", Token.KEYWORD3);
            XSLKeywords.add("round", Token.KEYWORD3);
            XSLKeywords.add("starts-with", Token.KEYWORD3);
            XSLKeywords.add("string", Token.KEYWORD3);
            XSLKeywords.add("string-length", Token.KEYWORD3);
            XSLKeywords.add("substring", Token.KEYWORD3);
            XSLKeywords.add("substring-after", Token.KEYWORD3);
            XSLKeywords.add("substring-before", Token.KEYWORD3);
            XSLKeywords.add("sum", Token.KEYWORD3);
            XSLKeywords.add("system-property", Token.KEYWORD3);
            XSLKeywords.add("translate", Token.KEYWORD3);

            //the xsl:_______ tags
            XSLKeywords.add("apply-imports", Token.KEYWORD1);
            XSLKeywords.add("apply-templates", Token.KEYWORD1);
            XSLKeywords.add("attribute", Token.KEYWORD1);
            XSLKeywords.add("attribute-set", Token.KEYWORD1);
            XSLKeywords.add("call-template", Token.KEYWORD1);
            XSLKeywords.add("choose", Token.KEYWORD1);
            XSLKeywords.add("comment", Token.KEYWORD1);
            XSLKeywords.add("copy", Token.KEYWORD1);
            XSLKeywords.add("copy-of", Token.KEYWORD1);
            XSLKeywords.add("decimal-format", Token.KEYWORD1);
            XSLKeywords.add("element", Token.KEYWORD1);
            XSLKeywords.add("fallback", Token.KEYWORD1);
            XSLKeywords.add("for-each", Token.KEYWORD1);
            XSLKeywords.add("if", Token.KEYWORD1);
            XSLKeywords.add("import", Token.KEYWORD1);
            XSLKeywords.add("include", Token.KEYWORD1);
            XSLKeywords.add("key", Token.KEYWORD1);
            XSLKeywords.add("message", Token.KEYWORD1);
            XSLKeywords.add("namespace-alias", Token.KEYWORD1);
            XSLKeywords.add("number", Token.KEYWORD1);
            XSLKeywords.add("otherwise", Token.KEYWORD1);
            XSLKeywords.add("output", Token.KEYWORD1);
            XSLKeywords.add("param", Token.KEYWORD1);
            XSLKeywords.add("preserve-space", Token.KEYWORD1);
            XSLKeywords.add("processing-instruction", Token.KEYWORD1);
            XSLKeywords.add("sort", Token.KEYWORD1);
            XSLKeywords.add("strip-space", Token.KEYWORD1);
            XSLKeywords.add("stylesheet", Token.KEYWORD1);
            XSLKeywords.add("template", Token.KEYWORD1);
            XSLKeywords.add("text", Token.KEYWORD1);
            XSLKeywords.add("transform", Token.KEYWORD1);
            XSLKeywords.add("value-of", Token.KEYWORD1);
            XSLKeywords.add("variable", Token.KEYWORD1);
            XSLKeywords.add("when", Token.KEYWORD1);
            XSLKeywords.add("with-param", Token.KEYWORD1);

            //common attributes
            XSLKeywords.add("select", Token.KEYWORD2);
            XSLKeywords.add("name", Token.KEYWORD2);
            XSLKeywords.add("match", Token.KEYWORD2);
            XSLKeywords.add("version", Token.KEYWORD2);
            XSLKeywords.add("encoding", Token.KEYWORD2);
            XSLKeywords.add("xmlns", Token.KEYWORD2);
            XSLKeywords.add("indent", Token.KEYWORD2);
            XSLKeywords.add("count", Token.KEYWORD2);
            XSLKeywords.add("from", Token.KEYWORD2);
            XSLKeywords.add("value", Token.KEYWORD2);
            XSLKeywords.add("order", Token.KEYWORD2);
            XSLKeywords.add("test", Token.KEYWORD2);
            XSLKeywords.add("standalone", Token.KEYWORD2);
            XSLKeywords.add("mode", Token.KEYWORD2);
            XSLKeywords.add("namespace", Token.KEYWORD2);
            XSLKeywords.add("use-attribute-sets", Token.KEYWORD2);
            XSLKeywords.add("digit", Token.KEYWORD2);
            XSLKeywords.add("zero-digit", Token.KEYWORD2);
            XSLKeywords.add("decimal-separator", Token.KEYWORD2);
            XSLKeywords.add("grouping-separator", Token.KEYWORD2);
            XSLKeywords.add("minus-sign", Token.KEYWORD2);
            XSLKeywords.add("pattern-separator", Token.KEYWORD2);
            XSLKeywords.add("percent", Token.KEYWORD2);
            XSLKeywords.add("per-mille", Token.KEYWORD2);
            XSLKeywords.add("infinity", Token.KEYWORD2);
            XSLKeywords.add("NaN", Token.KEYWORD2);
            XSLKeywords.add("href", Token.KEYWORD2);
            XSLKeywords.add("use", Token.KEYWORD2);
            XSLKeywords.add("terminate", Token.KEYWORD2);
            XSLKeywords.add("stylesheet-prefix", Token.KEYWORD2);
            XSLKeywords.add("result-prefix", Token.KEYWORD2);
            XSLKeywords.add("level", Token.KEYWORD2);
            XSLKeywords.add("format", Token.KEYWORD2);
            XSLKeywords.add("lang", Token.KEYWORD2);
            XSLKeywords.add("letter-value", Token.KEYWORD2);
            XSLKeywords.add("grouping-separator", Token.KEYWORD2);
            XSLKeywords.add("grouping-size", Token.KEYWORD2);
            XSLKeywords.add("method", Token.KEYWORD2);
            XSLKeywords.add("media-type", Token.KEYWORD2);
            XSLKeywords.add("doctype-public", Token.KEYWORD2);
            XSLKeywords.add("doctype-system", Token.KEYWORD2);
            XSLKeywords.add("omit-xml-declarations", Token.KEYWORD2);
            XSLKeywords.add("cdata-section-elements", Token.KEYWORD2);
            XSLKeywords.add("elements", Token.KEYWORD2);
            XSLKeywords.add("data-type", Token.KEYWORD2);
            XSLKeywords.add("lang", Token.KEYWORD2);
            XSLKeywords.add("case-order", Token.KEYWORD2);
            XSLKeywords.add("id", Token.KEYWORD2);
            XSLKeywords.add("extension-element-prefixes", Token.KEYWORD2);
            XSLKeywords.add("exclude-result-prefixes", Token.KEYWORD2);
            XSLKeywords.add("priority", Token.KEYWORD2);
            XSLKeywords.add("disable-output-escaping", Token.KEYWORD2);
        }

        return XSLKeywords;
    }

    private boolean doKeyword(Segment line, int i, char c) {
        try {
            int i1 = i + 1;

            int len = i - lastKeyword;

            byte id = keywords.lookup(line, lastKeyword, len);

            if (id != Token.NULL) {
                if (lastKeyword != lastOffset) {
                    addToken(lastKeyword - lastOffset, Token.NULL);
                }

                addToken(len, id);
                lastOffset = i;
            }

            lastKeyword = i1;
        } catch (ArrayIndexOutOfBoundsException e) {
            System.err.println("XSLTokenMaker->doKeyword: " + e);
            e.printStackTrace(System.err);
        }

        return false;
    }
}
