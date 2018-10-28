/*
 * XPontusTopComponentIF.java
 *
 * Created on June 20, 2007, 9:36 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi <yveszoundi at users dot sf dot net>
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
package net.sf.xpontus.modules.gui.components;

import net.sf.xpontus.plugins.ioc.IOCPlugin;

import java.awt.Component;


/**
 * Interface for XPontus main components (the main window to display)
 * This will allow to integrate xpontus code in other projects
 * @author Yves Zoundi
 */
public interface XPontusTopComponentIF {
    /**
     *
     */
    String TOP_COMPONENT_KEY = "XPONTUS_TOP_COMPONENT";

    /**
    * The name of the component
    * @return The name of the component
    */
    String getName();

    /**
     * Get the component to display
     * @return The component to display
     */
    Component getDisplayComponent();

    /**
     * activate the component
     */
    void activateComponent();

    /**
     *
     * @param container
     */
    void setIOCContainer(IOCPlugin container);

    /**
     * deactivate the component
     */
    void deactivateComponent();
}
