/*
 * EditorTabContainer.java
 *
 * Created on February 4, 2007, 10:26 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view;

import com.sun.java.help.impl.SwingWorker;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.event.DockableSelectionEvent;
import com.vlsolutions.swing.docking.event.DockableSelectionListener;
import com.vlsolutions.swing.docking.event.DockableStateWillChangeEvent;
import com.vlsolutions.swing.docking.event.DockableStateWillChangeListener;
import net.sf.xpontus.core.controller.handlers.PopupListener;
import net.sf.xpontus.core.utils.BeanContainer;
import java.util.Iterator;
import java.util.Vector;
import javax.swing.Action;
import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.tree.DefaultMutableTreeNode;
import net.sf.xpontus.controller.handlers.ModificationHandler;
import net.sf.xpontus.view.editor.SyntaxDocument;


/**
 *
 * @author mrcheeks
 */

/**
 * @author mrcheeks
 *
 */
public class EditorTabContainer
{
    private javax.swing.JPopupMenu editorPopup;
    private Vector editors = new Vector();
    private boolean actionsEnabled = false;
    private javax.swing.JPopupMenu popup;
    private final String[] ACTIONS = 
        {
            "action.copy", "action.cut", "action.paste", "action.selectall"
        };
    private BeanContainer applicationContext;
    private final String[] TEXT_ACTIONS = 
        {
            "action.print", "action.insertcomment", "action.insertcdatasection",
            "action.save", "action.saveas", "action.spellcheck",
            "action.saveall", "action.closetab", "action.closeothers",
            "action.closetaball", "action.closeothers", "action.gotoline",
            "action.selectall", "action.cut", "action.copy", "action.xpath",
            "action.paste", "action.undo", "action.redo", "action.find",
            "action.indentxml", "action.commentxml", "action.tidy",
            "action.checkxml", "action.validate", "action.validateschema"
        };
    private DockingDesktop desktop;
    private JEditorPane currentEditor;
private Dockable currentDockable;

    public Dockable getCurrentDockable(){
        return currentDockable;
    }
    /** Creates a new instance of EditorTabContainer */
    public EditorTabContainer()
    {
        popup = new javax.swing.JPopupMenu();
        editorPopup = new javax.swing.JPopupMenu();
        desktop = XPontusWindow.getInstance().getDockingDesktop();

        desktop.addDockableSelectionListener(new DockableSelectionListener()
            {
                public void selectionChanged(DockableSelectionEvent e)
                {
                    Dockable selectedDockable = e.getSelectedDockable();

                    if (selectedDockable instanceof EditorContainer)
                    {
                        EditorContainer container = (EditorContainer) selectedDockable;
                        currentDockable = selectedDockable;
                        
                        currentEditor = container.getEditorComponent();

                        Document doc = currentEditor.getDocument();
                        Object outlineData = doc.getProperty("OUTLINE_DATA");

                         XPontusWindow.getInstance()
                                                                          .getOutlineDockable()
                                                                          .getRootNode().removeAllChildren();
                         
                        if (outlineData != null)
                        {
                            DefaultMutableTreeNode rootNode = (DefaultMutableTreeNode) outlineData;
                            DefaultMutableTreeNode oldNode = XPontusWindow.getInstance()
                                                                          .getOutlineDockable()
                                                                          .getRootNode();
                            oldNode.add(rootNode);
                        }
                           
                             XPontusWindow.getInstance().getOutlineDockable()
                                         .updateAll(); 
                    }
                }
            });
        desktop.addDockableStateWillChangeListener(new DockableStateWillChangeListener()
            {
                public void dockableStateWillChange(
                    DockableStateWillChangeEvent event)
                {
                    DockableState current = event.getCurrentState();

                    if ((current != null) &&
                            (current.getDockable() instanceof EditorContainer) &&
                            event.getFutureState().isClosed())
                    {
                        EditorContainer editor = (EditorContainer) current.getDockable();

                        if (editors.size() == 1)
                        {
                            editor.getDockKey()
                                  .setDockableState(DockableState.STATE_CLOSED);

                            PaneForm pane = XPontusWindow.getInstance().getPane();
 
                            desktop.replace(editor, pane);

                            pane.getDockKey()
                                .setDockableState(DockableState.STATE_DOCKED);
                            //event.cancel(); 
                            editors.remove(editor);
                            enableDocumentActions(false);
                            XPontusWindow.getInstance().getOutlineDockable()
                                         .getRootNode().removeAllChildren();
                            XPontusWindow.getInstance().getOutlineDockable()
                                         .updateAll();
                        }
                        else
                        {
                            editors.remove(editor);
                        }

                        currentDockable = null;
                        currentEditor = null;
                    }
                }
            });
    }

