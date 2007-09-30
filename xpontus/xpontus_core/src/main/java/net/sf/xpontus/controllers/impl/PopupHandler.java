/*
 * PopupHandler.java
 *
 * Created on 9-Aug-2007, 8:21:05 PM
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
 */
package net.sf.xpontus.controllers.impl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.text.JTextComponent;


/**
 * Right click popup menu handler
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class PopupHandler extends MouseAdapter {
    private JTextComponent txt;
    private JPopupMenu popup = new JPopupMenu();
    private JMenuItem copyItem;
    private JMenuItem pasteItem;
    private JMenuItem cutItem;
    private JMenuItem selectAllItem;
    private JMenuItem clearItem;

    /**
     * Create a new instance of PopupListener
     */
    public PopupHandler() {
        // initialize the menu items of the popup menu
        clearItem = new JMenuItem("Clear");
        copyItem = new JMenuItem("Copy");
        pasteItem = new JMenuItem("Paste");
        cutItem = new JMenuItem("Cut");
        selectAllItem = new JMenuItem("Select all");

        // listener for the cut item
        cutItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    txt.cut();
                }
            });

        // listener for the copy item
        copyItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    txt.copy();
                }
            });

        // listener for the paste menu item
        pasteItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    txt.paste();
                }
            });

        // listener for the "clear" menu item
        clearItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    txt.setText("");
                }
            });

        // listener for the "Select all" menu item
        selectAllItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    txt.selectAll();
                }
            });

        // add the menu items to the popup menu
        popup.add(cutItem);
        popup.add(copyItem);
        popup.add(pasteItem);
        popup.add(clearItem);
        popup.add(selectAllItem);
    }

    /**
     * Create a new instance of PopupListener
     * @param popup A popup menu
     */
    public PopupHandler(JPopupMenu popup) {
        setPopup(popup);
    }

    /**
     * We only display the popup is right mouse button is clicked
     * @param e A mouse event
     */
    public void mouseReleased(MouseEvent e) {
        if (e.isPopupTrigger() || SwingUtilities.isRightMouseButton(e)) {
            txt = (JTextComponent) e.getSource();

            cutItem.setEnabled(txt.isEditable());
            pasteItem.setEnabled(txt.isEditable());

            popup.show(e.getComponent(), e.getX(), e.getY());
        }
    }

    /**
     * Set the popup menu associated to this listener
     * @param popup The popup menu associated to this listener
     */
    public void setPopup(JPopupMenu popup) {
        this.popup = popup;
    }

    /**
    * Return the popup menu associated to this listener
     * @return The popup menu associated to this listener
     */
    public JPopupMenu getPopup() {
        return popup;
    }
}
