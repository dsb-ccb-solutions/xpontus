/*
 * ScenarioModel.java
 *
 * Created on 19-Aug-2007, 9:13:02 AM
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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.util.Hashtable;


/**
 * Scenario model for user transformations
 * @author Yves Zoundi
 */
public class ScenarioModel {
    public boolean isNew = true;
    private PropertyChangeSupport pcs;
    private String input = "";
    private boolean externalDocument = false;    
    private String output = ""; 
    private String xsl = "";
    private String processor = "";
    private String name = "";
    private Hashtable parameters = new Hashtable();

    public ScenarioModel() {
        pcs = new PropertyChangeSupport(this);
    }

    

    /**
     * Returns the scenario's name
     * @return The scenario's name
     */
    public String getName() {
        return name;
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
     * The scenario's name
     * @param newValue The scenario's name
     */
    public void setName(String newValue) {
        String oldValue = this.name;
        this.name = newValue;
        pcs.firePropertyChange("name", oldValue, newValue);
    }

    /**
     * whether the document is an external document or the current document
     * @return whether the document is an external document or the current document
     */
    public boolean isExternalDocument() {
        return externalDocument;
    }

    /**
     * whether the document is an external document or the current document
     * @param newValue whether the document is an external document or the current document
     */
    public void setExternalDocument(boolean newValue) {
        boolean oldValue = this.externalDocument;
        this.externalDocument = newValue;
        pcs.firePropertyChange("externalDocument", oldValue, newValue);
    }

    /**
     * The input file
     * @return The input file
     */
    public String getInput() {
        return input;
    }

    /**
     * The input document
     * @param newValue The input document
     */
    public void setInput(String newValue) {
        String oldValue = this.input;
        this.input = newValue;
        pcs.firePropertyChange("input", oldValue, newValue);
    }

    /**
     * The output file
     * @return The output file
     */
    public String getOutput() {
        return output;
    }

    /**
     * The output file
     * @param newValue The output file
     */
    public void setOutput(String newValue) {
        String oldValue = this.output;
        this.output = newValue;
        pcs.firePropertyChange("output", oldValue, newValue);
    }

    /**
     * Return the processor parameters
     * @return The processor parameters
     */
    public Hashtable getParameters() {
        return parameters;
    }

    /**
     * The processor parameters
     * @param newValue The processor parameters
     */
    public void setParameters(Hashtable newValue) {
        Hashtable oldValue = this.parameters;
        this.parameters = newValue;
        pcs.firePropertyChange("parameters", oldValue, newValue);
    }

    /**
     * The XSLT processor name
     * @return The XSLT processor name
     */
    public String getProcessor() {
        return processor;
    }

    /**
     * The XSLT processor name
     * @param newValue The XSLT processor name
     */
    public void setProcessor(String newValue) {
        String oldValue = this.processor;
        this.processor = newValue;
        pcs.firePropertyChange("processor", oldValue, newValue);
    }

    
    /**
     * The XSL stylesheet for the transformation
     * @return The XSL stylesheet for the transformation
     */
    public String getXsl() {
        return xsl;
    }

    /**
     * The XSL stylesheet for the transformation
     * @param newValue The XSL stylesheet for the transformation
     */
    public void setXsl(String newValue) {
        String oldValue = this.xsl;
        this.xsl = newValue;
        pcs.firePropertyChange("xsl", oldValue, newValue);
    }
    
    public String toString(){
        return this.name;
    }
}
