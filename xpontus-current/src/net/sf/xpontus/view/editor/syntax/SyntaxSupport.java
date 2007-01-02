/*
 * SyntaxInfo.java
 *
 * Created on December 22, 2006, 1:05 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.editor.syntax;

import net.sf.xpontus.view.editor.ILexer;


/**
 *
 * @author Owner
 */
public class SyntaxSupport
  {
    private ILexer lexer;
    private IColorProvider colorProvider;

    /** Creates a new instance of SyntaxInfo */
    public SyntaxSupport()
      {
      }

    /**
     * @param lexer
     * @param colorProvider
     */
    public SyntaxSupport(ILexer lexer, IColorProvider colorProvider)
      {
        setLexer(lexer);
        setColorProvider(colorProvider);
      }

    /**
     * @param lexer
     */
    public void setLexer(ILexer lexer)
      {
        this.lexer = lexer;
      }

    /**
     * @return
     */
    public ILexer getLexer()
      {
        return lexer;
      }

    /**
     * @return
     */
    public IColorProvider getColorProvider()
      {
        return colorProvider;
      }

    /**
     * @param colorProvider
     */
    public void setColorProvider(IColorProvider colorProvider)
      {
        this.colorProvider = colorProvider;
      }
  }
