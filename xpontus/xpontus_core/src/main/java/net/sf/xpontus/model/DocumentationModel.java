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
package net.sf.xpontus.model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DocumentationModel {
    private PropertyChangeSupport pcs;
    private String type;
    private String title = "";
    private String input = "";
    private String output = "";
    private String header = "";
    private String footer = "";
    private String css = "";

    public DocumentationModel() {
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
    public String getCss() {
        return css;
    }

    /**
     *
     * @param newValue
     */
    public void setCss(String newValue) {
        String oldValue = this.css;
        this.css = newValue;
        pcs.firePropertyChange("css", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public String getFooter() {
        return footer;
    }

    /**
     *
     * @param newValue
     */
    public void setFooter(String newValue) {
        String oldValue = this.footer;
        this.footer = newValue;
        pcs.firePropertyChange("footer", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public String getHeader() {
        return header;
    }

    /**
     *
     * @param newValue
     */
    public void setHeader(String newValue) {
        String oldValue = this.header;
        this.header = newValue;
        pcs.firePropertyChange("header", oldValue, newValue);
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
        this.input = newValue;
        pcs.firePropertyChange("input", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public String getOutput() {
        return output;
    }

    /**
     *
     * @param newValue
     */
    public void setOutput(String newValue) {
        String oldValue = this.output;
        this.output = newValue;
        pcs.firePropertyChange("output", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param newValue
     */
    public void setTitle(String newValue) {
        String oldValue = this.title;
        this.title = newValue;
        pcs.firePropertyChange("title", oldValue, newValue);
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
        this.type = newValue;
        pcs.firePropertyChange("type", oldValue, newValue);
    }
}
