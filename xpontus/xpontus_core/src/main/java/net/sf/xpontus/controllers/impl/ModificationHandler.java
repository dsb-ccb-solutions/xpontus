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
import net.sf.xpontus.model.CaretPosition;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.syntax.SyntaxDocument;
import net.sf.xpontus.utils.EditorUtilities;

import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;


/**
 * Document modification handler
 * @author Yves Zoundi
 */
public class ModificationHandler implements DocumentListener, CaretListener {
    private IDocumentContainer editor;

    /** Creates a new instance of ModificationHandler
     * @param editor the document container
     */
    public ModificationHandler(IDocumentContainer editor) {
        this.editor = editor;
        editor.getEditorComponent().getDocument().addDocumentListener(this);
        editor.getEditorComponent().putClientProperty(XPontusConstantsIF.MODIFICATION_HANDLER,
            this);
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
                    editor.getEditorComponent().putClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED,
                        Boolean.valueOf(modified));

                    String msg = "Document modified";

                    if (!modified) {
                        msg = "Document saved";
                    }

                    editor.getStatusBar().setMessage(msg);
                }
            });

        final SyntaxDocument mDoc = (SyntaxDocument) editor.getEditorComponent()
                                                           .getDocument();

        if (mDoc.getOutlinePlugin() != null) {
            Thread m_worker = new Thread() {
                    public void run() {
                        mDoc.getOutlinePlugin().updateOutline(mDoc);
                    }
                };

            m_worker.setPriority(Thread.MIN_PRIORITY);
            m_worker.start();
        }

        if (mDoc.getCodeCompletion() != null) {
            Thread m_worker = new Thread() {
                    public void run() {
                        mDoc.getCodeCompletion().init(mDoc);
                    }
                };

            m_worker.setPriority(Thread.MIN_PRIORITY);
            m_worker.start();
        }
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
     * @param e
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
                    CaretPosition pos = EditorUtilities.getCaretPosition(editor.getEditorComponent());
                    editor.getStatusBar().setLineMessage(pos.toString());
                }
            });
    }
}
