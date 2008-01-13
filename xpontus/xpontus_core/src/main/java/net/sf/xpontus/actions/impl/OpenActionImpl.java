/*
 * OpenActionImpl.java
 *
 * Created on June 29, 2007, 7:16 PM
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
package net.sf.xpontus.actions.impl;

import edu.ucla.loni.ccb.vfsbrowser.VFSBrowser;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.vfs.FileObject;

import javax.swing.JFileChooser;
import net.sf.xpontus.constants.XPontusConstantsIF;


/**
 * @version 0.0.1
 * Action to open a document
 * @author Yves Zoundi
 */
public class OpenActionImpl extends XPontusThreadedActionImpl {
    public static final String BEAN_ALIAS = "action.open";
    private VFSBrowser vfsb;

    /**
     * Creates a new instance of OpenActionImpl
     */
    public OpenActionImpl() {
    }

    public void run() {
        if (vfsb == null) {
            vfsb = new VFSBrowser(); 
            vfsb.setDialogTitle("Select a file");
            vfsb.setMultiSelectionEnabled(true);
            vfsb.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }

        int answer = vfsb.showOpenDialog(XPontusComponentsUtils.getTopComponent()
                                                               .getDisplayComponent());

        // open the selected files
        if (answer == javax.swing.JFileChooser.APPROVE_OPTION) {
            DocumentTabContainer dtc = DefaultXPontusWindowImpl.getInstance()
                                                               .getDocumentTabContainer();

            try {
                FileObject[] tmps = vfsb.getSelectedFiles();

                for (int i = 0; i < tmps.length; i++) {
                    dtc.createEditorFromFileObject(tmps[i]);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
}
