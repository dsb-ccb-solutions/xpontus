/*
 * XQueryTokenMarker.java - XQuery token marker
 * Copyright (C) 2004 Rob Rohan
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */

package org.syntax.jedit.tokenmarker;

import javax.swing.text.Segment;
import org.syntax.jedit.KeywordMap;
import org.syntax.jedit.SyntaxUtilities;




/**
 * XQuery token marker - I am just learning XQuery so this could be tottaly wrong.
 * I am using XSLT as a base...
 *
 * @author Rob Rohan
 */
public class XQueryTokenMarker extends TokenMarker {
    // private members
    private static KeywordMap keywords;
    //private boolean js = true;
    private int lastOffset;
    private int lastKeyword;
    
    private static KeywordMap XQueryKeywords;
    
    public XQueryTokenMarker(){
        XQueryTokenMarker.keywords = getKeywords();
    }
    
    public byte markTokensImpl(byte token, Segment line, int lineIndex){
        char[] array = line.array;
        int offset = line.offset;
        lastOffset = offset;
        lastKeyword = offset;
        int length = line.count + offset;
        boolean backslash = false;
        
        //Ugly hack to handle multi-line tags
        boolean sk1 = token == Token.KEYWORD1;
        
        loop:
            for(int i = offset; i < length; i++){
            int i1 = (i+1);
            int ip1 = i+1;
            
            char c = array[i];
            if(c == '\\'){
                backslash = !backslash;
                continue;
            }
            
            switch(token){
                ///////////////////////////////////////////////////////////////////
                case Token.NULL:
                    switch(c){
                        case '<':
                            if(SyntaxUtilities.regionMatches(false, line,i1,"=")
                            || SyntaxUtilities.regionMatches(false, line,i1," ")
                            ){
                                addToken(i - lastOffset,token);
                                lastOffset = lastKeyword = i;
                                i += 1;
                                token = Token.NULL;
                            }else{
                                /* if(SyntaxUtilities.regionMatches(false, line,i1,"!--")){
                                    addToken(i - lastOffset,token);
                                    lastOffset = lastKeyword = i;
                                    i += 3;
                                    token = Token.COMMENT1;
                                    }else{ */
                                addToken(i-lastOffset, token);
                                lastOffset = i;
                                //addToken(i1 - lastOffset,token);
                                //token = Token.NULL;
                                token = Token.KEYWORD1;
                                //lastOffset = lastKeyword = i1;
                            }
                            break;
                        case '(':
                            doKeyword(line,i,c);
                            if(SyntaxUtilities.regionMatches(false, line,i1,":")){
                                addToken(i - lastOffset,token);
                                lastOffset = lastKeyword = i;
                                i += 1;
                                token = Token.COMMENT1;
                            }
                            break;
                            //case '/':
                            //    addToken(i1 - lastOffset,token);
                            //token = Token.NULL;
                            //    token = Token.OPERATOR;
                            //    lastOffset = lastKeyword = i1;
                            //    break;
                        case '&':
                            addToken(i - lastOffset,token);
                            lastOffset = lastKeyword = i;
                            token = Token.COMMENT2;
                            break;
                        case '@':
                            doKeyword(line,i,c);
                            //if(backslash)
                            //    backslash = false;
                            //else{
                            addToken(i - lastOffset,token);
                            token = Token.OPERATOR;
                            lastOffset = lastKeyword = i;
                            //}
                            break;
                        case '$':
                            doKeyword(line,i,c);
                            //if(backslash)
                            //    backslash = false;
                            //else{
                            addToken(i - lastOffset,token);
                            token = Token.OPERATOR;
                            lastOffset = lastKeyword = i;
                            //}
                            break;
                        case '?':
                            doKeyword(line,i,c);
                            //if(backslash)
                            //    backslash = false;
                            //else{
                            addToken(i - lastOffset,token);
                            token = Token.OPERATOR;
                            lastOffset = lastKeyword = i;
                            //}
                            break;
                        case '\"':
                        case '\'':
                            doKeyword(line,i,c);
                            //if(backslash)
                            //    backslash = false;
                            //else{
                            addToken(i - lastOffset,token);
                            token = Token.LITERAL2;
                            lastOffset = lastKeyword = i;
                            //}
                            break;
                        default:
                            backslash = false;
                            if(!Character.isLetterOrDigit(c) && c != '_' && c != '-' )
                                doKeyword(line,i,c);
                            break;
                    }
                    break;
                    
                case Token.KEYWORD1: // tag
                    switch(c) {
                        case '>':
                            addToken(ip1-lastOffset, token);
                            lastOffset = ip1;
                            token = Token.NULL;
                            sk1 = false;
                            break;
                            
                        case ' ':
                        case '\t':
                            addToken(i-lastOffset, token);
                            lastOffset = i;
                            token = Token.KEYWORD2;
                            sk1 = false;
                            break;
                        default:
                            if(sk1) {
                                token = Token.KEYWORD2;
                                sk1 = false;
                            }
                            break;
                    }
                    break;
                    
                case Token.KEYWORD2: // attribute
                    switch(c) {
                        case '>':
                            addToken(ip1-lastOffset, token);
                            lastOffset = ip1;
                            token = Token.NULL;
                            break;
                            
                        case '/':
                            addToken(i-lastOffset, token);
                            lastOffset = i;
                            token = Token.KEYWORD1;
                            break;
                        /* case '=':
                            addToken(i-lastOffset, token);
                            lastOffset = i;
                            token = Token.OPERATOR; */
                    }
                    break;
                    
                    /////////////////////////////////////////////////////////////////
                case Token.COMMENT1:
                    if(SyntaxUtilities.regionMatches(false,line,i,":)")) {
                        addToken((i + 2) - lastOffset,token);
                        lastOffset = lastKeyword = i + 2;
                        token = Token.NULL;
                    }
                    break;
                    
                    ///////////////////////////////////////////////////////////////////
                case Token.COMMENT2:
                    if(c == ';'){
                        addToken(i1 - lastOffset,token);
                        lastOffset = lastKeyword = i1;
                        token = Token.NULL;
                        break;
                    }
                    ///////////////////////////////////////////////////////////////////
                    //case Token.LITERAL1:
                    /* if(backslash)
                            backslash = false;
                    else if(c == '"'){
                            addToken(i1 - lastOffset,token);
                            token = Token.NULL;
                            lastOffset = lastKeyword = i1;
                    }
                break; */
                    ///////////////////////////////////////////////////////////////////
                case Token.LITERAL2:
                    if(backslash)
                        backslash = false;
                    else if(c == '\'' || c == '\"'){
                        addToken(i1 - lastOffset,Token.LITERAL1);
                        token = Token.NULL;
                        lastOffset = lastKeyword = i1;
                    }
                    break;
                    
                case Token.OPERATOR:
                    addToken(i - lastOffset,Token.OPERATOR);
                    token = Token.NULL;
                    lastOffset = lastKeyword = i;
                    break;
                    
                default:
                    throw new InternalError("Invalid state: " + token);
            }
            }
        
        //if the token is still null check keyword?
        if(token == Token.NULL) doKeyword(line,length,'\0');
        
        switch(token){
            /* case Token.LITERAL1:
            case Token.LITERAL2:
                    addToken(length - lastOffset,Token.INVALID);
                    token = Token.NULL;
                    break; */
            case Token.KEYWORD2:
                addToken(length - lastOffset,token);
                
                if(!backslash) token = Token.NULL;
            default:
                addToken(length - lastOffset,token);
                break;
        }
        
        return token;
    }
    
