/*
 *
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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
package net.sf.xpontus.plugins.schema_converter_plugin;

import net.sf.xpontus.actions.impl.XPontusDialogActionImpl;

import java.net.URL;

import javax.swing.Action;
import javax.swing.ImageIcon;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SchemaGenerationAction extends XPontusDialogActionImpl {
    /**
     * Default constructor
     */
    public SchemaGenerationAction() {
        setName("Schema converter");
        setDialogClassName(SchemaGeneratorView.class.getName());

        URL url = getClass().getResource("Export16.gif");
        ImageIcon icon = new ImageIcon(url);
        this.putValue(Action.SMALL_ICON, icon);
        this.setWindowClassLoader(this.getClass().getClassLoader());
    }
}
