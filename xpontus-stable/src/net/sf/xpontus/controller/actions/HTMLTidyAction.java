/*
 * HTMLTidyAction.java
 *
 * Created on 1 octobre 2005, 13:29
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
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.ThreadedAction;
import net.sf.xpontus.model.options.JTidyOptionModel;
import net.sf.xpontus.utils.MsgUtils;
import net.sf.xpontus.view.XPontusWindow;

import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.HTMLTokenMarker;
import org.syntax.jedit.tokenmarker.TokenMarker;

import org.w3c.tidy.Tidy;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;


/**
 * Action to tidy HTML
 * @author Yves Zoundi
 */
public class HTMLTidyAction extends ThreadedAction {
    private Tidy tidy;
    private MsgUtils _msg;

    /** Creates a new instance of HTMLTidyAction */
    public HTMLTidyAction() {
        _msg = MsgUtils.getInstance();
    }

    /**
     *
     */
    private void init() {
        tidy = new Tidy();
        tidy.setTidyMark(false);
        tidy.setXHTML(true);

        JTidyOptionModel m = new JTidyOptionModel();
        JTidyOptionModel obj = (JTidyOptionModel) m.load();
        tidy.setDropEmptyParas(obj.isDropEmptyParasOption());
        tidy.setIndentContent(obj.isIndentOption());
        tidy.setLogicalEmphasis(obj.isLogicalEmphasisOption());
        tidy.setUpperCaseTags(obj.isUppercaseTagsOption());
        tidy.setUpperCaseAttrs(obj.isUppercaseAttrOption());
    }

    /**
     *
     */
    public void execute() {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        if (tidy == null) {
            init();

            tidy.setErrout(pw);
        }

        try {
            String log = "Unable to format! see Messages Window";
            final javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                              .getCurrentEditor();
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage(_msg.getString(
                    "msg.formatting"));

            byte[] bt = edit.getText().getBytes();

            SyntaxDocument _doc = (SyntaxDocument) edit.getDocument();

            _doc.readLock();

            java.io.InputStream in = new java.io.ByteArrayInputStream(bt);
            final java.io.InputStream _backup = new java.io.ByteArrayInputStream(bt);

            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            tidy.parse(in, out);

            _doc.readUnlock();

            if (new String(out.toByteArray()).trim().equals("")) {
                //                System.out.println("Restore");
                edit.read(_backup, null);
            } else {
                edit.read(new java.io.ByteArrayInputStream(out.toByteArray()),
                    null);
                log = _msg.getString("msg.formattingDone");
            }

            TokenMarker tk = new HTMLTokenMarker();
            ((SyntaxDocument) edit.getDocument()).setTokenMarker(tk);

            edit.putClientProperty("TOKEN_MARKER", tk);

            edit.repaint();
            edit.putClientProperty("FILE_MODIFIED", Boolean.TRUE);

            javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
            edit.putClientProperty("UNDO_MANAGER", _undo);
            edit.getDocument().addUndoableEditListener(new UndoableEditListener() {
                    public void undoableEditHappened(UndoableEditEvent event) {
                        ((javax.swing.undo.UndoManager) edit.getClientProperty(
                            "UNDO_MANAGER")).addEdit(event.getEdit());
                    }
                });

            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);

            XPontusWindow.getInstance().append(sw.toString());
        } catch (Exception e) {
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage("Error see messages window!");
            XPontusWindow.getInstance().append(sw.toString());
        }
    }
}
