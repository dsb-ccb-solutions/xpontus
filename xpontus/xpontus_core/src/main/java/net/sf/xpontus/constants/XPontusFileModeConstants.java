/*
 * XPontusFileModeConstants.java
 *
 * Created on 24-Aug-2007, 8:25:24 PM
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
package net.sf.xpontus.constants;


/**
 * Interface for file modes edition
 * @author Yves Zoundi
 */
public interface XPontusFileModeConstants {
    /**
     * Mode for Docbook XML files
     */
    String DOCBOOK_MODE = "DOCBOOK_MODE";

    /**
     * Mode for HTML/XHTML files
     */
    String XHTML_MODE = "XHTML_MODE";

    /**
     * Mode for XML files
     */
    String XML_MODE = "XML_MODE";

    /**
     * Mode for DTD documents
     */
    String DTD_MODE = "DTD_MODE";

    /**
     * Mode for XML Schemas documents
     */
    String XML_SCHEMA_MODE = "XML_SCHEMA_MODE";

    /**
     * Mode for relax ng documents
     *
     */
    String RELAX_NG_MODE = "RELAX_NG_MODE";

    /**
     * Mode for XSL stylesheets documents
     */
    String XSL_MODE = "XSL_MODE";
}
