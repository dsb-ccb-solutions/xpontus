/*
 * DefaultColorProvider.java
 *
 * Created on December 17, 2006, 2:56 PM
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

import java.util.HashMap;
import java.util.Map;

import javax.swing.text.MutableAttributeSet;


/**
 * Default syntax coloring style
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class DefaultColorProvider implements IColorProvider
{
    protected Map<Integer, MutableAttributeSet> styles = new HashMap<Integer, MutableAttributeSet>();

    /** Creates a new instance of ColorProvider */
    public DefaultColorProvider()
    {
    }

    /**
     * Apply a style to tokens
     * @param tokenIds  the token ids
     * @param style a style to apply to the tokens
     */
    public void addAll(int[] tokenIds, MutableAttributeSet style)
    {
        for (int i = 0; i < tokenIds.length; i++)
        {
            addStyle(tokenIds[i], style);
        }
    }

    /**
     * Apply a style to a token
     * @param tokenId  The token identifier
     * @param style the style to apply
     */
    public void addStyle(int tokenId, MutableAttributeSet style)
    {
        this.styles.put(Integer.valueOf(tokenId), style);
    }

    /**
     * Returns the styles map
     * @return the styles map
     */
    public Map<Integer, MutableAttributeSet> getStyles()
    {
        return styles;
    }
}
