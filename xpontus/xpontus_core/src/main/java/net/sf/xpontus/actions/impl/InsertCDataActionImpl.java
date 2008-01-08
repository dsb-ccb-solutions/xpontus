/*
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
 */
package net.sf.xpontus.actions.impl;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


/**
 * Insert a CDATA section
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.1
 */
public class InsertCDataActionImpl extends DefaultDocumentAwareActionImpl {
    public static final String BEAN_ALIAS = "action.commentxml";

    public InsertCDataActionImpl() {
    }

    public void run() {
        DocumentTabContainer dtc = DefaultXPontusWindowImpl.getInstance()
                                                           .getDocumentTabContainer();

        JTextComponent editor = dtc.getCurrentEditor();
        Document doc = editor.getDocument();
        int pos = editor.getSelectionStart();

        try {
            editor.getDocument().insertString(pos, "<![CDATA[  ]]>", null);
            editor.setCaretPosition(pos + 10);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
