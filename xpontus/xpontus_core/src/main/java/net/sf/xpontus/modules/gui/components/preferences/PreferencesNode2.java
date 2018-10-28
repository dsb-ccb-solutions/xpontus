/*
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
 *
 */
package net.sf.xpontus.modules.gui.components.preferences;

import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author Yves Zoundi
 */
public class PreferencesNode2 extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = 7095699284936445164L;
    private final String id;
    private PreferencesPluginIF plugin;

    public PreferencesNode2(Object arg0, String id)
    {
        super(arg0);
        this.id = id;
        this.plugin = (PreferencesPluginIF) arg0;
    }

    public final String getId()
    {
        return id;
    }

    public String toString()
    {
        return plugin.getPreferencesPanelComponent().getTitle();
    }
}
