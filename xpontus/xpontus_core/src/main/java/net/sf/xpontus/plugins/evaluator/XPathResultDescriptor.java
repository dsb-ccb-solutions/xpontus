/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.evaluator;


/**
 *
 * @author Propriétaire
 */
public class XPathResultDescriptor {
    private String value;
    private int line = -1;
    private int column = -1;
    public boolean lineInfo = false;

    public XPathResultDescriptor(String value) {
        this.value = value;
    }

    public boolean hasLineInfo() {
        return lineInfo;
    }

    public void setLineInfo(boolean lineInfo) {
        this.lineInfo = lineInfo;
    }/* (non-Javadoc)
     * @see java.lang.Object#toString()
     */
    public String toString() {
        return value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public int getLine() {
        return line;
    }

    public void setLine(int line) {
        this.line = line;
    }

    public int getColumn() {
        return column;
    }

    public void setColumn(int column) {
        this.column = column;
    }
}
