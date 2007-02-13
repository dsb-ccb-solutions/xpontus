/*

Rudimentary lexer grammar for a non-validating XML parser.
Lexer is not intended to be used by parser, but is standalone.
Use something like

            while ( lexer.nextToken().getType() != Token.EOF_TYPE );

to iterate through tokens.

Replace print statements (only there to make something visible) with your 
own code and have fun.

Limitations:
- internal DTD is parsed but not processed
- only supported encoding is iso-8859-1 aka extended ASCII aka ISO-latin-1
- special entity references (like &amp; &lt;) do not get resolved (to '&', '<')
- uses SAX attribute implementation (could easily be dropped)
  [TJP: commented out so it compiles w/o SAX.]
- probably many more 

The good thing about some of these limitations is, that the parsed XML
can be written *literally* unmodified.

Author: Olli Z. (oliver@zeigermann.de)

Initial date: 07.02.1999 (02/07/99)
Complete revision: 16.01.2003 (01/16/03)

Developed and testes with ANTLR 2.7.2
*/
header {
  package net.sf.xpontus.parsers;

import java.util.HashMap;
import java.util.Stack;

import javax.swing.tree.DefaultMutableTreeNode;
import net.infonode.properties.propertymap.ref.ThisPropertyMapRef;

import net.sf.xpontus.view.XPontusWindow;

import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import antlr.NoViableAltException;
import antlr.ParserSharedInputState;
import antlr.RecognitionException;
import antlr.SemanticException;
import antlr.Token;
import antlr.TokenBuffer;
import antlr.TokenStream;
import antlr.TokenStreamException;
import antlr.collections.impl.BitSet;
}

class XMLParser extends Parser;
{
	java.util.Stack stack = new java.util.Stack();
 private DefaultMutableTreeNode current;
    
   	private DefaultMutableTreeNode root;
	org.xml.sax.ContentHandler contentHandler;
	org.xml.sax.ErrorHandler errorHandler;
	
	XMLParserLocator locator = new XMLParserLocator();

public DefaultMutableTreeNode getRootNode() {
		return root;
	}

	public XMLParser(TokenStream lexer) {
		this(lexer, 1);
		root = XPontusWindow.getInstance().getOutlineDockable().getRootNode();
		root.removeAllChildren();
		this.current = root;
	}

