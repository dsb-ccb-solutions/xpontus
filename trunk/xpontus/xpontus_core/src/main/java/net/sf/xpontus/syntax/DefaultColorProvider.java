/*
 * ColorProvider.java
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

import javax.swing.text.MutableAttributeSet;


/**
 *
 * @author Yves Zoundi
 */
public class DefaultColorProvider implements IColorProvider {
    protected java.util.Map styles = new java.util.HashMap();

    /** Creates a new instance of ColorProvider */
    public DefaultColorProvider() {
    }

    /**
     *
     * @param tokenIds
     * @param style
     */
    public void addAll(int[] tokenIds, MutableAttributeSet style) {
        for (int i = 0; i < tokenIds.length; i++) {
            addStyle(tokenIds[i], style);
        }
    }

    /**
     *
     * @param tokenId
     * @param style
     */
    public void addStyle(int tokenId, MutableAttributeSet style) {
        this.styles.put(new Integer(tokenId), style);
    }

    /**
     *
     * @return
     */
    public java.util.Map getStyles() {
        return styles;
    }
}
