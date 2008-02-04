// $ANTLR 2.7.7 (20060930): "xml.g" -> "XMLParser.java"$
package net.sf.xpontus.parsers;

import antlr.NoViableAltException;
import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.SemanticException;
import antlr.Token;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.TokenStreamException;

import antlr.collections.impl.BitSet;
import antlr.collections.impl.BitSet;

import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import javax.swing.tree.DefaultMutableTreeNode;


public class XMLParser extends antlr.LLkParser implements XMLParserTokenTypes {
    public static final String[] _tokenNames = {
            "<0>", "EOF", "<2>", "NULL_TREE_LOOKAHEAD", "XMLDECL", "DOCTYPE",
            "EMPTYTAG", "STARTTAG", "ENDTAG", "CDATABLOCK", "PCDATA", "COMMENT",
            "PI", "INTERNAL_DTD", "PI_OR_XMLDECL", "COMMENT_DATA", "PCDATA_DATA",
            "INTERNAL_CDATA_DATA", "ATTR", "STRING_NO_QUOTE", "STRING", "NAME",
            "NAMECHAR", "DIGIT", "LETTER", "WS", "ESC", "NL"
        };
    public static final BitSet _tokenSet_0 = new BitSet(mk_tokenSet_0());
    public static final BitSet _tokenSet_1 = new BitSet(mk_tokenSet_1());
    public static final BitSet _tokenSet_2 = new BitSet(mk_tokenSet_2());
    public static final BitSet _tokenSet_3 = new BitSet(mk_tokenSet_3());
    public static final BitSet _tokenSet_4 = new BitSet(mk_tokenSet_4());
    public static final BitSet _tokenSet_5 = new BitSet(mk_tokenSet_5());
    public static final BitSet _tokenSet_6 = new BitSet(mk_tokenSet_6());
    java.util.Stack stack = new java.util.Stack();
    private DefaultMutableTreeNode current;
    private DefaultMutableTreeNode root;
    org.xml.sax.ContentHandler contentHandler;
    org.xml.sax.ErrorHandler errorHandler;
    XMLParserLocator locator = new XMLParserLocator();
    java.util.HashMap elm2attributes = new java.util.HashMap();

    public XMLParser(TokenStream lexer) {
        this(lexer, 1);
        root = new DefaultMutableTreeNode();
        this.current = root;
    }

    protected XMLParser(TokenBuffer tokenBuf, int k) {
        super(tokenBuf, k);
        tokenNames = _tokenNames;
    }

    public XMLParser(TokenBuffer tokenBuf) {
        this(tokenBuf, 1);
    }

    protected XMLParser(TokenStream lexer, int k) {
        super(lexer, k);
        tokenNames = _tokenNames;
    }

    public XMLParser(ParserSharedInputState state) {
        super(state, 1);
        tokenNames = _tokenNames;
    }

    public DefaultMutableTreeNode getRootNode() {
        return root;
    }

    public void setAttributes(java.util.HashMap attributes) {
        elm2attributes = attributes;
    }

    public void setContentHandler(org.xml.sax.ContentHandler handler) {
        this.contentHandler = handler;
    }

    public void setErrorHandler(org.xml.sax.ErrorHandler handler) {
        this.errorHandler = handler;
    }

    boolean correctEndTag(String endTag) {
        String peekName = (String) this.stack.peek();

        return endTag.equals(peekName);
    }

    void startTag(Token token) {
        try {
            if (contentHandler != null) {
                AttributesImpl attrs = (AttributesImpl) this.elm2attributes.get(token.getText());
                contentHandler.startElement(null, token.getText(),
                    token.getText(), attrs);
            }

            stack.push(token.getText());

            DefaultMutableTreeNode element = new XmlNode(token);
            current.add(element);
            current = element;
        } catch (SAXException ex) {
            ex.printStackTrace();
        }
    }

    void endTag(Token token) throws SemanticException {
        try {
            if (contentHandler != null) {
                contentHandler.endElement(null, token.getText(), token.getText());
            }

            if (!correctEndTag(token.getText())) {
                throw new SemanticException("expecting end tag '" +
                    token.getText() + "'" + token.getLine());
            }

            stack.pop();
            current = (DefaultMutableTreeNode) current.getParent();
        } catch (SAXException ex) {
            ex.printStackTrace();
        }
    }

