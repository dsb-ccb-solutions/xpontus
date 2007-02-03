package net.sf.xpontus.view.editor.syntax.plain;


import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Segment;
import net.sf.xpontus.view.editor.syntax.ILexer;


/**
 * @author Yves Zoundi
 * Lexer for plain text files
 */
public class PlainParser implements ILexer
{
    /**
     * Default constructor when using reflection calls
     */
    public PlainParser()
    {
    }

    public int getLastTokenTypeOnLine(Segment text, int initialTokenType) {
        return 0;
    }

    public List getTokens(Segment seg, int initialTokenType, int offs) {
        return new ArrayList();
    }

    

    
}
