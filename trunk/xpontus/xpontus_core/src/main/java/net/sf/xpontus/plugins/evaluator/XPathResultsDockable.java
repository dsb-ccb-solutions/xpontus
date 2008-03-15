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

import com.vlsolutions.swing.docking.DockKey;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableModel;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPathResultsDockable extends OutputDockable {
    public static final String DOCKABLE_ID = "XPATH_WINDOW";
    private DockKey m_key;
    private JScrollPane scrollPane;
    private JTable xpathResultsTable;

    public XPathResultsDockable() {
        super();
        xpathResultsTable = new JTable();

        Dimension m_dimension = new Dimension(300, 100);
        xpathResultsTable.setPreferredScrollableViewportSize(m_dimension);

        m_key = new DockKey(DOCKABLE_ID, "XPath");
        m_key.setResizeWeight(0.1f);

        scrollPane = new JScrollPane(xpathResultsTable);

        xpathResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        xpathResultsTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = xpathResultsTable.getSelectedRow();

                        if (selectedRow != -1) {
                            XPathResultDescriptor xrd = (XPathResultDescriptor) xpathResultsTable.getValueAt(selectedRow,
                                    0);

                            if (xrd.lineInfo) {
                                String s = xrd.value;
                                String[] lineinfo = s.split(",")[0].split(":");
                                gotoLine(lineinfo);
                            }
                        }
                    }
                }
            });
    }

    public void setResultsModel(TableModel model) {
        xpathResultsTable.setModel(model);
        String m_title = "Results (" + model.getRowCount() + ")";
        xpathResultsTable.getColumnModel().getColumn(0).setHeaderValue(m_title); 
        xpathResultsTable.revalidate();
        xpathResultsTable.repaint();
        
    }

    /**
         * @param lineNumber
         */
    private void gotoLine(String[] lineInfo) {
        JTextComponent edit = DefaultXPontusWindowImpl.getInstance()
                                                      .getDocumentTabContainer()
                                                      .getCurrentEditor();

        Element element = edit.getDocument().getDefaultRootElement();

        int lineNumber = Integer.parseInt(lineInfo[0]);
        int columnNumber = Integer.parseInt(lineInfo[1]) - 1;

        if (element.getElement(lineNumber - 1) == null) {
            XPontusComponentsUtils.showErrorMessage("No such line :" +
                (lineNumber - 1));

            return;
        }

        int pos = element.getElement(lineNumber - 1).getStartOffset();

        edit.requestFocus();
        edit.grabFocus();
        edit.setCaretPosition(pos);
        edit.moveCaretPosition(pos + columnNumber);
    }

    public void println(String message) {
    }

    public String getId() {
        return DOCKABLE_ID;
    }

    public void println(String message, int style) {
    }

    public DockKey getDockKey() {
        return m_key;
    }

    public Component getComponent() {
        return scrollPane;
    }
}