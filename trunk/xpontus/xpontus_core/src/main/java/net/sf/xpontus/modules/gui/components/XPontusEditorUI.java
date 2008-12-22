/*
 * XPontusEditorUI.java
 *
 * Created on July 1, 2007, 7:02 PM
 *
 * Copyright (C) 2005-2007 Yves Zoundi <yveszoundi at users dot sf dot net>
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
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.2
 */
public class XPontusEditorUI extends BasicEditorPaneUI
{
    private final EditorKit editorKit;

    /** Creates a new instance of XPontusEditorUI
     * @param editor The text component
     * @param fileExtension The filename extension
     */
    public XPontusEditorUI(JTextComponent editor, String fileExtension)
    {
        editorKit = new SyntaxEditorkit(editor, fileExtension);
    }

    /**
     * Returns The editorkit to use for this text component
     * @param editor The text component
     * @return The editorkit to use for this text component
     */
    public EditorKit getEditorKit(JTextComponent editor)
    {
        return editorKit;
    }
}
