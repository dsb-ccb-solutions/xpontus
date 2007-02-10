/*
 * TokenUtilities.java
 *
 * Created on February 3, 2007, 5:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.utils;

import net.sf.xpontus.view.editor.syntax.Token;

import javax.swing.text.Segment;


/**
 *
 * @author Yves Zoundi
 */
public class TokenUtilities
{
    private TokenUtilities()
    {
    }

    public static Token createToken( Token token, Segment text, int pos, int kind)
    {
        int begin = token.beginColumn + 1;
        
         String image = new String(text.array, pos, text.count);

        return new Token(image, kind);
    }
}
