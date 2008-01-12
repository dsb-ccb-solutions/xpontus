/*
 * ModificationHandler.java
 *
 * Created on 9-Aug-2007, 6:41:22 PM
 *
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
 *
 */
package net.sf.xpontus.controllers.impl;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusFileConstantsIF;
import net.sf.xpontus.modules.gui.components.DocumentContainer;
import net.sf.xpontus.syntax.SyntaxDocument;

import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;


/**
 * Document modification handler
 * @author Yves Zoundi
 */
public class ModificationHandler implements DocumentListener, CaretListener {
    private DocumentContainer editor;

    /** Creates a new instance of ModificationHandler
     * @param editor the document container
     */
    public ModificationHandler(DocumentContainer editor) {
        this.editor = editor;
        editor.getEditorComponent().getDocument().addDocumentListener(this);
        editor.getEditorComponent()
              .putClientProperty(XPontusConstantsIF.MODIFICATION_HANDLER, this);
        editor.getEditorComponent().addCaretListener(this);
    }

    /**
     * put a modified flag on the document
     * @param modified
     */
    public void setModified(final boolean modified) {
        JTextComponent jtc = editor.getEditorComponent();
        Object locked = jtc.getClientProperty(XPontusFileConstantsIF.FILE_LOCKED);

        if (locked != null) {
            return;
        }

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    editor.getEditorComponent()
                          .putClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED,
                        Boolean.valueOf(modified));

                    String msg = "Document modified";

                    if (!modified) {
                        msg = "Document saved";
                    }

                    editor.getStatusBar().setMessage(msg);

                    final SyntaxDocument mDoc = (SyntaxDocument) editor.getEditorComponent()
                                                                       .getDocument();

                    if (mDoc.getCodeCompletion() != null) {
                        //   System.out.println("parsing for completion");
                        new Thread() {
                                public void run() {
//                                    editor.getStatusBar()
//                                          .setOperationMessage("Building code completion...");
                                    mDoc.getCodeCompletion().init(mDoc);
//                                    editor.getStatusBar()
//                                          .setOperationMessage("Database completion updated...");
                                }
                            }.start();

                        //  System.out.println("parsing for completion done");
                    }
                }
            });
    }

    /**
     * implements DocumentListener *
     * @param e
     */
    public void changedUpdate(DocumentEvent e) {
    }

    /**
     * implements DocumentListener *
     * @param e
     */
    public void insertUpdate(DocumentEvent e) {
        setModified(true);
    }

    /**
     * implements DocumentListener
     * @param e
     */
    public void removeUpdate(DocumentEvent e) {
        setModified(true);
    }

    /**
     *
     * @param arg0
     */
    public void caretUpdate(CaretEvent e) {
        final CaretEvent evt = e;

        JTextComponent jtc = editor.getEditorComponent();
        Object locked = jtc.getClientProperty(XPontusFileConstantsIF.FILE_LOCKED);

        if (locked != null) {
            return;
        }

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    int caretPosition = editor.getEditorComponent()
                                              .getCaretPosition();
                    Element root = editor.getEditorComponent().getDocument()
                                         .getDefaultRootElement();
                    int line = root.getElementIndex(caretPosition);
                    int lineStart = root.getElement(line).getStartOffset();
                    int lineNumber = line + 1;
                    int columnNumber = caretPosition - lineStart + 1;
                    String msg = lineNumber + ":" + columnNumber;
                    editor.getStatusBar().setLineMessage(msg);
                }
            });
    }
}
