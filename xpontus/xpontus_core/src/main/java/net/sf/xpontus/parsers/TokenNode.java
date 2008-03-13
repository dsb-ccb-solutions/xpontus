/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.parsers;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author mrcheeks
 */
public class TokenNode extends DefaultMutableTreeNode {
    public int line;
    public int column;
    public int endLine = -1;
    public int endColumn = -1;

    public TokenNode(String aNode, int line, int column) {
        this.setUserObject(aNode);
        this.line = line;
        this.column = column;
    }
}
