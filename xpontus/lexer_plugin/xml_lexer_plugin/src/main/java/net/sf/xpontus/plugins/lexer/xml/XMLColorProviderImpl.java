package net.sf.xpontus.plugins.lexer.xml;

import net.sf.xpontus.plugins.color.PlainColorProviderImpl;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 * Tokens coloring for xml mode
 * @author Yves Zoundi
 */
public class XMLColorProviderImpl extends PlainColorProviderImpl
{
    /**
     * Default constructor
     */
    public XMLColorProviderImpl()
    {
        MutableAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setForeground(keyword, Color.BLUE);

        int[] keywords = { XMLParserConstants.TAG_NAME };

        MutableAttributeSet attStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(attStyle, new Color(127, 0, 85));

        addAll(keywords, keyword);

        MutableAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setForeground(comment, new Color(0, 100, 0));
        StyleConstants.setItalic(comment, true);

        MutableAttributeSet xmlDeclStyle = new SimpleAttributeSet(); 
        StyleConstants.setForeground(xmlDeclStyle, Color.MAGENTA);

        int[] xmlDeclTokenIds = 
            {
                XMLParserConstants.PI_START, XMLParserConstants.XML_TARGET,
                XMLParserConstants.PI_TARGET, XMLParserConstants.PI_CONTENT_END,
                XMLParserConstants.TEXT_IN_PI_CONTENT,
                XMLParserConstants.ERR_IN_PI_CONTENT,
                XMLParserConstants.KW_IN_XML_DECL,
                XMLParserConstants.TEXT_IN_XML_DECL, XMLParserConstants.ATT_NAME,
                XMLParserConstants.BR_IN_XML_DECL,
                XMLParserConstants.XML_DECL_END,
                XMLParserConstants.Q_IN_XML_DECL, XMLParserConstants.DOCTYPE,
                XMLParserConstants.PUBLIC, XMLParserConstants.SYSTEM,
                XMLParserConstants.TEXT_IN_DOCTYPE,
                XMLParserConstants.ERR_IN_DOCTYPE
            };
        addAll(xmlDeclTokenIds, xmlDeclStyle);

        MutableAttributeSet stringStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(stringStyle, Color.RED);

        int[] stringTokenIds = 
            {
                XMLParserConstants.TEXT_IN_GREF_CHARS,
                XMLParserConstants.TEXT_IN_GREF_STRING
            };
        addAll(stringTokenIds, stringStyle);

        int[] c = 
            {
                XMLParserConstants.COMMENT_END, XMLParserConstants.COMMENT_START,
                XMLParserConstants.TEXT_IN_COMMENT,
                XMLParserConstants.ERR_IN_COMMENT
            };
        addAll(c, comment);
    }
}
