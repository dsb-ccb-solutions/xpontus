package net.sf.xpontus.view.editor.syntax;

import java.util.List;
import javax.swing.text.Segment;


/**
 * @author mrcheeks
 *
 */
public interface ILexer
{
    public int getLastTokenTypeOnLine(Segment text, int initialTokenType);

    public List getTokens(Segment seg, int initialTokenType, int offs);
}
