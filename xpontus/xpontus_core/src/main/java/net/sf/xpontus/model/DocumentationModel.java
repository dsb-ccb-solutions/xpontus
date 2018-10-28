/*
 *
 * DocumentationModel.java
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
 * The documentation generation model
 * @version 0.0.1
 * @author Yves Zoundi
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

    /**
     * The default constructor
     */
    public DocumentationModel() {
        this.pcs = new PropertyChangeSupport(this);
    }

    /**
     * Adds a new PropertyChangeListener
     * @param x  a PropertyChangeListener
     */
    public void addPropertyChangeListener(PropertyChangeListener x) {
        pcs.addPropertyChangeListener(x);
    }

    /**
     * Removes a new PropertyChangeListener
     * @param x  a PropertyChangeListener
     */
    public void removePropertyChangeListener(PropertyChangeListener x) {
        pcs.removePropertyChangeListener(x);
    }

    /**
     * Returns the css stylesheet path
     * @return the css stylesheet path
     */
    public String getCss() {
        return css;
    }

    /**
     * Set the css stylesheet path
     * @param newCss  the css stylesheet path
     */
    public void setCss(String newCss) {
        String oldValue = this.css;
        this.css = newCss;
        pcs.firePropertyChange("css", oldValue, newCss);
    }

    /**
     * Returns the footer text
     * @return The footer text
     */
    public String getFooter() {
        return footer;
    }

    /**
     * Sets the footer text
     * @param newFooter the footer text
     */
    public void setFooter(String newFooter) {
        String oldValue = this.footer;
        this.footer = newFooter;
        pcs.firePropertyChange("footer", oldValue, newFooter);
    }

    /**
     * Returns the header
     * @return  the header
     */
    public String getHeader() {
        return header;
    }

    /**
     * Sets the header
     * @param newHeader The header
     */
    public void setHeader(String newHeader) {
        String oldValue = this.header;
        this.header = newHeader;
        pcs.firePropertyChange("header", oldValue, newHeader);
    }

    /**
     * Returns the input file
     * @return the input file
     */
    public String getInput() {
        return input;
    }

    /**
     * Sets the input file path
     * @param newInput The input files path
     */
    public void setInput(String newInput) {
        String oldValue = this.input;
        this.input = newInput;
        pcs.firePropertyChange("input", oldValue, newInput);
    }

    /**
     * Returns the output directory
     * @return the output directory
     */
    public String getOutput() {
        return output;
    }

    /**
     * Sets the ouput directory
     * @param newOutput The output path
     */
    public void setOutput(String newOutput) {
        String oldValue = this.output;
        this.output = newOutput;
        pcs.firePropertyChange("output", oldValue, newOutput);
    }

    /**
     * Returns the documentation title
     * @return the documentation title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Sets the documentation title
     * @param newTitle The documentation title
     */
    public void setTitle(String newTitle) {
        String oldValue = this.title;
        this.title = newTitle;
        pcs.firePropertyChange("title", oldValue, newTitle);
    }

    /**
     * Returns the documentation type
     * @return the documentation ttype
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the documentation type
     * @param newType the documentation type
     */
    public void setType(String newType) {
        String oldValue = this.type;
        this.type = newType;
        pcs.firePropertyChange("type", oldValue, newType);
    }
}
