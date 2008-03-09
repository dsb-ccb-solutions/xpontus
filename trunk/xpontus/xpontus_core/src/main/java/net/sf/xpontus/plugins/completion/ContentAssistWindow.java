/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.completion;

import java.awt.Cursor;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.text.Collator;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.ListSelectionModel;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.JTextComponent;
import javax.swing.text.SimpleAttributeSet;


/**
 *
 * @author mrcheeks
 */
public class ContentAssistWindow {
    private static ContentAssistWindow INSTANCE;
    private JTextComponent jtc;
    private int offset;

    public ContentAssistWindow() {
    }

    public static ContentAssistWindow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContentAssistWindow();
        }

        return INSTANCE;
    }

    public void complete(final JTextComponent jtc,
        final CodeCompletionIF contentAssist, int off, final String str,
        final AttributeSet set) {
        this.jtc = jtc;
        this.offset = off;

        List completionData = contentAssist.getCompletionList(str, off);

        if (completionData == null) {
            return;
        }

        if (completionData.size() > 0) {
            CompletionWindow window = CompletionWindow.getInstance(); 
            window.getCompletionListModel().updateData(completionData);
            window.showWindow(jtc);
        }
    }
}
