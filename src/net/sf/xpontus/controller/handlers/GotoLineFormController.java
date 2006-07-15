/*
 * GotoLineFormController.java
 *
 * Created on 1 août 2005, 17:45
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

import net.sf.xpontus.core.controller.handlers.BaseController;
import net.sf.xpontus.utils.MsgUtils;
import net.sf.xpontus.view.GotoLineFormView;
import net.sf.xpontus.view.XPontusWindow;

import javax.swing.text.Element;


/**
 * Controller for the go to line dialog actions
 * @author Yves Zoundi
 */
public class GotoLineFormController extends BaseController {
    private GotoLineFormView gotoLineView;
    private MsgUtils _msg;

    /** Creates a new instance of GotoLineFormController */
    public GotoLineFormController() {
    }

    /**
     *
     * @param gotoLineView
     */
    public GotoLineFormController(GotoLineFormView gotoLineView) {
        setGotoLineView(gotoLineView);
        _msg = MsgUtils.getInstance();
    }

    /**
     *
     * @return
     */
    public GotoLineFormView getGotoLineView() {
        return gotoLineView;
    }

    /**
     *
     * @param gotoLineView
     */
    public void setGotoLineView(GotoLineFormView gotoLineView) {
        this.gotoLineView = gotoLineView;
    }

    public void close() {
        gotoLineView.setVisible(false);
    }

    public void gotoline() {
        javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                    .getCurrentEditor();
        int lineNumber = gotoLineView.getLine();
        Element element = edit.getDocument().getDefaultRootElement();

        if (element.getElement(lineNumber - 1) == null) {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                _msg.getString("msg.noSuchLine"), _msg.getString("msg.error"),
                javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }

        int pos = element.getElement(lineNumber - 1).getStartOffset();
        edit.setCaretPosition(pos);
    }
}
