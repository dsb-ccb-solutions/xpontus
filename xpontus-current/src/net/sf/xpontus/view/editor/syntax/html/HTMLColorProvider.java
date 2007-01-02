package net.sf.xpontus.view.editor.syntax.html;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;
import net.sf.xpontus.view.editor.syntax.StyleInfo;

import java.awt.Color;


public class HTMLColorProvider extends DefaultColorProvider
  {
    public HTMLColorProvider()
      {
        StyleInfo comment = new StyleInfo();
        comment.bold = true;
        comment.color = new Color(0, 139, 0);

        StyleInfo value = new StyleInfo();
        value.bold = true;
        value.color = Color.RED;

        StyleInfo stringStyle = new StyleInfo();
        stringStyle.bold = true;
        stringStyle.color = Color.red;

        StyleInfo keyword = new StyleInfo();
        keyword.bold = true;
        keyword.color = new Color(128, 0, 0);

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

        StyleInfo tagStyle = new StyleInfo();
        tagStyle.color = Color.BLUE;
        tagStyle.bold = true;
        addStyle(HtmlParserConstants.TAG_NAME, tagStyle);

        int[] strs = { HtmlParserConstants.QUOTED_STRING };
        addAll(strs, stringStyle);
      }
  }
