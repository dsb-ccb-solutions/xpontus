/*
 * OpenRemoteAction.java
 *
 * Created on November 8, 2005, 7:03 PM
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
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
import net.sf.xpontus.view.editor.KitInfo;

import org.apache.commons.io.FilenameUtils;

//import org.syntax.jedit.tokenmarker.TokenMarker;
import javax.swing.ProgressMonitorInputStream;


/**
 * Action to open one or multiple remote files
 * @author Yves Zoundi
 */
public class OpenRemoteAction extends ThreadedAction {
    private javax.swing.JFileChooser chooser;

    /** Creates a new instance of OpenAction */
    public OpenRemoteAction() {
    }

    public void execute() {
        XPontusWindow form = XPontusWindow.getInstance();

        String url = javax.swing.JOptionPane.showInputDialog(XPontusWindow.getInstance()
                                                                          .getFrame(),
                "Enter the document url(http,ftp)", "Open a remote document",
                javax.swing.JOptionPane.INFORMATION_MESSAGE);

        if (url != null) {
            if (!url.equals("")) {
                try {
                    java.net.URL _url = new java.net.URL(url);
                    java.io.InputStream is = _url.openStream();
                    java.io.InputStream progressIs = new ProgressMonitorInputStream(XPontusWindow.getInstance()
                                                                                                 .getFrame(),
                            "Opening ...", is);
                    KitInfo kit = KitInfo.getInstance();
                    String ext = FilenameUtils.getExtension(_url.getPath());
                    XPontusWindow.getInstance().getPane()
                                 .createEditorFromStream(progressIs, ext);
                    progressIs.close();
                    is.close();
                } catch (Exception e) {
                    javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                           .getFrame(),
                        e.getLocalizedMessage(), "Error",
                        javax.swing.JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }
}
