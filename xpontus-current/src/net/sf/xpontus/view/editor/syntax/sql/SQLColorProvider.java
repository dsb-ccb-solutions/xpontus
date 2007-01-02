package net.sf.xpontus.view.editor.syntax.sql;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;
import net.sf.xpontus.view.editor.syntax.StyleInfo;

import java.awt.Color;


public class SQLColorProvider extends DefaultColorProvider
{
    public SQLColorProvider()
    {
        StyleInfo comment = new StyleInfo();
        comment.bold = true;
        comment.italic = true;
        comment.color = new Color(0, 100, 0);

        int[] comments = { SQLParserConstants.K_COMMENT };
        addAll(comments, comment);

        StyleInfo keyword = new StyleInfo();
        keyword.bold = true;
        keyword.color = Color.BLUE;

        StyleInfo stringStyle = new StyleInfo();
        stringStyle.bold = true;
        stringStyle.color = new Color(0, 0, 0);

        int[] str = 
            {
                SQLParserConstants.S_QUOTED_IDENTIFIER,
                SQLParserConstants.S_CHAR_LITERAL
            };
        addAll(str, stringStyle);

        StyleInfo intStyle = new StyleInfo();
        intStyle.bold = true;
        intStyle.color = new Color(107, 66, 38);
        addStyle(SQLParserConstants.S_NUMBER, intStyle);

        int[] keywords = 
            {
                SQLParserConstants.K_AS, SQLParserConstants.K_ASC,
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

        addAll(keywords, keyword);
    }
}
