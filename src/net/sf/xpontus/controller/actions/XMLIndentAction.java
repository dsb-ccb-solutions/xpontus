/*
 * XMLIndentAction.java
 *
 * Created on 8 septembre 2005, 07:48
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
import net.sf.xpontus.utils.EncodingHelper;
import net.sf.xpontus.utils.MsgUtils;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.TokenMarker;
import org.syntax.jedit.tokenmarker.XMLTokenMarker;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 * Action to indent xml code
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class XMLIndentAction extends ThreadedAction {
    /** Creates a new instance of XMLIndentAction */
    public XMLIndentAction() {
    }

    /**
     * @see net.sf.xpontus.core.controller.actions#execute()
     */
    public void execute() {
        final javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                          .getCurrentEditor();
        byte[] bt = edit.getText().getBytes();
        java.io.InputStream is = new java.io.ByteArrayInputStream(bt);

        MsgUtils _msg = MsgUtils.getInstance();
        StringBuffer buff = new StringBuffer("\n");
        buff.append(_msg.getString("msg.formatting"));
        XPontusWindow.getInstance().append(buff.toString());
        XPontusWindow.getInstance().getStatusBar()
                     .setOperationMessage(buff.toString());

        try {
            SyntaxDocument mdoc = (SyntaxDocument) edit.getDocument();

            mdoc.readLock();

            Reader reader = EncodingHelper.getReader(is); 

            SAXReader sReader = new SAXReader();
            sReader.setValidation(false);
            sReader.setEntityResolver(null);
            org.dom4j.Document doc = sReader.read(reader);
            org.dom4j.io.OutputFormat format = org.dom4j.io.OutputFormat.createPrettyPrint();
            
            format.setEncoding("UTF-8");
            format.setIndentSize(4);
             

            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            XMLWriter xmlWriter = new XMLWriter(out, format);
            xmlWriter.write(doc);
             
            InputStream iss = new java.io.ByteArrayInputStream(out.toByteArray());
            reader = new InputStreamReader(iss, "UTF-8");
            mdoc.readUnlock();
            edit.read(reader, null);

            SyntaxDocument _doc = (SyntaxDocument) edit.getDocument();
            TokenMarker tk = new XMLTokenMarker();
            edit.putClientProperty("TOKEN_MARKER", tk);
            _doc.setTokenMarker(tk);
            edit.putClientProperty("FILE_MODIFIED", Boolean.TRUE);
            edit.repaint();

            javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
            edit.putClientProperty("UNDO_MANAGER", _undo);

            edit.getDocument().addUndoableEditListener(new UndoableEditListener() {
                    public void undoableEditHappened(UndoableEditEvent event) {
                        ((javax.swing.undo.UndoManager) edit.getClientProperty(
                            "UNDO_MANAGER")).addEdit(event.getEdit());
                    }
                });

            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage(_msg.getString(
                    "msg.formattingDone"));

            XPontusWindow.getInstance()
                         .append(_msg.getString("msg.formattingDone"));
        } catch (Exception e) {
            XPontusWindow.getInstance().append(e.getMessage());
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage("Error see messages window!");
        }
    }
}
