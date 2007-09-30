/*
 * HTMLCodeCompletionPluginImpl.java
 * 
 * Created on 2007-09-22, 20:47:25
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.plugins.codecompletion.html;

import net.sf.xpontus.plugins.completion.CodeCompletionIF;

/**
 *
 * @author Propriétaire
 */
public class HTMLCodeCompletionPluginImpl implements CodeCompletionIF{

    public String getMimeType() {
        return "text/html";
    }

    public String getFileMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
