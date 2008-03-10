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

import com.ibm.icu.text.CharsetDetector;

import com.vlsolutions.swing.docking.Dockable;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.controllers.impl.ModificationHandler;
import net.sf.xpontus.controllers.impl.XPontusUndoManager;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.modules.gui.components.XPontusEditorUI;
import net.sf.xpontus.utils.FileHistoryList;
import net.sf.xpontus.utils.MimeTypesProvider;
import net.sf.xpontus.utils.XPontusComponentsUtils;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;
import org.apache.commons.vfs.provider.local.LocalFile;

import java.awt.Toolkit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


/**
 * Action to save a document under a new name
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SaveAsActionImpl extends SimpleDocumentAwareActionImpl
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
    public void execute()
    {
        if (chooser == null)
        {
            chooser = new JFileChooser();
        }

        DocumentTabContainer dtc = DefaultXPontusWindowImpl.getInstance()
                                                           .getDocumentTabContainer();

        chooser.setDialogTitle("Save " +
            dtc.getCurrentDockable().getDockKey().getName());

        doSave();
    }

    public void doSave()
    {
        int answer = chooser.showSaveDialog(XPontusComponentsUtils.getTopComponent()
                                                                  .getDisplayComponent());

        // open the selected files
        if (answer == JFileChooser.APPROVE_OPTION)
        {
            try
            {
                File f = chooser.getSelectedFile();
                FileObject dest = VFS.getManager().toFileObject(f);

                if (dest.exists())
                {
                    Toolkit.getDefaultToolkit().beep();

                    int rep = JOptionPane.showConfirmDialog(chooser,
                            "Erase the file?", "The file exists!",
                            JOptionPane.YES_NO_OPTION);

                    if (rep == JOptionPane.YES_OPTION)
                    {
                        save(dest);
                    }
                    else
                    {
                        doSave();
                    }
                }
                else
                {
                    save(dest);
                }
            }
            catch (Exception e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @param fo
     * @throws java.lang.Exception
     */
    public void save(FileObject fo) throws Exception
    {
        // get the current document
        IDocumentContainer container = (IDocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                    .getDocumentTabContainer()
                                                                                    .getCurrentDockable();

        final JTextComponent editor = container.getEditorComponent();

        OutputStream bos = null;

        if (fo instanceof LocalFile)
        {
            bos = new FileOutputStream(fo.getName().getPath());
        }
        else
        {
            fo.getContent().getOutputStream();
        }

        FileHistoryList.addFile(fo.getName().getURI());

        final byte[] b = editor.getText().getBytes();
        bos.write(b);
        bos.flush();
        bos.close();

        String m_ext = FilenameUtils.getExtension(fo.getName().getBaseName());

        // dummy mime type detection
        String mm = MimeTypesProvider.getInstance()
                                     .getMimeType(fo.getName().getBaseName());

        final Object currentMime = editor.getClientProperty(XPontusConstantsIF.CONTENT_TYPE);

        // add the mime type
        editor.putClientProperty(XPontusConstantsIF.CONTENT_TYPE, mm);

        // add information about the file location
        editor.putClientProperty(XPontusConstantsIF.FILE_OBJECT, fo);

        Dockable dc = DefaultXPontusWindowImpl.getInstance()
                                              .getDocumentTabContainer()
                                              .getCurrentDockable();
        dc.getDockKey().setTooltip(fo.getURL().toExternalForm());
        dc.getDockKey().setName(fo.getName().getBaseName());

        // removed the modified flag
        ModificationHandler handler = (ModificationHandler) editor.getClientProperty(XPontusConstantsIF.MODIFICATION_HANDLER);
        handler.setModified(false);

        if (currentMime != null)
        {
            if (!currentMime.equals(mm))
            {
                System.out.println("Mime type changed");
                System.out.println("extension is :" + m_ext);
                editor.setUI(new XPontusEditorUI(editor,
                        fo.getName().getBaseName()));

                CharsetDetector detector = new CharsetDetector();
                detector.setText(b);

                if (m_ext != null)
                {
                    Document doc = editor.getDocument();

                    if (m_ext.toLowerCase().endsWith("xsl"))
                    {
                        doc.putProperty("BUILTIN_COMPLETION", "XSL");
                    }
                    else if (m_ext.toLowerCase().endsWith("xsd"))
                    {
                        doc.putProperty("BUILTIN_COMPLETION", "XSD");
                    }
                    else if (m_ext.toLowerCase().endsWith("html") ||
                            m_ext.endsWith("htm"))
                    {
                        doc.putProperty("BUILTIN_COMPLETION", "HTML");
                    }
                }

                handler = new ModificationHandler(container);
                editor.read(detector.detect().getReader(), null);
                handler.setModified(false);

                XPontusUndoManager _undo = new XPontusUndoManager();
                editor.putClientProperty(XPontusConstantsIF.UNDO_MANAGER, _undo);

                editor.getDocument().addUndoableEditListener(new UndoableEditListener()
                    {
                        public void undoableEditHappened(
                            UndoableEditEvent event)
                        {
                            ((XPontusUndoManager) editor.getClientProperty(XPontusConstantsIF.UNDO_MANAGER)).addEdit(event.getEdit());
                        }
                    });
            }
        }
    }
}
