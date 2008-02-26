/*
 * DefaultPane.java
 *
 * Created on July 1, 2007, 10:40 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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

import com.vlsolutions.swing.docking.*;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JLabel;


/**
 * The default pane to display when no document is opened
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class DefaultPane extends JLabel implements Dockable {
    private DockKey key = new DockKey("Editor");

    /** Creates a new instance of DefaultPane */
    public DefaultPane() {
        key.setCloseEnabled(false);
        key.setAutoHideEnabled(false);
        key.setResizeWeight(0.7f);

        Dimension dim = new Dimension(600, 400);
        this.setMinimumSize(dim);
        this.setPreferredSize(dim);
    }

    /**
     *
     * @return The key of this panel
     */
    public DockKey getDockKey() {
        return key;
    }

    /**
     * The default component to display when no document is opened
     * @return This component
     */
    public Component getComponent() {
        return this;
    }
}
