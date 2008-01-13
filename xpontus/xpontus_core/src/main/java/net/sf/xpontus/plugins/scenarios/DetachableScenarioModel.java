/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.scenarios;

import java.util.Hashtable;


/**
 *
 * @author Propriétaire
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

    public boolean isPreview() {
        return preview;
    }

    public void setPreview(boolean preview) {
        this.preview = preview;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public boolean isExternalDocument() {
        return externalDocument;
    }

    public void setExternalDocument(boolean externalDocument) {
        this.externalDocument = externalDocument;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public Hashtable getParameters() {
        return parameters;
    }

    public void setParameters(Hashtable parameters) {
        this.parameters = parameters;
    }

    public String getProcessor() {
        return processor;
    }

    public String toString() {
        return this.alias;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
    }

    public String getXsl() {
        return xsl;
    }

    public void setXsl(String xsl) {
        this.xsl = xsl;
    }
}
