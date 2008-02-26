/* Generated By:JavaCC: Do not edit this line. DTDParserConstants.java */
package net.sf.xpontus.plugins.lexer.dtd;

public interface DTDParserConstants
{
    int EOF = 0;
    int CRLF = 1;
    int TEXT = 2;
    int CONTENT = 3;
    int WS = 4;
    int DECL_START = 5;
    int PI_START = 6;
    int TEXT_IN_DEFAULT = 7;
    int COND_END_IN_DEFAULT = 8;
    int ERR_IN_DEFAULT = 9;
    int XML_TARGET = 10;
    int PI_TARGET = 11;
    int ERR_IN_PI = 12;
    int PI_CONTENT_START = 13;
    int PI_END = 14;
    int KW_IN_XML_DECL = 15;
    int TEXT_IN_XML_DECL = 16;
    int BR_IN_XML_DECL = 17;
    int XML_DECL_END = 18;
    int Q_IN_XML_DECL = 19;
    int TEXT_IN_PI_CONTENT = 20;
    int ERR_IN_PI_CONTENT = 21;
    int PI_CONTENT_END = 22;
    int BR_IN_PI_CONTENT = 23;
    int ENTITY = 24;
    int ATTLIST = 25;
    int DOCTYPE = 26;
    int ELEMENT = 27;
    int NOTATION = 28;
    int TEXT_IN_DECL = 29;
    int WS_IN_DECL = 30;
    int ERR_IN_DECL = 31;
    int COND = 32;
    int DECL_END = 33;
    int KW_IN_ENTITY = 34;
    int BR_IN_ENTITY = 35;
    int TEXT_IN_ENTITY = 36;
    int ENTITY_END = 37;
    int KW_IN_ELEMENT = 38;
    int SYMBOL_IN_ELEMENT = 39;
    int TEXT_IN_ELEMENT = 40;
    int ELEMENT_END = 41;
    int KW_IN_NOTATION = 42;
    int TEXT_IN_NOTATION = 43;
    int BR_IN_NOTATION = 44;
    int NOTATION_END = 45;
    int KW_IN_COND = 46;
    int TEXT_IN_COND = 47;
    int ERR_IN_COND = 48;
    int COND_END = 49;
    int ERR_IN_ATTLIST = 50;
    int KW_IN_ATTLIST = 51;
    int TEXT_IN_ATTLIST = 52;
    int ATTLIST_END = 53;
    int PREF_START = 54;
    int TEXT_IN_PREF = 55;
    int PREF_END = 56;
    int CHARS_START = 57;
    int TEXT_IN_CHARS = 58;
    int CHARS_END = 59;
    int STRING_START = 60;
    int TEXT_IN_STRING = 61;
    int STRING_END = 62;
    int COMMENT_START = 63;
    int TEXT_IN_COMMENT = 64;
    int ERR_IN_COMMENT = 65;
    int COMMENT_END = 66;
    int CHREF_START = 67;
    int CREF_START = 68;
    int TEXT_IN_CREF = 69;
    int CREF_END = 70;
    int ERR_IN_CREF = 71;
    int TEXT_IN_CHREF = 72;
    int CHREF_END = 73;
    int ERR_IN_CHREF = 74;
    int IN_CHREF = 0;
    int IN_CREF = 1;
    int IN_COMMENT = 2;
    int IN_STRING = 3;
    int IN_CHARS = 4;
    int IN_DOCTYPE = 5;
    int IN_TAG_ATTLIST = 6;
    int IN_PREF = 7;
    int IN_ATTLIST = 8;
    int IN_COND = 9;
    int IN_NOTATION = 10;
    int IN_ELEMENT = 11;
    int IN_ENTITY = 12;
    int IN_DECL = 13;
    int IN_PI_CONTENT = 14;
    int IN_XML_DECL = 15;
    int IN_PI = 16;
    int DEFAULT = 17;
    String[] tokenImage = 
        {
            "<EOF>", "\"\\n\"", "<TEXT>", "<CONTENT>", "<WS>", "\"<!\"",
            "\"<?\"", "<TEXT_IN_DEFAULT>", "\"]]>\"", "<ERR_IN_DEFAULT>",
            "\"xml\"", "<PI_TARGET>", "<ERR_IN_PI>", "<PI_CONTENT_START>",
            "\"?>\"", "<KW_IN_XML_DECL>", "<TEXT_IN_XML_DECL>",
            "<BR_IN_XML_DECL>", "\"?>\"", "\"?\"", "<TEXT_IN_PI_CONTENT>",
            "<ERR_IN_PI_CONTENT>", "\"?>\"", "\"?\"", "\"ENTITY\"",
            "\"ATTLIST\"", "\"DOCTYPE\"", "\"ELEMENT\"", "\"NOTATION\"",
            "<TEXT_IN_DECL>", "<WS_IN_DECL>", "<ERR_IN_DECL>", "\"[\"", "\">\"",
            "<KW_IN_ENTITY>", "<BR_IN_ENTITY>", "<TEXT_IN_ENTITY>", "\">\"",
            "<KW_IN_ELEMENT>", "<SYMBOL_IN_ELEMENT>", "<TEXT_IN_ELEMENT>",
            "\">\"", "<KW_IN_NOTATION>", "<TEXT_IN_NOTATION>",
            "<BR_IN_NOTATION>", "\">\"", "<KW_IN_COND>", "<TEXT_IN_COND>",
            "<ERR_IN_COND>", "\"[\"", "<ERR_IN_ATTLIST>", "<KW_IN_ATTLIST>",
            "<TEXT_IN_ATTLIST>", "\">\"", "\"%\"", "<TEXT_IN_PREF>",
            "<PREF_END>", "\"\\\'\"", "<TEXT_IN_CHARS>", "\"\\\'\"", "\"\\\"\"",
            "<TEXT_IN_STRING>", "\"\\\"\"", "\"<!--\"", "<TEXT_IN_COMMENT>",
            "<ERR_IN_COMMENT>", "\"-->\"", "\"&#x\"", "\"&#\"", "<TEXT_IN_CREF>",
            "<CREF_END>", "<ERR_IN_CREF>", "<TEXT_IN_CHREF>", "<CHREF_END>",
            "<ERR_IN_CHREF>",
        };
}
