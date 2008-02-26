/*
 * AssistProcessor.java
 *
 * Created on February 11, 2007, 2:06 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.codecompletion.xml;

import java.util.List;

/**
 * Content assist interface
 * @author Yves Zoundi
 */
public interface AssistProcessor {
    public List getCompletionList();
}
