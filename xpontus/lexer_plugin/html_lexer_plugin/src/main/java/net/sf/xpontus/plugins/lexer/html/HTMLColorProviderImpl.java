package net.sf.xpontus.plugins.lexer.html;


import net.sf.xpontus.syntax.*;
import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class HTMLColorProviderImpl extends DefaultColorProvider
{
    public HTMLColorProviderImpl()
    {
        SimpleAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setBold(comment, true);
        StyleConstants.setForeground(comment, new Color(0, 139, 0));

        SimpleAttributeSet value = new SimpleAttributeSet();
        StyleConstants.setBold(value, true);
        StyleConstants.setForeground(value, Color.RED);

        SimpleAttributeSet stringStyle = new SimpleAttributeSet();
        StyleConstants.setBold(stringStyle, true);
        StyleConstants.setForeground(stringStyle, Color.red);

        SimpleAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setBold(keyword, true);
        StyleConstants.setForeground(keyword, new Color(128, 0, 0));

        int[] comments = 
            {
                HtmlParserConstants.COMMENT_START,
                HtmlParserConstants.COMMENT_END,
                HtmlParserConstants.COMMENT_WORD
            };
        addAll(comments, comment);

        int[] keywords = { HtmlParserConstants.ATTR_NAME };
        addAll(keywords, keyword);

        int[] attrs = { HtmlParserConstants.ATTR_VAL };
        addAll(attrs, value);

        SimpleAttributeSet tagStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(tagStyle, Color.BLUE);
        StyleConstants.setBold(tagStyle, true);
        addStyle(HtmlParserConstants.TAG_NAME, tagStyle);

        int[] strs = { HtmlParserConstants.QUOTED_STRING };
        addAll(strs, stringStyle);

        SimpleAttributeSet declStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(declStyle, Color.MAGENTA);
        StyleConstants.setBold(declStyle, true);

        int[] decls = 
            {
                HtmlParserConstants.DECL_START, HtmlParserConstants.DECL_END,
                HtmlParserConstants.DECL_ANY
            };
        addAll(decls, declStyle);
    }
}
