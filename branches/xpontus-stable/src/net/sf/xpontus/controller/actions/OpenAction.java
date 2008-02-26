/*
 * OpenAction.java
 *
 * Created on 18 juillet 2005, 02:55
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

import java.io.File;
import net.sf.xpontus.controller.handlers.RecentFileListActionListener;


/**
 * Action to open one or multiple files
 * @author Yves Zoundi
 */
public class OpenAction extends ThreadedAction {
    private javax.swing.JFileChooser chooser;
    
    /** Creates a new instance of OpenAction */
    public OpenAction() {
        chooser = new javax.swing.JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
    }
    
    /**
     * The initial directory to open
     * @param dir the initial directory to open
     */
    public void setFileDir(String dir) {
        chooser.setCurrentDirectory(new File(dir));
    }
    
    /**
     * @see net.sf.xpontus.core.controller.actions#execute()
     */
    public void execute() {
        int answer = chooser.showOpenDialog(XPontusWindow.getInstance()
        .getFrame());
        
        if (answer == javax.swing.JFileChooser.APPROVE_OPTION) {
            final java.io.File[] selectedFiles = chooser.getSelectedFiles();
            
            for (int i = 0; i < selectedFiles.length; i++) {
                XPontusWindow.getInstance().getPane().createEditorFromFile(selectedFiles[i]);
                RecentFileListActionListener listener = (RecentFileListActionListener)XPontusWindow.getInstance().getApplicationContext().getBean("recentFilesListener");
                listener.addFile(selectedFiles[i]);
            }
        }
    }
}
