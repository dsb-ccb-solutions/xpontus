/*
 * Db4oPlugin.java
 *
 * Created on 16-Aug-2007, 7:45:17 AM
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
 */
package net.sf.xpontus.plugins.settings.db4o;

import com.db4o.Db4o;

import org.java.plugin.Plugin;


/**
 * Class description
 * @author Yves Zoundi
 */
public class Db4oPlugin extends Plugin {
    public Db4oPlugin() {
    }

    protected void doStart() throws Exception {
        Db4o.configure().unicode(true);
        Db4o.configure().activationDepth(99);
        Db4o.configure().updateDepth(99);
        Db4o.configure().lockDatabaseFile(false);
    }

    protected void doStop() throws Exception {
    }
}
