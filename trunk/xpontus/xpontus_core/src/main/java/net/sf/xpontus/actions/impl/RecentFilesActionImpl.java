/*
 *
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
 *
 *
 */
package net.sf.xpontus.actions.impl;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.utils.FileHistoryList;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.List;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.PopupMenuEvent;
import javax.swing.event.PopupMenuListener;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class RecentFilesActionImpl extends AbstractXPontusActionImpl
    implements PopupMenuListener {
    public static final String BEAN_ALIAS = "action.recentfiles";
    private JMenu menu;

    public RecentFilesActionImpl(JMenu menu) {
        this.menu = menu;
        menu.getPopupMenu().addPopupMenuListener(this);
    }

    public void execute() {
    }

    public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
        fillSubMenu();
    }

    public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
        menu.removeAll();
    }

    public void popupMenuCanceled(PopupMenuEvent e) {
    }

    private void fillSubMenu() {
        menu.removeAll();

        List<String> files = FileHistoryList.getFileHistoryList();

        System.out.println("recent:" + files.size());

        for (final String path : files) {
            final JMenuItem item = new JMenuItem(path);
            item.putClientProperty("FILE_OBJECT", path);
            menu.add(item);
            item.addActionListener(new ShowHistoryListener());
        }
    }

    private class ShowHistoryListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            JMenuItem item = (JMenuItem) e.getSource();

            String path = (String) item.getClientProperty("FILE_OBJECT");
            FileObject fo = null;

            try {
                fo = VFS.getManager().resolveFile(path);

                if (fo == null) {
                    return;
                }

                if (!fo.exists()) {
                    fo = null;
                }
            } catch (Exception err) {
            }

            if ((fo == null)) {
                FileHistoryList.removeEntry(path);
                menu.remove(item);

                return;
            }

            DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer()
                                    .createEditorFromFileObject(fo);
        }
    }
}
