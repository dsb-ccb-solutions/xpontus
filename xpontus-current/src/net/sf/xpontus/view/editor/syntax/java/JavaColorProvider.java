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
import net.sf.xpontus.view.editor.syntax.StyleInfo;

import java.awt.Color;


/**
 *
 * @author mrcheeks
 */
public class JavaColorProvider extends DefaultColorProvider
  {
    /** Creates a new instance of JavaccColorProvider */
    public JavaColorProvider()
      {
        StyleInfo keyword = new StyleInfo();
        keyword.color = new Color(127, 0, 85);
        keyword.bold = true;

        StyleInfo s_package = new StyleInfo();
        s_package.bold = true;
        s_package.color = Color.MAGENTA;

        int[] k = 
            {
                JavaParserConstants.FINAL, JavaParserConstants.EXTENDS,
                JavaParserConstants.SUPER, JavaParserConstants.PUBLIC,
                JavaParserConstants.PROTECTED, JavaParserConstants.PRIVATE,
                JavaParserConstants.INTERFACE, JavaParserConstants.INT,
                JavaParserConstants.NEW, JavaParserConstants.IMPLEMENTS,
                JavaParserConstants.IMPORT, JavaParserConstants.CASE,
                JavaParserConstants.CATCH, JavaParserConstants.SWITCH,
                JavaParserConstants.DEFAULT, JavaParserConstants.BOOLEAN,
                JavaParserConstants.CLASS, JavaParserConstants.CHAR,
                JavaParserConstants.IF, JavaParserConstants.ELSE,
                JavaParserConstants.TRUE, JavaParserConstants.FALSE,
                JavaParserConstants.FINALLY, JavaParserConstants.INSTANCEOF,
                JavaParserConstants.NATIVE, JavaParserConstants.VOID,
                JavaParserConstants.VOLATILE, JavaParserConstants.LONG,
                JavaParserConstants.FOR, JavaParserConstants.RETURN,
                JavaParserConstants.STATIC, JavaParserConstants.THIS,
                JavaParserConstants.THROW, JavaParserConstants.THROWS
            };

        addAll(k, keyword);

        StyleInfo comment = new StyleInfo();
        comment.color = new Color(0, 100, 0);
        comment.italic = true;
        comment.bold = true;

        int[] c = 
            {
                JavaParserConstants.SINGLE_LINE_COMMENT,
                JavaParserConstants.MULTI_LINE_COMMENT,
                JavaParserConstants.FORMAL_COMMENT
            };
        addAll(c, comment);

        addStyle(JavaParserConstants.PACKAGE, s_package);

        StyleInfo _str = new StyleInfo();
        _str.color = Color.red;
        _str.bold = true;

        addStyle(JavaParserConstants.STRING_LITERAL, _str);

        StyleInfo operator = new StyleInfo();
        operator.color = Color.BLACK;
        operator.bold = true;

        int[] ops = { 69, 70, 71, 72, 73, 74, 75 };
        addAll(ops, operator);
      }
  }
