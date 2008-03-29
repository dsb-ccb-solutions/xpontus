/*
 * PlainLexerModuleImpl.java
 *
 * Created on May 26, 2007, 8:02 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.plugins.lexer;

import net.sf.xpontus.plugins.color.PlainColorProviderImpl;
import net.sf.xpontus.syntax.IColorProvider;
import net.sf.xpontus.syntax.Token;

import java.util.ArrayList;
import java.util.List;

import javax.swing.text.Segment;


/**
 * Plugin to provide a lexer for plain text files
 * @author Yves Zoundi
 */
public class PlainLexerModuleImpl implements LexerPluginIF {
    private List tokens = new ArrayList();
    private String[] SUPPORTED_EXTENSIONS = { "txt", "texte", "text" };

    /**
     * Creates a new instance of PlainLexerModuleImpl
     */
    public PlainLexerModuleImpl() {
    }

    /**
     *
     * @return
     */
    public String getMimeType() {
        return "text/plain";
    }

    /**
     *
     * @return
     */
    public String getName() {
        return "Plain lexer";
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return "Plain text syntax analyzer";
    }

    /**
     *
     * @return
     */
    public String getLexerClassName() {
        return getClass().getName();
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

        if (tokens.size() == 0) {
            tokens.add(new Token("", DEFAULT_LEXER_STATE));
        }

        return tokens;
    }

    public IColorProvider getColorer() {
        return new PlainColorProviderImpl();
    }

    public String[] getSupportedExtensions() {
        return SUPPORTED_EXTENSIONS;
    }
}