    private void createEditorPopup()
    {
        popup = new javax.swing.JPopupMenu();

        for (int i = 0; i < ACTIONS.length; i++)
        {
            popup.add((Action) this.applicationContext.getBean(ACTIONS[i]));
        }
    }

    /**
     * @return
     */
    public Iterator getEditorsIterator()
    {
        return editors.iterator();
    }

    /**
    * @return
    */
    public Vector getEditorsVector()
    {
        return editors;
    }

    private void configureEditorPopup()
    {
        for (int i = 0; i < ACTIONS.length; i++)
        {
            editorPopup.add((Action) applicationContext.getBean(ACTIONS[i]));
        }
    }

    /**
     * @param editor
     */
    public void setupEditor(EditorContainer editor)
    {
        SyntaxDocument doc = ((SyntaxDocument)editor.getEditorComponent().getDocument());
        
        if (editors.size() == 0)
        {
            
            
            doc.setLoading(true);
            PaneForm pane = XPontusWindow.getInstance().getPane();

            System.out.println("pane is not null:" + (pane != null));

            DockingDesktop desk = XPontusWindow.getInstance().getDockingDesktop();

            desk.registerDockable(editor);

            desk.replace(pane, editor);
//            desk.r

            editor.getDockKey().setDockableState(DockableState.STATE_DOCKED);

            editor.getEditorComponent().requestFocusInWindow();
        }
        else
        {
            DockingDesktop desk = XPontusWindow.getInstance().getDockingDesktop();

            final int last = editors.size() - 1;
            desk.createTab((EditorContainer) editors.get(last), editor,
                (last + 1), true);
        }

        editors.add(editor);

        if (!actionsEnabled)
        {
            enableDocumentActions(true);
        }

        currentEditor = editor.getEditorComponent(); 
        currentDockable = editor;
        currentEditor.addMouseListener(new PopupListener(popup));
        doc.setLoading(false);
        
        
        XPontusWindow.getInstance().configureDragAndDrop(currentEditor);
        

        
        
    }

    /**
     * @param file
     */
    public void createEditorFromFile(java.io.File file)
    {
        EditorContainer container = new EditorContainer();
        container.setup(file);
        container.completeSetup();
        setupEditor(container);
    }

    /**
     * @param url
     */
    public void createEditorFromURL(java.net.URL url)
    {
        EditorContainer container = new EditorContainer();
        container.setup(url);
        container.completeSetup();
        setupEditor(container);
    }

    /**
     *
     * @param is
     * @param tk
     */
    public void createEditorFromStream(java.io.InputStream is, String ext)
    {
        EditorContainer container = new EditorContainer();
        container.setup(is, ext, null);
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

        final SwingWorker worker = new SwingWorker()
            {
                public Object construct()
                {
                    for (int i = 0; i < TEXT_ACTIONS.length; i++)
                    {
                        ((Action) applicationContext.getBean(TEXT_ACTIONS[i])).setEnabled(b);
                    }

                    return null;
                }
            };

        worker.start();
    }

    /**
     *
     * @return
     */
    public final BeanContainer getApplicationContext()
    {
        return applicationContext;
    }

    /**
     *
     *
     * @param applicationContext
     */
    public final void setApplicationContext(BeanContainer applicationContext)
    {
        this.applicationContext = applicationContext;
        enableDocumentActions(false);
        createEditorPopup();
    }

    public JEditorPane getCurrentEditor()
    {
        return currentEditor;
    }
}
