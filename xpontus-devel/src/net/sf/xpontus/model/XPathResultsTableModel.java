package net.sf.xpontus.model;

import net.sf.saxon.om.NodeInfo;

import java.util.List;

import javax.swing.table.AbstractTableModel;


/**
 * @author Yves Zoundi
 * A table model to present the xpath results
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
    public XPathResultsTableModel(List rows)
      {
        columns[0] = "Results";

        final int nbResults = rows.size();
        data = new String[nbResults][nbResults];
        numRows = nbResults;
        this.mRows = new String[nbResults];

        for (int i = 0; i < mRows.length; i++)
          {
            System.out.println(rows.get(i).toString());
            NodeInfo element = (NodeInfo) rows.get(i);
            StringBuffer buff = new StringBuffer();
            buff.append("line " + element.getLineNumber());
            
            buff.append(", Element " + element.getDisplayName());
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
