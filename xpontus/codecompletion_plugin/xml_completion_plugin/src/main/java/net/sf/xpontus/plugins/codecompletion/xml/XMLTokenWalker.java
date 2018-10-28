/*
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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

import org.apache.commons.lang.math.IntRange;

import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 *
 * @author Yves Zoundi
 */
public class XMLTokenWalker
{
    private static final int LOWER_BOUND_INDEX = 0;
    private static final int HIGHER_BOUND_INDEX = 1;
    private int offset = 0;
    private TokenNode nearestTokenNode;
    private Document doc;

    public TokenNode getNearestTokenNode()
    {
        return nearestTokenNode;
    }

    public void setPositionInformation(Document doc, int offset, int lineInfo,
        int columnInfo)
    {
        this.offset = offset;
        this.doc = doc;
    }

    public void walk(DefaultMutableTreeNode treeNode)
    {
        final int treeNodeChildCount = treeNode.getChildCount();
        int[] offsets = {  };

        TokenNode tokenNode = null;

        for (int i = 0; i < treeNodeChildCount; i++)
        {
            tokenNode = (TokenNode) treeNode.getChildAt(i);
            offsets = getTokenNodeOffset(tokenNode);

            if (offsets[LOWER_BOUND_INDEX] > (offset))
            {
                break;
            }

            if (nearestTokenNode != null)
            {
                if (isBestMatch(tokenNode))
                {
                    nearestTokenNode = tokenNode;
                }
            }
            else
            {
                nearestTokenNode = tokenNode;
            }

            walk(tokenNode);
        }
    }

    public int[] getTokenNodeOffset(TokenNode tokenNode)
    {
        Element element = doc.getDefaultRootElement();

        // we need to remove some info from here
        int tokenLineNumber = tokenNode.line - 1;
        int tokenColumnNumber = tokenNode.column - 1;

        int[] offsets = new int[2];

        int lineOffset = element.getElement(tokenLineNumber).getStartOffset();
        offsets[LOWER_BOUND_INDEX] = (lineOffset + tokenColumnNumber) - 1;

        if (tokenNode.endLine == -1)
        {
            offsets[HIGHER_BOUND_INDEX] = offsets[LOWER_BOUND_INDEX];
        }
        else
        {
            int lineOffset2 = element.getElement(tokenNode.endLine - 1)
                                     .getStartOffset();
            offsets[HIGHER_BOUND_INDEX] = lineOffset2 + tokenColumnNumber;
        }

        return offsets;
    }

    private boolean isBestMatch(TokenNode tn)
    {
        int[] offsets = getTokenNodeOffset(tn);

        IntRange m_range = new IntRange(offsets[LOWER_BOUND_INDEX],
                offsets[HIGHER_BOUND_INDEX]);

        return m_range.containsInteger(offset);
    }
}
