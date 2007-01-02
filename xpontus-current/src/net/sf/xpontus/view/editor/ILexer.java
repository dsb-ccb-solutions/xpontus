package net.sf.xpontus.view.editor;

import net.sf.xpontus.view.editor.syntax.CharStream;
import net.sf.xpontus.view.editor.syntax.Token;


public interface ILexer
  {
    public void ReInit(CharStream stream);

    public Token getNextToken();
  }
