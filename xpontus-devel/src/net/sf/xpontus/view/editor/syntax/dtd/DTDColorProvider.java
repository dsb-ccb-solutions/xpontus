package net.sf.xpontus.view.editor.syntax.dtd;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;

import java.awt.Color;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class DTDColorProvider extends DefaultColorProvider
{
    public DTDColorProvider()
    {
        // style for a comment
        MutableAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setBold(comment, true);
        StyleConstants.setForeground(comment, new Color(0, 100, 0));

        MutableAttributeSet _comment = new SimpleAttributeSet();
        StyleConstants.setBold(_comment, true);
        StyleConstants.setForeground(_comment, new Color(210, 105, 30));

        MutableAttributeSet strStyle = new SimpleAttributeSet();
        StyleConstants.setForeground(strStyle, Color.RED);

        int[] strs = { DTDParserConstants.TEXT_IN_STRING };
        addAll(strs, strStyle);

        int[] _comments = 
            {
                DTDParserConstants.COMMENT_END,
                DTDParserConstants.TEXT_IN_COMMENT,
                DTDParserConstants.COMMENT_START
            };
        addAll(_comments, _comment);

        MutableAttributeSet attrs = new SimpleAttributeSet();
        StyleConstants.setBold(attrs, true);
        StyleConstants.setForeground(attrs, Color.MAGENTA);

        int[] _attrs = { DTDParserConstants.KW_IN_ATTLIST };
        addAll(_attrs, attrs);

        MutableAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setForeground(keyword, new Color(127, 0, 85));
        StyleConstants.setBold(keyword, true);

        int[] keywords = 
            {
                DTDParserConstants.ATTLIST, DTDParserConstants.ELEMENT,
                DTDParserConstants.KW_IN_ELEMENT
            };
        addAll(keywords, keyword);
    }
}
