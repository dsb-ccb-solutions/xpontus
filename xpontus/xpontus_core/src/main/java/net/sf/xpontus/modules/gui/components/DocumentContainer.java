/*
 * DocumentContainer.java
 *
 * Created on July 1, 2007, 10:44 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.modules.gui.components;

import com.ibm.icu.text.CharsetDetector;

import com.vlsolutions.swing.docking.*;
import com.vlsolutions.swing.docking.DockKey;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusFileConstantsIF;
import net.sf.xpontus.controllers.impl.ModificationHandler;
import net.sf.xpontus.controllers.impl.PopupHandler;
import net.sf.xpontus.syntax.LineView;
import net.sf.xpontus.syntax.SyntaxDocument;
import net.sf.xpontus.utils.MimeTypesProvider;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.JTextComponent;


/**
 * Document container
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class DocumentContainer implements Dockable {
    private JComponent documentPanel;
    private JStatusBar status;
    private JScrollPane scrollPane;
    private Action closeAllInTab;
    private Action closeAllOtherInTab;
    private DockGroup group = new DockGroup("Editors");
    private JTextComponent editor;
    private DockKey key;
    private final String image = "/net/sf/xpontus/icons/file.gif";

    /**
     * Creates a new instance of EditorContainer
     */
    public DocumentContainer() {
        documentPanel = new JPanel();
        documentPanel.setLayout(new BorderLayout());
        editor = new JEditorPane();
        status = new JStatusBar();
        
        scrollPane = new JScrollPane(editor);
        scrollPane.setRowHeaderView(new LineView(editor));

        documentPanel.add(scrollPane, BorderLayout.CENTER);
        documentPanel.add(status, BorderLayout.SOUTH);
    }

    /**
     *
     * @return
     */
    public JStatusBar getStatusBar() {
        return status;
    }

    /**
     *
     */
    public void completeSetup() {
        Dimension dim = new Dimension(600, 400);
        this.getComponent().setMinimumSize(dim);
        this.getComponent().setPreferredSize(dim);

        key.setResizeWeight(0.7f);
        key.setDockGroup(group);

        ImageIcon icon = new ImageIcon(getClass().getResource(image));
        key.setIcon(icon);

        init();

        new ModificationHandler(this);
        editor.addMouseListener(new PopupHandler());
    }

    /**
     *
     * @param url
     */
    public void setup(java.net.URL url) {
        try {
            FileObject fo = VFS.getManager().resolveFile(url.toExternalForm());
            setup(fo, url.toExternalForm(), null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setup() {
        String mm = MimeTypesProvider.getInstance().getMimeType("file.xml");

        editor.putClientProperty(XPontusConstantsIF.CONTENT_TYPE, mm);

        editor.putClientProperty(XPontusConstantsIF.FILE_OBJECT, null);

        editor.setUI(new XPontusEditorUI(editor, "file.xml"));

        SyntaxDocument doc = (SyntaxDocument) editor.getDocument();
        doc.setLoading(true);

        key = new DockKey("Untitled" + this.hashCode() + "");

        try {
            CharsetDetector detector = new CharsetDetector();
            InputStream is = getClass()
                                 .getResourceAsStream("/net/sf/xpontus/templates/template.xml");
            detector.setText(new BufferedInputStream(is));

            try {
                editor.read(detector.detect().getReader(), null);
            } catch (Exception ioe) {
                editor.read(new InputStreamReader(is), null);
            }

            javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
            editor.putClientProperty(XPontusConstantsIF.UNDO_MANAGER, _undo);

            editor.getDocument().addUndoableEditListener(new UndoableEditListener() {
                    public void undoableEditHappened(UndoableEditEvent event) {
                        ((javax.swing.undo.UndoManager) editor.getClientProperty(XPontusConstantsIF.UNDO_MANAGER)).addEdit(event.getEdit());
                    }
                });

            editor.putClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED,
                Boolean.FALSE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     *
     * @param file
     */
    public void setup(java.io.File file) {
        try {
            FileObject fo = VFS.getManager().toFileObject(file);
            setup(fo, file.getAbsolutePath(), file.getName());
            editor.setEditable(file.canWrite());
            
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param fo
     * @param ext
     * @param fileinfo
     */
    public void setup(FileObject fo, String ext, String fileinfo) {
        if (ext == null) {
            ext = "file.txt";
        }

        String m_ext = FilenameUtils.getExtension(ext);

        if ((m_ext == null) || m_ext.trim().equals("")) {
            ext = "file.txt";
        }

        String mm = MimeTypesProvider.getInstance().getMimeType(ext);

        editor.putClientProperty(XPontusConstantsIF.CONTENT_TYPE, mm);

        editor.putClientProperty(XPontusConstantsIF.FILE_OBJECT, fo);

        editor.setUI(new XPontusEditorUI(editor, ext));

        SyntaxDocument doc = (SyntaxDocument) editor.getDocument();
        doc.setLoading(true);

        key = new DockKey(((fileinfo != null) && !fileinfo.trim().equals(""))
                ? fileinfo : ("Untitled" + this.hashCode() + ""));

        try {
            if (fo != null) {
                CharsetDetector detector = new CharsetDetector();

                byte[] b = IOUtils.toByteArray(fo.getContent().getInputStream());
                detector.setText(b);

                try {
                    editor.read(detector.detect().getReader(), null);
                } catch (Exception ioe) {
                    editor.read(new InputStreamReader(
                            new ByteArrayInputStream(b)), null);
                }

                javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
                editor.putClientProperty(XPontusConstantsIF.UNDO_MANAGER, _undo);

                editor.getDocument().addUndoableEditListener(new UndoableEditListener() {
                        public void undoableEditHappened(
                            UndoableEditEvent event) {
                            ((javax.swing.undo.UndoManager) editor.getClientProperty(XPontusConstantsIF.UNDO_MANAGER)).addEdit(event.getEdit());
                        }
                    });
            }

            editor.putClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED,
                Boolean.FALSE);
            
            editor.setCaretPosition(0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Init the component
     */
    public void init() {
        DockingDesktop desk = ((DefaultXPontusWindowImpl) XPontusComponentsUtils.getTopComponent()).getDesktop();

        closeAllInTab = TabbedContainerActions.createCloseAllAction(this, desk);
        closeAllOtherInTab = TabbedContainerActions.createCloseAllOtherAction(this,
                desk);

        DockableActionCustomizer customizer = new DockableActionCustomizer() {
                public void visitTabSelectorPopUp(JPopupMenu popUpMenu,
                    Dockable dockable) {
                    popUpMenu.add(closeAllInTab);
                    popUpMenu.add(closeAllOtherInTab);
                    popUpMenu.revalidate();
                }
            };

        customizer.setSingleDockableTitleBarPopUpCustomizer(true);
        customizer.setTabSelectorPopUpCustomizer(true);
        key.setActionCustomizer(customizer);
    }

    /**
     * The unique key of this component
     * @return The unique key of this dockable
     */
    public DockKey getDockKey() {
        return key;
    }

    /**
     * Returns the component to display
     * @return The component to display
     */
    public Component getComponent() {
        return documentPanel;
    }

    /**
     *
     * @return The text editor of this component
     */
    public JTextComponent getEditorComponent() {
        return editor;
    }
}
