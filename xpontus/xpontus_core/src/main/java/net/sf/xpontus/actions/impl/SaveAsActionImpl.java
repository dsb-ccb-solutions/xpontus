/*
 * SaveAsActionImpl.java
 *
 * Created on June 30, 2007, 11:59 AM
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

import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableContainer;
import com.vlsolutions.swing.docking.DockingUtilities;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.controllers.impl.ModificationHandler;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.utils.MimeTypesProvider;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileSystemManager;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.local.LocalFile;

import java.awt.Toolkit;

import java.io.File;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;


/**
 * Action to save a document under a new name
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SaveAsActionImpl extends DefaultDocumentAwareActionImpl {
    public static final String BEAN_ALIAS = "action.saveas";
    private JFileChooser chooser;

    /** Creates a new instance of SaveAsActionImpl */
    public SaveAsActionImpl() {
    }

    /**
     *
     * Save the document under a new name
     */
    public void run() {
        if (chooser == null) {
            chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }

        int answer = chooser.showSaveDialog(XPontusComponentsUtils.getTopComponent()
                                                                  .getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION) {
            try {
                File dest = chooser.getSelectedFile();

                if (dest.exists()) {
                    Toolkit.getDefaultToolkit().beep();

                    int rep = JOptionPane.showConfirmDialog(chooser,
                            "Erase the file?", "The file exists!",
                            JOptionPane.YES_NO_OPTION);

                    if (rep == JOptionPane.YES_OPTION) {
                        save(dest);
                    }
                } else {
                    save(dest);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void save(File output) throws Exception {
        // get the current document
        JTextComponent editor = DefaultXPontusWindowImpl.getInstance()
                                                        .getDocumentTabContainer()
                                                        .getCurrentEditor();

        // get an outputstream to save the document using Apache Commons VFS
        FileSystemManager fsm = VFS.getManager();
        OutputStream bos = null;
        FileObject fo = fsm.resolveFile(output.getAbsolutePath());

        boolean local = false;

        if (fo instanceof LocalFile) {
            bos = FileUtils.openOutputStream(output);
            local = true;
        } else {
            bos = fo.getContent().getOutputStream();
        }

        bos.write(editor.getText().getBytes());
        bos.flush();
        bos.close();

        // dummy mime type detection
        String mm = MimeTypesProvider.getInstance().getMimeType(output.getName());

        // add the mime type
        editor.putClientProperty(XPontusConstantsIF.CONTENT_TYPE, mm);

        if (local) {
            fo = fsm.toFileObject(output);
        }

        // add information about the file location
        editor.putClientProperty(XPontusConstantsIF.FILE_OBJECT, fo);

        Dockable dc = DefaultXPontusWindowImpl.getInstance()
                                              .getDocumentTabContainer()
                                              .getCurrentDockable();
        dc.getDockKey().setTooltip(fo.getURL().toExternalForm());
        dc.getDockKey().setName(fo.getName().getBaseName());

        DockableContainer dcp = DockingUtilities.findDockableContainer(dc);

        System.out.println("Parent:" + (dcp.getClass().getName()));

        // removed the modified flag
        ModificationHandler handler = (ModificationHandler) editor.getClientProperty(XPontusConstantsIF.MODIFICATION_HANDLER);
        handler.setModified(false);
    }
}
