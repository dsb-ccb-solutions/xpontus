/*
 * TokenInfo.java
 *
 * Created on December 22, 2006, 1:07 AM
 *
 * Copyright (C) 2005-2008 Yves Zoundi
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
package net.sf.xpontus.syntax;


/**
 * Class TokenInfo ...
 *
 * @author Yves Zoundi
 * Created on Apr 5, 2008
 */
public class TokenInfo {
    public String text;
    public int start;
    public int end;
    public int size;
    public int kind;

    /**
     * Constructor TokenInfo creates a new TokenInfo instance.
     */
    public TokenInfo() {
    }

    /**
     * Constructor TokenInfo creates a new TokenInfo instance.
     *
     * @param token of type Token
     * @param offset of type int
     */
    public TokenInfo(Token token, int offset) {
        this.text = token.image;
        this.size = token.image.length();
        this.start = token.beginColumn + offset;

        this.end = this.start + size;
        this.kind = token.kind;
    }
}
