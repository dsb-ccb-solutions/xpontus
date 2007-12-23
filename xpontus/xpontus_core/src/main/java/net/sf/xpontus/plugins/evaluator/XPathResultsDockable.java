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

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Component;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPathResultsDockable extends OutputDockable {
    private JTable xpathResultsTable;

    public XPathResultsDockable(int id, String key, Component comp) {
        super(id, key, comp);

        xpathResultsTable = (JTable) getComponent();

        xpathResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //        xpathResultsTable.getSelectionModel()
        //                         .addListSelectionListener(new RowSelectionListener());
        xpathResultsTable.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) {
                        int selectedRow = xpathResultsTable.getSelectedRow();

                        if (selectedRow != -1) {
                            String s = null;

                            for (int i = 0;
                                    i < xpathResultsTable.getColumnCount();
                                    i++) {
                                s = (xpathResultsTable.getValueAt(selectedRow, i)).toString();

                                String[] lineinfo = s.split(",")[0].split(":");
                                //                    int pos = "line ".length();
                                //                    int lineNumber = Integer.parseInt(line.substring(pos,
                                //                                line.length()));
                                gotoLine(lineinfo);
                            }
                        }
                    }
                }
            });
    }

    public void setResultsModel(TableModel model) {
        xpathResultsTable.setModel(model);
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
        //            Element lineElement = element.getElement(lineNumber - 1);
        //            int position = lineElement.getEndOffset() - 1;
        edit.requestFocus();
        edit.grabFocus();
        edit.setCaretPosition(pos);
        edit.moveCaretPosition(pos + columnNumber);
    }

    public void println(String message) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    //    private class RowSelectionListener implements ListSelectionListener {
    //        public void valueChanged(ListSelectionEvent e) {
    //            // Ignore extra messages.
    //            if (e.getValueIsAdjusting()) {
    //                return;
    //            }
    //
    //            ListSelectionModel lsm = (ListSelectionModel) e.getSource();
    //
    //            if (!lsm.isSelectionEmpty()) {
    //                int selectedRow = lsm.getMinSelectionIndex();
    //                String s = null;
    //
    //                for (int i = 0; i < xpathResultsTable.getColumnCount(); i++) {
    //                    s = (xpathResultsTable.getValueAt(selectedRow, i)).toString();
    //
    //                    String[] lineinfo = s.split(",")[0].split(":");
    //                    //                    int pos = "line ".length();
    //                    //                    int lineNumber = Integer.parseInt(line.substring(pos,
    //                    //                                line.length()));
    //                    gotoLine(lineinfo);
    //                }
    //            }
    //        }

    //    }
}
