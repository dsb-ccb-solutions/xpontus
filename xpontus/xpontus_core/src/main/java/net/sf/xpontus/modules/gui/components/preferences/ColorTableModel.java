/* 
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
 */
package net.sf.xpontus.modules.gui.components.preferences;

import javax.swing.table.AbstractTableModel;


/**
 *
 * @author Yves Zoundi
 */
public class ColorTableModel extends AbstractTableModel
{ 
	private static final long serialVersionUID = -3065385023594023077L;
	private final String[] columnNames;
    private final Object[][] data;

    public ColorTableModel(Object[][] data, String[] columnNames)
    {
        this.columnNames = columnNames;
        this.data = data;
    }

    public int getColumnCount()
    {
        return columnNames.length;
    }

    public int getRowCount()
    {
        return data.length;
    }

    public String getColumnName(int col)
    {
        return columnNames[col];
    }

    public Object getValueAt(int row, int col)
    {
        return data[row][col];
    }

    /*
     * JTable uses this method to determine the default renderer
     * editor for each cell.  If we didn't implement this method,
     * then the last column would contain text ("true"/"false"),
     * rather than a check box.
     */
    public Class getColumnClass(int c)
    {
        return getValueAt(0, c).getClass();
    }

    public boolean isCellEditable(int row, int col)
    {
        //Note that the data/cell address is constant,
        //no matter where the cell appears onscreen.
        if (col < 1)
        {
            return false;
        }
        else
        {
            return true;
        }
    }

    public void setValueAt(Object value, int row, int col)
    {
        data[row][col] = value;
        fireTableCellUpdated(row, col);
    }
}
