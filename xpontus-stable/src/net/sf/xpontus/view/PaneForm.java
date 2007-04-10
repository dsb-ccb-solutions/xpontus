/*
 * PaneForm.java
 *
 *
 * Created on 1 ao�t 2005, 17:45
 *
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

package net.sf.xpontus.view;


import com.sun.java.help.impl.SwingWorker;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Reader;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JEditorPane;
import javax.swing.JPopupMenu;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import net.sf.xpontus.controller.actions.OpenAction;
import net.sf.xpontus.controller.handlers.ModificationHandler;
import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.core.controller.handlers.PopupListener;
import net.sf.xpontus.core.utils.IconUtils;
import net.sf.xpontus.model.options.EditorOptionModel;
import net.sf.xpontus.utils.EncodingHelper;
import net.sf.xpontus.view.components.JStatusBar;
import net.sf.xpontus.view.editor.KitInfo;
import net.sf.xpontus.view.editor.LineView;
import net.sf.xpontus.view.editor.XPontusEditorKit;
import org.springframework.context.ApplicationContext;
import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.TokenMarker;
import net.sf.xpontus.view.tabbedpane.*;
/**
 * The container which stores document into tabs
 * @author  Yves Zoundi
 */
public class PaneForm extends CloseableTabbedPane {
    
    final ImageIcon ic = IconUtils.getInstance().getIcon("/net/sf/xpontus/icons/_PATH_/file.gif");
    
    /** Creates new form BeanForm */
    public PaneForm() {
        initComponents();
//        this.addCloseListener(new CloseListenerImpl());
//        this.addDoubleClickListener(new DoubleClickListenerImpl());
//        this.addMouseListener(new PopupListener(editorPopup));
       
    }
    
    private class TabListener implements MouseListener {
        private javax.swing.Action closeOthers;
        public JPopupMenu popupmenu;
        public TabListener() {
            popupmenu = new JPopupMenu();
            popupmenu.add((Action)applicationContext.getBean("action.closetab"));
            closeOthers = (Action)applicationContext.getBean("action.closeothers");
            popupmenu.add(closeOthers);
            popupmenu.add((Action)applicationContext.getBean("action.closetaball"));
        }
        
        /** This method will display the popup if the mouseevent is a popup event
         *  and the event occurred over a tab
         */
        protected void checkPopup(MouseEvent e) {
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                if(PaneForm.this.getTabCount()>0){
                    int tab = PaneForm.this.getUI().tabForCoordinate(PaneForm.this, e.getX(), e.getY());
                    if (tab != -1) {
//                        boolean b = PaneForm.this.getTabCount()>0;
//                        closeOthers.setEnabled(b);
                        popupmenu.show(PaneForm.this, e.getX(), e.getY());
                    }
                }
            }
        }
        
        
        public void mouseClicked(MouseEvent e) {
            if(e.getClickCount() == 2){
                if(PaneForm.this.getTabCount()>0){
                    DockingDesktop desktop = XPontusWindow.getInstance().getDesktop();
                    
                    XPontusWindow.DockablePaneForm paneForm = (XPontusWindow.DockablePaneForm) XPontusWindow.getInstance()
                    .getPane();
                    
                    int state = paneForm.getDockKey().getDockableState();
                    
                    if (state == DockableState.STATE_MAXIMIZED) {
                        desktop.restore(paneForm);
                    } else {
                        desktop.maximize(paneForm);
                    }
                } else{
                    checkPopup(e);
                }
            }
        }
        
