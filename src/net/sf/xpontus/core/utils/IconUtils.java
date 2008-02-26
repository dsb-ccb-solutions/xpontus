/*
 * IconUtils.java
 *
 * Created on November 23, 2005, 8:00 PM
 *
 *  Copyright (C) 2005 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.core.utils;

import javax.swing.ImageIcon;


/**
 * A class to describe user preferences
 * @author Yves Zoundi
 */
public class IconUtils
  {
    private static IconUtils iconUtils;
    private static String style = "Crystal";

    private IconUtils()
      {
      }

    /**
     *
     * @param style
     */
    public static void setStyle(String _style)
      {
        style = _style;
      }

    /**
     *
     * @return
     */
    public static IconUtils getInstance()
      {
        if (iconUtils == null)
          {
            iconUtils = new IconUtils();
          }

        return iconUtils;
      }

    /**
     *
     * @param image
     * @return
     */
    public ImageIcon getIcon(String image)
      {
        String path = image.replaceFirst("_PATH_", style);
        java.net.URL url = getClass().getResource(path);

        if (url == null)
          {
            return null;
          }
        else
          {
            ImageIcon icon = new ImageIcon(url);

            return icon;
          }
      }
  }
