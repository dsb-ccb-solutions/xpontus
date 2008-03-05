package net.sf.xpontus.plugins.lexer.html;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.syntax.*;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class HTMLColorProviderImpl extends DefaultColorProvider {
    public HTMLColorProviderImpl() {
        Color[] colors = new Color[6];
        String[] props = {
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.STRING_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.ATTRIBUTE_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.COMMENT_PROPERTY,
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.TAGS_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.DECLARATION_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.ATTRIBUTES_PROPERTY
            };

        for (int i = 0; i < props.length; i++) {
            System.out.println("~~~~~~~~~~~~~~~~~~~~~~~~~~:" +
                XPontusConfig.getValue(props[i]));
            colors[i] = (Color) XPontusConfig.getValue(props[i]);
        }

        SimpleAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setBold(comment, true);
        StyleConstants.setForeground(comment, colors[2]);

        SimpleAttributeSet value = new SimpleAttributeSet();
        StyleConstants.setBold(value, true);
        StyleConstants.setForeground(value, colors[1]);

        SimpleAttributeSet stringStyle = new SimpleAttributeSet();
        StyleConstants.setBold(stringStyle, true);
        StyleConstants.setForeground(stringStyle, colors[5]);

        SimpleAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setBold(keyword, true);
        StyleConstants.setForeground(keyword, new Color(128, 0, 0));

        SimpleAttributeSet tagStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(tagStyle, colors[3]);
        StyleConstants.setBold(tagStyle, true);

        SimpleAttributeSet declStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(declStyle, colors[4]);
        StyleConstants.setBold(declStyle, true);

        int[] comments = {
                HtmlParserConstants.COMMENT_START,
                HtmlParserConstants.COMMENT_END,
                HtmlParserConstants.COMMENT_WORD
            };
        addAll(comments, comment);

        int[] keywords = { HtmlParserConstants.ATTR_NAME };
        addAll(keywords, keyword);

        int[] attrs = { HtmlParserConstants.ATTR_VAL };
        addAll(attrs, value);

        addStyle(HtmlParserConstants.TAG_NAME, tagStyle);

        int[] strs = { HtmlParserConstants.QUOTED_STRING };
        addAll(strs, stringStyle);

        int[] decls = {
                HtmlParserConstants.DECL_START, HtmlParserConstants.DECL_END,
                HtmlParserConstants.DECL_ANY
            };
        addAll(decls, declStyle);
    }
}
