/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenarios;

import java.util.Hashtable;


/**
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class DetachableScenarioModel {
    private String input = "";
    private boolean externalDocument = false;
    private String output = "";
    private String xsl = "";
    private String processor = "";
    private String alias = "";
    private Hashtable parameters = new Hashtable();
    private boolean preview = false;

    /**
     * @return
     */
    public boolean isPreview() {
        return preview;
    }

    /**
     * @param preview
     */
    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    /**
     * @return
     */
    public String getAlias() {
        return alias;
    }

    /**
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     * @return
     */
    public boolean isExternalDocument() {
        return externalDocument;
    }

    /**
     * @param externalDocument
     */
    public void setExternalDocument(boolean externalDocument) {
        this.externalDocument = externalDocument;
    }

    /**
     * @return
     */
    public String getInput() {
        return input;
    }

    /**
     * @param input
     */
    public void setInput(String input) {
        this.input = input;
    }

    /**
     * @return
     */
    public String getOutput() {
        return output;
    }

    /**
     * @param output
     */
    public void setOutput(String output) {
        this.output = output;
    }

    /**
     * @return
     */
    public Hashtable getParameters() {
        return parameters;
    }

    /**
     * @param parameters
     */
    public void setParameters(Hashtable parameters) {
        this.parameters = parameters;
    }

    /**
     * @return
     */
    public String getProcessor() {
        return processor;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return this.alias;
    }

    /**
     * @param processor
     */
    public void setProcessor(String processor) {
        this.processor = processor;
    }

    /**
     * @return
     */
    public String getXsl() {
        return xsl;
    }

    /**
     * @param xsl
     */
    public void setXsl(String xsl) {
        this.xsl = xsl;
    }
}
