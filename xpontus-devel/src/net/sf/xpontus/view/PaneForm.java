/*
 * PaneForm.java
 *
 *
 * Created on 1 août 2005, 17:45
 *
 *  Copyright (C) 2005 Yves Zoundi
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

package net.sf.xpontus.view;

import com.vlsolutions.swing.docking.*;
import java.awt.*;
import javax.swing.*;
import javax.swing.event.*;
import net.sf.xpontus.core.utils.IconUtils;
import java.awt.event.*;

/**
 * The container which stores document into tabs
 * 
 * @author Yves Zoundi
 */
public class PaneForm extends JLabel implements Dockable {
	DockKey key = new DockKey("Editor");

	public DockKey getDockKey() {
		return key;
	}

	public Component getComponent() {
		return this;// XPontusWindow.getInstance().getDockingDesktop();
	}

	final ImageIcon ic = IconUtils.getInstance().getIcon(
			"/net/sf/xpontus/icons/_PATH_/file.gif");

	/** Creates new form BeanForm */
	public PaneForm() {
		key.setCloseEnabled(false);

		key.setAutoHideEnabled(false);
		key.setResizeWeight(0.7f);
		Dimension dim = new Dimension(600, 300);
		this.setMinimumSize(dim);
		this.setPreferredSize(dim);

	}

}
