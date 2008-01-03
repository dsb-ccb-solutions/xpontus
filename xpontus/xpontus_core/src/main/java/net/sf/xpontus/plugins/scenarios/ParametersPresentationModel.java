/*
 * ScenarioExecutionView.java
 *
 * Created on August 22, 2007, 5:52 PM
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
package net.sf.xpontus.plugins.scenarios;

import com.jgoodies.binding.list.LinkedListModel;
import com.jgoodies.binding.list.SelectionInList;
import com.jgoodies.binding.value.ValueHolder;
import com.jgoodies.binding.value.ValueModel;

import java.util.*;

import javax.swing.ListModel;
import javax.swing.table.AbstractTableModel;


/**
 *
 * @author Yves Zoundi <yveszoundit at users dot sf dot net>
 */
public class ParametersPresentationModel {
    private static final String[] COLUMNS = new String[] { "Name", "Value" };
    private LinkedListModel parametersListModel = new LinkedListModel();
    private ValueModel parameterSelectionHolder;
    private SelectionInList parameterSelectionInList;
    private List parameters = new Vector();
    private AbstractTableModel tableModel = new ParametersTableModel();

    public ParametersPresentationModel() {
    }

    public void setParameters(List parameters) {
        this.parameters.clear();
        parametersListModel.clear();
        this.parameters = parameters;
        retrieveData();
        parameterSelectionHolder = new ValueHolder();

        parameterSelectionInList = new SelectionInList((ListModel) parametersListModel,
                parameterSelectionHolder);

        tableModel.fireTableDataChanged();
    }

    private void retrieveData() {
        for (int i = 0; i < parameters.size(); i++) {
            parametersListModel.add(parameters.get(i));
        }
    }

    /**
     *
     * @return
     */
    public ValueModel getParameterSelectionHolder() {
        return parameterSelectionHolder;
    }

    /**
     *
     * @return
     */
    public SelectionInList getParameterSelectionInList() {
        return parameterSelectionInList;
    }

    /**
     *
     * @return
     */
    public LinkedListModel getParametersListModel() {
        return parametersListModel;
    }

    public List getParameters() {
        return parameters;
    }

    /**
     *
     * @return
     */
    public AbstractTableModel getTableModel() {
        return tableModel;
    }

    public class ParametersTableModel extends AbstractTableModel {
        public int getRowCount() {
            return parameterSelectionInList.getSize();
        }

        public int getColumnCount() {
            return COLUMNS.length;
        }

        public boolean isCellEditable(int row, int column) {
            return false;
        }

        public String getColumnName(int column) {
            return COLUMNS[column];
        }

        public Object getValueAt(int rowIndex, int columnIndex) {
            ParameterModel pm = (ParameterModel) parameterSelectionInList.getElementAt(rowIndex);

            switch (columnIndex) {
            case 0:
                return pm.getName();

            case 1:
                return pm.getValue();
            }

            return "";
        }
    }
}

