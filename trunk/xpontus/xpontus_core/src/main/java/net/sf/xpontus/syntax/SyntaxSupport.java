/*
 * SyntaxSupport.java
 *
 * Created on December 22, 2006, 1:05 AM
 *
 * Copyright (C) 2005-2008 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.syntax;


 /**
  * Class SyntaxSupport ...
  *
  * @author Yves Zoundi
  * Created on Apr 5, 2008
  */
 public class SyntaxSupport {
    private ILexer lexer;
    private IColorProvider colorProvider;

    /** Creates a new instance of SyntaxInfo */
    public SyntaxSupport() {
    }

     /**
      * Constructor SyntaxSupport creates a new SyntaxSupport instance.
      *
      * @param lexer of type ILexer
      * @param colorProvider of type IColorProvider
      */
     public SyntaxSupport(ILexer lexer, IColorProvider colorProvider) {
        setLexer(lexer);
        setColorProvider(colorProvider);
    }

    /**
     * Method setLexer sets the lexer of this SyntaxSupport object.
     *
     * @param lexer the lexer of this SyntaxSupport object.
     *
     */
    public void setLexer(ILexer lexer) {
        this.lexer = lexer;
    }

     /**
      * Method getLexer returns the lexer of this SyntaxSupport object.
      *
      * @return the lexer (type ILexer) of this SyntaxSupport object.
      */
     public ILexer getLexer() {
        return lexer;
    }


    /**
     * Method getColorProvider returns the colorProvider of this SyntaxSupport object.
     *
     * @return the colorProvider (type IColorProvider) of this SyntaxSupport object.
     */
    public IColorProvider getColorProvider() {
        return colorProvider;
    }

     /**
      * Method setColorProvider sets the colorProvider of this SyntaxSupport object.
      *
      * @param colorProvider the colorProvider of this SyntaxSupport object.
      *
      */
     public void setColorProvider(IColorProvider colorProvider) {
        this.colorProvider = colorProvider;
    }
}
