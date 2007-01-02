package net.sf.xpontus.view.editor.syntax.php;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;
import net.sf.xpontus.view.editor.syntax.StyleInfo;

import java.awt.Color;


public class PHPColorProvider extends DefaultColorProvider
  {
    public PHPColorProvider()
      {
        StyleInfo comment = new StyleInfo();
        comment.bold = true;
        comment.color = Color.DARK_GRAY;

        StyleInfo value = new StyleInfo();
        value.bold = true;
        value.color = Color.MAGENTA;
        addStyle(PHPParserConstants.VAR_NAME, value);

        StyleInfo stringStyle = new StyleInfo();
        stringStyle.bold = true;
        stringStyle.color = Color.red;

        StyleInfo keyword = new StyleInfo();
        keyword.bold = true;
        keyword.color = Color.blue;

        int[] comments = 
            {
                PHPParserConstants.SLASH_SLASH_COMMENT,
                PHPParserConstants.NUMERAL_COMMENT,
                PHPParserConstants.SLASH_STAR_COMMENT
            };
        addAll(comments, comment);

        int[] str = 
            {
                PHPParserConstants.DOUBLE_STRING_LITERAL,
                PHPParserConstants.DOUBLE_STRING_LITERAL_END,
                PHPParserConstants.DOUBLE_STRING_LITERAL_START
            };
        addAll(str, stringStyle);

        int[] keywords = 
            {
                PHPParserConstants.ABSTRACT, PHPParserConstants.FUNCTION,
                PHPParserConstants.VAR, PHPParserConstants.AND,
                PHPParserConstants.BREAK, PHPParserConstants.CASE,
                PHPParserConstants.CATCH, PHPParserConstants.CLASS,
                PHPParserConstants.AS, PHPParserConstants.CONTINUE,
                PHPParserConstants.DEFINE, PHPParserConstants.DEFAULT,
                PHPParserConstants.DO, PHPParserConstants.EXTENDS,
                PHPParserConstants.ELSE, PHPParserConstants.ELSEIF,
                PHPParserConstants.WHILE, PHPParserConstants.FOR,
                PHPParserConstants.FOREACH, PHPParserConstants.ECHO,
                PHPParserConstants.FINAL, PHPParserConstants.INCLUDE,
                PHPParserConstants.INCLUDE_ONCE, PHPParserConstants.NEW,
                PHPParserConstants.OR, PHPParserConstants.PRIVATE,
                PHPParserConstants.PUBLIC, PHPParserConstants.PROTECTED,
                PHPParserConstants.PRINT, PHPParserConstants.REQUIRE,
                PHPParserConstants.REQUIRE_ONCE, PHPParserConstants.RETURN,
                PHPParserConstants.STATIC, PHPParserConstants.SWITCH,
                PHPParserConstants.THROW, PHPParserConstants.TRY
            };

        addAll(keywords, keyword);

        StyleInfo operator = new StyleInfo();
        operator.color = Color.BLACK;
        operator.bold = true;

        int[] ops = { PHPParserConstants.DOLLAR, PHPParserConstants.DOLLAR1 };
        addAll(ops, operator);
      }
  }
