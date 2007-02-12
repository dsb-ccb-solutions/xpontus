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
 *
 * @author Owner
 */
public interface ISchemaDocumentationGenerator {
    
    // set the css stylesheet to use to generate the documentation 
    public void setCSS(String css);
    
    // set the title for the documentation
    public void setTitle(String title);
    
    // set the source directory
    public void setSourceDirectory(String sourceDirectory);
    
    // set the output directory
    public void setOutputDirectory(String outputDirectory);
    
    // set the footer for the documentation
    public void setFooter(String footer);
    
    // set the header for the documentation
    public void setHeader(String header);
    
    
    
    
    
}
