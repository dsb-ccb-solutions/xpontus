/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.utils;

import java.awt.Color;


/**
 *
 * @author mrcheeks
 */
public class ColorUtils {
    public static String colorToString(Color color) {
        StringBuffer sb = new StringBuffer();
        sb.append("" + color.getRed());
        sb.append(",");
        sb.append("" + color.getGreen());
        sb.append(",");
        sb.append("" + color.getBlue());

        return sb.toString();
    }

    public static Color stringToColor(String rgbString) {
        String[] t = rgbString.split(",");
        int m_red = Integer.parseInt(t[0]);
        int m_green = Integer.parseInt(t[1]);
        int m_blue = Integer.parseInt(t[2]);

        return new Color(m_red, m_green, m_blue);
    }
}
