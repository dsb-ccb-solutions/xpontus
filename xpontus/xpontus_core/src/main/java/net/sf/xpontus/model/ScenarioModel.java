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

import java.util.Hashtable;


/**
 * Scenario model for user transformations
 * @author Yves Zoundi
 */
public class ScenarioModel {
    private String input;
    private boolean externalDocument;
    private String output;
    private String type;
    private String xsl;
    private String processor;
    private String name;
    private Hashtable parameters;

    public ScenarioModel() {
    }

    /**
     * Returns the scenario's name
     * @return The scenario's name
     */
    public String getName() {
        return name;
    }

    /**
     * The scenario's name
     * @param name The scenario's name
     */
    public void setName(String name) {
        this.name = name;
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
     * @param externalDocument whether the document is an external document or the current document
     */
    public void setExternalDocument(boolean externalDocument) {
        this.externalDocument = externalDocument;
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
     * @param input The input document
     */
    public void setInput(String input) {
        this.input = input;
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
     * @param output The output file
     */
    public void setOutput(String output) {
        this.output = output;
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
     * @param parameters The processor parameters
     */
    public void setParameters(Hashtable parameters) {
        this.parameters = parameters;
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
     * @param processor The XSLT processor name
     */
    public void setProcessor(String processor) {
        this.processor = processor;
    }

    /**
     * The scenario output type(XML, XSLT, TEXT, PDF, ETC.)
     * @return The scenario output type(XML, XSLT, TEXT, PDF, ETC.)
     */
    public String getType() {
        return type;
    }

    /**
     * The scenario output type(XML, XSLT, TEXT, PDF, ETC.)
     * @param type The scenario output type(XML, XSLT, TEXT, PDF, ETC.)
     */
    public void setType(String type) {
        this.type = type;
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
     * @param xsl The XSL stylesheet for the transformation
     */
    public void setXsl(String xsl) {
        this.xsl = xsl;
    }
}
