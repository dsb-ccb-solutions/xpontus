/*
 * ReloadAction.java
 *
 * Created on April 10, 2007, 7:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.controller.actions;

import com.ibm.icu.text.CharsetDetector;
import java.awt.Event;
import java.awt.event.KeyEvent;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.io.Reader;
import javax.swing.InputMap;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;
import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.DefaultEditorKit;
import net.sf.xpontus.controller.handlers.ModificationHandler;
import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.model.options.EditorOptionModel;
import net.sf.xpontus.view.XPontusCaret;
import net.sf.xpontus.view.XPontusWindow;
import net.sf.xpontus.view.editor.KitInfo;
import net.sf.xpontus.view.editor.LineView;
import net.sf.xpontus.view.editor.XPontusEditorKit;
import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.TokenMarker;

/**
 *
 * @author mrcheeks
 */
public class ReloadAction extends BaseAction{
    
    /** Creates a new instance of ReloadAction */
    public ReloadAction() {
    }

    public void execute() {
        try {
            final JEditorPane editor = XPontusWindow.getInstance()
                                                    .getCurrentEditor();

            String filePath = (String) editor.getClientProperty("FILE_PATH");

            if (editor.getClientProperty("FILE_NEW").equals(Boolean.TRUE) ) {
                JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                           .getFrame(),
                    "Please save the file first!");

                return;
            }

            
            if( editor.getClientProperty("FILE_MODIFIED").equals(Boolean.TRUE)){
                StringBuffer buff = new StringBuffer();
                buff.append("The file has been modified.\n");
                buff.append("Do you want to discard all changes?");
                buff.append("\n");
                int rep = JOptionPane.showConfirmDialog(XPontusWindow.getInstance().getFrame(), buff.toString(), "Warning", JOptionPane.YES_NO_OPTION);
                if(rep == JOptionPane.NO_OPTION ){
                    buff = null;
                    return;
                }
            }
            File fFile = new File(filePath);

            if (!fFile.exists()) {
                SaveAction action = (SaveAction) XPontusWindow.getInstance()
                                                              .getApplicationContext()
                                                              .getBean("action.save");

                JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                           .getFrame(),
                    "The file doesn't exist!");

                return;
            }

            InputStream fis = new FileInputStream(fFile);

            InputStream bis = new BufferedInputStream(fis);

            CharsetDetector detector = new CharsetDetector();

            detector.setText(bis);

            Reader reader = detector.detect().getReader();

            editor.setCaret(new XPontusCaret());

            KitInfo kit = KitInfo.getInstance(); 

            TokenMarker tk = kit.getTokenMarker(fFile);
                XPontusEditorKit ekit = new XPontusEditorKit();
                editor.setEditorKit(ekit);

            editor.read(reader, null);

           SyntaxDocument _doc = (SyntaxDocument) editor.getDocument(); 
           _doc.setTokenMarker(tk);

            javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
            editor.putClientProperty("UNDO_MANAGER", _undo);

            editor.getDocument().addUndoableEditListener(new UndoableEditListener() {
                    public void undoableEditHappened(UndoableEditEvent event) {
                        ((javax.swing.undo.UndoManager) editor.getClientProperty(
                            "UNDO_MANAGER")).addEdit(event.getEdit());
                    }
                });

            InputMap inputMap = editor.getInputMap();

            KeyStroke key = KeyStroke.getKeyStroke(KeyEvent.VK_A,
                    Event.CTRL_MASK);
            inputMap.put(key, DefaultEditorKit.selectAllAction);

            editor.putClientProperty("FILE_PATH", fFile.getAbsolutePath());

            editor.putClientProperty("FILE", fFile);
            editor.putClientProperty("FILE_NEW", Boolean.FALSE);

          

            String filename = fFile.getName();
            String tooltip = fFile.getAbsolutePath();
            ModificationHandler handler = new ModificationHandler(editor);
            editor.grabFocus();
            handler.setSaved();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
                ex.getMessage());
        }
    }
    
}
