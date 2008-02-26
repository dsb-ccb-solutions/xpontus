/*
 * ScenarioModel.java
 *
 *
 * Created on 1 août 2005, 17:46
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.model;

import net.sf.xpontus.core.model.ObservableModel;


/**
 * A class which describes an XSLT scenario
 * @author Yves Zoundi
 */
public class ScenarioModel extends ObservableModel {
    private String outputType = "HTML";
    private String name;
    private String outputFile;
    private boolean external = false;
    private String xmlURI;
    private String xslURI;
    private java.util.Hashtable params;

    /** Creates a new instance of ScenarioModel */
    public ScenarioModel() {
        params = new java.util.Hashtable();
    }

    /**
     *
     * @return The name of the scenario
     */
    public String toString() {
        return name;
    }

    /**
     * get the output type for the transformation
     * @return The output type
     */
    public String getOutputType() {
        return outputType;
    }

    /**
     * set the output type for the transformation
     * @param outputType The output type
     */
    public void setOutputType(String outputType) {
        this.outputType = outputType;
        updateView();
    }

    /**
     * check if the scenario is using an external xml file as input
     * @return The document is either an opened document or an external file
     */
    public boolean isExternal() {
        return external;
    }

    /**
     * set whether or not the scenario is relative to the current opened document or an external file
     * @param external The document is either an opened document or an external file
     */
    public void setExternal(boolean external) {
        this.external = external;
        updateView();
    }

    /**
     * return the xml input document
     * @return The XML URI of the scenario
     */
    public String getXmlURI() {
        return xmlURI;
    }

    /**
     * the the xml input document
     * @param xmlURI The XML URI of the scenario
     */
    public void setXmlURI(String xmlURI) {
        this.xmlURI = xmlURI;
        updateView();
    }

    /**
     * get the xsl stylesheet
     * @return The stylesheet URI of the scenario
     */
    public String getXslURI() {
        return xslURI;
    }

    /**
     * set the xsl stylesheet
     * @param xslURI The stylesheet URI of the scenario
     */
    public void setXslURI(String xslURI) {
        this.xslURI = xslURI;
        updateView();
    }

    /**
     * get the scenario's parameters
     * @return The scenario's parameters
     */
    public java.util.Hashtable getParams() {
        return params;
    }

    /**
     * set the scenario's parameters
     * @param params The scenario's parameters
     */
    public void setParams(java.util.Hashtable params) {
        this.params = params;
        updateView();
    }

    /**
     * get the scenario's name
     * @return The scenario's name
     */
    public String getName() {
        return name;
    }

    /**
     * set the scenario's name
     * @param name The scenario's name
     */
    public void setName(String name) {
        this.name = name;
        updateView();
    }

    /**
     * validate a scenario
     * @return The validity of the scenario
     */
    public boolean isValid() {
        if (xslURI == null) {
            return false;
        }

        if (outputFile == null) {
            return false;
        }

        if (xmlURI == null) {
            if (external) {
                return false;
            }
        }

        return true;
    }

    /**
     * get the output file
     * @return  The output file
     */
    public String getOutputFile() {
        return outputFile;
    }

    /**
     * set the output file
     * @param outputFile The output file
     */
    public void setOutputFile(String outputFile) {
        this.outputFile = outputFile;
    }
}