        public void mousePressed(MouseEvent e) {
            
            checkPopup(e);
        }
        
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                checkPopup(e);
            } else{
                int i = getSelectedIndex();
                
                // nothing selected
                if (i == -1) {
                    return;
                }
                
                CloseableTabbedPane.WrapperIcon icon = (CloseableTabbedPane.WrapperIcon)getIconAt(i);
                
                // close tab, if icon was clicked
                if (icon != null && icon.contains(e.getX(), e.getY())) {
                     ((BaseAction)applicationContext.getBean("action.closetab")).execute();
                }
            }
            
        }
        
        public void mouseEntered(MouseEvent e) {}
        public void mouseExited(MouseEvent e) {}
    }
    
    /**
     *
     * @return
     */
    public boolean isEmpty(){
        return this.getTabCount() < 1;
    }
    
    /**
     *
     * @return
     */
    public javax.swing.JEditorPane getCurrentEditor(){
        return getEditorAt(this.getSelectedIndex());
    }
    
    
    /**
     *
     * @param index
     * @return
     */
    public javax.swing.JEditorPane getEditorAt(int index){
        if(!isEmpty()){
            java.awt.Component obj = this.getComponentAt(index);
            javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane)obj;
            javax.swing.JViewport viewPort = scrollPane.getViewport();
            javax.swing.JEditorPane editor = (javax.swing.JEditorPane)viewPort.getComponent(0);
            return editor;
        } else{
            return null;
        }
    }
    
    private void configureEditorPopup(){
        for(int i=0;i<ACTIONS.length;i++){
            editorPopup.add((Action)applicationContext.getBean(ACTIONS[i]));
        }
    }
    
    /**
     *
     * @param file
     * @return
     */
    public int isOpen(java.io.File file){
        String filename = file.getAbsolutePath();
        int openedFiles = getTabCount();
        for(int i=0;i<openedFiles;i++){
            if(filename.equals(this.getToolTipTextAt(i))){
                return i;
            }
        }
        return -1;
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code ">//GEN-BEGIN:initComponents
    private void initComponents() {
        popup = new javax.swing.JPopupMenu();
        editorPopup = new javax.swing.JPopupMenu();

        addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                tabChanged(evt);
            }
        });

    }// </editor-fold>//GEN-END:initComponents
    
    private void tabChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_tabChanged
        int index = getSelectedIndex();
        ((Action)applicationContext.getBean("action.closeothers")).setEnabled(getTabCount()>1);
        if(!(isEmpty())){
            if (index != -1) {
                if(! ((Action)XPontusWindow.getInstance().getApplicationContext().getBean("action.copy")).isEnabled()){
                    enableDocumentActions(true);
                }
                String tip = (String)getCurrentEditor().getClientProperty("FILE_PATH");
                if (tip == null) {
                    tip = "untitled";
                    XPontusWindow.getInstance().setMessage(tip);
                }
                if(!tip.equals("untitled")){
                    ((OpenAction)applicationContext.getBean("action.open")).setFileDir(tip);
                    JStatusBar st = (JStatusBar)XPontusWindow.getInstance().getStatusbar();
                    st.setMessageWithTip(tip);
                }
                getCurrentEditor().grabFocus();
                
                
            } else{
                XPontusWindow.getInstance().setMessage("");
                XPontusWindow.getInstance().getStatusBar().setNotificationMessage("");
                enableDocumentActions(false);
            }
        }
    }//GEN-LAST:event_tabChanged
    
    /**
     *
     */
    public void createEditorFromFile(){
        final javax.swing.JEditorPane editor;
        
        try{
            editor = new javax.swing.JEditorPane();
            editor.setCaret(new XPontusCaret());
            
            editor.addMouseListener(new PopupListener(editorPopup));
            
            XPontusEditorKit ekit = new XPontusEditorKit();
            editor.setEditorKit(ekit);
            editor.putClientProperty("TOKEN_MARKER", null);
            
            editor.putClientProperty("FILE_PATH", "untitled - " + untitledCounter);
            editor.putClientProperty("FILE", null);
            editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);
            editor.putClientProperty("FILE_NEW", Boolean.TRUE);
            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(editor);
            
            EditorOptionModel m = (EditorOptionModel)new EditorOptionModel().load();
            if(m.isShowLineNumbers()){
                scrollPane.setRowHeaderView(new LineView(editor));
            }
            String filename = "untitled - " + untitledCounter;
            ModificationHandler handler = new ModificationHandler(editor);
            javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
            editor.putClientProperty("UNDO_MANAGER",_undo);
            
            editor.getDocument().addUndoableEditListener(new UndoableEditListener(){
                public void undoableEditHappened(UndoableEditEvent event) {
                    ((javax.swing.undo.UndoManager)editor.getClientProperty("UNDO_MANAGER")).addEdit(event.getEdit());
                }
                
            });
            InputMap inputMap = editor.getInputMap();
            
            KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_A,
                    Event.CTRL_MASK);
            inputMap.put(key, DefaultEditorKit.selectAllAction);
            
            addTab(filename, scrollPane, ic, true );
            
            
            setSelectedIndex(getTabCount()-1);
            
            this.setToolTipTextAt(getTabCount() - 1, filename);
            
            getCurrentEditor().grabFocus();
            configureEditor(editor);
            untitledCounter++;
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    private void configureEditor(JEditorPane editor){
        XPontusWindow.getInstance().configureDragAndDrop(editor);
    }
    /**
     *
     * @param file
     */
    public void createEditorFromFile(java.io.File file){
        java.io.InputStream is;
        int opened = isOpen(file);
        if(opened!=-1){
            this.setSelectedIndex(opened);
            return;
        }
        final javax.swing.JEditorPane editor;
        KitInfo kit = KitInfo.getInstance();
        Reader reader = null;
        boolean isEditable = true;
        
        try{
            is = new java.io.FileInputStream(file);
            
            reader = EncodingHelper.getReader(is);
            
            editor = new javax.swing.JEditorPane();
            isEditable = file.canWrite();
            
            editor.setEditable(isEditable);
            if(isEditable){
                editor.setCaret(new XPontusCaret());
                TokenMarker tk = kit.getTokenMarker(file);
                XPontusEditorKit ekit = new XPontusEditorKit();
                editor.setEditorKit(ekit);
                
                
                editor.addMouseListener(new PopupListener(editorPopup));
                
                editor.read(reader, null);
                
                ((SyntaxDocument) editor.getDocument()).setTokenMarker(tk);
                editor.putClientProperty("TOKEN_MARKER", tk);
                
            } else{
                editor.read(reader, null);
                editor.putClientProperty("TOKEN_MARKER", null);
                
            }
            javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
            editor.putClientProperty("UNDO_MANAGER",_undo);
            
            editor.getDocument().addUndoableEditListener(new UndoableEditListener(){
                public void undoableEditHappened(UndoableEditEvent event) {
                    ((javax.swing.undo.UndoManager)editor.getClientProperty("UNDO_MANAGER")).addEdit(event.getEdit());
                }
                
            });
            InputMap inputMap = editor.getInputMap();
            
            KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_A,
                    Event.CTRL_MASK);
            inputMap.put(key, DefaultEditorKit.selectAllAction);
            editor.putClientProperty("FILE_PATH",file.getAbsolutePath());
            
            editor.putClientProperty("FILE", file);
            editor.putClientProperty("FILE_NEW", Boolean.FALSE);
            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(editor);
            EditorOptionModel m = (EditorOptionModel)new EditorOptionModel().load();
            if(m.isShowLineNumbers()){
                scrollPane.setRowHeaderView(new LineView(editor));
            }
            String filename = file.getName();
            String tooltip = file.getAbsolutePath();
            ModificationHandler handler = new ModificationHandler(editor);
            
            addTab(filename, scrollPane, ic, true );
            
            
            setSelectedIndex(getTabCount()-1);
            
            this.setToolTipTextAt(getTabCount() - 1, tooltip);
            
            
//            addTab(filename, ic, scrollPane, tooltip);
            
            setSelectedIndex(getTabCount()-1);
            configureEditor(editor);
            getCurrentEditor().grabFocus();
            handler.setSaved();
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    
    /**
     *
     * @param is
     * @param tk
     */
    public void createEditorFromStream(java.io.InputStream is, String ext){
        
        final javax.swing.JEditorPane editor;
        KitInfo kit = KitInfo.getInstance();
        Reader reader = null;
        boolean isEditable = true;
        untitledCounter++;
        
        try{
            
            reader = EncodingHelper.getReader(is);
            editor = new javax.swing.JEditorPane();
            
            editor.setEditable(isEditable);
            
            editor.setCaret(new XPontusCaret());
            
            TokenMarker tk = kit.getTokenMarker(ext);
            
            XPontusEditorKit ekit = new XPontusEditorKit();
            editor.setEditorKit(ekit);
            
            editor.addMouseListener(new PopupListener(editorPopup));
            
            editor.read(reader, null);
            
            ((SyntaxDocument) editor.getDocument()).setTokenMarker(tk);
            editor.putClientProperty("TOKEN_MARKER", tk);
            javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
            editor.putClientProperty("UNDO_MANAGER",_undo);
            
            editor.getDocument().addUndoableEditListener(new UndoableEditListener(){
                public void undoableEditHappened(UndoableEditEvent event) {
                    ((javax.swing.undo.UndoManager)editor.getClientProperty("UNDO_MANAGER")).addEdit(event.getEdit());
                }
                
            });
            InputMap inputMap = editor.getInputMap();
            
            KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_A,
                    Event.CTRL_MASK);
            inputMap.put(key, DefaultEditorKit.selectAllAction);
            editor.putClientProperty("FILE_PATH", "untitled" + untitledCounter);
            editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);
            editor.putClientProperty("FILE", null);
            editor.putClientProperty("FILE_NEW", Boolean.TRUE);
            javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(editor);
            EditorOptionModel m = (EditorOptionModel)new EditorOptionModel().load();
            if(m.isShowLineNumbers()){
                scrollPane.setRowHeaderView(new LineView(editor));
            }
            String filename = "untitled" + untitledCounter;
            String tooltip = "untitled" + untitledCounter;
//            addTab(filename, ic, scrollPane, tooltip);
            addTab(filename, scrollPane, ic, true );
            
            
            setSelectedIndex(getTabCount()-1);
            
            this.setToolTipTextAt(getTabCount() - 1, tooltip);
            
            ModificationHandler handler = new ModificationHandler(editor);
            setSelectedIndex(getTabCount()-1);
            getCurrentEditor().grabFocus();
            configureEditor(editor);
        } catch(Exception e){
            e.printStackTrace();
        }
        
    }
    
    /**
     *
     * @param b
     */
    public void enableDocumentActions(final boolean b){
        final SwingWorker worker = new SwingWorker() {
            public Object construct() {
                for(int i=0;i<TEXT_ACTIONS.length;i++){
                    ((Action)applicationContext.getBean(TEXT_ACTIONS[i])).setEnabled(b);
                }
                ((Action)applicationContext.getBean("action.closeothers")).setEnabled(getTabCount()>1);
                return null;
            }
        };
        worker.start();
    }
    
    /**
     *
     * @return
     */
    public final ApplicationContext getApplicationContext() {
        return applicationContext;
    }
    
    /**
     *
     *
     * @param applicationContext
     */
    public final void setApplicationContext(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
        enableDocumentActions(false);
        configureEditorPopup();
         this.addMouseListener(new TabListener());
    }
    
    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPopupMenu editorPopup;
    private javax.swing.JPopupMenu popup;
    // End of variables declaration//GEN-END:variables
    private final String ACTIONS[] = {
        "action.copy",
        "action.cut",
        "action.paste",
        "action.selectall",
        "action.save",
        "action.closetab"
    };
    private ApplicationContext applicationContext;
    private int untitledCounter =1;
    private final String TEXT_ACTIONS[] = {
        "action.print",
        "action.insertcomment",
        "action.insertcdatasection",
        "action.save",
        "action.saveas",
        "action.saveall",
        "action.closetab",
        "action.closeothers",
        "action.closetaball",
        "action.closeothers",
        "action.gotoline",
        "action.selectall",
        "action.cut",
        "action.copy",
        "action.paste",
        "action.undo",
        "action.redo",
        "action.find"  ,
        "action.indentxml",
        "action.commentxml",
        "action.tidy",
        "action.checkxml",
        "action.validate",
        "action.validateschema",
        "action.generatedtd"
    };
}