    public static KeywordMap getKeywords(){
        //get all of the keywords with style types
        //(maybe this should be in a file?)
        if(XQueryKeywords == null){
            XQueryKeywords = new KeywordMap(false);
            
            //this will get populated when the xslt is analysed
            //but it will look like this:
            //XQueryKeywords.add("xsl",Token.LABEL);
            
            //operators  //Axis
            XQueryKeywords.add("self",Token.OPERATOR);
            XQueryKeywords.add("child",Token.OPERATOR);
            XQueryKeywords.add("parent",Token.OPERATOR);
            XQueryKeywords.add("attribute",Token.OPERATOR);
            XQueryKeywords.add("descendant",Token.OPERATOR);
            XQueryKeywords.add("descendant-or-self",Token.OPERATOR);
            XQueryKeywords.add("ancestor",Token.OPERATOR);
            XQueryKeywords.add("ancestor-or-self",Token.OPERATOR);
            XQueryKeywords.add("following-sibling",Token.OPERATOR);
            XQueryKeywords.add("preceding-sibling",Token.OPERATOR);
            XQueryKeywords.add("following",Token.OPERATOR);
            XQueryKeywords.add("preceding",Token.OPERATOR);
            XQueryKeywords.add("namespace",Token.OPERATOR);
            //////////////////////////////////////
            XQueryKeywords.add(".",Token.OPERATOR);
            XQueryKeywords.add("..",Token.OPERATOR);
            //XQueryKeywords.add("//",Token.OPERATOR);
            XQueryKeywords.add("::",Token.OPERATOR);
            XQueryKeywords.add("or",Token.OPERATOR);
            XQueryKeywords.add("and",Token.OPERATOR);
            XQueryKeywords.add("=",Token.OPERATOR);
            XQueryKeywords.add("!=",Token.OPERATOR);
            XQueryKeywords.add("*",Token.OPERATOR);
            XQueryKeywords.add("div",Token.OPERATOR);
            XQueryKeywords.add("mod",Token.OPERATOR);
            XQueryKeywords.add("true",Token.OPERATOR);
            XQueryKeywords.add("false",Token.OPERATOR);
            XQueryKeywords.add("|",Token.OPERATOR);
            XQueryKeywords.add("+",Token.OPERATOR);
            XQueryKeywords.add("-",Token.OPERATOR);
            //XQueryKeywords.add("?",Token.OPERATOR);
            //XQueryKeywords.add("$",Token.OPERATOR);
            
            /////////////////////////////////////////////////////////////////////////
            
            XQueryKeywords.add("declare",Token.KEYWORD3);
            XQueryKeywords.add("namespace",Token.KEYWORD3);
            XQueryKeywords.add("function",Token.KEYWORD3);
            XQueryKeywords.add("return",Token.KEYWORD3);
            XQueryKeywords.add("let",Token.KEYWORD3);
            XQueryKeywords.add("for",Token.KEYWORD3);
            XQueryKeywords.add("in",Token.KEYWORD3);
            XQueryKeywords.add("some",Token.KEYWORD3);
            XQueryKeywords.add("order",Token.KEYWORD3);
            XQueryKeywords.add("by",Token.KEYWORD3);
            XQueryKeywords.add("as",Token.KEYWORD3);
            XQueryKeywords.add("where",Token.KEYWORD3);
            XQueryKeywords.add("every",Token.KEYWORD3);
            
            XQueryKeywords.add("satisfies",Token.KEYWORD3);
            XQueryKeywords.add("intersect",Token.KEYWORD3);
            XQueryKeywords.add("collation",Token.KEYWORD3);
            
            XQueryKeywords.add("if",Token.KEYWORD3);
            XQueryKeywords.add("then",Token.KEYWORD3);
            XQueryKeywords.add("else",Token.KEYWORD3);
            
            
            //xpath functions
            XQueryKeywords.add("boolean",Token.LABEL);
            XQueryKeywords.add("ceiling",Token.LABEL);
            XQueryKeywords.add("concat",Token.LABEL);
            XQueryKeywords.add("contains",Token.LABEL);
            XQueryKeywords.add("count",Token.LABEL);
            XQueryKeywords.add("current",Token.LABEL);
            XQueryKeywords.add("document",Token.LABEL);
            XQueryKeywords.add("element-available",Token.LABEL);
            XQueryKeywords.add("false",Token.LABEL);
            XQueryKeywords.add("floor",Token.LABEL);
            XQueryKeywords.add("format-number",Token.LABEL);
            XQueryKeywords.add("function-available",Token.LABEL);
            XQueryKeywords.add("generate-id",Token.LABEL);
            XQueryKeywords.add("id",Token.LABEL);
            XQueryKeywords.add("key",Token.LABEL);
            XQueryKeywords.add("lang",Token.LABEL);
            XQueryKeywords.add("last",Token.LABEL);
            XQueryKeywords.add("local-name",Token.LABEL);
            XQueryKeywords.add("name",Token.LABEL);
            XQueryKeywords.add("namespace-uri",Token.LABEL);
            XQueryKeywords.add("normalize-space",Token.LABEL);
            XQueryKeywords.add("not",Token.LABEL);
            XQueryKeywords.add("number",Token.LABEL);
            XQueryKeywords.add("position",Token.LABEL);
            XQueryKeywords.add("round",Token.LABEL);
            XQueryKeywords.add("starts-with",Token.LABEL);
            XQueryKeywords.add("string",Token.LABEL);
            XQueryKeywords.add("string-length",Token.LABEL);
            XQueryKeywords.add("substring",Token.LABEL);
            XQueryKeywords.add("substring-after",Token.LABEL);
            XQueryKeywords.add("substring-before",Token.LABEL);
            XQueryKeywords.add("sum",Token.LABEL);
            XQueryKeywords.add("system-property",Token.LABEL);
            XQueryKeywords.add("translate",Token.LABEL);
            
            //I dont know if these are valid...
            XQueryKeywords.add("avg",Token.LABEL);
            XQueryKeywords.add("base-uri",Token.LABEL);
            XQueryKeywords.add("codepoints-to-string",Token.LABEL);
            XQueryKeywords.add("compare",Token.LABEL);
            XQueryKeywords.add("context-item",Token.LABEL);
            XQueryKeywords.add("current-date",Token.LABEL);
            XQueryKeywords.add("current-dateTime",Token.LABEL);
            XQueryKeywords.add("current-time",Token.LABEL);
            XQueryKeywords.add("data",Token.LABEL);
            XQueryKeywords.add("dayTimeDuration-from-seconds",Token.LABEL);
            XQueryKeywords.add("deep-equal",Token.LABEL);
            XQueryKeywords.add("default-collation",Token.LABEL);
            XQueryKeywords.add("distinct-nodes",Token.LABEL);
            XQueryKeywords.add("distinct-values",Token.LABEL);
            XQueryKeywords.add("empty",Token.LABEL);
            XQueryKeywords.add("ends-with",Token.LABEL);
            XQueryKeywords.add("error",Token.LABEL);
            XQueryKeywords.add("escape-uri",Token.LABEL);
            XQueryKeywords.add("exists",Token.LABEL);
            XQueryKeywords.add("expanded-QName",Token.LABEL);
            XQueryKeywords.add("get-day-from-date",Token.LABEL);
            XQueryKeywords.add("get-day-from-dateTime",Token.LABEL);
            XQueryKeywords.add("get-days-from-dayTimeDuration",Token.LABEL);
            XQueryKeywords.add("get-dayTimeDuration-from-dateTimes",Token.LABEL);
            XQueryKeywords.add("get-hours-from-dateTime",Token.LABEL);
            XQueryKeywords.add("get-hours-from-dayTimeDuration",Token.LABEL);
            XQueryKeywords.add("get-hours-from-time",Token.LABEL);
            XQueryKeywords.add("get-in-scope-namespaces",Token.LABEL);
            XQueryKeywords.add("get-local-name-from-QName",Token.LABEL);
            XQueryKeywords.add("get-minutes-from-dateTime",Token.LABEL);
            XQueryKeywords.add("get-minutes-from-dayTimeDuration",Token.LABEL);
            XQueryKeywords.add("get-minutes-from-time",Token.LABEL);
            XQueryKeywords.add("get-month-from-date",Token.LABEL);
            XQueryKeywords.add("get-month-from-dateTime",Token.LABEL);
            XQueryKeywords.add("get-months-from-yearMonthDuration",Token.LABEL);
            XQueryKeywords.add("get-namespace-uri-for-prefix",Token.LABEL);
            XQueryKeywords.add("get-seconds-from-dateTime",Token.LABEL);
            XQueryKeywords.add("get-seconds-from-dayTimeDuration",Token.LABEL);
            XQueryKeywords.add("get-seconds-from-time",Token.LABEL);
            XQueryKeywords.add("get-timezone-from-date",Token.LABEL);
            XQueryKeywords.add("get-timezone-from-dateTime",Token.LABEL);
            XQueryKeywords.add("get-timezone-from-time",Token.LABEL);
            XQueryKeywords.add("get-year-from-date",Token.LABEL);
            XQueryKeywords.add("get-year-from-dateTime",Token.LABEL);
            XQueryKeywords.add("get-yearMonthDuration-from-dateTimes",Token.LABEL);
            XQueryKeywords.add("get-years-from-yearMonthDuration",Token.LABEL);
            XQueryKeywords.add("index-of",Token.LABEL);
            XQueryKeywords.add("input",Token.LABEL);
            XQueryKeywords.add("insert",Token.LABEL);
            XQueryKeywords.add("item-at",Token.LABEL);
            XQueryKeywords.add("lang",Token.LABEL);
            XQueryKeywords.add("last",Token.LABEL);
            XQueryKeywords.add("lower-case",Token.LABEL);
            XQueryKeywords.add("matches",Token.LABEL);
            XQueryKeywords.add("max",Token.LABEL);
            XQueryKeywords.add("min",Token.LABEL);
            XQueryKeywords.add("normalize-unicode",Token.LABEL);
            XQueryKeywords.add("remove",Token.LABEL);
            XQueryKeywords.add("replace",Token.LABEL);
            XQueryKeywords.add("root",Token.LABEL);
            XQueryKeywords.add("sequence-deep-equal",Token.LABEL);
            XQueryKeywords.add("sequence-node-equal",Token.LABEL);
            XQueryKeywords.add("string-join",Token.LABEL);
            XQueryKeywords.add("string-pad",Token.LABEL);
            XQueryKeywords.add("string-to-codepoints",Token.LABEL);
            XQueryKeywords.add("subsequence",Token.LABEL);
            XQueryKeywords.add("tokenize",Token.LABEL);
            XQueryKeywords.add("unparsed-entity-uri",Token.LABEL);
            XQueryKeywords.add("unparsed-entity-public-id",Token.LABEL);
            XQueryKeywords.add("unparsed-text",Token.LABEL);
            XQueryKeywords.add("unordered",Token.LABEL);
            XQueryKeywords.add("upper-case",Token.LABEL);
            
        }
        
        return XQueryKeywords;
    }
    
    private boolean doKeyword(Segment line, int i, char c){
        try{
            int i1 = i+1;
            int len = i - lastKeyword;
            if(len >= 0){
                byte id = keywords.lookup(line,lastKeyword,len);
                if(id != Token.NULL){
                    if(lastKeyword != lastOffset){
                        addToken(lastKeyword - lastOffset,Token.NULL);
                    }
                    addToken(len,id);
                    lastOffset = i;
                }
            }
            
            lastKeyword = i1;
            
        }catch(ArrayIndexOutOfBoundsException e){
            System.err.println("XQueryTokenMaker->doKeyword: " + e);
            e.printStackTrace(System.err);
        }
        return false;
    }
}