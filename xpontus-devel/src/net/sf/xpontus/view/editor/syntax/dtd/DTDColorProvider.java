package net.sf.xpontus.view.editor.syntax.dtd;

import java.awt.Color;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;


public class DTDColorProvider extends DefaultColorProvider
{
    public DTDColorProvider()
    {
 // style for a comment
        SimpleAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setBold(comment, true);
        StyleConstants.setForeground(comment, new Color(0, 100, 0));
        
         int[] c = 
            {
                DTDParserConstants.COMMENT_END, DTDParserConstants.COMMENT_START,
                DTDParserConstants.TEXT_IN_COMMENT,
                DTDParserConstants.ERR_IN_COMMENT
            };
        addAll(c, comment);
        
    }
}
