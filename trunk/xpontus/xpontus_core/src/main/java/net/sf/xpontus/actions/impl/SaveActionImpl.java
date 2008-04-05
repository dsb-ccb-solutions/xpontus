/*
 * SaveActionImpl.java
 *
 * Created on June 30, 2007, 11:55 AM
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
package net.sf.xpontus.actions.impl;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.controllers.impl.ModificationHandler;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.utils.FileHistoryList;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.provider.local.LocalFile;

import java.io.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.*;
import javax.swing.text.JTextComponent;


/**
 * Action to save a document
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class SaveActionImpl extends SimpleDocumentAwareActionImpl {
    public static final String BEAN_ALIAS = "action.save";
    private static final Pattern ENCODING_PATTERN = Pattern.compile(
            "(?<=encoding=\")[^\"]*(?=\")");

    /** Creates a new instance of SaveActionImpl */
    public SaveActionImpl() {
    }

    public void saveDocument(String title) {
        int rep = JOptionPane.showConfirmDialog(DefaultXPontusWindowImpl.getInstance()
                                                                        .getDisplayComponent(),
                "The file " + title +
                " has been modified. Do you want to save it?",
                "Save document?", JOptionPane.YES_NO_OPTION);

        if (rep == JOptionPane.YES_OPTION) {
            execute();
        }
    }

    /**
     *  Save the document
     */
    public void execute() {
        try {
            JTextComponent editor = DefaultXPontusWindowImpl.getInstance()
                                                            .getDocumentTabContainer()
                                                            .getCurrentEditor();

            Object o = editor.getClientProperty(XPontusConstantsIF.FILE_OBJECT);

            // a new file with no recorded location
            if (o == null) {
                new SaveAsActionImpl().execute();
            }
            // save existing file
            else {
                FileObject fo = (FileObject) o;

                FileHistoryList.addFile(fo.getName().getURI());

                OutputStream bos = null;

                if (fo instanceof LocalFile) {
                    bos = new FileOutputStream(fo.getName().getPath());
                } else {
                    bos = fo.getContent().getOutputStream();
                }

                String encoding = "UTF-8";

                String m_encoding = getEncoding(editor.getText());

                if (!m_encoding.trim().equals("")) {
                    encoding = m_encoding;
                }

                OutputStreamWriter m_writer = new OutputStreamWriter(bos,
                        encoding);

                m_writer.write(editor.getText());

                //bos.write(editor.getText().getBytes());
                m_writer.flush();

                m_writer.close();

                bos.flush();

                bos.close();

                ModificationHandler handler = (ModificationHandler) editor.getClientProperty(XPontusConstantsIF.MODIFICATION_HANDLER);
                handler.setModified(false);
            }
        } catch (Exception e) {
        }
    }

    private String getEncoding(String xml) throws IOException {
        BufferedReader reader = null;

        try {
            reader = new BufferedReader(new InputStreamReader(
                        new ByteArrayInputStream(xml.getBytes())));

            String prolog = reader.readLine();
            Matcher matcher = ENCODING_PATTERN.matcher(prolog);

            return matcher.find() ? matcher.group() : "";
        } finally {
            if (reader != null) {
                reader.close();
            }
        }
    }
}
