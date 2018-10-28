/*
 * MimeDescriptor.java
 *
 * Created on 25 mai 2007, 14:54
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
package net.sf.xpontus.mimes;

import java.util.ArrayList;
import java.util.List;


/**
 * Mime type descriptor
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class MimeDescriptor {
    private String mime;
    private String description;
    private List<String> fileTypes = new ArrayList<String>();

    /**
     * Method getDescription returns the description of this MimeDescriptor object.
     *
     * @return the description (type String) of this MimeDescriptor object.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Method setDescription sets the description of this MimeDescriptor object.
     *
     * @param description the description of this MimeDescriptor object.
     *
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * Method getFileTypes returns the fileTypes of this MimeDescriptor object.
     *
     * @return the fileTypes (type List<String>) of this MimeDescriptor object.
     */
    public List<String> getFileTypes() {
        return fileTypes;
    }

    /**
     * Method setFileTypes sets the fileTypes of this MimeDescriptor object.
     *
     * @param fileTypes the fileTypes of this MimeDescriptor object.
     *
     */
    public void setFileTypes(List<String> fileTypes) {
        this.fileTypes = fileTypes;
    }

    /**
     * Method getMime returns the mime of this MimeDescriptor object.
     *
     * @return the mime (type String) of this MimeDescriptor object.
     */
    public String getMime() {
        return mime;
    }

    /**
     * Method setMime sets the mime of this MimeDescriptor object.
     *
     * @param mime the mime of this MimeDescriptor object.
     *
     */
    public void setMime(String mime) {
        this.mime = mime;
    }
}
