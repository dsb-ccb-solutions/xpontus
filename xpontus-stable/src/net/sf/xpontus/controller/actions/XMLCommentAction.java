/*
 * XMLCommentAction.java
 *
 * Created on 10 septembre 2005, 15:01
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

import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.view.XPontusWindow;


/**
 * A class to add XML comments to a text selection
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class XMLCommentAction extends BaseAction {
    /** Creates a new instance of XMLCommentAction */
    public XMLCommentAction() {
    }

    public void execute() {
        javax.swing.JEditorPane editor;
        editor = XPontusWindow.getInstance().getCurrentEditor();

        int debut = editor.getSelectionStart();
        int end = editor.getSelectionEnd();

        try {
            editor.getDocument().insertString(end, " -->", null);
            editor.getDocument().insertString(debut, "<!-- ", null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
