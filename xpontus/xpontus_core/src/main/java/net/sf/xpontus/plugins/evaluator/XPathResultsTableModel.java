/*
 *
 *
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
 */
package net.sf.xpontus.plugins.evaluator;

import org.apache.xerces.dom.ElementImpl;
import org.apache.xerces.dom.TextImpl;

import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.swing.table.AbstractTableModel;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPathResultsTableModel extends AbstractTableModel {
    private static final long serialVersionUID = -7783523895240189020L;
    private final String[] columns = new String[1];
    private final int numColumns = 1;
    private Object[] mRows;
    private Object[][] data;
    private int numRows;

    /**
     * @param rows
     * @param locator
     */
    public XPathResultsTableModel(NodeList rows, DOMAddLines locator) {
        columns[0] = "Results";

        final int nbResults = rows.getLength();
        data = new Object[nbResults][nbResults];
        numRows = nbResults;
        this.mRows = new Object[nbResults];

        for (int i = 0; i < mRows.length; i++) {
            final Node element = (Node) rows.item(i);

            XPathResultDescriptor res = new XPathResultDescriptor();

            if (element instanceof ElementImpl) {
                StringBuffer buff = new StringBuffer();
                buff.append(locator.getLineInfo(element) + ",");
                buff.append(element.getNodeName());
                res.lineInfo = true;
                res.value = buff.toString();
            } else if (element instanceof TextImpl) {
                StringBuffer buff = new StringBuffer();
                System.out.println("hum.. some text");
                res.lineInfo = false;

                TextImpl ti = (TextImpl) element;
                buff.append("getData" + ti.toString() + ",getNodeValue" +
                    ti.getNodeValue() + ",getTextContent" + ",getWholeText" +
                    ti.getWholeText());

                System.out.println("TextImpl Buffer:" + buff.toString());

                String texte = buff.toString() +
                    "([etc... REST OF THE TEXT...])";
                int taille = texte.length();
                int max = taille;

                //                if (max > 15) {
                //                    max = 15;
                //                }
                res.value = texte.substring(0, max);
            } else {
                res.lineInfo = false;
                res.value = element.getNodeValue();
            }

            data[i][0] = res;
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount() {
        return numColumns;
    }

    public String getColumnName(int columnIndex) {
        return columns[columnIndex];
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount() {
        return numRows;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column) {
        Object o = data[row][column];

        return o;
    }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }
}
