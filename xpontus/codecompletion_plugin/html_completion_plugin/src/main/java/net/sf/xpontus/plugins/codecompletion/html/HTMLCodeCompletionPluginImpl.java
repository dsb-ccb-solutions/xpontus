/*
 * HTMLCodeCompletionPluginImpl.java
 * 
 * Created on 2007-09-22, 20:47:25
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.plugins.codecompletion.html;

import java.util.List;
import javax.swing.text.Document;
import net.sf.xpontus.plugins.completion.CodeCompletionIF;

/**
 *
 * @author Propri�taire
 */
public class HTMLCodeCompletionPluginImpl implements CodeCompletionIF{

    public String getMimeType() {
        return "text/html";
    }

    public String getFileMode() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public List getCompletionList() {
        return null;
    }

    public List getAttributesCompletionList(String tagCompletionName) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isTrigger(String str) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void init(Document doc) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
