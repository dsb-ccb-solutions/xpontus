/*
 * TemplateManagerHandler.java
 *
 * Created on July 23, 2006, 2:24 PM
 *
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
package net.sf.xpontus.controller.handlers;

import net.sf.xpontus.view.TemplateManagerView;
import net.sf.xpontus.view.XPontusWindow;

import javax.swing.JOptionPane;


/**
 *
 * @author Yves Zoundi
 */
public class TemplateManagerHandler
  {
    public static final String CLOSE_METHOD = "close";
    public static final String MOVE_UP_METHOD = "moveUp";
    public static final String MOVE_DOWN_METHOD = "moveDown";
    public static final String DELETE_METHOD = "delete";
    public static final String OPEN_METHOD = "open";
    private TemplateManagerView view;

    /** Creates a new instance of TemplateManagerHandler */
    public TemplateManagerHandler(TemplateManagerView view)
      {
        this.view = view;
      }

    public void close()
      {
        view.setVisible(false);
      }

    public void moveUp()
      {
        JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
            "Not implemented yet!");
      }

    public void moveDown()
      {
        JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
            "Not implemented yet!");
      }

    public void delete()
      {
        JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
            "Not implemented yet!");
      }

    public void open()
      {
        JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
            "Not implemented yet!");
        view.setVisible(false);
      }
  }
