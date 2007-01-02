/*
 * ModificationHandler.java
 *
  *
 * Created on 1 août 2005, 17:46
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
package net.sf.xpontus.controller.handlers;

import net.sf.xpontus.view.TextPosition;
import net.sf.xpontus.view.XPontusWindow;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Element;
import javax.swing.text.Highlighter;
import javax.swing.text.Highlighter.HighlightPainter;
import javax.swing.text.JTextComponent;


/**
 * Controller to handle document modifications
 * @author Yves Zoundi
 */
public class ModificationHandler implements DocumentListener, CaretListener
{
    private javax.swing.JEditorPane editor;

    /** Creates a new instance of ModificationHandler */
    public ModificationHandler(javax.swing.JEditorPane editor)
    {
        this.editor = editor;
        editor.getDocument().addDocumentListener(this);
        editor.addCaretListener(this);
    }

    public void setModified()
    {
        editor.putClientProperty("FILE_MODIFIED", Boolean.TRUE);
    }

    public void setSaved()
    {
        editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);
    }

    /** implements DocumentListener **/
    public void changedUpdate(DocumentEvent e)
    {
    }

    public void updateLineInfo()
    {
        XPontusWindow.getInstance().updateLineInfo(getTextPosition().toString());
    }

    public TextPosition getTextPosition()
    {
        int caretPosition = editor.getCaretPosition();
        Element root = editor.getDocument().getDefaultRootElement();
        int line = root.getElementIndex(caretPosition);
        int lineStart = root.getElement(line).getStartOffset();
        int lineEnd = root.getElement(line).getEndOffset();
        int lineNumber = line + 1;
        int columnNumber = caretPosition - lineStart + 1;
        return new TextPosition(lineNumber, columnNumber);
    }

    /** implements DocumentListener **/
    public void insertUpdate(DocumentEvent e)
    {
        setModified();
    }

    /** implements DocumentListener **/
    public void removeUpdate(DocumentEvent e)
    {
        setModified();
    }

    public void caretUpdate(CaretEvent caretEvent)
    {
      
        updateLineInfo();
    }
}
