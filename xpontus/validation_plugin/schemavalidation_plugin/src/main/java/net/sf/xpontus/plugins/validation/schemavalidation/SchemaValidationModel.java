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
package net.sf.xpontus.plugins.validation.schemavalidation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class SchemaValidationModel {
    private PropertyChangeSupport pcs;
    private boolean useCurrentDocument = true;
    private String input = "";
    private String schema = "";
    private String type = "DTD";

    public SchemaValidationModel() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     *
     * @param x
     */
    public void addPropertyChangeListener(PropertyChangeListener x) {
        pcs.addPropertyChangeListener(x);
    }

    /**
     *
     * @param x
     */
    public void removePropertyChangeListener(PropertyChangeListener x) {
        pcs.removePropertyChangeListener(x);
    }

    /**
     *
     * @return
     */
    public String getInput() {
        return input;
    }

    /**
     *
     * @param newValue
     */
    public void setInput(String newValue) {
        String oldValue = this.input;
        input = newValue;
        pcs.firePropertyChange("input", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public String getSchema() {
        return schema;
    }

    /**
     *
     * @param newValue
     */
    public void setSchema(String newValue) {
        String oldValue = this.schema;
        schema = newValue;
        pcs.firePropertyChange("schema", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public String getType() {
        return type;
    }

    /**
     *
     * @param newValue
     */
    public void setType(String newValue) {
        String oldValue = this.type;
        type = newValue;
        pcs.firePropertyChange("type", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public boolean isUseCurrentDocument() {
        return useCurrentDocument;
    }

    /**
     *
     * @param newValue 
     */
    public void setUseCurrentDocument(boolean newValue) {
        boolean oldValue = this.useCurrentDocument;
        useCurrentDocument = newValue;
        pcs.firePropertyChange("useCurrentDocument", oldValue, newValue);
    }
}
