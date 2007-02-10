package net.sf.xpontus.view;

public class TextPosition
  {
    private int line;
    private int column;

    public TextPosition(int line, int column)
      {
        this.line = line;
        this.column = column;
      }

    public String toString()
      {
        return this.line + ":" + this.column;
      }
  }
