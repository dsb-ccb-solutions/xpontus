/*
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *
 */
package net.sf.xpontus.plugins.codecompletion.xml;

import net.sf.xpontus.parsers.TokenNode;

import org.apache.commons.lang.text.StrBuilder;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XMLTokenWalker {
    private int column;
    private int offset = 0;
    private int line;
    private TokenNode nearestTokenNode;
    private Document doc;

    public TokenNode getNearestTokenNode() {
        if (nearestTokenNode == null) {
            System.out.println("token node is null");

            return null;
        }

        System.out.println("CompletionToken:" + nearestTokenNode.toString());

        if (nearestTokenNode.endLine != -1) {
            System.out.println("endLine of token:" + nearestTokenNode.endLine);

            if (nearestTokenNode.endLine < line) {
                TreeNode parent = nearestTokenNode.getParent();

                if (parent instanceof TokenNode) {
                    nearestTokenNode = (TokenNode) parent;
                }
            }
        } else {
            System.out.println("No end information");
        }

        return nearestTokenNode;
    }

  public  void setPositionInformation(Document doc, int offset, int lineInfo,
        int columnInfo) {
        this.line = lineInfo + 1;
        this.offset = offset;
        this.column = columnInfo + 2;
        this.doc = doc;

        System.out.println("Offset:" + offset + ",Line max is:" + this.line +
            "," + this.column);
        nearestTokenNode = null;
    }

    public void walk(DefaultMutableTreeNode n) {
        int total = n.getChildCount();

        for (int i = 0; i < total; i++) {
            TokenNode tn = (TokenNode) n.getChildAt(i);
            int tokenLine = tn.line;
            int tokenColumn = tn.column;

            if (tokenLine > line) {
                System.out.println("ligne depassee :" + tokenLine);

                break;
            }

            if ((tokenLine == line) && (tokenColumn > column)) {
                break;
            }

            if ((tokenLine < line) ||
                    ((tokenLine == line) && (tokenColumn < column))) {
                if (nearestTokenNode != null) {
                    if (isBestMatch(tn)) {
                        nearestTokenNode = tn;
                    }
                } else {
                    nearestTokenNode = tn;
                }
            }

            walk(tn);
        }
    }

    public void printToken(TokenNode n) {
        StrBuilder sb = new StrBuilder();
        sb.append("image:" + n.toString());
        sb.append(",startLine:" + n.line);
        sb.append(",startColumn:" + n.column);
        sb.append(",endLine:" + n.endLine);
        sb.append(",endColumn:" + n.endColumn);
        System.out.println(sb.toString());
    }

    public int[] getTokenNodeOffset(TokenNode n) {
        Element element = doc.getDefaultRootElement();

        // we need to remove some info from here
        int m_line = n.line - 1;
        int m_column = n.column - 1;

        int[] offsets = new int[2];

        int lineOffset = element.getElement(m_line).getStartOffset();
        offsets[0] = lineOffset + m_column - 1;

        if (n.endLine == -1) {
            offsets[1] = offsets[0];
        } else {
            int lineOffset2 = element.getElement(n.endLine - 1).getStartOffset();
            offsets[1] = lineOffset2 + m_column + 1;
        }

        return offsets;
    }
    
    public boolean isInInterVall(int[] pos){
        
        return offset > pos[0] && offset < pos[1];
    }

    private boolean isBestMatch(TokenNode tn) {
        System.out.println("=================================");
        System.out.println("Evaluating token:" + tn.toString());

        printToken(tn);

        int[] offsets = getTokenNodeOffset(tn);
        System.out.println("Current interval:" + offsets[0] + "," + offsets[1]);
        
        int[] offsets2 = getTokenNodeOffset(nearestTokenNode);
        
        if(isInInterVall(offsets) && isInInterVall(offsets2)){
            int diff = offsets[1] - offsets[0];
            int diff2 = offsets2[1] - offsets2[0];
            return diff < diff2;
        }

        return (offset > offsets[0]) && (offset < offsets[1]);
    }
}
