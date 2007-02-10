/*
 * OpenRemoteAction.java
 *
 * Created on November 8, 2005, 7:03 PM
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
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.ThreadedAction;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.io.FilenameUtils;

import javax.swing.ProgressMonitorInputStream;


/**
 * Action to open one or multiple remote files
 * @author Yves Zoundi
 */
public class OpenRemoteAction extends ThreadedAction
  {
    private javax.swing.JFileChooser chooser;

    /** Creates a new instance of OpenAction */
    public OpenRemoteAction()
      {
      }

    public void execute()
      {
        XPontusWindow form = XPontusWindow.getInstance();

        String url = javax.swing.JOptionPane.showInputDialog(XPontusWindow.getInstance()
                                                                          .getFrame(),
                XPontusWindow.getInstance().getI18nMessage("openlocation.key"),
                XPontusWindow.getInstance()
                             .getI18nMessage("action.openremote.description"),
                javax.swing.JOptionPane.INFORMATION_MESSAGE);

        if (url != null)
          {
            if (!url.equals(""))
              {
                try
                  {
                    java.net.URL _url = new java.net.URL(url);
                    XPontusWindow.getInstance().getTabContainer().createEditorFromURL(_url);
                  }
                catch (Exception e)
                  {
                    javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                           .getFrame(),
                        e.getLocalizedMessage(), "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                  }
              }
          }
      }
  }
