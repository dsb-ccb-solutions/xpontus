/*
 * EnvironmentModel.java
 *
 * Created on 2 octobre 2005, 16:25
 *
 *  Copyright (C) 2005 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.core.model;

import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.swing.table.AbstractTableModel;


/**
 * A class which contains the java environment properties of the user
 */
public class EnvironmentModel extends AbstractTableModel
  {
    private final String[] columns = new String[2];
    private final int numColumns;
    private final String[][] rows;
    private final int numRows;

    /**
     * Create a new instance of EnvironmentModel
     */
    public EnvironmentModel()
      {
        numColumns = columns.length;

        ResourceBundle bundle = null;
        String file = "net.sf.xpontus.core.model.environment";

        try
          {
            Locale locale = Locale.getDefault();
            bundle = ResourceBundle.getBundle(file, locale);
          }
        catch (Exception e)
          {
            bundle = ResourceBundle.getBundle(file, Locale.US);
          }

        columns[0] = bundle.getString("column.property");
        columns[1] = bundle.getString("column.value");

        Properties env = System.getProperties();
        numRows = env.size();
        rows = new String[numRows][numRows];

        Enumeration envProperties = env.keys();

        for (int i = 0; envProperties.hasMoreElements(); i++)
          {
            String key = (String) envProperties.nextElement();
            rows[i][0] = key;
            rows[i][1] = System.getProperty(key);
          }
      }

    /**
     * return the number of columns
     */
    public int getColumnCount()
      {
        return numColumns;
      }

    /**
     * return the number of rows
     */
    public int getRowCount()
      {
        return numRows;
      }

    /**
     *
     * @param row
     * @param column
     * @return
     */
    public Object getValueAt(int row, int column)
      {
        return rows[row][column];
      }

    /**
     *
     * @param columnIndex
     * @return
     */
    public String getColumnName(int columnIndex)
      {
        return columns[columnIndex];
      }

    /**
     *
     * @param row
     * @param column
     * @return
     */
    public boolean isCellEditable(int row, int column)
      {
        return false;
      }
  }
