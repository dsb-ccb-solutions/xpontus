/*
 * GenerateDtdAction.java
 *
 * Created on November 1, 2005, 7:00 PM
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
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.view.XPontusWindow;
import net.sf.xpontus.view.editor.DTDGenerator;


/**
 * Action to generate a DTD from an opened XML Document
 * @author Yves Zoundi
 */
public class GenerateDtdAction extends BaseAction {
    private DTDGenerator generator;

    /** Creates a new instance of GenerateDtdAction */
    public GenerateDtdAction() {
        generator = new DTDGenerator();
    }

    /**
     * @see net.sf.xpontus.core.controller.actions#execute()
     */
    public void execute() {
        javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                    .getCurrentEditor();
        byte[] bt = edit.getText().getBytes();
        java.io.InputStream is = new java.io.ByteArrayInputStream(bt);
        javax.swing.JFileChooser chooser = new javax.swing.JFileChooser();
        int answer = chooser.showSaveDialog(XPontusWindow.getInstance()
                                                         .getFrame());

        if (answer == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File out = chooser.getSelectedFile();

            if (generator.generate(is, out)) {
                XPontusWindow.getInstance().getPane().createEditorFromFile(out);
            } else {
                javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                       .getFrame(),
                    generator.msg);
            }
        }
    }
}
