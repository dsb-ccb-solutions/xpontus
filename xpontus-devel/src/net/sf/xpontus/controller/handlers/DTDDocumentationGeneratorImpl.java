/*
 * DTDDocumentationGeneratorImpl.java
 *
 * Created on February 11, 2007, 9:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.controller.handlers;

import DTDDoc.DTDDoc;


/**
 *
 * @author Owner
 */
public class DTDDocumentationGeneratorImpl
    implements ISchemaDocumentationGenerator {
    private DTDDoc dtdDoc;
    private String css;
    private String title;
    private String outputDirectory;
    private String sourceDirectory;
    private String footer;
    private String header;
    
    public String getFooter(){
        return this.footer;
    }
    
    public void setFooter(String footer){
        this.footer = footer;
    }
    
    
    public String getHeader(){
        return this.header;
    }
    
    
    public void setHeader(String header){
        this.header = header;
    }
    
    public String getOutputDirectory(){
        return this.outputDirectory;
    }
    
    public void setOutputDirectory(String outputDirectory){
        this.outputDirectory = outputDirectory;
    }
    
    public String getTitle(){
        return this.title;
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public String getCss(){
        return this.css;
        
    }
    
    public void setCss(String css){
        this.css = css;
    }
    
    public String getSourceDirectory(){
        return this.sourceDirectory;
    }
    
    public void setSourceDirectory(String sourceDirectory){
        this.sourceDirectory = sourceDirectory;
    }

    /** Creates a new instance of DTDDocumentationGeneratorImpl */
    public DTDDocumentationGeneratorImpl() {
    }

    public void run() {
        
    }
}
