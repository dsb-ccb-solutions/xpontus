/*
 * ObservableModel.java
 *
 * Created on 9-Aug-2007, 8:21:05 PM
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
package net.sf.xpontus.model;

import java.io.Serializable;

import java.util.Observable;


/**
 *
 * @author Propriétaire
 */
public class ObservableModel extends Observable implements Serializable {
    /**
     * default constructor
     */
    public ObservableModel() {
    }

    /**
     * notify the observers that the model has changed
     */
    protected void updateView() {
        setChanged();
        notifyObservers();
    }
}
