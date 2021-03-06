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

import java.util.Iterator;
import java.util.List;
import java.util.Vector;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.text.JTextComponent;
import javax.swing.tree.DefaultMutableTreeNode;

import net.sf.xpontus.actions.impl.SaveActionImpl;
import net.sf.xpontus.actions.impl.ViewMessagesWindowActionImpl;
import net.sf.xpontus.actions.impl.ViewOutlineWindowActionImpl;
import net.sf.xpontus.actions.impl.ViewXPathWindowActionImpl;
import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusFileConstantsIF;
import net.sf.xpontus.events.TabChangeEventListener;
import net.sf.xpontus.events.TabChangedEvent;
import net.sf.xpontus.plugins.evaluator.XPathResultsDockable;
import net.sf.xpontus.plugins.ioc.IOCPlugin;
import net.sf.xpontus.plugins.perspectives.PerspectiveHelper;
import net.sf.xpontus.syntax.SyntaxDocument;
import net.sf.xpontus.utils.DocumentAwareComponentHolder;
import net.sf.xpontus.utils.DocumentContainerChangeEvent;
import net.sf.xpontus.utils.FileHistoryList;
import net.sf.xpontus.utils.MimeTypesProvider;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.vfs.FileObject;

import com.sun.java.help.impl.SwingWorker;
import com.vlsolutions.swing.docking.DockGroup;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.DockingUtilities;
import com.vlsolutions.swing.docking.TabbedDockableContainer;
import com.vlsolutions.swing.docking.event.DockableSelectionEvent;
import com.vlsolutions.swing.docking.event.DockableSelectionListener;
import com.vlsolutions.swing.docking.event.DockableStateWillChangeEvent;
import com.vlsolutions.swing.docking.event.DockableStateWillChangeListener;


