/*
 *
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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
package net.sf.xpontus.controllers.impl;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.GotoLineView;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import javax.swing.text.Element;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class GotoLineController {
    public static final String GOTO_LINE_METHOD = "gotoLine";
    public static final String CLOSE_WINDOW_METHOD = "closeWindow";
    private GotoLineView view;

    public GotoLineController(GotoLineView view) {
        setView(view);
    }

    public GotoLineView getView() {
        return view;
    }

    public void setView(GotoLineView view) {
        this.view = view;
    }

    public void closeWindow() {
        view.setVisible(false);
    }

    public void gotoLine() {
        JTextComponent edit = DefaultXPontusWindowImpl.getInstance()
                                                      .getDocumentTabContainer()
                                                      .getCurrentEditor();
        int lineNumber = view.getLine();

        Element element = edit.getDocument().getDefaultRootElement();

        if (element.getElement(lineNumber - 1) == null) {
            XPontusComponentsUtils.showErrorMessage("No such line");

            return;
        }

        int pos = element.getElement(lineNumber - 1).getStartOffset();
        edit.setCaretPosition(pos);
    }
}
