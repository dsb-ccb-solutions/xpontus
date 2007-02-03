package net.sf.xpontus.view.editor.syntax.sql;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class SQLColorProvider extends DefaultColorProvider
{
    public SQLColorProvider()
    {
        SimpleAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setBold(comment, true);
        StyleConstants.setItalic(comment, true);
        StyleConstants.setForeground(comment, new Color(0, 100, 0));

        SimpleAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setBold(keyword, true);
        StyleConstants.setForeground(keyword, Color.BLUE);

        SimpleAttributeSet intStyle = new SimpleAttributeSet();
        StyleConstants.setBold(intStyle, true);
        StyleConstants.setForeground(intStyle, new Color(107, 66, 38));

        SimpleAttributeSet stringStyle = new SimpleAttributeSet();
        StyleConstants.setBold(stringStyle, true);
        StyleConstants.setForeground(stringStyle, new Color(0, 0, 0));

        int[] comments = 
            {
                SQLParserConstants.START_MULTILINE_COMMENT,
                SQLParserConstants.IN_MULTILINE_COMMENT,
                SQLParserConstants.SINGLE_LINE_COMMENT,
                SQLParserConstants.MULTILINE_COMMENT_CHAR,
                SQLParserConstants.END_MULTILINE_COMMENT
            };
        int[] str = 
            {
                SQLParserConstants.S_QUOTED_IDENTIFIER,
                SQLParserConstants.S_CHAR_LITERAL
            };
        int[] keywords = 
            {
                SQLParserConstants.K_AS, SQLParserConstants.K_ASC,
                
                SQLParserConstants.K_CREATE, SQLParserConstants.K_CONSTRAINT,
                SQLParserConstants.K_KEY, SQLParserConstants.K_FOREIGN,
                SQLParserConstants.K_REFERENCES, SQLParserConstants.K_PRIMARY,
                SQLParserConstants.K_BEGIN, SQLParserConstants.K_BETWEEN,
                SQLParserConstants.K_BINARY_INTEGER,
                SQLParserConstants.K_BOOLEAN, SQLParserConstants.K_BY,
                SQLParserConstants.K_CHAR, SQLParserConstants.K_CLOSE,
                SQLParserConstants.K_CONNECT, SQLParserConstants.K_CONSTANT,
                SQLParserConstants.K_CURRENT, SQLParserConstants.K_CURSOR,
                SQLParserConstants.K_DATE, SQLParserConstants.K_DECIMAL,
                SQLParserConstants.K_DECLARE, SQLParserConstants.K_DEFAULT,
                SQLParserConstants.K_DELETE, SQLParserConstants.K_DESC,
                SQLParserConstants.K_DISTINCT, SQLParserConstants.K_DO,
                SQLParserConstants.K_ELSE, SQLParserConstants.K_ELSIF,
                SQLParserConstants.K_END, SQLParserConstants.K_EXCEPTION,
                SQLParserConstants.K_EXCEPTION_INIT,
                SQLParserConstants.K_EXCLUSIVE, SQLParserConstants.K_EXISTS,
                SQLParserConstants.K_EXIT, SQLParserConstants.K_FETCH,
                SQLParserConstants.K_FLOAT, SQLParserConstants.K_FOR,
                SQLParserConstants.K_FROM, SQLParserConstants.K_FUNCTION,
                SQLParserConstants.K_GOTO, SQLParserConstants.K_GROUP,
                SQLParserConstants.K_HAVING, SQLParserConstants.K_IF,
                SQLParserConstants.K_IN, SQLParserConstants.K_INDEX,
                SQLParserConstants.K_INSERT, SQLParserConstants.K_INTEGER,
                SQLParserConstants.K_INTERSECT, SQLParserConstants.K_INTO,
                SQLParserConstants.K_IS, SQLParserConstants.K_LEVEL,
                SQLParserConstants.K_LIKE, SQLParserConstants.K_LOCK,
                SQLParserConstants.K_LOOP, SQLParserConstants.K_MINUS,
                SQLParserConstants.K_MODE, SQLParserConstants.K_NATURAL,
                SQLParserConstants.K_NOT, SQLParserConstants.K_NOWAIT,
                SQLParserConstants.K_NULL, SQLParserConstants.K_NUMBER,
                SQLParserConstants.K_OF, SQLParserConstants.K_ONLY,
                SQLParserConstants.K_OPEN, SQLParserConstants.K_OR,
                SQLParserConstants.K_ORDER, SQLParserConstants.K_OTHERS,
                SQLParserConstants.K_OUT, SQLParserConstants.K_PACKAGE,
                SQLParserConstants.K_POSITIVE, SQLParserConstants.K_PRAGMA,
                SQLParserConstants.K_PRIOR, SQLParserConstants.K_PROCEDURE,
                SQLParserConstants.K_RAISE, SQLParserConstants.K_READ,
                SQLParserConstants.K_REAL, SQLParserConstants.K_RECORD,
                SQLParserConstants.K_REF, SQLParserConstants.K_RETURN,
                SQLParserConstants.K_REVERSE, SQLParserConstants.K_ROLLBACK,
                SQLParserConstants.K_ROW, SQLParserConstants.K_SAVEPOINT,
                SQLParserConstants.K_SEGMENT, SQLParserConstants.K_SELECT,
                SQLParserConstants.K_SET, SQLParserConstants.K_SHARE,
                SQLParserConstants.K_SMALLINT, SQLParserConstants.K_SQL,
                SQLParserConstants.K_START, SQLParserConstants.K_TABLE,
                SQLParserConstants.K_THEN, SQLParserConstants.K_TO,
                SQLParserConstants.K_TRANSACTION, SQLParserConstants.K_UNION,
                SQLParserConstants.K_UPDATE, SQLParserConstants.K_USE,
                SQLParserConstants.K_VALUES, SQLParserConstants.K_VARCHAR2,
                SQLParserConstants.K_VARCHAR, SQLParserConstants.K_WHEN,
                SQLParserConstants.K_WHERE, SQLParserConstants.K_WHILE,
                SQLParserConstants.K_WITH, SQLParserConstants.K_WORK,
                SQLParserConstants.K_WRITE,
            };

        addAll(comments, comment);
        addStyle(SQLParserConstants.S_NUMBER, intStyle);
        addAll(str, stringStyle);
        addAll(keywords, keyword);
    }
}
