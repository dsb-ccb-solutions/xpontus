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

import javax.swing.table.AbstractTableModel; 
import org.apache.xerces.dom.ElementImpl;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPathResultsTableModel extends AbstractTableModel
  {
    private static final long serialVersionUID = -7783523895240189020L;
    private final String[] columns = new String[1];
    private final int numColumns = 1;
    private String[] mRows;
    private String[][] data;
    private int numRows;

    /**
     * @param rows
     */
    public XPathResultsTableModel(NodeList rows, DOMAddLines locator)
      {
        columns[0] = "Results";

        final int nbResults = rows.getLength();
        data = new String[nbResults][nbResults];
        numRows = nbResults;
        this.mRows = new String[nbResults];

        for (int i = 0; i < mRows.length; i++)
          { 
            final Node element = (Node) rows.item(i);
            StringBuffer buff = new StringBuffer();
//            buff.append("line " + element.getLineNumber());
//            
            buff.append(locator.getLineInfo(element) + ",");
            buff.append(element.getNodeName());
//            buff.append("," + rows.item(i).toString());
              
            
            data[i][0] = buff.toString();
          }
      }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
      {
        return numColumns;
      }

    public String getColumnName(int columnIndex)
      {
        return columns[columnIndex];
      }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
      {
        return numRows;
      }

    /* (non-Javadoc)
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column)
      {
        return data[row][column];
      }

    /* (non-Javadoc)
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    public boolean isCellEditable(int rowIndex, int columnIndex)
      {
        return false;
      }
  }