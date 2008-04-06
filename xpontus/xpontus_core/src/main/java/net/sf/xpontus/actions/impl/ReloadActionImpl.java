/*
 *

 *
 * Created on June 30, 2007, 11:56 AM
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

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusFileConstantsIF;
import net.sf.xpontus.controllers.impl.ModificationHandler;
import net.sf.xpontus.events.TabChangeEventListener;
import net.sf.xpontus.events.TabChangedEvent;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.vfs.FileObject;

import java.awt.Toolkit;

import java.io.BufferedInputStream;
import java.io.InputStream;

import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;


/**
 * @version 0.0.1
 * @author Yves Zoundi<yveszoundi AT users DOT sf DOT net>
 * Class to reload a document
 */
public class ReloadActionImpl extends DefaultDocumentAwareActionImpl {
    /**
     *
     */
    public static final String BEAN_ALIAS = "action.reload";
    private TabChangeEventListener tce;

    public ReloadActionImpl() {
        super();
        tce = new TabChangeEventListener() {
                    public void onTabChange(TabChangedEvent e) {
                        if (e.getSource() != null) {
                            IDocumentContainer m_container = e.getSource();
                            JTextComponent jtc = m_container.getEditorComponent();
                            Object o = jtc.getClientProperty(XPontusFileConstantsIF.FILE_LAST_MODIFIED_DATE);

                            try {
                                if (o != null) {
                                    FileObject fo = (FileObject) jtc.getClientProperty(XPontusFileConstantsIF.FILE_OBJECT);

                                    if (!fo.exists()) {
                                        return;
                                    }

                                    long currentLastModifiedTime = fo.getContent()
                                                                     .getLastModifiedTime();
                                    long previousLastModifiedTime = Long.parseLong(o.toString());

                                    if (currentLastModifiedTime != previousLastModifiedTime) {
                                        int rep = JOptionPane.showConfirmDialog(XPontusComponentsUtils.getTopComponent()
                                                                                                      .getDisplayComponent(),
                                                "The file has been modified. Reload the file(discarding any changes)?",
                                                "", JOptionPane.YES_NO_OPTION);

                                        if (rep == JOptionPane.YES_OPTION) {
                                            run();
                                        }
                                    }
                                }
                            } catch (Exception err) {
                            }
                        }
                    }
                };
        DocumentTabContainer.addTabChangeEventListener(tce);
    }

    /* (non-Javadoc)
    * @see net.sf.xpontus.actions.XPontusActionIF#execute()
    */
    public void run() {
        try {
            IDocumentContainer m_documentContainer = (IDocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                                  .getDocumentTabContainer()
                                                                                                  .getCurrentDockable();

            JTextComponent editor = m_documentContainer.getEditorComponent();

            Object filePath = editor.getClientProperty(XPontusFileConstantsIF.FILE_OBJECT);

            if (filePath == null) {
                JOptionPane.showMessageDialog(XPontusComponentsUtils.getTopComponent()
                                                                    .getDisplayComponent(),
                    "Please save the file first!");

                return;
            }

            ModificationHandler handler = (ModificationHandler) editor.getClientProperty(XPontusConstantsIF.MODIFICATION_HANDLER);

            if (editor.getClientProperty(XPontusFileConstantsIF.FILE_MOFIFIED)
                          .equals(Boolean.TRUE)) {
                StringBuffer buff = new StringBuffer();
                buff.append("The file has been modified.\n");
                buff.append("Do you want to discard all changes?");
                buff.append("\n");

                int rep = JOptionPane.showConfirmDialog(XPontusComponentsUtils.getTopComponent()
                                                                              .getDisplayComponent(),
                        buff.toString(), "Warning", JOptionPane.YES_NO_OPTION);

                if (rep == JOptionPane.NO_OPTION) {
                    return;
                }
            } 

            FileObject fFile = (FileObject) (filePath);

            if (!fFile.exists()) {
                JOptionPane.showMessageDialog(editor,
                    "The file still doesn't exist!");

                return;
            }

            editor.putClientProperty(XPontusFileConstantsIF.FILE_LAST_MODIFIED_DATE,
                "" + fFile.getContent().getLastModifiedTime());

            editor.getDocument().remove(0, editor.getDocument().getLength());

            InputStream newIs = new BufferedInputStream(fFile.getContent()
                                                             .getInputStream());
            CharsetDetector chd = new CharsetDetector();
            chd.setText(newIs);
            editor.read(chd.detect().getReader(), null);

            editor.setCaretPosition(0);
            editor.grabFocus();
            
            handler.setModified(false);
        } catch (Exception ex) {
            Toolkit.getDefaultToolkit().beep();
        }
    }
}
