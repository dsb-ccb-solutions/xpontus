/* 
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
package net.sf.xpontus.modules.gui.components.preferences;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.BannerPanel;

import java.awt.BorderLayout;

import javax.swing.Icon;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class EditorPanelDialog extends AbstractDialogPage {
    /**
	 * 
	 */
	private static final long serialVersionUID = 320490676255742109L;

	public EditorPanelDialog(String name, Icon icon) {
        super(name, icon);
    }

    public void lazyInitialize() {
        setLayout(new BorderLayout());

        BannerPanel bannerPanel = new BannerPanel(getName());
        bannerPanel.setBackground(getBackground().brighter());
        add(bannerPanel, BorderLayout.NORTH);
        add(new EditorPanel(), BorderLayout.CENTER);
    }
}
