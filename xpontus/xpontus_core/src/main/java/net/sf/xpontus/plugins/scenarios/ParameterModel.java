/*
 * ScenarioExecutionView.java
 *
 * Created on August 22, 2007, 5:52 PM
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
package net.sf.xpontus.plugins.scenarios;

import com.jgoodies.binding.beans.Model;


/**
 *
 * @author Yves Zoundi <yveszoundit at users dot sf dot net>
 */
public class ParameterModel extends Model {
    public static final String PROPERTY_NAME = "name";
    public static final String PROPERTY_VALUE = "value";
    private String name;
    private String value;

    public ParameterModel() {
    }

    public ParameterModel(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        String old = this.name;
        this.name = name;
        this.firePropertyChange("name", old, this.name);
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        String old = this.value;
        this.value = value;
        this.firePropertyChange("value", old, this.value);
    }

    public void copyTo(ParameterModel m_model) {
        m_model.setName(this.name);
        m_model.setValue(this.value);
    }
}
