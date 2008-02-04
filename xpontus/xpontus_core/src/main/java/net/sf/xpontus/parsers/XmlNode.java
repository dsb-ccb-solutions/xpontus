/*
 * XmlNode.java
 *
 * Created on January 27, 2007, 9:32 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.parsers;

import antlr.Token;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author mrcheeks
 */
public class XmlNode extends DefaultMutableTreeNode {
    public int line;
    public int column;

    public XmlNode(String aNode, int line, int column) {
        this.setUserObject(aNode);
        this.line = line;
        this.column = column;
    }

    /** Creates a new instance of XmlNode */
    public XmlNode(Token token) {
        this.setUserObject(token.getText());
        this.column = token.getColumn() - 1;
        this.line = token.getLine() - 1;
    }
}