/**
 * Editor container
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class DocumentTabContainer
{
    protected static DockGroup group = new DockGroup("Editors");
    private static List<TabChangeEventListener> tabChangeListenersList = new CopyOnWriteArrayList<TabChangeEventListener>();
    private static final DefaultMutableTreeNode EMPTY_OUTLINE = new DefaultMutableTreeNode();
    private Vector<IDocumentContainer> editors = new Vector<IDocumentContainer>();
    private boolean actionsEnabled = false;
    private DockingDesktop desktop;
    private JTextComponent currentEditor;
    private boolean closeAccepted = false;
    private Dockable currentDockable;
    private Dockable previousSelection;
    private ExecutorService executorSVC = Executors.newSingleThreadExecutor();

    /** Creates a new instance of EditorTabContainer
     * @param m_desktop
     */
    public DocumentTabContainer(final DockingDesktop m_desktop)
    {
        this.desktop = m_desktop;

        desktop.addDockableSelectionListener(new DockableSelectionListener()
            {
                public void selectionChanged(DockableSelectionEvent e)
                {
                    Dockable selectedDockable = e.getSelectedDockable();

                    if (selectedDockable == null)
                    {
                        DefaultXPontusWindowImpl.getInstance().getOutline()
                                                .updateAll(EMPTY_OUTLINE);

                        return;
                    }

                    if (selectedDockable instanceof IDocumentContainer)
                    {
                        IDocumentContainer container = (IDocumentContainer) selectedDockable;
                        container.getEditorComponent().grabFocus();

                        currentDockable = selectedDockable;

                        currentEditor = container.getEditorComponent();

                        currentEditor.setCaretPosition(currentEditor.getCaretPosition());

                        boolean newSelection = false;

                        if (previousSelection == null)
                        {
                            previousSelection = selectedDockable;
                            newSelection = true;
                        }
                        else if (previousSelection.getDockKey()
                                                      .equals(selectedDockable.getDockKey()))
                        {
                        }
                        else
                        {
                            newSelection = true;
                            previousSelection = selectedDockable;
                        }

                        currentEditor.requestFocusInWindow();
                        currentEditor.requestFocus();
                        currentEditor.grabFocus();

                        if (newSelection)
                        {
                            updateSelection(container);
                        }
                    }
                }
            });
        desktop.addDockableStateWillChangeListener(new DockableStateWillChangeListener()
            {
                public boolean isAccepted()
                {
                    return closeAccepted;
                }

                public void dockableStateWillChange(
                    DockableStateWillChangeEvent event)
                {
                    DockableState current = event.getCurrentState();

                    if ((current != null) &&
                            (current.getDockable() instanceof IDocumentContainer) &&
                            event.getFutureState().isClosed())
                    {
                        closeAccepted = true;

                        IDocumentContainer editor = (IDocumentContainer) current.getDockable();
                        SaveActionImpl saveAction = (SaveActionImpl) DefaultXPontusWindowImpl.getInstance()
                                                                                             .getIOCContainer()
                                                                                             .getBean(SaveActionImpl.BEAN_ALIAS);

                        Object mh = editor.getEditorComponent()
                                          .getClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED);

                        if (mh != null)
                        {
                            if (mh.equals(Boolean.TRUE))
                            {
                                saveAction.saveDocument(editor.getDockKey()
                                                              .getTooltip());
                            }
                        }

                        closeAccepted = true;

                        if (editors.size() == 1)
                        {
                            editor.getDockKey()
                                  .setDockableState(DockableState.STATE_CLOSED);

                            Dockable pane = ((DefaultXPontusWindowImpl) XPontusComponentsUtils.getTopComponent()).getDefaultPane();

                            desktop.replace(editor, pane);

                            previousSelection = null;

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
                        }
                        else
                        {
                            DocumentAwareComponentHolder.getInstance()
                                                        .notifyComponents(new DocumentContainerChangeEvent(
                                    editor));
                            editors.remove(editor);
                            editors.get(editors.size() - 1).getEditorComponent()
                                   .grabFocus();
                        }
                    }
                    else if ((current != null) &&
                            (current.getDockable() instanceof OutlineViewDockable) &&
                            event.getFutureState().isClosed())
                    {
                        IOCPlugin iocContainer = DefaultXPontusWindowImpl.getInstance()
                                                                         .getIOCContainer();
                        ViewOutlineWindowActionImpl m_action = (ViewOutlineWindowActionImpl) iocContainer.getBean(ViewOutlineWindowActionImpl.BEAN_ALIAS);
                        m_action.setName("Show outline");
                    }
                    else if ((current != null) &&
                            (current.getDockable() instanceof MessagesWindowDockable) &&
                            event.getFutureState().isClosed())
                    {
                        IOCPlugin iocContainer = DefaultXPontusWindowImpl.getInstance()
                                                                         .getIOCContainer();
                        ViewMessagesWindowActionImpl m_action = (ViewMessagesWindowActionImpl) iocContainer.getBean(ViewMessagesWindowActionImpl.BEAN_ALIAS);
                        m_action.setName("Show Messages window");
                    }
                    else if ((current != null) &&
                            (current.getDockable() instanceof XPathResultsDockable) &&
                            event.getFutureState().isClosed())
                    {
                        IOCPlugin iocContainer = DefaultXPontusWindowImpl.getInstance()
                                                                         .getIOCContainer();
                        ViewXPathWindowActionImpl m_action = (ViewXPathWindowActionImpl) iocContainer.getBean(ViewXPathWindowActionImpl.BEAN_ALIAS);
                        m_action.setName("Show XPath window");
                    }
                }
            });
    }

    private void updateSelection(final IDocumentContainer container)
    {
        SyntaxDocument m_doc = (SyntaxDocument) currentEditor.getDocument();
        Object o = m_doc.getProperty(XPontusConstantsIF.OUTLINE_INFO);

        if (o != null)
        {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
            DefaultXPontusWindowImpl.getInstance().getOutline().updateAll(node);
        }
        else
        {
            DefaultXPontusWindowImpl.getInstance().getOutline()
                                    .updateAll(new DefaultMutableTreeNode());
        }

        DocumentAwareComponentHolder.getInstance()
                                    .notifyComponents(new DocumentContainerChangeEvent(
                container));

        SwingWorker worker = new SwingWorker()
            {
                public Object construct()
                {
                    fireTabChangeEvent(new TabChangedEvent(container));

                    return null;
                }
            };

        worker.start();
    }

    public static void addTabChangeEventListener(
        TabChangeEventListener listener)
    {
        tabChangeListenersList.add(listener);
    }

    /**
     * @param listener
     */
    public static void removeTabChangeEventListener(
        TabChangeEventListener listener)
    {
        tabChangeListenersList.remove(listener);
    }

    /**
     * @param e
     */
    protected void fireTabChangeEvent(final TabChangedEvent e)
    {
        for (final TabChangeEventListener tce : tabChangeListenersList)
        {
            tce.onTabChange(e);
        }
    }

    /**
     *
     * @return
     */
    public synchronized Dockable getCurrentDockable()
    {
        return currentDockable;
    }

    /**
     * @return
     */
    public Iterator<IDocumentContainer> getEditorsAsIterator()
    {
        return editors.iterator();
    }

    /**
     *
     * @return
     */
    public IDocumentContainer[] getEditorsAsArray()
    {
        IDocumentContainer[] editorsArray = new IDocumentContainer[editors.size()];
        editors.copyInto(editorsArray);

        return editorsArray;
    }

    /**
    * @return
    */
    public Vector<IDocumentContainer> getEditorsAsVector()
    {
        return editors;
    }

    /**
     * @param editor
     */
    public void setupEditor(IDocumentContainer editor)
    {
        DockingDesktop desk = DefaultXPontusWindowImpl.getInstance().getDesktop();

        if (editors.size() == 0)
        {
            Dockable pane = DefaultXPontusWindowImpl.getInstance()
                                                    .getDefaultPane();

            desk.registerDockable(editor);

            int lastState = pane.getDockKey().getDockableState();

            if (lastState == DockableState.STATE_MAXIMIZED)
            {
                desk.restore(pane);
                desk.replace(pane, editor);
                editor.getDockKey().setDockableState(DockableState.STATE_DOCKED);
                desk.maximize(editor);
            }
            else
            {
                desk.replace(pane, editor);
                editor.getDockKey().setDockableState(DockableState.STATE_DOCKED);
            }

            editor.getEditorComponent().requestFocusInWindow();
        }
        else
        {
            final int last = editors.size() - 1;
            Dockable lastDockable = (Dockable) editors.get(last);
            int lastState = lastDockable.getDockKey().getDockableState();

            if (lastState == DockableState.STATE_MAXIMIZED)
            {
                desk.restore(lastDockable);
                desk.createTab(lastDockable, editor, (last + 1), true);
                editor.getDockKey().setDockableState(DockableState.STATE_DOCKED);
                desk.maximize(editor);
            }
            else
            {
                desk.createTab(lastDockable, editor, (last + 1), true);
            }

            editor.getEditorComponent()
                  .setCaretPosition(editor.getEditorComponent()
                                          .getCaretPosition());
            editor.getEditorComponent().grabFocus();
        }

        editors.add(editor);

        if (!actionsEnabled)
        {
            enableDocumentActions(true);
        }

        currentEditor = editor.getEditorComponent();
        currentDockable = editor;

        DocumentAwareComponentHolder.getInstance()
                                    .notifyComponents(new DocumentContainerChangeEvent(
                editor));

        final SyntaxDocument mDoc = (SyntaxDocument) editor.getEditorComponent()
                                                           .getDocument();

        CurrentLineHighlighter.install(currentEditor);

        if (mDoc.getOutlinePlugin() != null)
        {
            executorSVC.submit(new Runnable()
                {
                    public void run()
                    {
                        mDoc.getOutlinePlugin().updateOutline(mDoc);
                    }
                });
        }

        if (mDoc.getCodeCompletion() != null)
        {
            executorSVC.submit(new Runnable()
                {
                    public void run()
                    {
                        mDoc.getCodeCompletion().init(mDoc);
                    }
                });
        }
    }

    /**
     * @param file
     */
    public void createEditorFromFile(java.io.File file)
    {
        if (!file.exists())
        {
            return;
        }

        String mimeType = MimeTypesProvider.getInstance()
                                           .getMimeType(file.getName());
        IDocumentContainer container = PerspectiveHelper.createPerspective(mimeType);
        container.setup(file);
        container.completeSetup();
        setupEditor(container);
    }

    /**
     * @param fileObject
     */
    public void createEditorFromFileObject(FileObject fileObject)
    {
        try
        {
            if (!fileObject.exists())
            {
                return;
            }
        }
        catch (Exception e)
        {
            return;
        }

        FileHistoryList.addFile(fileObject.getName().getURI());

        IDocumentContainer[] openEditors = getEditorsAsArray();
        IDocumentContainer alreadyOpened = null;

        for (IDocumentContainer dct : openEditors)
        {
            Object fileObjectProperty = dct.getEditorComponent()
                                           .getClientProperty(XPontusConstantsIF.FILE_OBJECT);

            if ((fileObjectProperty != null) && fileObjectProperty.equals(fileObject))
            {
                alreadyOpened = dct;

                break;
            }
        }

        if (alreadyOpened != null)
        {
            TabbedDockableContainer dockableContainer = DockingUtilities.findTabbedDockableContainer(alreadyOpened);

            if (dockableContainer != null)
            {
                dockableContainer.setSelectedDockable(alreadyOpened);
                alreadyOpened.getEditorComponent().grabFocus();
            }
        }
        else
        {
            String mimeType = MimeTypesProvider.getInstance()
                                               .getMimeType(fileObject.getName()
                                                              .getBaseName());
            IDocumentContainer container = PerspectiveHelper.createPerspective(mimeType);
            container.setup(fileObject);
            container.completeSetup();
            setupEditor(container);
        }
    }

    /**
     *
     */
    public void createEditorForNewFile()
    {
        IDocumentContainer container = PerspectiveHelper.createPerspective(
                "text/xml");
        container.setup();
        container.completeSetup();
        setupEditor(container);
    }

    /**
     * Method createEditorForTemplate ...
     *
     * @param templateName of type String
     * @param templatePath of type String
     */
    public void createEditorForTemplate(String templateName, String templatePath)
    {
        IDocumentContainer container = PerspectiveHelper.createPerspective(
                "text/xml");
        container.setupFromTemplate(templateName, templatePath);
        container.completeSetup();
        setupEditor(container);
    }

    /**
     * @param url
     */
    public void createEditorFromURL(java.net.URL url)
    {
        IDocumentContainer container = PerspectiveHelper.createPerspective(url.toExternalForm());
        container.setup(url);
        container.completeSetup();
        setupEditor(container);
    }

    /**
     *
     * @param b
     */
    public void enableDocumentActions(final boolean b)
    {
        actionsEnabled = b;
    }

    /**
     *
     * @return
     */
    public synchronized JTextComponent getCurrentEditor()
    {
        return currentEditor;
    }
}
