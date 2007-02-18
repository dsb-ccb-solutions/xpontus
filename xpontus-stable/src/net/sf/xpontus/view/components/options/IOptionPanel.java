/*
 * IOptionPanel.java
 *
 * Created on February 16, 2006, 8:25 PM
 *
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
package net.sf.xpontus.view.components.options;

import javax.swing.JComponent;


/**
 * An interface for a a preferences' panel
 * @author Yves Zoundi
 */
public interface IOptionPanel {
    /**
     *
     * @return
     */
    public JComponent getControl();

    /**
     *
     * @return
     */
    public String getDisplayName();

    /**
     *
     */
    public void save();

    /**
     *
     */
    public void read();
}
