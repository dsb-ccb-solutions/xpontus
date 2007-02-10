package net.sf.xpontus.view.editor.syntax.plain;


import java.util.ArrayList;
import java.util.List;
import javax.swing.text.Segment;
import net.sf.xpontus.view.editor.syntax.ILexer;
import net.sf.xpontus.view.editor.syntax.Token;


/**
 * @author Yves Zoundi
 * Lexer for plain text files it doesn't do nothing with javacc
 * It just return the lexed text as a single token in the default state
 */
public class PlainParser implements ILexer
{
    private List tokens = new ArrayList();
    /**
     * Default constructor when using reflection calls
     */
    public PlainParser()
    {
    }

    /**
     * 
     * @param text 
     * @param initialTokenType 
     * @return 
     */
    public int getLastTokenTypeOnLine(Segment text, int initialTokenType) {
        return DEFAULT_LEXER_STATE;
    }

    /**
     * 
     * @param seg 
     * @param initialTokenType 
     * @param offs 
     * @return 
     */
    public List getTokens(Segment seg, int initialTokenType, int offs) {
        tokens.clear();
        tokens.add(new Token(seg.toString(), DEFAULT_LEXER_STATE));
        
        if(tokens.size() == 0){
            tokens.add(new Token("", DEFAULT_LEXER_STATE));
        }
        
        return tokens;
    }

    

    
}
