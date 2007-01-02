/*
 * LineView.java
 *
 * Created on 1 août 2005, 17:45
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
package net.sf.xpontus.view.editor;

import java.awt.Color;

import javax.swing.JLabel;


/**
 * A class which adds line numbering to the editor
 * @author Yves Zoundi
 */
public final class LineView extends javax.swing.JLabel
  {
    /**
         *
         */
    private static final long serialVersionUID = 8918688944159766510L;
    private javax.swing.JEditorPane edit;
    private java.awt.FontMetrics tpFontMetrics;
    private java.awt.Font fnt;
    private final int rhWidth = 50;

    /**
     *
     * @param edit the editor which has to display line numbers
     */
    public LineView(javax.swing.JEditorPane edit)
      {
        this.edit = edit;
        setForeground(Color.BLACK);
        fnt = edit.getFont();
        setFont(fnt);
        tpFontMetrics = getFontMetrics(fnt);
      }

    /**
     *
     * @return The preferred size of the line numbering column
     */
    public java.awt.Dimension getPreferredSize()
      {
        java.awt.Dimension dim;
        int w = rhWidth + 10;
        int h = (int) edit.getPreferredSize().getHeight();
        dim = new java.awt.Dimension(w, h);

        return dim;
      }

    /**
     *
     * @param g A graphic component
     */
    public void paintComponent(java.awt.Graphics g)
      {
        super.paintComponent(g);

        int h = g.getFontMetrics().getHeight();
        int i = h - tpFontMetrics.getDescent();
        int row = 1;

        while (i < getHeight())
          {
            String s = Integer.toString(row);
            g.drawString(s, (5 + rhWidth) - tpFontMetrics.stringWidth(s), i);
            i += h;
            row++;
          }
      }
  }