    public void reportError(RecognitionException arg0) {
        try {
            if (errorHandler != null) {
                XMLParserLocator locator = new XMLParserLocator();
                locator.setColNumber(arg0.getColumn());
                locator.setRowNumber(arg0.getLine());

                SAXParseException parseException = new SAXParseException(arg0.getErrorMessage(),
                        locator);
                this.errorHandler.error(parseException);
            }
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void startDocument() {
        try {
            if (this.contentHandler != null) {
                this.contentHandler.setDocumentLocator(locator);
                this.contentHandler.startDocument();
            }
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public void endDocument() {
        try {
            if (this.contentHandler != null) {
                this.contentHandler.endDocument();
            }
        } catch (SAXException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public final void parse() throws RecognitionException, TokenStreamException {
        try { // for error handling
            document();
        } catch (RecognitionException ex) {
            reportError(ex);
            recover(ex, _tokenSet_0);
        }
    }

    public final void document()
        throws RecognitionException, TokenStreamException {
        try { // for error handling
            startDocument();
            prolog();
            element();
_loop4: 
            do {
                if ((((LA(1) >= PCDATA) && (LA(1) <= PI)))) {
                    misc();
                } else {
                    break _loop4;
                }
            } while (true);

            endDocument();
        } catch (RecognitionException ex) {
            reportError(ex);
            recover(ex, _tokenSet_0);
        }
    }

    public final void prolog()
        throws RecognitionException, TokenStreamException {
        try { // for error handling

            switch (LA(1)) {
            case XMLDECL: {
                match(XMLDECL);

                break;
            }

            case DOCTYPE:
            case EMPTYTAG:
            case STARTTAG:
            case PCDATA:
            case COMMENT:
            case PI:
                break;

            default:
                throw new NoViableAltException(LT(1), getFilename());
            }

_loop8: 
            do {
                if ((((LA(1) >= PCDATA) && (LA(1) <= PI)))) {
                    misc();
                } else {
                    break _loop8;
                }
            } while (true);

            switch (LA(1)) {
            case DOCTYPE: {
                match(DOCTYPE);
_loop11: 
                do {
                    if ((((LA(1) >= PCDATA) && (LA(1) <= PI)))) {
                        misc();
                    } else {
                        break _loop11;
                    }
                } while (true);

                break;
            }

            case EMPTYTAG:
            case STARTTAG:
                break;

            default:
                throw new NoViableAltException(LT(1), getFilename());
            }
        } catch (RecognitionException ex) {
            reportError(ex);
            recover(ex, _tokenSet_1);
        }
    }

    public final void element()
        throws RecognitionException, TokenStreamException {
        Token empty = null;
        Token s = null;
        Token e = null;

        try { // for error handling

            switch (LA(1)) {
            case EMPTYTAG: {
                empty = LT(1);
                match(EMPTYTAG);
                locator.setColNumber(empty.getColumn());
                locator.setRowNumber(empty.getLine());
                startTag(empty);

                locator.setColNumber(empty.getColumn() +
                    empty.getText().length());
                locator.setRowNumber(empty.getLine());
                endTag(empty);

                break;
            }

            case STARTTAG: {
                s = LT(1);
                match(STARTTAG);

                locator.setColNumber(s.getColumn());
                locator.setRowNumber(s.getLine());
                startTag(s);
                content();
                e = LT(1);
                match(ENDTAG);

                locator.setColNumber(e.getColumn() + e.getText().length() + 3);
                locator.setRowNumber(e.getLine());
                endTag(e);

                break;
            }

            default:
                throw new NoViableAltException(LT(1), getFilename());
            }
        } catch (RecognitionException ex) {
            reportError(ex);
            recover(ex, _tokenSet_2);
        }
    }

    public final void misc() throws RecognitionException, TokenStreamException {
        try { // for error handling

            switch (LA(1)) {
            case COMMENT: {
                match(COMMENT);

                break;
            }

            case PI: {
                match(PI);

                break;
            }

            case PCDATA: {
                match(PCDATA);

                break;
            }

            default:
                throw new NoViableAltException(LT(1), getFilename());
            }
        } catch (RecognitionException ex) {
            reportError(ex);
            recover(ex, _tokenSet_3);
        }
    }

    public final void content()
        throws RecognitionException, TokenStreamException {
        try { // for error handling
_loop15: 
            do {
                if ((_tokenSet_4.member(LA(1)))) {
                    element_or_data();
                } else {
                    break _loop15;
                }
            } while (true);
        } catch (RecognitionException ex) {
            reportError(ex);
            recover(ex, _tokenSet_5);
        }
    }

    public final void element_or_data()
        throws RecognitionException, TokenStreamException {
        try { // for error handling

            switch (LA(1)) {
            case EMPTYTAG:
            case STARTTAG: {
                element();

                break;
            }

            case CDATABLOCK: {
                match(CDATABLOCK);

                break;
            }

            case PCDATA: {
                match(PCDATA);

                break;
            }

            case COMMENT: {
                match(COMMENT);

                break;
            }

            default:
                throw new NoViableAltException(LT(1), getFilename());
            }
        } catch (RecognitionException ex) {
            reportError(ex);
            recover(ex, _tokenSet_6);
        }
    }

    private static final long[] mk_tokenSet_0() {
        long[] data = { 2L, 0L };

        return data;
    }

    private static final long[] mk_tokenSet_1() {
        long[] data = { 192L, 0L };

        return data;
    }

    private static final long[] mk_tokenSet_2() {
        long[] data = { 8130L, 0L };

        return data;
    }

    private static final long[] mk_tokenSet_3() {
        long[] data = { 7394L, 0L };

        return data;
    }

    private static final long[] mk_tokenSet_4() {
        long[] data = { 3776L, 0L };

        return data;
    }

    private static final long[] mk_tokenSet_5() {
        long[] data = { 256L, 0L };

        return data;
    }

    private static final long[] mk_tokenSet_6() {
        long[] data = { 4032L, 0L };

        return data;
    }
}
