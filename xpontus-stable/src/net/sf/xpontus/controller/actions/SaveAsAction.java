/*
 * SaveAsAction.java
 *
 * Created on 18 juillet 2005, 02:54
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
import net.sf.xpontus.model.options.XMLOptionModel;
import net.sf.xpontus.view.PaneForm;
import net.sf.xpontus.view.XPontusWindow;
import net.sf.xpontus.view.editor.KitInfo;

import org.apache.commons.io.FilenameUtils;

import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.TokenMarker;
import org.syntax.jedit.tokenmarker.XMLTokenMarker;


/**
 * Action to save a document under another name
 * @author Yves Zoundi
 */
public class SaveAsAction extends BaseAction {
    private javax.swing.JFileChooser chooser;

    /** Creates a new instance of SaveAsAction */
    public SaveAsAction() {
        chooser = new javax.swing.JFileChooser();
    }

    /**
     *
     * @param file
     */
    public void save(java.io.File file) {
        javax.swing.JEditorPane editor = XPontusWindow.getInstance()
                                                      .getCurrentEditor();

        SyntaxDocument _doc = (SyntaxDocument) editor.getDocument();
        TokenMarker tk1 = (TokenMarker) editor.getClientProperty("TOKEN_MARKER");

        try {
            if (tk1 != null) {
                if (tk1.getClass() == XMLTokenMarker.class) {
                    XMLOptionModel m1 = new XMLOptionModel();
                    m1 = (XMLOptionModel) m1.load();
                    editor.write(new java.io.OutputStreamWriter(
                            new java.io.FileOutputStream(file),
                            m1.getXmlEncoding()));
                } else {
                    editor.write(new java.io.FileWriter(file));
                }
            } else {
                editor.write(new java.io.FileWriter(file));
            }

            editor.putClientProperty("FILE", file);
            editor.putClientProperty("FILE_PATH", file.getAbsolutePath());
            editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);
            editor.putClientProperty("FILE_NEW", Boolean.FALSE);

            String extension = FilenameUtils.getExtension(file.getName());
            TokenMarker tk = KitInfo.getInstance().getTokenMarker(extension);
            editor.putClientProperty("TOKEN_MARKER", tk);
            _doc.setTokenMarker(tk);
            XPontusWindow.getInstance().setMessage(file.getAbsolutePath());

            int i = XPontusWindow.getInstance().getPane().getSelectedIndex();
            XPontusWindow.getInstance().getPane()
                         .setToolTipTextAt(i, file.getAbsolutePath());
            XPontusWindow.getInstance().getPane().setTitleAt(i, file.getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void execute() {
        if (chooser.showSaveDialog(XPontusWindow.getInstance().getFrame()) == javax.swing.JFileChooser.APPROVE_OPTION) {
            java.io.File file = chooser.getSelectedFile();

            if (file.exists()) {
                int answer = javax.swing.JOptionPane.showConfirmDialog(XPontusWindow.getInstance()
                                                                                    .getFrame(),
                        "Erase");

                if (answer == javax.swing.JOptionPane.YES_OPTION) {
                    PaneForm pane = XPontusWindow.getInstance().getPane();
                    int index = pane.getSelectedIndex();
                    int ouvert = pane.isOpen(file);

                    if (ouvert != -1) {
                        pane.remove(ouvert);
                    }

                    save(file);
                } else {
                    return;
                }
            } else {
                save(file);
            }
        }
    }
}
