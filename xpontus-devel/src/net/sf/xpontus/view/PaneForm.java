/*
 * PaneForm.java
 *
 *
 * Created on 1 ao�t 2005, 17:45
 *
 *  Copyright (C) 2005 Yves Zoundi
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

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import com.sun.java.help.impl.SwingWorker;
import java.awt.BorderLayout;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.BufferedInputStream;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import net.sf.xpontus.controller.actions.OpenAction;
import net.sf.xpontus.controller.handlers.ModificationHandler;
import net.sf.xpontus.core.controller.handlers.PopupListener;
import net.sf.xpontus.core.utils.BeanContainer;
import net.sf.xpontus.core.utils.IconUtils;
import net.sf.xpontus.model.options.EditorOptionModel;
import net.sf.xpontus.view.components.JStatusBar;
import net.sf.xpontus.view.editor.LineView;
import net.sf.xpontus.view.editor.SyntaxDocument;
import net.sf.xpontus.view.editor.SyntaxEditorkit;
import net.sf.xpontus.view.editor.syntax.xml.XMLParser;
import org.apache.commons.io.FilenameUtils;

/**
 * The container which stores document into tabs
 *
 * @author Yves Zoundi
 */
public class PaneForm extends javax.swing.JTabbedPane {
    
    final ImageIcon ic = IconUtils.getInstance().getIcon(
            "/net/sf/xpontus/icons/_PATH_/file.gif");
    
    /** Creates new form BeanForm */
    public PaneForm() {
        this.applicationContext = XPontusWindow.getInstance()
        .getApplicationContext();
        enableDocumentActions(false);
        // this.putClientProperty(Options.EMBEDDED_TABS_KEY, Boolean.TRUE);
        initComponents();
        this.addMouseListener(new TabListener());
        configureEditorPopup();
    }
    
    /**
     *
     * @return
     */
    public boolean isEmpty() {
        return this.getTabCount() < 1;
    }
    
    /**
     *
     * @return
     */
    public javax.swing.JEditorPane getCurrentEditor() {
        return getEditorAt(this.getSelectedIndex());
    }
    
    /**
     *
     * @param index
     * @return
     */
    public javax.swing.JEditorPane getEditorAt(int index) {
        if (!isEmpty()) {
            java.awt.Component obj = this.getComponentAt(index);
            javax.swing.JScrollPane scrollPane = (javax.swing.JScrollPane) obj;
            javax.swing.JViewport viewPort = scrollPane.getViewport();
            javax.swing.JEditorPane editor = (javax.swing.JEditorPane) viewPort
                    .getComponent(0);
            return editor;
        } else {
            return null;
        }
    }
    
    private void configureEditorPopup() {
        for (int i = 0; i < ACTIONS.length; i++) {
            editorPopup.add((Action) applicationContext.getBean(ACTIONS[i]));
        }
    }
    
    private class TabListener extends PopupListener {
        
        public TabListener() {
            popup.add((Action) applicationContext.getBean("action.closetab"));
            popup.add((Action) applicationContext.getBean("action.closeothers"));
            popup.add((Action) applicationContext.getBean("action.closetaball"));
            setPopup(popup);
        }
        
        /**
         * This method will display the popup if the mouseevent is a popup event
         * and the event occurred over a tab
         */
        public void mouseReleased(MouseEvent e) {
            if (e.isPopupTrigger() || e.getButton() == MouseEvent.BUTTON3) {
                if (PaneForm.this.getTabCount() > 0) {
                    int tab = PaneForm.this.getUI().tabForCoordinate(
                            PaneForm.this, e.getX(), e.getY());
                    if (tab != -1) {
                        boolean c = (PaneForm.this.getTabCount() > 1);
                        ((Action) applicationContext
                                .getBean("action.closeothers")).setEnabled(c);
                        popup.show(PaneForm.this, e.getX(), e.getY());
                    }
                }
            }
        }
    }
    
    /**
     *
     * @param file
     * @return
     */
    public int isOpen(java.io.File file) {
        String filename = file.getAbsolutePath();
        int openedFiles = getTabCount();
        for (int i = 0; i < openedFiles; i++) {
            if (filename.equals(this.getToolTipTextAt(i))) {
                return i;
            }
        }
        return -1;
    }
    
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc=" Generated Code
	// ">//GEN-BEGIN:initComponents
	private void initComponents() {
		popup = new javax.swing.JPopupMenu();
		editorPopup = new javax.swing.JPopupMenu();

		addChangeListener(new javax.swing.event.ChangeListener() {
			public void stateChanged(javax.swing.event.ChangeEvent evt) {
				tabChanged(evt);
			}
		});

	}// </editor-fold>//GEN-END:initComponents
        
