/*
 * ISchemaDocumentationGenerator.java
 *
 * Created on February 11, 2007, 9:23 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.controller.handlers;


/**
 * An interface to set the parameters of a schema documentation tool
 * @author Yves Zoundi
 */
public interface ISchemaDocumentationGenerator {
    
    /** set the footer */
    public void setFooter(String footer); 
    
    /** set the header */
    public void setHeader(String header);
        
    /** set the output directory */
    public void setOutputDirectory(String outputDirectory);
     
    /** set the title */
    public void setTitle(String title);
     
    /** set the css stylesheet */
    public void setCss(String css);
     
    /** set the source directory */
    public void setSourceDirectory(String sourceDirectory);
    
    /** generate the documentation */
    public void run();
}
