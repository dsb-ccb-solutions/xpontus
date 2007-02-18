/*
 * BaseAction.java
 *
 * Created on November 23, 2005, 8:00 PM
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

import net.sf.xpontus.core.utils.IconUtils;
import net.sf.xpontus.core.utils.L10nHelper;
import net.sf.xpontus.utils.MsgUtils;
import net.sf.xpontus.view.XPontusWindow;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;


/**
 * the base class of all the actions of XPontus XML Editor
 * @author Yves Zoundi
 */
public abstract class BaseAction extends AbstractAction {
    private String name;
    private String actionCommand;
    private String icon;
    private String accelerator;
    private String description;
    private ActionEvent event;

    /** Creates a new instance of AbstractCommandAction */
    public BaseAction() {
    }

    public abstract void execute();

    /**
     *
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
        this.putValue(Action.NAME, L10nHelper.getInstance().getValue(name));
    }

    /**
     *
     * @param event
     */
    public void actionPerformed(ActionEvent event) {
        this.setEvent(event);
        execute();
    }

    /**
     *
     * @return
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     *
     * @param actionCommand
     */
    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
        this.putValue(Action.ACTION_COMMAND_KEY, actionCommand);
    }

    /**
     *
     * @return
     */
    public String getIcon() {
        return icon;
    }

    /**
     *
     * @param icon
     */
    public void setIcon(String icon) {
        this.icon = icon;

        ImageIcon _icon = IconUtils.getInstance().getIcon(icon);
        this.putValue(Action.SMALL_ICON, _icon);
    }

    /**
     *
     * @return
     */
    public String getAccelerator() {
        return accelerator;
    }

    /**
     *
     * @param accelerator
     */
    public void setAccelerator(String accelerator) {
        this.accelerator = accelerator;
        this.putValue(Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke(accelerator));
    }

    /**
     *
     * @return
     */
    public String getDescription() {
        return description;
    }

    /**
     *
     * @param description
     */
    public void setDescription(String description) {
        this.description = description;
        this.putValue(Action.SHORT_DESCRIPTION,
            L10nHelper.getInstance().getValue(description));
    }

    /**
     *
     * @return
     */
    public ActionEvent getEvent() {
        return event;
    }

    /**
     *
     * @param event
     */
    public void setEvent(ActionEvent event) {
        this.event = event;
    }

    public void warning(String message) {
        JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
            message, MsgUtils.getInstance().getString("msg.error"),
            JOptionPane.ERROR_MESSAGE);
    }
}
