/*
 * DocumentTabContainer.java
 *
 * Created on July 1, 2007, 10:51 AM
 *
 * Copyright (C) 2005-2008 Yves Zoundi
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

import com.vlsolutions.swing.docking.DockGroup;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.event.DockableSelectionEvent;
import com.vlsolutions.swing.docking.event.DockableSelectionListener;
import com.vlsolutions.swing.docking.event.DockableStateWillChangeEvent;
import com.vlsolutions.swing.docking.event.DockableStateWillChangeListener;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.syntax.SyntaxDocument;
import net.sf.xpontus.utils.DocumentAwareComponentHolder;
import net.sf.xpontus.utils.DocumentContainerChangeEvent;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.vfs.FileObject;

import java.util.Iterator;
import java.util.Vector;

import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author Yves Zoundi
 */
public class DocumentTabContainer {
    protected static DockGroup group = new DockGroup("Editors");
    private Vector editors = new Vector();
    private boolean actionsEnabled = false;
    private DockingDesktop desktop;
    private JTextComponent currentEditor;
    private Dockable currentDockable;

    /** Creates a new instance of EditorTabContainer
     * @param desktop
     */
    public DocumentTabContainer(final DockingDesktop desktop) {
        this.desktop = desktop;

        desktop.addDockableSelectionListener(new DockableSelectionListener() {
                public void selectionChanged(DockableSelectionEvent e) {
                    Dockable selectedDockable = e.getSelectedDockable();

                    if (selectedDockable == null) {
                        return;
                    }

                    if (selectedDockable instanceof DocumentContainer) {
                        DocumentContainer container = (DocumentContainer) selectedDockable;

                        currentDockable = selectedDockable;

                        currentEditor = container.getEditorComponent();

                        SyntaxDocument m_doc = (SyntaxDocument) currentEditor.getDocument();
                        Object o = m_doc.getProperty(XPontusConstantsIF.OUTLINE_INFO);

                        if (o != null) {
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
                            DefaultXPontusWindowImpl.getInstance().getOutline()
                                                    .updateAll(node);
                        } else {
                            DefaultXPontusWindowImpl.getInstance().getOutline()
                                                    .updateAll(new DefaultMutableTreeNode());
                        }

                        DocumentAwareComponentHolder.getInstance()
                                                    .notifyComponents(new DocumentContainerChangeEvent(
                                container));

                        currentEditor.requestFocus();
                        currentEditor.grabFocus();
                    }
                }
            });
        desktop.addDockableStateWillChangeListener(new DockableStateWillChangeListener() {
                public void dockableStateWillChange(
                    DockableStateWillChangeEvent event) {
                    DockableState current = event.getCurrentState();

                    if ((current != null) &&
                            (current.getDockable() instanceof DocumentContainer) &&
                            event.getFutureState().isClosed()) {
                        DocumentContainer editor = (DocumentContainer) current.getDockable();

                        if (editors.size() == 1) {
                            editor.getDockKey()
                                  .setDockableState(DockableState.STATE_CLOSED);

                            Dockable pane = ((DefaultXPontusWindowImpl) XPontusComponentsUtils.getTopComponent()).getDefaultPane();

                            desktop.replace(editor, pane);

                            DefaultXPontusWindowImpl.getInstance().getOutline()
                                                    .updateAll(new DefaultMutableTreeNode());

                            pane.getDockKey()
                                .setDockableState(DockableState.STATE_DOCKED);
                            DocumentAwareComponentHolder.getInstance()
                                                        .notifyComponents(new DocumentContainerChangeEvent(
                                    null));
                            editors.remove(editor);

                            currentDockable = null;
                            currentEditor = null;
                        } else {
                            DocumentAwareComponentHolder.getInstance()
                                                        .notifyComponents(new DocumentContainerChangeEvent(
                                    editor));
                            editors.remove(editor);
                        }
                    }
                }
            });
    }

    /**
     *
     * @return
     */
    public Dockable getCurrentDockable() {
        return currentDockable;
    }

    /**
     * @return
     */
    public Iterator getEditorsAsIterator() {
        return editors.iterator();
    }

    /**
     *
     * @return
     */
    public DocumentContainer[] getEditorsAsArray() {
        DocumentContainer[] editorsArray = new DocumentContainer[editors.size()];
        editors.copyInto(editorsArray);

        return editorsArray;
    }

    /**
    * @return
    */
    public Vector getEditorsAsVector() {
        return editors;
    }

    /**
     * @param editor
     */
    public void setupEditor(DocumentContainer editor) {
        DockingDesktop desk = DefaultXPontusWindowImpl.getInstance().getDesktop();

        if (editors.size() == 0) {
            Dockable pane = DefaultXPontusWindowImpl.getInstance()
                                                    .getDefaultPane();

            desk.registerDockable(editor);

            int lastState = pane.getDockKey().getDockableState();

            if (lastState == DockableState.STATE_MAXIMIZED) {
                desk.restore(pane);
                desk.replace(pane, editor);
                editor.getDockKey().setDockableState(DockableState.STATE_DOCKED);
                desk.maximize(editor);
            } else {
                desk.replace(pane, editor);
                editor.getDockKey().setDockableState(DockableState.STATE_DOCKED);
            }

            editor.getEditorComponent().requestFocusInWindow();
        } else {
            final int last = editors.size() - 1;
            Dockable lastDockable = (Dockable) editors.get(last);
            int lastState = lastDockable.getDockKey().getDockableState();

            if (lastState == DockableState.STATE_MAXIMIZED) {
                desk.restore(lastDockable);
                desk.createTab(lastDockable, editor, (last + 1), true);
                editor.getDockKey().setDockableState(DockableState.STATE_DOCKED);
                desk.maximize(editor);
            } else {
                desk.createTab(lastDockable, editor, (last + 1), true);
            }
        }

        editors.add(editor);

        if (!actionsEnabled) {
            enableDocumentActions(true);
        }

        currentEditor = editor.getEditorComponent();
        currentDockable = editor;

        DocumentAwareComponentHolder.getInstance()
                                    .notifyComponents(new DocumentContainerChangeEvent(
                editor));
    }

    /**
     * @param file
     */
    public void createEditorFromFile(java.io.File file) {
        if (!file.exists()) {
            return;
        }

        DocumentContainer container = new DocumentContainer();
        container.setup(file);
        container.completeSetup();
        setupEditor(container);
    }

    /**
     * @param fo
     */
    public void createEditorFromFileObject(FileObject fo) {
        try {
            if (!fo.exists()) {
                return;
            }
        } catch (Exception e) {
            return;
        }

        DocumentContainer container = new DocumentContainer();
        container.setup(fo);
        container.completeSetup();
        setupEditor(container);
    }

    /**
     *
     */
    public void createEditorForNewFile() {
        DocumentContainer container = new DocumentContainer();
        container.setup();
        container.completeSetup();
        setupEditor(container);
    }

    /**
     * @param url
     */
    public void createEditorFromURL(java.net.URL url) {
        DocumentContainer container = new DocumentContainer();
        container.setup(url);
        container.completeSetup();
        setupEditor(container);
    }

    /**
     *
     * @param b
     */
    public void enableDocumentActions(final boolean b) {
        actionsEnabled = b;
    }

    /**
     *
     * @return
     */
    public JTextComponent getCurrentEditor() {
        return currentEditor;
    }
}
