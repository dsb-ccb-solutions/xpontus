package net.sf.xpontus.view.editor.syntax.c;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;
import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class CColorProvider extends DefaultColorProvider
{
    public CColorProvider()
    {
        SimpleAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setForeground( keyword, new Color(127, 0, 85));
        StyleConstants.setBold(keyword, true);

        SimpleAttributeSet comment = new SimpleAttributeSet();
         StyleConstants.setForeground(comment,new Color(0, 100, 0));
        StyleConstants.setBold(comment,true);
        StyleConstants.setItalic(comment, true);

        SimpleAttributeSet s_package = new SimpleAttributeSet();
        StyleConstants.setBold(s_package,true);
        StyleConstants.setForeground(s_package,Color.MAGENTA);

        SimpleAttributeSet operator = new SimpleAttributeSet();
        StyleConstants.setForeground(operator,Color.BLACK);
        StyleConstants.setBold(operator,true);

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

        SimpleAttributeSet _str = new SimpleAttributeSet();
        StyleConstants.setForeground(_str,Color.RED);
       StyleConstants.setBold( _str,true);

        addStyle(CParserConstants.STRING_LITERAL, _str);
    }
}