        public int getColumnAtCaret() {
            int caretPosition = getCurrentEditor().getCaretPosition();
            Element root = getCurrentEditor().getDocument().getDefaultRootElement();
            int line = root.getElementIndex(caretPosition);
            int lineStart = root.getElement(line).getStartOffset();
            
            return caretPosition - lineStart + 1;
        }
        
        /*
         * * Return the current line number at the Caret position.
         */
        public int getLineAtCaret() {
            int caretPosition = getCurrentEditor().getCaretPosition();
            Element root = getCurrentEditor().getDocument().getDefaultRootElement();
            
            return root.getElementIndex(caretPosition) + 1;
        }
        
        private void tabChanged(javax.swing.event.ChangeEvent evt) {// GEN-FIRST:event_tabChanged
            int index = getSelectedIndex();
            if (!(isEmpty())) {
                if (index != -1) {
                    if (!((Action) XPontusWindow.getInstance()
                    .getApplicationContext().getBean("action.copy"))
                    .isEnabled()) {
                        enableDocumentActions(true);
                    }
                    // String t =
                    // (String)getCurrentEditor().getClientProperty("FILE_PATH");
                    String tip = (String) getCurrentEditor().getClientProperty(
                            "FILE_PATH");
                    if (tip == null) {
                        tip = "untitled";
                        XPontusWindow.getInstance().setMessage(tip);
                    }
                    if (!tip.equals("untitled")) {
                        ((OpenAction) applicationContext.getBean("action.open"))
                        .setFileDir(tip);
                        JStatusBar st = (JStatusBar) XPontusWindow.getInstance()
                        .getStatusbar();
                        st.setMessageWithTip(tip);
                    }
                    getCurrentEditor().grabFocus();
                    XPontusWindow.getInstance().updateLineInfo(
                            getLineAtCaret() + ":" + getColumnAtCaret());
                } else {
                    XPontusWindow.getInstance().setMessage("");
                    XPontusWindow.getInstance().getStatusBar()
                    .setNotificationMessage("");
                    XPontusWindow.getInstance().updateLineInfo("");
                    enableDocumentActions(false);
                }
            }
        }// GEN-LAST:event_tabChanged
        
        
        /**
         *
         * @param file
         */
        public void createEditorFromFile(java.io.File file) {
            java.io.InputStream is;
            int opened = isOpen(file);
            if (opened != -1) {
                this.setSelectedIndex(opened);
                return;
            }
            final javax.swing.JEditorPane editor;
            BufferedInputStream reader = null;
            boolean isEditable = true;
            
            try {
                is = new java.io.FileInputStream(file);
                reader = new BufferedInputStream(is);
                CharsetDetector detector = new CharsetDetector();
                detector.setText(reader);
                CharsetMatch match = detector.detect();
                
                String ch = match.getName();
                
                editor = new javax.swing.JEditorPane();
                isEditable = file.canWrite();
                
                editor.setEditable(isEditable);
                if (isEditable) {
                    String ext = FilenameUtils.getExtension(file.getName());
                    editor.setEditorKit(new SyntaxEditorkit(editor, ext));
                    editor.addMouseListener(new PopupListener(editorPopup));
                    editor.read(reader, ch);
                    
                } else {
                    editor.read(reader, null);
                    editor.putClientProperty("TOKEN_MARKER", null);
                    
                }
                javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
                editor.putClientProperty("UNDO_MANAGER", _undo);
                
                editor.getDocument().addUndoableEditListener(
                        new UndoableEditListener() {
                    public void undoableEditHappened(UndoableEditEvent event) {
                        ((javax.swing.undo.UndoManager) editor
                                .getClientProperty("UNDO_MANAGER"))
                                .addEdit(event.getEdit());
                    }
                    
                });
                InputMap inputMap = editor.getInputMap();
                
                KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_A,
                        Event.CTRL_MASK);
                inputMap.put(key, DefaultEditorKit.selectAllAction);
                editor.putClientProperty("FILE_PATH", file.getAbsolutePath());
                
                editor.putClientProperty("FILE", file);
                editor.putClientProperty("FILE_NEW", Boolean.FALSE);
                javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(
                        editor);
                EditorOptionModel m = (EditorOptionModel) new EditorOptionModel()
                .load();
                if (m.isShowLineNumbers()) {
                    scrollPane.setRowHeaderView(new LineView(editor));
                }
                String filename = file.getName();
                String tooltip = file.getAbsolutePath();
                ModificationHandler handler = new ModificationHandler(editor);
                
                SyntaxDocument doc = (SyntaxDocument)editor.getDocument();
                
                if(doc.getLexer().getClass() == XMLParser.class){
                    
                }
                else{
                    
                }
                JPanel editorContainer = new JPanel(new BorderLayout());
                
                
                addTab(filename, ic, scrollPane, tooltip);
                
                setSelectedIndex(getTabCount() - 1);
                editor.requestFocusInWindow();
                handler.setSaved();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        /**
         *
         * @param is
         * @param tk
         */
        public void createEditorFromStream(java.io.InputStream is, String ext) {
            
            final javax.swing.JEditorPane editor;
            BufferedInputStream reader = null;
            boolean isEditable = true;
            untitledCounter++;
            
            try {
                reader = new BufferedInputStream(is);
                CharsetDetector detector = new CharsetDetector();
                detector.setText(reader);
                CharsetMatch match = detector.detect();
                
                String ch = match.getName();
                
                editor = new javax.swing.JEditorPane();
                
                editor.setEditable(isEditable);
                
                editor.setEditorKit(new SyntaxEditorkit(editor, ext));
                
                editor.addMouseListener(new PopupListener(editorPopup));
                
                editor.read(reader, ch);
                
                javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
                editor.putClientProperty("UNDO_MANAGER", _undo);
                
                editor.getDocument().addUndoableEditListener(
                        new UndoableEditListener() {
                    public void undoableEditHappened(UndoableEditEvent event) {
                        ((javax.swing.undo.UndoManager) editor
                                .getClientProperty("UNDO_MANAGER"))
                                .addEdit(event.getEdit());
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
                
                javax.swing.JScrollPane scrollPane = new javax.swing.JScrollPane(
                        editor);
                EditorOptionModel m = (EditorOptionModel) new EditorOptionModel()
                .load();
                if (m.isShowLineNumbers()) {
                    scrollPane.setRowHeaderView(new LineView(editor));
                }
                String filename = "untitled" + untitledCounter;
                String tooltip = "untitled" + untitledCounter;
                addTab(filename, ic, scrollPane, tooltip);
                new ModificationHandler(editor);
                setSelectedIndex(getTabCount() - 1);
                editor.requestFocusInWindow();
            } catch (Exception e) {
                e.printStackTrace();
            }
            
        }
        
        /**
         *
         * @param b
         */
        public void enableDocumentActions(final boolean b) {
            final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    for (int i = 0; i < TEXT_ACTIONS.length; i++) {
                        ((Action) applicationContext.getBean(TEXT_ACTIONS[i]))
                        .setEnabled(b);
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
        public final BeanContainer getApplicationContext() {
            return applicationContext;
        }
        
        /**
         *
         *
         * @param applicationContext
         */
        public final void setApplicationContext(BeanContainer applicationContext) {
            this.applicationContext = applicationContext;
        }
        
	// Variables declaration - do not modify//GEN-BEGIN:variables
	private javax.swing.JPopupMenu editorPopup;

	private javax.swing.JPopupMenu popup;

	// End of variables declaration//GEN-END:variables
        private final String ACTIONS[] = { "action.copy", "action.cut",
        "action.paste", "action.selectall", "action.save",
        "action.closetab" };
        
        private BeanContainer applicationContext;
        
        private int untitledCounter = 1;
        
        private final String TEXT_ACTIONS[] = { "action.print",
        "action.insertcomment", "action.insertcdatasection", "action.save",
        "action.saveas", "action.spellcheck", "action.saveall",
        "action.closetab", "action.closeothers", "action.closetaball",
        "action.closeothers", "action.gotoline", "action.selectall",
        "action.cut", "action.copy", "action.xpath", "action.paste",
        "action.undo", "action.redo", "action.find", "action.indentxml",
        "action.commentxml", "action.tidy", "action.checkxml",
        "action.validate", "action.validateschema" };
}
