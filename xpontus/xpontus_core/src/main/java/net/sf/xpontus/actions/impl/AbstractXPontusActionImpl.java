/*
 * AbstractXPontusActionImpl.java
 *
 * Created on 25 mai 2007, 14:54
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

import net.sf.xpontus.actions.XPontusActionIF;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.awt.event.ActionEvent;

import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.KeyStroke;


/**
 * Base class for xpontus actions handlers
 * @author Yves Zoundi
 */
public abstract class AbstractXPontusActionImpl extends AbstractAction
    implements XPontusActionIF
{
    private static final long serialVersionUID = 5322489591504438775L;
    private Log log = LogFactory.getLog(getClass().getName());
    private ActionEvent event;
    private String name;
    private String actionCommand;
    private String description;
    private String accelerator;
    private String icon;
    private Boolean isI18nEnabled = Boolean.FALSE;

    /**
     * Creates a new instance of AbstractXPontusActionImpl
     */
    public AbstractXPontusActionImpl()
    {
    }

    /**
     * Return a logger
     * @return A logger
     */
    public Log getLogger()
    {
        return log;
    }

    /**
     * Returns the action's name
     * @return The name of the action
     */
    public String getName()
    {
        return name;
    }

    /**
     * Set the action's name
     * @param name the action's name
     */
    public void setName(String name)
    {
        this.name = name;

        String i18nName = this.name;

        if (isI18nEnabled.booleanValue())
        {
            // TODO not implemented
        }

        this.putValue(Action.NAME, i18nName);
    }

    /**
     * Returns the id of the action
     * @return The id of the action
     */
    public String getActionCommand()
    {
        return actionCommand;
    }

    /**
     * Set the id of the action
     * @param actionCommand The id of the action
     */
    public void setActionCommand(String actionCommand)
    {
        this.actionCommand = actionCommand;
        this.putValue(Action.ACTION_COMMAND_KEY, actionCommand);
    }

    /**
     * Returns the description of the action
     * @return The description of the action
     */
    public String getDescription()
    {
        return description;
    }

    /**
     * Set the description of the action
     * @param description The description of the action
     */
    public void setDescription(String description)
    {
        this.description = description;

        String i18nDescription = description;

        if (isI18nEnabled.booleanValue())
        {
            // TODO not implemented
        }

        this.putValue(Action.SHORT_DESCRIPTION, i18nDescription);
    }

    /**
     * Returns the keyboard shortcut of the action
     * @return The keyboard shortcut of the action
     */
    public String getAccelerator()
    {
        return accelerator;
    }

    /**
     * Set the keyboard shortcut of the action
     * @param accelerator The keyboard shortcut of the action
     */
    public void setAccelerator(String accelerator)
    {
        this.accelerator = accelerator;
        this.putValue(Action.ACCELERATOR_KEY,
            KeyStroke.getKeyStroke(accelerator));
    }

    /**
     * Returns the path of the icon of this action
     * @return The path of the icon of this action
     */
    public String getIcon()
    {
        return icon;
    }

    /**
     * Set the path of the icon of this action
     * @param icon The path of the icon of this action
     */
    public void setIcon(String icon)
    {
        this.icon = icon;

        URL url = getClass().getResource(icon);
        ImageIcon mIcon = new ImageIcon(url);
        this.putValue(Action.SMALL_ICON, mIcon);
    }

    /**
     *
     * @return
     */
    public Boolean getIsI18nEnabled()
    {
        return isI18nEnabled;
    }

    /**
     *
     * @param isI18nEnabled
     */
    public void setIsI18nEnabled(Boolean isI18nEnabled)
    {
        this.isI18nEnabled = isI18nEnabled;
    }

    /**
     * Event handling
     * @param e the event firing up this action
     */
    public void actionPerformed(ActionEvent e)
    {
        setEvent(e);
        execute();
    }

    /**
     * Returns the event firing up this action
     * @return The event firing up this action
     */
    public ActionEvent getEvent()
    {
        return event;
    }

    /**
     * Set the event firing up this action
     * @param event The event firing up this action
     */
    public void setEvent(ActionEvent event)
    {
        this.event = event;
    }
}
