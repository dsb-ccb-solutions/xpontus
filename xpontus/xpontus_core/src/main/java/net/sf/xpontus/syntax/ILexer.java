package net.sf.xpontus.syntax;

import java.util.List;

import javax.swing.text.Segment;


/**
 * @author Yves Zoundi
 *
 */
public interface ILexer {
    public static final int DEFAULT_LEXER_STATE = 0;

    /**
     *
     * @param text
     * @param initialTokenType
     * @return
     */
    public int getLastTokenTypeOnLine(Segment text, int initialTokenType);

    /**
     *
     * @param seg
     * @param initialTokenType
     * @param offs
     * @return
     */
    public List getTokens(Segment seg, int initialTokenType, int offs);
}
