/*
 * EditorContainer.java
 *
 * Created on February 4, 2007, 7:27 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view;

import com.vlsolutions.swing.docking.*;
import com.vlsolutions.swing.docking.DockKey;

import net.sf.xpontus.controller.handlers.ModificationHandler;
import net.sf.xpontus.core.utils.IconUtils;
import net.sf.xpontus.io.SmartEncodingInputStream;
import net.sf.xpontus.model.options.EditorOptionModel;
import net.sf.xpontus.view.editor.LineView;
import net.sf.xpontus.view.editor.SyntaxDocument;
import net.sf.xpontus.view.editor.SyntaxEditorkit;
import net.sf.xpontus.view.editor.syntax.xml.XMLParser;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Event;
import java.awt.event.KeyEvent;

import java.io.*;

import javax.swing.*;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;


/**
 *
 * @author mrcheeks
 */
public class EditorContainer implements Dockable {
    private Log logger = LogFactory.getLog(EditorContainer.class);
    private JScrollPane scrollPane;
    private Action closeAllInTab;
    private Action closeAllOtherInTab;
    private DockGroup group = new DockGroup("Editors");
    private JEditorPane editor;
    private DockKey key;
    private String image = "/net/sf/xpontus/icons/_PATH_/file.gif";

    /**
     * Creates a new instance of EditorContainer
     */
    public EditorContainer() {
        editor = new JEditorPane();
        scrollPane = new JScrollPane(editor);
    }

    public void completeSetup() {
        //        key.setResizeWeight(0.7f);
        Dimension dim = new Dimension(600, 100);
        this.getComponent().setMinimumSize(dim);
        this.getComponent().setPreferredSize(dim);
        key.setIcon(IconUtils.getInstance().getIcon(image));

        key.setResizeWeight(0.7f);
        key.setDockGroup(group);
        init();
    }

    public void setup(java.net.URL url) {
        String ext = FilenameUtils.getExtension(url.toExternalForm());

        try {
            FileObject fo = VFS.getManager().resolveFile(url.toExternalForm());
            editor.putClientProperty(EditorContainerConstants.FILE_OBJECT, fo);
            setup(fo.getContent().getInputStream(), ext, null);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void setup() {
        setup(null, ".txt", null);
    }

    public void setup(java.io.File file) {
        try {
            FileObject fo = VFS.getManager().toFileObject(file);

            java.io.InputStream is = fo.getContent().getInputStream();

            String ext = FilenameUtils.getExtension(file.getName());
            setup(is, ext, file.getName());
            editor.putClientProperty(EditorContainerConstants.FILE_OBJECT, fo);
            editor.setEditable(file.canWrite());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void setup(java.io.InputStream is, String ext, String fileinfo) {
        editor.setEditorKit(new SyntaxEditorkit(editor, ext));

        SyntaxDocument doc = (SyntaxDocument) editor.getDocument();
        doc.setLoading(true);

        key = new DockKey((fileinfo != null) ? fileinfo
                                             : ("Untitled" + this.hashCode() +
                ""));

        try {
            if (is != null) {
                SmartEncodingInputStream sei = new SmartEncodingInputStream(is);

                Reader reader = sei.getReader(); 

                Reader bufferedReader = new BufferedReader(reader);

                editor.read(bufferedReader, null);

                javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
                editor.putClientProperty("UNDO_MANAGER", _undo);

                editor.getDocument().addUndoableEditListener(new UndoableEditListener() {
                        public void undoableEditHappened(
                            UndoableEditEvent event) {
                            ((javax.swing.undo.UndoManager) editor.getClientProperty(
                                "UNDO_MANAGER")).addEdit(event.getEdit());
                        }
                    });
            }

            InputMap inputMap = editor.getInputMap();

            KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_A,
                    Event.CTRL_MASK);
            inputMap.put(key, DefaultEditorKit.selectAllAction);

            editor.putClientProperty("FILE_NEW", Boolean.FALSE);

            EditorOptionModel m = (EditorOptionModel) new EditorOptionModel().load();

            if (m.isShowLineNumbers()) {
                scrollPane.setRowHeaderView(new LineView(editor));
            }

            ModificationHandler handler = new ModificationHandler(editor);

            if (doc.getLexer().getClass() == XMLParser.class) {
                editor.putClientProperty(EditorContainerConstants.OUTLINE_VIEW_ENABLED,
                    Boolean.TRUE);
            }

            handler.setSaved();
            doc.setLoading(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void init() {
        DockingDesktop desk = XPontusWindow.getInstance().getDockingDesktop();

        closeAllInTab = TabbedContainerActions.createCloseAllAction(this, desk);
        closeAllOtherInTab = TabbedContainerActions.createCloseAllOtherAction(this,
                desk);

        DockableActionCustomizer customizer = new DockableActionCustomizer() {
                public void visitTabSelectorPopUp(JPopupMenu popUpMenu,
                    Dockable dockable) {
                    popUpMenu.add(new JMenuItem(closeAllInTab));
                    popUpMenu.add(new JMenuItem(closeAllOtherInTab));
                    popUpMenu.revalidate();
                }
            };

        customizer.setSingleDockableTitleBarPopUpCustomizer(true);
        customizer.setTabSelectorPopUpCustomizer(true);
        key.setActionCustomizer(customizer);
    }

    public DockKey getDockKey() {
        return key;
    }

    public Component getComponent() {
        return scrollPane;
    }

    public JEditorPane getEditorComponent() {
        return editor;
    }
}