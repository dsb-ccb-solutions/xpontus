/*
 * OutlinePluginIF.java
 *
 * Created on 2007-08-08, 14:57:07
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
package net.sf.xpontus.plugins.outline;

import javax.swing.text.Document;


/**
 * Plugin interface for outline builders
 * @version 0.0.1
 * @author Yves Zoundi
 */
public interface OutlinePluginIF {
    /**
     * 
     * @return 
     */
    String getContentType();

    /**
     * 
     * @param doc 
     */
    void updateOutline(Document doc);
}
