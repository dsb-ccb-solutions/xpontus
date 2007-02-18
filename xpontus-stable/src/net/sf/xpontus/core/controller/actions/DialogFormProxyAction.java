/*
 * DialogFormProxyAction.java
 *
 * Created on 2 octobre 2005, 16:25
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
package net.sf.xpontus.core.controller.actions;

import javax.swing.JDialog;


/**
 * Generic action to display a dialog
 * @author Yves Zoundi
 */
public class DialogFormProxyAction extends BaseAction {
    private String dialogClassName;
    private Class dialogClass;
    private JDialog dialog;

    /** Creates a new instance of DialogFormProxyAction */
    public DialogFormProxyAction() {
    }

    /**
     * @see net.sf.xpontus.core.controller.actions#execute()
     *
     */
    public void execute() {
        init();
        dialog.setLocationRelativeTo(dialog.getOwner());
        dialog.setVisible(true);
    }

    private void init() {
        if (dialog == null) {
            try {
                dialog = (JDialog) dialogClass.newInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     *
     * @return the dialog class name
     */
    public String getDialogClassName() {
        return dialogClassName;
    }

    /**
     * Set the dialog class name
     * @param dialogClassName Tje dialog class name
     */
    public void setDialogClassName(String dialogClassName) {
        this.dialogClassName = dialogClassName;

        try {
            this.dialogClass = Class.forName(dialogClassName);
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * Return the dialog class
     * @return The dialog class
     */
    public Class getDialogClass() {
        return dialogClass;
    }

    /**
     * Set the dialog class
     * @param dialogClass The dialog class
     */
    public void setDialogClass(Class dialogClass) {
        this.dialogClass = dialogClass;
        this.dialogClassName = dialogClass.getName();
    }

    /**
     * return the dialog to display
     * @return the dialog to display
     */
    public JDialog getDialog() {
        return dialog;
    }

    /**
     * Set the dialog to display
     * @param dialog The dialog to display
     */
    public void setDialog(JDialog dialog) {
        this.dialog = dialog;
    }
}
