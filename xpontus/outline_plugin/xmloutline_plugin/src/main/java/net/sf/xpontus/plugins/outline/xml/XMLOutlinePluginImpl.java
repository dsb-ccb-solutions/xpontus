/*
 * XMLOutlinePluginImpl.java
 * 
 * Created on 2007-10-02, 18:17:22
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

package net.sf.xpontus.plugins.outline.xml;

import javax.swing.text.Document; 
import net.sf.xpontus.plugins.outline.OutlinePluginIF;

/**
 *
 * @author Yves Zoundi
 */
public class XMLOutlinePluginImpl implements OutlinePluginIF{

    /**
     * Returns the content type supported by this plugin implementation
     * @return 
     */
    public String getContentType() {
        return "text/xml";
    }

    /**
     * Update the outline view of the document
     * @param doc The document being edited
     */
    public void updateOutline(Document doc) {
         doc = null;
    }

}
