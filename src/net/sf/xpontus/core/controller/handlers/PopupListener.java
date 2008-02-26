/*
 * PopupListener.java
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
package net.sf.xpontus.core.controller.handlers;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;


/**
 * A mouse listener to display a popup menu
 * @author Yves Zoundi
 */
public class PopupListener extends MouseAdapter
  {
    private JPopupMenu popup;

    /**
     * Create a new instance of PopupListener
     */
    public PopupListener()
      {
      }

    /**
     * Create a new instance of PopupListener
     * @param popup A popup menu
     */
    public PopupListener(JPopupMenu popup)
      {
        setPopup(popup);
      }

    /**
     *
     * @param e
     */
    public void mouseReleased(MouseEvent e)
      {
        if (e.isPopupTrigger() || (e.getButton() == 3))
          {
            popup.show(e.getComponent(), e.getX(), e.getY());
          }
      }

    /**
     * Set the popup menu associated to this listener
     * @param popup The popup menu associated to this listener
     */
    public void setPopup(JPopupMenu popup)
      {
        this.popup = popup;
      }

    /**
     * Return the popup menu associated to this listener
     * @return The popup menu associated to this listener
     */
    public JPopupMenu getPopup()
      {
        return popup;
      }
  }
