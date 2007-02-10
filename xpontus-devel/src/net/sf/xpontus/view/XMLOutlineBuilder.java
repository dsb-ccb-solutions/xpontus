/*
 * SAXTreeViewer.java
 *
 * Created on January 26, 2007, 5:03 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view;

import net.sf.xpontus.core.utils.BeanUtilities;
import net.sf.xpontus.parsers.XMLLexer;
import net.sf.xpontus.parsers.XMLParser;
import net.sf.xpontus.parsers.XmlNode;

import java.io.Reader;
import java.io.StringReader;

import java.util.Enumeration;

import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.text.Element;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;


// This is an XML book - no need for explicit Swing imports

/**
 *
 * @author Yves Zoundi
 */
public class XMLOutlineBuilder
{
    private DefaultTreeModel treeModel;

    /**
     * <p>
     * This initializes the needed Swing settings.
     * </p>
     */
    public XMLOutlineBuilder()
    {
    }

    /**
     * <p>
     * This will construct the tree using Swing.
     * </p>
     *
     * @param filename
     *            <code>String</code> path to XML document.
     */
    public void init(javax.swing.text.Document doc)
    {
        try
        {
            String mText = doc.getText(0, doc.getLength());
            Reader mReader = new StringReader(mText);
            XMLParser parser = new XMLParser(new XMLLexer(mReader));
            parser.parse();

            XmlNode child = (XmlNode) parser.getRootNode().getFirstChild();

            XmlNode lexerNode = recursivelyCopyNodes(child);

            doc.putProperty("OUTLINE_DATA", lexerNode);
            XPontusWindow.getInstance().getOutlineDockable().updateAll();
        }
        catch (Exception err)
        {
        }
    }

    private XmlNode recursivelyCopyNodes(XmlNode aNode)
    {
        XmlNode copy = new XmlNode(aNode.toString(), aNode.line, aNode.column);
        BeanUtilities.copyProperties(aNode, copy);
        copy.setAllowsChildren(aNode.getAllowsChildren());

        if (aNode.getChildCount() != 0)
        {
            Enumeration children = aNode.children();

            while (children.hasMoreElements())
            {
                XmlNode child = (XmlNode) children.nextElement();
                XmlNode childCopy = this.recursivelyCopyNodes(child);
                copy.add(childCopy);
            }
        }

        return copy;
    }

    private void gotoLine(int line, int column)
    {
        JEditorPane edit = XPontusWindow.getInstance().getCurrentEditor();
        Element element = edit.getDocument().getDefaultRootElement();

        if (element.getElement(line) == null)
        {
            JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
                XPontusWindow.getInstance().getI18nMessage("msg.noSuchLine"),
                XPontusWindow.getInstance().getI18nMessage("msg.error"),
                JOptionPane.WARNING_MESSAGE);

            return;
        }

        int lineOffset = element.getElement(line).getStartOffset();

        int tokenOffset = lineOffset + column;

        edit.setCaretPosition(tokenOffset);
    }
}
