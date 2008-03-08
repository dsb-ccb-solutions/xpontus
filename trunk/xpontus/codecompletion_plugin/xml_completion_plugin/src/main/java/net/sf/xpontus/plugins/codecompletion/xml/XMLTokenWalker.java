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

import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeNode;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XMLTokenWalker {
    private int column;
    private int line;
    private TokenNode nearestTokenNode;
    

    public TokenNode getNearestTokenNode() {
        if(nearestTokenNode.endLine != -1){
            if(nearestTokenNode.endLine < line){
                TreeNode parent = nearestTokenNode.getParent();
                if(parent instanceof TokenNode){
                    nearestTokenNode = (TokenNode) parent;
                }
            }
        }
        return nearestTokenNode;
    }
    
    

    void setPositionInformation(int lineInfo, int columnInfo) {
        this.line = lineInfo + 1;
        System.out.println("Line max is:" + this.line);
        this.column = columnInfo + 1;
        nearestTokenNode = null;
    }

    public void walk(DefaultMutableTreeNode n) {
        int total = n.getChildCount();

        for (int i = 0; i < total; i++) {
            TokenNode tn = (TokenNode) n.getChildAt(i);
            int tokenLine = tn.line;
            int tokenColumn = tn.column;

            System.out.println("walking node:" + tn.toString() + " - " +
                tokenLine + ":" + tokenColumn);

            if (tokenLine > line) {
                System.out.println("ligne depassee :" + tokenLine);

                break;
            }

            if ((tokenLine == line) && (tokenColumn > column)) {
                System.out.println("bonne ligne:" + line +
                    ",colonne depassee:" + tokenColumn);

                break;
            }

            nearestTokenNode = tn;

            walk(tn);
        }
    }
}
