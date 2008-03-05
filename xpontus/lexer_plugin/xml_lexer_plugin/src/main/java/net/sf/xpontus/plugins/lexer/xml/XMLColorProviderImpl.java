package net.sf.xpontus.plugins.lexer.xml;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.plugins.color.PlainColorProviderImpl;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 * Tokens coloring for xml mode
 * @author Yves Zoundi
 */
public class XMLColorProviderImpl extends PlainColorProviderImpl {
    /**
     * Default constructor
     */
    public XMLColorProviderImpl() {
        Color[] colors = new Color[6];
        String[] props = new String[XMLLexerPreferencesConstantsIF.AVAILABLE_PROPERTIES.length];

        for (int i = 0; i < props.length; i++) {
            props[i] = XMLLexerPreferencesConstantsIF.class.getName() + "$" +
                XMLLexerPreferencesConstantsIF.AVAILABLE_PROPERTIES[i];
        }

        for (int i = 0; i < props.length; i++) {
            colors[i] = (Color) XPontusConfig.getValue(props[i]);
        }

        MutableAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setForeground(keyword,
            colors[XMLLexerPreferencesConstantsIF.TAGS_COLOR_INDEX]);

        MutableAttributeSet attStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(attStyle,
            colors[XMLLexerPreferencesConstantsIF.ATTRIBUTES_COLOR_INDEX]);

        MutableAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setForeground(comment,
            colors[XMLLexerPreferencesConstantsIF.COMMENT_COLOR_INDEX]);
        StyleConstants.setItalic(comment, true);

        MutableAttributeSet xmlDeclStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(xmlDeclStyle,
            colors[XMLLexerPreferencesConstantsIF.DECLARATION_COLOR_INDEX]);

        MutableAttributeSet stringStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(stringStyle,
            colors[XMLLexerPreferencesConstantsIF.STRING_COLOR_INDEX]);

        // configure the tag colors
        int[] keywords = { XMLParserConstants.TAG_NAME }; 
        addAll(keywords, keyword); 
        
        // configure the attribute names colors
        addAll(new int[]{XMLParserConstants.ATT_NAME}, attStyle);
        
        // configure the processing instructions colors
        int[] xmlDeclTokenIds = {
                XMLParserConstants.PI_START, XMLParserConstants.XML_TARGET,
                XMLParserConstants.PI_TARGET, XMLParserConstants.PI_CONTENT_END,
                XMLParserConstants.TEXT_IN_PI_CONTENT,
                XMLParserConstants.ERR_IN_PI_CONTENT,
                XMLParserConstants.KW_IN_XML_DECL,
                XMLParserConstants.TEXT_IN_XML_DECL, 
                XMLParserConstants.BR_IN_XML_DECL,
                XMLParserConstants.XML_DECL_END,
                XMLParserConstants.Q_IN_XML_DECL, XMLParserConstants.DOCTYPE,
                XMLParserConstants.PUBLIC, XMLParserConstants.SYSTEM,
                XMLParserConstants.TEXT_IN_DOCTYPE,
                XMLParserConstants.ERR_IN_DOCTYPE
            };
        
        addAll(xmlDeclTokenIds, xmlDeclStyle);

        // configure the attribute values colors
        int[] stringTokenIds = {
                XMLParserConstants.TEXT_IN_GREF_CHARS,
                XMLParserConstants.TEXT_IN_GREF_STRING
            };
        addAll(stringTokenIds, stringStyle);

        // configure the comments colors
        int[] c = {
                XMLParserConstants.COMMENT_END, XMLParserConstants.COMMENT_START,
                XMLParserConstants.TEXT_IN_COMMENT,
                XMLParserConstants.ERR_IN_COMMENT
            };
        addAll(c, comment);
    }
}
