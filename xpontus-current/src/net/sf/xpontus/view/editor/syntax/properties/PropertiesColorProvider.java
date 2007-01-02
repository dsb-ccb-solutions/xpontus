package net.sf.xpontus.view.editor.syntax.properties;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;
import net.sf.xpontus.view.editor.syntax.StyleInfo;

import java.awt.Color;


public class PropertiesColorProvider extends DefaultColorProvider
  {
    public PropertiesColorProvider()
      {
        StyleInfo comment = new StyleInfo();
        comment.bold = true;
        comment.color = new Color(0, 100, 0);

        StyleInfo value = new StyleInfo();
        value.bold = true;
        value.color = Color.MAGENTA;

        StyleInfo keyword = new StyleInfo();
        keyword.bold = true;
        keyword.color = Color.blue;

        addStyle(PropertyParserConstants.COMMENT, comment);
        addStyle(PropertyParserConstants.KEY, keyword);
        addStyle(PropertyParserConstants.VALUE, value);
      }
  }
