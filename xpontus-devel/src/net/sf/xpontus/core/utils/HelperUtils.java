/*
 * EnvironmentModel.java
 *
 * Created on 2 octobre 2005, 16:25
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

import java.awt.*;

import java.net.URL;

import java.util.HashMap;

import javax.help.HelpBroker;
import javax.help.HelpSet;

import javax.swing.JComponent;
import javax.swing.JLabel;


/**
 * A helper class for the HelpAction
 * @author Yves Zoundi
 */
public class HelperUtils
  {
    private URL helpURL;

    /**
     * Create a new instance of HelperUtils
     */
    public HelperUtils()
      {
      }

    /**
     *
     * @return
     */
    public URL getHelpURL()
      {
        return helpURL;
      }

    /**
     *
     * @param helpURL
     */
    public void setHelpURL(URL helpURL)
      {
        this.helpURL = helpURL;
      }

    /**
     *
     * @return
     */
    public HelpBroker getHelpBroker()
      {
        HelpSet hs = null;
        HelpBroker hb = null;

        try
          {
            hs = new HelpSet(null, helpURL);
            hb = hs.createHelpBroker();

            Point location = new Point(200, 200);
            hb.setLocation(location);

            final HashMap map = new HashMap();
            Object aakey = RenderingHints.KEY_ANTIALIASING;
            Object rdkey = RenderingHints.KEY_RENDERING;
            Object aaval = RenderingHints.VALUE_ANTIALIAS_ON;
            Object rdval = RenderingHints.VALUE_RENDER_QUALITY;
            map.put(aakey, aaval);
            map.put(rdkey, rdval);

            JComponent label = new JLabel()
                  {
                    public void paintComponent(Graphics g)
                      {
                        Graphics2D g2 = (Graphics2D) g;
                        g2.setRenderingHints(map);
                        super.paintComponent(g);
                      }
                  };

            hb.setFont(label.getFont());
          }
        catch (Exception e)
          {
            e.printStackTrace();

            //            return null;
          }

        return hb;
      }
  }
