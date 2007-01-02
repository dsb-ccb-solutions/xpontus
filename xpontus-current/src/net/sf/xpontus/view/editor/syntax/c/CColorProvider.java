package net.sf.xpontus.view.editor.syntax.c;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;
import net.sf.xpontus.view.editor.syntax.StyleInfo;

import java.awt.Color;


public class CColorProvider extends DefaultColorProvider
  {
    public CColorProvider()
      {
        StyleInfo keyword = new StyleInfo();
        keyword.color = new Color(127, 0, 85);
        keyword.bold = true;

        StyleInfo comment = new StyleInfo();
        comment.color = new Color(0, 100, 0);
        comment.italic = true;
        comment.bold = true;

        StyleInfo s_package = new StyleInfo();
        s_package.bold = true;
        s_package.color = Color.MAGENTA;

        StyleInfo operator = new StyleInfo();
        operator.color = Color.BLACK;
        operator.bold = true;

        int[] k = 
            {
                CParserConstants.BREAK, CParserConstants.CASE,
                CParserConstants.CHAR, CParserConstants.CONST,
                CParserConstants.CONTINUE, CParserConstants.DEFAULT,
                CParserConstants.ELSE, CParserConstants.ENUM,
                CParserConstants.EXTERN, CParserConstants.FOR,
                CParserConstants.GOTO, CParserConstants.IF, CParserConstants.INT,
                CParserConstants.LONG, CParserConstants.REGISTER,
                CParserConstants.RETURN, CParserConstants.SHORT,
                CParserConstants.SIGNED, CParserConstants.SIZEOF,
                CParserConstants.TYPEDEF, CParserConstants.UNION,
                CParserConstants.UNSIGNED, CParserConstants.VOID,
                CParserConstants.VOLATILE, CParserConstants.WHILE,
                CParserConstants.STRUCT,
            };

        addAll(k, keyword);

        StyleInfo _str = new StyleInfo();
        _str.color = Color.red;
        _str.bold = true;

        addStyle(CParserConstants.STRING_LITERAL, _str);
      }
  }
