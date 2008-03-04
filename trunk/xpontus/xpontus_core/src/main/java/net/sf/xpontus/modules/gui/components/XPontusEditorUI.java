/*
 * XPontusEditorUI.java
 *
 * Created on July 1, 2007, 7:02 PM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
 */
package net.sf.xpontus.modules.gui.components;

import net.sf.xpontus.syntax.SyntaxEditorkit;

import javax.swing.plaf.basic.BasicEditorPaneUI;
import javax.swing.text.EditorKit;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Yves Zoundi
 */
public class XPontusEditorUI extends BasicEditorPaneUI {
    private final String ext;
    private final EditorKit kit;

    /** Creates a new instance of XPontusEditorUI
     * @param jtc The text component
     * @param ext The filename extension
     */
    public XPontusEditorUI(JTextComponent jtc, String ext) {
        this.ext = ext;
        kit = new SyntaxEditorkit(jtc, ext);
    }

    /**
     * Returns The editorkit to use for this text component
     * @param tc The text component
     * @return The editorkit to use for this text component
     */
    public EditorKit getEditorKit(JTextComponent jtc) {
        return kit;
    }
}
