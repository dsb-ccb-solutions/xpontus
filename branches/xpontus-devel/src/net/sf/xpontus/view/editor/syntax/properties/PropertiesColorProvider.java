package net.sf.xpontus.view.editor.syntax.properties;

import net.sf.xpontus.view.editor.syntax.DefaultColorProvider;

import java.awt.Color;

import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;


public class PropertiesColorProvider extends DefaultColorProvider
{
    public PropertiesColorProvider()
    {
        // style for a comment
        SimpleAttributeSet comment = new SimpleAttributeSet();
        StyleConstants.setBold(comment, true);
        StyleConstants.setForeground(comment, new Color(0, 100, 0));

        // style for a property value
        SimpleAttributeSet value = new SimpleAttributeSet();
        StyleConstants.setBold(value, true);
        StyleConstants.setForeground(value, Color.MAGENTA);

        // style for properties names
        SimpleAttributeSet keyword = new SimpleAttributeSet();
        StyleConstants.setBold(keyword, true);
        StyleConstants.setForeground(keyword, Color.BLUE);

        // configure the token for the styles
        addStyle(PropertiesParserConstants.COMMENT, comment);
        addStyle(PropertiesParserConstants.KEY, keyword);
        addStyle(PropertiesParserConstants.VALUE, value);
    }
}
