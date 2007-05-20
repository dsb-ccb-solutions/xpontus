/*
 * ModificationHandler.java
 *
  *
 * Created on 1 ao�t 2005, 17:46
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
package net.sf.xpontus.controller.handlers;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import net.sf.xpontus.view.PaneForm;
import net.sf.xpontus.view.XPontusWindow;


/**
 * Controller to handle document modifications
 * @author Yves Zoundi
 */
public class ModificationHandler implements DocumentListener {
    private javax.swing.JEditorPane editor;

    /** Creates a new instance of ModificationHandler
     * @param editor the document container
     */
    public ModificationHandler(javax.swing.JEditorPane editor) {
        this.editor = editor;
        // add the document modification notifier to the current document
        editor.getDocument().addDocumentListener(this);
    }

    /**
     * put a modified flag on the document
     */
    public void setModified() {
        editor.putClientProperty("FILE_MODIFIED", Boolean.TRUE); 
    }

    /**
     * put a saved flag on the document
     */
    public void setSaved() {
        editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);  
    }

    /** implements DocumentListener **/
    public void changedUpdate(DocumentEvent e) {
    }

    /** implements DocumentListener **/
    public void insertUpdate(DocumentEvent e) {
        setModified();
    }

    /** implements DocumentListener **/
    public void removeUpdate(DocumentEvent e) {
        setModified();
    }
}
