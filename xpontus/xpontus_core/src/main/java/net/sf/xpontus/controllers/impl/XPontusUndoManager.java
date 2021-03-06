/*
 * XPontusUndoManager.java
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
 *
 */
package net.sf.xpontus.controllers.impl;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;


/**
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class XPontusUndoManager extends CompoundEdit
{
    private static final long serialVersionUID = -1456867159752560978L;
    private int m_IdxAdd = 0;

    /* (non-Javadoc)
     * @see javax.swing.undo.CompoundEdit#getUndoPresentationName()
     */
    public String getUndoPresentationName()
    {
        return ((UndoableEdit) edits.elementAt(m_IdxAdd - 1)).getPresentationName();
    }

    /* (non-Javadoc)
     * @see javax.swing.undo.CompoundEdit#getRedoPresentationName()
     */
    public String getRedoPresentationName()
    {
        return ((UndoableEdit) edits.elementAt(m_IdxAdd)).getPresentationName();
    }

    /* (non-Javadoc)
     * @see javax.swing.undo.CompoundEdit#addEdit(javax.swing.undo.UndoableEdit)
     */
    public boolean addEdit(UndoableEdit anEdit)
    {
        if (edits.size() > m_IdxAdd)
        {
            edits.setElementAt(anEdit, m_IdxAdd++);

            for (int i = m_IdxAdd; i < edits.size(); i++)
            {
                edits.removeElementAt(i);
            }
        }
        else
        {
            edits.addElement(anEdit);
            m_IdxAdd++;
        }

        return true;
    }

    /* (non-Javadoc)
     * @see javax.swing.undo.CompoundEdit#canUndo()
     */
    public synchronized boolean canUndo()
    {
        if (m_IdxAdd > 0)
        {
            UndoableEdit edit = (UndoableEdit) edits.elementAt(m_IdxAdd - 1);

            return (edit != null) && edit.canUndo();
        }

        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.undo.CompoundEdit#canRedo()
     */
    public synchronized boolean canRedo()
    {
        if (edits.size() > m_IdxAdd)
        {
            UndoableEdit edit = (UndoableEdit) edits.elementAt(m_IdxAdd);

            return (edit != null) && edit.canRedo();
        }

        return false;
    }

    /* (non-Javadoc)
     * @see javax.swing.undo.CompoundEdit#undo()
     */
    public synchronized void undo() throws CannotUndoException
    {
        ((UndoableEdit) edits.elementAt(--m_IdxAdd)).undo();
    }

    /* (non-Javadoc)
     * @see javax.swing.undo.CompoundEdit#redo()
     */
    public synchronized void redo() throws CannotRedoException
    {
        ((UndoableEdit) edits.elementAt(m_IdxAdd++)).redo();
    }
}
