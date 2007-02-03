/*
 * JavaccColorProvider.java
 *
 * Created on December 25, 2006, 5:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.editor.syntax.java;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


/**
 *
 * author mrcheeks
 */
public class JavaColorProvider extends DefaultColorProvider
{
    /** Creates a new instance of JavaccColorProvider */
    public JavaColorProvider()
    {
        MutableAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setForeground(keyword, new Color(127, 0, 85));
        StyleConstants.setBold(keyword, true);

        MutableAttributeSet s_package = new SimpleAttributeSet();
        StyleConstants.setBold(s_package, true);
        StyleConstants.setForeground(s_package, Color.MAGENTA);

        int[] k = 
            {
                JavaParserConstants.INT, JavaParserConstants.BOOLEAN,
                JavaParserConstants.CHAR, JavaParserConstants.TRUE,
                JavaParserConstants.LONG, JavaParserConstants.FALSE,
                JavaParserConstants.BREAK, JavaParserConstants._DEFAULT,
                JavaParserConstants.CONTINUE, JavaParserConstants.FINAL,
                JavaParserConstants.NULL, JavaParserConstants.EXTENDS,
                JavaParserConstants.SUPER, JavaParserConstants.PUBLIC,
                JavaParserConstants.PROTECTED, JavaParserConstants.PRIVATE,
                JavaParserConstants.INTERFACE, JavaParserConstants.NEW,
                JavaParserConstants.IMPLEMENTS, JavaParserConstants.IMPORT,
                JavaParserConstants.CASE, JavaParserConstants.TRY,
                JavaParserConstants.CATCH, JavaParserConstants.SWITCH,
                JavaParserConstants.CLASS, JavaParserConstants.IF,
                JavaParserConstants.ELSE, JavaParserConstants.FINALLY,
                JavaParserConstants.INSTANCEOF, JavaParserConstants.NATIVE,
                JavaParserConstants.VOID, JavaParserConstants.VOLATILE,
                JavaParserConstants.FOR, JavaParserConstants.RETURN,
                JavaParserConstants.STATIC, JavaParserConstants.THIS,
                JavaParserConstants.THROW, JavaParserConstants.THROWS,
                JavaParserConstants.DO, JavaParserConstants.GOTO,
                JavaParserConstants.TRANSIENT, JavaParserConstants.WHILE
            };

        addAll(k, keyword);

        MutableAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setForeground(comment, new Color(0, 100, 0));
        StyleConstants.setBold(comment, true);
        StyleConstants.setItalic(comment, true);

        int[] c = 
            {
                JavaParserConstants.START_MULTILINE_COMMENT,
                JavaParserConstants.IN_MULTILINE_COMMENT,
                JavaParserConstants.SINGLE_LINE_COMMENT,
                JavaParserConstants.MULTILINE_COMMENT_CHAR,
                JavaParserConstants.END_MULTILINE_COMMENT
            };
        addAll(c, comment);

        addStyle(JavaParserConstants.PACKAGE, s_package);

        MutableAttributeSet _str = new SimpleAttributeSet();
        StyleConstants.setForeground(_str, Color.red);
        StyleConstants.setBold(_str, true);

        addStyle(JavaParserConstants.STRING_LITERAL, _str);

        MutableAttributeSet operator = new SimpleAttributeSet();
        StyleConstants.setForeground(operator, Color.BLACK);
        StyleConstants.setBold(operator, true);

        int[] ops = 
            {
                JavaParserConstants.LPAREN, JavaParserConstants.RPAREN,
                JavaParserConstants.LBRACKET, JavaParserConstants.RBRACKET,
                JavaParserConstants.RBRACE, JavaParserConstants.LBRACE
            };
        addAll(ops, operator);
    }
}
