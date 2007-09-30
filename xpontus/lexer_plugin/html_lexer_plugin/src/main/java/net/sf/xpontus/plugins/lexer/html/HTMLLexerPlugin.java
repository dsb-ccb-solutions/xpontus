/*
 * HTMLLexerPlugin.java
 * 
 * Created on 20-Jul-2007, 9:18:09 AM
 * 
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.plugins.lexer.html;

import org.java.plugin.Plugin;

/**
 *
 * @author Yves Zoundi
 */
public class HTMLLexerPlugin extends Plugin{

    public HTMLLexerPlugin() {
    }

    protected void doStart() throws Exception {
        log.info("HTML Lexer plugin started...");
    }

    protected void doStop() throws Exception {
        log.info("HTML Lexer plugin stopped...");
    }

}
