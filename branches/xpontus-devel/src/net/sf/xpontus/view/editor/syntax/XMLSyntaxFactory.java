/*
 * XMLSyntaxFactory.java
 *
 * Created on February 9, 2007, 12:31 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.view.editor.syntax;

import java.util.List;

/**
 *
 * @author Yves Zoundi
 */
public class XMLSyntaxFactory {
    
    private List lexerList;
    private String lexerClassName = "";
    private List supportedExtensions;
    
    public ILexer findLexerForExtension(String extension){
        
        
        
        
        
        
        
        return null;
    }
    
    public boolean isSupported(String extension){
        return supportedExtensions.contains(extension);
    }
    
    
}
