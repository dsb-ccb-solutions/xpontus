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

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.utils.MimeTypesProvider;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import java.io.File;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import net.sf.xpontus.controllers.impl.ModificationHandler;


/**
 * Action to save a document under a new name
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SaveAsActionImpl extends DefaultDocumentAwareActionImpl
{
    public static final String BEAN_ALIAS = "action.saveas";
    private JFileChooser chooser;

    /** Creates a new instance of SaveAsActionImpl */
    public SaveAsActionImpl()
    {
    }

    /**
     *
     * Save the document under a new name
     */
    public void run()
    {
        if (chooser == null)
        {
            chooser = new JFileChooser();
            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        }

        int answer = chooser.showOpenDialog(XPontusComponentsUtils.getTopComponent()
                                                                  .getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                File dest = chooser.getSelectedFile();

                if (dest.exists())
                {
                    int rep = JOptionPane.showConfirmDialog(chooser,
                            "Erase the file?", "The file exists!",
                            JOptionPane.YES_NO_OPTION);

                    if (rep == JOptionPane.YES_OPTION)
                    {
                        save(dest);
                    }
                }
                else
                {
                    save(dest);
                }
            }
            catch (Exception e)
            {
            }
        }
    }

    public void save(File output) throws Exception
    {
        // get an outputstream to save the document using Apache Commons VFS
        FileObject fo = VFS.getManager().toFileObject(output);
        FileContent content = fo.getContent();
        OutputStream bos = content.getOutputStream();
        
        // get the current document
        JTextComponent editor = DefaultXPontusWindowImpl.getInstance()
                                                        .getDocumentTabContainer()
                                                        .getCurrentEditor();
        bos.write(editor.getText().getBytes());
        bos.flush();
        bos.close();

        // dummy mime type detection
        String mm = MimeTypesProvider.getInstance().getMimeType(output.getName());

        // add the mime type
        editor.putClientProperty(XPontusConstantsIF.CONTENT_TYPE, mm);

        // add information about the file location
        editor.putClientProperty(XPontusConstantsIF.FILE_OBJECT, fo);

        // removed the modified flag
        ModificationHandler handler = (ModificationHandler) editor.getClientProperty(XPontusConstantsIF.MODIFICATION_HANDLER);
        handler.setModified(false);
    }
}
