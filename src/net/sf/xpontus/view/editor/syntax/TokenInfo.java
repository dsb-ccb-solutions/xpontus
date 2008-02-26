package net.sf.xpontus.view.editor.syntax;

public class TokenInfo
{
    public String text;
    public int start;
    public int end;
    public int size;
    public int kind;

    public TokenInfo()
    {
    }

    public TokenInfo(Token token, int offset)
    {
        this.text = token.image;
        this.size = token.image.length();
        this.start = token.beginColumn + offset;

        this.end = this.start + size;
        this.kind = token.kind;
    }
}