	java.util.HashMap elm2attributes = new java.util.HashMap();    

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
		String peekName = (String)this.stack.peek();
		return endTag.equals(peekName);
	}
	
        void startTag(Token token) {
		try {

			if (contentHandler != null) {
				AttributesImpl attrs = (AttributesImpl) this.elm2attributes
						.get(token.getText());
				contentHandler.startElement(null, token.getText(), token
						.getText(), attrs);
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
				contentHandler.endElement(null, token.getText(), token
						.getText());
			}

			if (!correctEndTag(token.getText())) {
				throw new SemanticException("expecting end tag '"
						+ token.getText() + "'" + token.getLine());
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
    	        SAXParseException parseException = new SAXParseException(arg0.getErrorMessage(),locator);
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
    
}


parse: document ;
		

document
        : {startDocument();} prolog element  (misc)* { endDocument(); }
        ;


prolog
        : (XMLDECL)? (misc)* (DOCTYPE (misc)*)?
        ;


element
    :  empty:EMPTYTAG {	locator.setColNumber(empty.getColumn());
		                locator.setRowNumber(empty.getLine());
    					startTag(empty); 
    					
    					locator.setColNumber(empty.getColumn()+empty.getText().length());
		                locator.setRowNumber(empty.getLine());
    					endTag(empty); 
    				 }
    |  s:STARTTAG   { 
				    locator.setColNumber(s.getColumn());
		            locator.setRowNumber(s.getLine());
    				startTag(s); } content e:ENDTAG 
    				
    				{ 
    				locator.setColNumber(e.getColumn()+e.getText().length()+3);
		            locator.setRowNumber(e.getLine());
    				endTag(e); }
    ;



content
    :  (element_or_data)* 
    ;

element_or_data
    : element
    | CDATABLOCK
    | PCDATA 
    | COMMENT
    ;

misc
    : COMMENT
    | PI
    | PCDATA
    ;






class XMLLexer extends Lexer;
options {
    // needed to tell "<!DOCTYPE..."
    // from "<?..." and "<tag..." and "</tag...>" and "<![CDATA...>"
    // also on exit branch "]]>", "-->"
	k=3;
	charVocabulary = '\3'..'\377'; // extended ASCII (3-255 in octal notation)
	caseSensitive=true;
}

tokens {
    PI;
    XMLDECL;
    EMPTYTAG;
}

{

java.util.HashMap elm2attributes = new java.util.HashMap();    

private String dtdLocation = null;
private String schemaLocation = null;
private String dtdpubid = null;
public String getDTDLocation(){
    return dtdLocation;
}

public String getSchemaLocation(){
    return schemaLocation;
}

public String encoding = null;

/** set tabs to 4, just round column up to next tab + 1
12345678901234567890
    x   x   x   x
 */
public void tab() {
	int t = 1;
	int c = getColumn();
	int nc = (((c-1)/t)+1)*t+1;
	setColumn( nc );
}

public String getdtdPublicId(){
    return dtdpubid;
}

}



DOCTYPE!
    :
        "<!DOCTYPE" WS rootElementName:NAME 
        WS
        ( 
            ( "SYSTEM" WS sys1:STRING {dtdLocation = sys1.getText(); dtdpubid = pub.getText();}
            | "PUBLIC" WS pub:STRING WS sys2:STRING {dtdLocation = sys2.getText();}
            )
            ( WS )?
        )?
        ( dtd:INTERNAL_DTD ( WS )? 
            { 
            }   

        )?
		'>'
	;

protected INTERNAL_DTD
    :
        '['!
        // reports warning, but is absolutely ok (checked generated code)
        // besides this warning was not generated with k=1 which is 
        // enough for this rule...
        ( options {greedy=false;} : NL
        | STRING // handle string specially to avoid to mistake ']' in string for end dtd
        | .
        )*
        ']'!
    ;

PI_OR_XMLDECL
    :
        { AttributesImpl attrs = new AttributesImpl(); }
        "<?" 
        target:NAME
        ( WS )?
		( ATTR[attrs]  ( WS )? )*
        {
            if (target.getText().equalsIgnoreCase("xml")) {
				if (attrs.hasAttribute("encoding")) {
					this.encoding = attrs.getValue("encoding");
				}
                $setType(XMLDECL); 
            } else {
                $setType(PI);
            }
        }
		"?>"
	;


//////////////////

COMMENT!
	:	"<!--" c:COMMENT_DATA "-->"
        { 
        }
	;

protected COMMENT_DATA
    : 
        ( options {greedy=false;} : NL
        | .
        )*
    ;

//////////////////

ENDTAG! :
        "</" g:NAME ( WS )? '>'
        { 
	        $setText(g.getText());
        }
	;

//////////////////

STARTTAG! : 
        { AttributesImpl attributes = new AttributesImpl(); }
        '<' 
        g:NAME
        ( WS )?
		( ATTR [attributes] ( WS )? )*
		( "/>"
            { 
                $setType(EMPTYTAG);
                $setText(g.getText());
                
                elm2attributes.put(g.getText(), attributes);
            }

		| '>'
            { 
	          $setText(g.getText());
              elm2attributes.put(g.getText(), attributes);
            }
		)
	;

PCDATA!	: 
        p:PCDATA_DATA
//        { System.out.println("PCDATA: "+p.getText()); }
	;

protected PCDATA_DATA
	: 
        ( options {greedy=true;} : NL
        | ~( '<' | '>' | '\n' | '\r' )
        )+
    ;

CDATABLOCK!
	: "<![CDATA[" INTERNAL_CDATA_DATA "]]>"
        { 
        }
	;

protected INTERNAL_CDATA_DATA
    : 
        ( options {greedy=false;} : NL
        | .
        )*
    ;

protected ATTR  [AttributesImpl attributes]
	:	name:NAME ( WS )? '=' ( WS )? value:STRING_NO_QUOTE
        
		{ 
			attributes.addAttribute(name.getText(),value.getText()); 
                        if(name.getText().equals("xsi:noSchemaLocation") || name.getText().equals("xsi:schemaLocation")){
                            schemaLocation =  value.getText();
                        }
                        
        }
        
       ;

protected STRING_NO_QUOTE
	:	'"'! (~'"')* '"'!
	|	'\''! (~'\'')* '\''!
	;

protected STRING
	:	'"' (~'"')* '"'
	|	'\'' (~'\'')* '\''
	;

protected NAME
	:	( LETTER | '_' | ':') ( options {greedy=true;} : NAMECHAR )*
	;

protected NAMECHAR
	: LETTER | DIGIT | '.' | '-' | '_' | ':'
	;

protected DIGIT
	:	'0'..'9'
	;

protected LETTER
	: 'a'..'z' 
	| 'A'..'Z'
	;

protected WS
	:	(	options {
                greedy = true;
			}
		:	' '
		|	ESC
		)+
	;

protected ESC
	: ( '\t'
	 	|	NL
		)
	;

// taken from html.g
// Alexander Hinds & Terence Parr
// from antlr 2.5.0: example/html 
//
// '\r' '\n' can be matched in one alternative or by matching
// '\r' in one iteration and '\n' in another.  I am trying to
// handle any flavor of newline that comes in, but the language
// that allows both "\r\n" and "\r" and "\n" to all be valid
// newline is ambiguous.  Consequently, the resulting grammar
// must be ambiguous.  I'm shutting this warning off.
protected NL
    : (	options {
	generateAmbigWarnings=false;
	greedy = true;
    }
		: '\n'
		|	"\r\n"
		|	'\r'
		)
		{ newline(); }
	;
