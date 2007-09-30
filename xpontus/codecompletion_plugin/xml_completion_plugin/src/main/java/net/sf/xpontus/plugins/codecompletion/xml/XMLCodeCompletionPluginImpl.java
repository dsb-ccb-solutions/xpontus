/*
 * XMLCodeCompletionPluginImpl.java
 * 
 * Created on 2007-09-22, 20:46:05
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.plugins.codecompletion.xml;

import java.util.List;
import net.sf.xpontus.plugins.completion.CodeCompletionIF;

/**
 * Code completion plugin for XML files
 * @version
 * @author Yves Zoundi
 */
public class XMLCodeCompletionPluginImpl implements CodeCompletionIF {

    public XMLCodeCompletionPluginImpl(){ 
    }
    
    public List getCompletionList(){
        return null;
    }
    public String getMimeType() {
        return "text/xml";
    }

    public String getFileMode() {
        return "generic";
    }

}
