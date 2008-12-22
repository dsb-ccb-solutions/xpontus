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

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.SwingUtilities;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;


/**
 * Document modification handler
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class ModificationHandler implements DocumentListener, CaretListener
{
    private IDocumentContainer editor;
    private ExecutorService executor = Executors.newSingleThreadExecutor();

    /** Creates a new instance of ModificationHandler
     * @param editor the document container
     */
    public ModificationHandler(IDocumentContainer editor)
    {
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
    public void setModified(final boolean modified)
    {
        JTextComponent textComponent = editor.getEditorComponent();
        Object locked = textComponent.getClientProperty(XPontusFileConstantsIF.FILE_LOCKED);

        if (locked != null)
        {
            return;
        }

        SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    editor.getEditorComponent()
                          .putClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED,
                        Boolean.valueOf(modified));

                    String msg = "Document modified";

                    if (!modified)
                    {
                        msg = "Document saved";
                    }

                    editor.getStatusBar().setMessage(msg);
                }
            });

        final SyntaxDocument mDoc = (SyntaxDocument) editor.getEditorComponent()
                                                           .getDocument();

        if (mDoc.getOutlinePlugin() != null)
        {
            executor.submit(new Runnable()
                {
                    public void run()
                    {
                        mDoc.getOutlinePlugin().updateOutline(mDoc);
                    }
                });
        }

        if (mDoc.getCodeCompletion() != null)
        {
            executor.submit(new Runnable()
                {
                    public void run()
                    {
                        mDoc.getCodeCompletion().init(mDoc);
                    }
                });
        }
    }

    /**
     * implements DocumentListener *
     * @param e
     */
    public void changedUpdate(DocumentEvent e)
    {
    }

    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#insertUpdate(javax.swing.event.DocumentEvent)
     */
    public void insertUpdate(DocumentEvent e)
    {
        setModified(true);
    }

    /* (non-Javadoc)
     * @see javax.swing.event.DocumentListener#removeUpdate(javax.swing.event.DocumentEvent)
     */
    public void removeUpdate(DocumentEvent e)
    {
        setModified(true);
    }

    /* (non-Javadoc)
     * @see javax.swing.event.CaretListener#caretUpdate(javax.swing.event.CaretEvent)
     */
    public void caretUpdate(CaretEvent e)
    {
        JTextComponent jtc = editor.getEditorComponent();
        Object locked = jtc.getClientProperty(XPontusFileConstantsIF.FILE_LOCKED);

        if (locked != null)
        {
            return;
        }

        SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    CaretPosition pos = EditorUtilities.getCaretPosition(editor.getEditorComponent());
                    editor.getStatusBar().setLineMessage(pos.toString());
                }
            });
    }
}
