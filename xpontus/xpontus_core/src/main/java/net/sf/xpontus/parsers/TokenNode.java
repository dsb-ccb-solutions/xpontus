/*
 * TokenNode.java
 *
 * Created on June 20, 2007, 9:36 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
 */
package net.sf.xpontus.parsers;

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * A tree node representing a token in the outline window
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class TokenNode extends DefaultMutableTreeNode
{
    private static final long serialVersionUID = -7966117448741500075L;
    public int line = 0;
    public int column = 0;
    public int endLine = -1;
    public int endColumn = -1;

    /**
     * @param aNode
     * @param line
     * @param column
     */
    public TokenNode(String aNode, int line, int column)
    {
        this.setUserObject(aNode);
        this.line = line;
        this.column = column;
    }
}
