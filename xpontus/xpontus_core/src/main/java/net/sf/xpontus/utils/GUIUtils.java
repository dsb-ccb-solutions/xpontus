/*
 *
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
 *
 *
 */
package net.sf.xpontus.utils;

import org.apache.commons.lang.text.StrBuilder;

import java.awt.Font;


/**
 * Hopefully his class will be doing lots of usefull stuff
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class GUIUtils {
    // the constructor should not be called
    private GUIUtils() {
    }

    /**
     * Return a font
     * @param fontString the font information
     * @return a font from a string descriptor
     */
    public static Font getFontFromString(String fontString) {
        String[] m_table = fontString.split(",");

        String m_family = m_table[0];
        int m_style = Integer.parseInt(m_table[1]);
        int m_size = Integer.parseInt(m_table[2]);

        Font m_font = new Font(m_family, m_style, m_size);

        return m_font;
    }

    /**
     * Returns a String from a font
     * @param m_font The font to convert
     * @return a String from a font
     */
    public static String fontToString(Font m_font) {
        StrBuilder sb = new StrBuilder();
        sb.append(m_font.getFamily());
        sb.append(","); 
        sb.append("" + m_font.getStyle()); 
        sb.append(",");  
        sb.append(m_font.getSize());

        return sb.toString();
    }
}
