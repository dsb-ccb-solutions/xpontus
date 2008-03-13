/*
 * SaveAllActionImpl.java
 *
 * Created on 2007-08-08, 15:17:13
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
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.IOUtils;
import org.apache.commons.vfs.FileObject;

import java.awt.Component;

import java.io.FileWriter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.swing.JFileChooser;
import javax.swing.text.JTextComponent;


/**
 * Action to save all opened documents
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class SaveAllActionImpl extends DefaultDocumentAwareActionImpl {
    public static final String BEAN_ALIAS = "action.saveas";
    private JFileChooser chooser = null;

    public SaveAllActionImpl() {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
    }

    public void run() {
        // I'LL DEAL WITH THAT LATER
        //        Component c = XPontusComponentsUtils.getTopComponent()
        //                                            .getDisplayComponent();
        //        int answer = chooser.showSaveDialog(c);
        //
        //        if (answer == JFileChooser.APPROVE_OPTION) {
        //            DefaultXPontusWindowImpl w = DefaultXPontusWindowImpl.getInstance();
        //            DocumentTabContainer dtc = w.getDocumentTabContainer();
        //            IDocumentContainer[] dc = dtc.getEditorsAsArray();
        //
        //            for (int i = 0; i < dc.length; i++) {
        //                try {
        //                    IDocumentContainer document = dc[i];
        //                    JTextComponent jtc = document.getEditorComponent();
        //                    FileObject fo = null;
        //                    OutputStream bos = fo.getContent().getOutputStream();
        //                    Writer writer = new FileWriter("/home/mrcheeks/test.txt");
        //                    jtc.write(new OutputStreamWriter(bos));
        //                    IOUtils.closeQuietly(writer);
        //                    IOUtils.closeQuietly(bos);
        //                } catch (Exception ex) {
        //                    ex.printStackTrace();
        //                }
        //            }
        //        }
    }
}
