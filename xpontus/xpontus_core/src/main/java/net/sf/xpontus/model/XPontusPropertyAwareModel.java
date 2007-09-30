/*
 * XPontusPropertyAwareModel.java
 *
 * Created on 25 mai 2007, 14:44
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
package net.sf.xpontus.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 *
 * @author Yves Zoundi
 */
public class XPontusPropertyAwareModel {
    protected PropertyChangeSupport changeSupport;

    /**
     * Creates a new instance of XPontusPropertyAwareModel
     */
    public XPontusPropertyAwareModel() {
        changeSupport = new PropertyChangeSupport(this);
    }

    /**
     *
     * @return
     */
    public PropertyChangeSupport getChangeSupport() {
        return changeSupport;
    }

    /**
     *
     * @param changeSupport
     */
    public void setChangeSupport(PropertyChangeSupport changeSupport) {
        this.changeSupport = changeSupport;
    }

    /**
     *
     * @param x
     */
    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    /**
     *
     * @param x
     */
    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }
}
