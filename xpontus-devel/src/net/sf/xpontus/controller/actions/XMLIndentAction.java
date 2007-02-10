/*
 * XMLIndentAction.java
 *
 * Created on 8 septembre 2005, 07:48
 *
 *
 * Copyright (C) 2005 Yves Zoundi
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
import net.sf.xpontus.view.*;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

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
public class XMLIndentAction extends ThreadedAction
  {
    /** Creates a new instance of XMLIndentAction */
    public XMLIndentAction()
      {
      }

    public void execute()
      {
        final javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                          .getCurrentEditor();
        byte[] bt = edit.getText().getBytes();
        java.io.InputStream is = new java.io.ByteArrayInputStream(bt);

        StringBuffer buff = new StringBuffer("\n");
        buff.append(XPontusWindow.getInstance().getI18nMessage("msg.formatting"));
        XPontusWindow.getInstance()
                     .append(ConsoleOutputWindow.MESSAGES_WINDOW,
            buff.toString());
        XPontusWindow.getInstance().getStatusBar()
                     .setOperationMessage(buff.toString());

        try
          {
            Reader reader = new InputStreamReader(is);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                                                            .newDocumentBuilder();
            InputSource src = new InputSource(reader);
            Document doc = builder.parse(src);
            OutputFormat formatter = new OutputFormat();
            formatter.setIndenting(true);
            formatter.setEncoding("UTF-8");

            java.io.ByteArrayOutputStream out = new java.io.ByteArrayOutputStream();
            XMLSerializer serializer = new XMLSerializer(out, formatter);
            serializer.serialize(doc);

            InputStream iss = new java.io.ByteArrayInputStream(out.toByteArray());
            reader = new InputStreamReader(iss, "UTF-8");

            javax.swing.text.Document _doc = edit.getDocument();
            _doc.remove(0, _doc.getLength());
            edit.read(reader, null);

            edit.putClientProperty("FILE_MODIFIED", Boolean.TRUE);
            edit.repaint();

            javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
            edit.putClientProperty("UNDO_MANAGER", _undo);

            edit.getDocument().addUndoableEditListener(new UndoableEditListener()
                  {
                    public void undoableEditHappened(UndoableEditEvent event)
                      {
                        ((javax.swing.undo.UndoManager) edit.getClientProperty(
                            "UNDO_MANAGER")).addEdit(event.getEdit());
                      }
                  });

            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage(XPontusWindow.getInstance()
                                                              .getI18nMessage("msg.formattingDone"));

            XPontusWindow.getInstance()
                         .append(ConsoleOutputWindow.MESSAGES_WINDOW,
                XPontusWindow.getInstance().getI18nMessage("msg.formattingDone"));
          }
        catch (Exception e)
          {
            XPontusWindow.getInstance()
                         .append(ConsoleOutputWindow.ERRORS_WINDOW,
                e.getMessage());
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage("Error see messages window!");
          }
      }
  }
