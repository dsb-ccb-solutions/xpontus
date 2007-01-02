package net.sf.xpontus.view.editor.syntax.plain;

import net.sf.xpontus.view.editor.ILexer;
import net.sf.xpontus.view.editor.syntax.CharStream;
import net.sf.xpontus.view.editor.syntax.Token;


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

    /* (non-Javadoc)
     * @see net.sf.xpontus.view.editor.ILexer#ReInit(net.sf.xpontus.view.editor.syntax.CharStream)
     */
    public void ReInit(CharStream stream)
      {
        // hopefully there is nothing to do
      }

    /* (non-Javadoc)
     * @see net.sf.xpontus.view.editor.ILexer#getNextToken()
     */
    public Token getNextToken()
      {
        // nothing else here
        return null;
      }
  }
