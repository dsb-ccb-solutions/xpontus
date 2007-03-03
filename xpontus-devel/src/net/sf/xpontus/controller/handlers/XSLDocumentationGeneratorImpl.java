/*
 * XSLDocumentationGeneratorImpl.java
 *
 * Created on March 2, 2007, 7:45 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.controller.handlers;


/**
 *
 * @author mrcheeks
 */
public class XSLDocumentationGeneratorImpl
    implements ISchemaDocumentationGenerator {
    private String css;
    private String title;
    private String outputDirectory;
    private String sourceDirectory;
    private String footer;
    private String header;

    /** Creates a new instance of DTDDocumentationGeneratorImpl */
    public XSLDocumentationGeneratorImpl() {
    }

    public String getFooter() {
        return this.footer;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public String getHeader() {
        return this.header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getOutputDirectory() {
        return this.outputDirectory;
    }

    public void setOutputDirectory(String outputDirectory) {
        this.outputDirectory = outputDirectory;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getCss() {
        return this.css;
    }

    public void setCss(String css) {
        this.css = css;
    }

    public String getSourceDirectory() {
        return this.sourceDirectory;
    }

    public void setSourceDirectory(String sourceDirectory) {
        this.sourceDirectory = sourceDirectory;
    }

    public void run() {
    }
}
