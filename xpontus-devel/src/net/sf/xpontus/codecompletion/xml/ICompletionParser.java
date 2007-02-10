/*
 * ICompletionParser.java
 *
 * Created on February 7, 2007, 8:25 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.codecompletion.xml;

import java.io.Reader;
import java.util.List;
import java.util.Map;

/**
 *
 * @author Yves Zoundi
 */
public interface ICompletionParser {
    
    public void init(List tagList, Map nsTagListMap);
    public void updateCompletionInfo(String uri, Reader in);
    
}
