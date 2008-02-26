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

    /**
     * a abstract method to execute an action
     */
    public abstract void execute();

    /**
     * set the action's name
     * @return the action's name
     */
    public String getName() {
        return name;
    }

    /**
     * get the action's name
     * @param name the action's name
     */
    public void setName(String name) {
        this.name = name;
        this.putValue(Action.NAME, L10nHelper.getInstance().getValue(name));
    }

    /**
    * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
    */
    public void actionPerformed(ActionEvent event) {
        this.setEvent(event);
        execute();
    }

    /**
     * return the action's command
     * @return the action's command
     */
    public String getActionCommand() {
        return actionCommand;
    }

    /**
     * set the action's command
     * @param actionCommand the action's command
     */
    public void setActionCommand(String actionCommand) {
        this.actionCommand = actionCommand;
        this.putValue(Action.ACTION_COMMAND_KEY, actionCommand);
    }

    /**
     * return the action's icon path
     * @return the action's icon path
     */
    public String getIcon() {
        return icon;
    }

    /**
     * set  the action's icon path
     * @param icon  the action's icon path
     */
    public void setIcon(String icon) {
        this.icon = icon;

        ImageIcon _icon = IconUtils.getInstance().getIcon(icon);
        this.putValue(Action.SMALL_ICON, _icon);
    }

    /**
     * get  the action's accelerator key
     * @return  the action's accelerator key
     */
    public String getAccelerator() {
        return accelerator;
    }

    /**
     * set  the action's accelerator key
     * @param accelerator the action's accelerator key
     */
    public void setAccelerator(String accelerator) {
        this.accelerator = accelerator;
        this.putValue(Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke(accelerator));
    }

    /**
     * return  the action's description
     * @return  the action's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * set  the action's description
     * @param description  the action's description
     */
    public void setDescription(String description) {
        this.description = description;
        this.putValue(Action.SHORT_DESCRIPTION,
            L10nHelper.getInstance().getValue(description));
    }

    /**
     * return the event triggering this action
     * @return the event triggering this action
     */
    public ActionEvent getEvent() {
        return event;
    }

    /**
     * (should not be used) set the event triggering this action
     * @param event the event triggering this action
     */
    public void setEvent(ActionEvent event) {
        this.event = event;
    }

    /**
     * show a warning message
     * @param message a message to display
     */
    public void warning(String message) {
        JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
            message, MsgUtils.getInstance().getString("msg.error"),
            JOptionPane.ERROR_MESSAGE);
    }
}
