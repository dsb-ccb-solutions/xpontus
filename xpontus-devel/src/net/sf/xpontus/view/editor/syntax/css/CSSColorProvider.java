/*
 * CSSColorProvider.java
 *
 * Created on January 4, 2007, 8:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.editor.syntax.css;

import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import net.sf.xpontus.view.editor.syntax.*;



/**
 *
 * @author Owner
 */
public class CSSColorProvider extends DefaultColorProvider
{
    /** Creates a new instance of CSSColorProvider */
    public CSSColorProvider()
    {
  SimpleAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setBold(comment, true);
        StyleConstants.setForeground(comment, new Color(0, 100, 0));

        int[] comments = 
            {
                CSSParserConstants.CCOMMENT_START,
                CSSParserConstants.CCOMMENT_END, 
                CSSParserConstants.IN_CCOMMENT,
                CSSParserConstants.COMMENT_START,
                CSSParserConstants.COMMENT_END,
                CSSParserConstants.IN_COMMENT,
                CSSParserConstants.TEXT_IN_COMMENT,
                CSSParserConstants.IN_RULESET
            };

        addAll(comments, comment);
        
        // style for properties names
        SimpleAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setBold(keyword, true);
        StyleConstants.setForeground(keyword, Color.BLUE);
        
        addStyle(CSSParserConstants.PROP, keyword);
    }
}
