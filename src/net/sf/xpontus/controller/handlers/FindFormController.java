/*
 * SearchFormController.java
 *
 * Created on 1 août 2005, 17:46
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
package net.sf.xpontus.controller.handlers;

import net.sf.xpontus.core.controller.handlers.BaseController;
import net.sf.xpontus.utils.MsgUtils;
import net.sf.xpontus.view.SearchFormView;
import net.sf.xpontus.view.XPontusWindow;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;


/**
 * Controller for the find/replace dialog
 * @author Yves Zoundi
 */
public class FindFormController extends BaseController {
    private SearchFormView searchForm;

    /** Creates a new instance of SearchFormController */
    public FindFormController() {
    }

    public FindFormController(SearchFormView searchForm) {
        setSearchForm(searchForm);
    }

    public void find() {
        if (searchForm.getFindString().equals("")) {
            MsgUtils msg = MsgUtils.getInstance();
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                msg.getString("msg.enterRequiredInformation"),
                msg.getString("msg.error"),
                javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        } else {
            find(searchForm.getFindString(), searchForm.isMatchCase(),
                searchForm.isDownDirection());
        }
    }

    /**
     *
     * @param findString
     * @param replaceString
     * @param matchCase
     * @param direction
     */
    public void replaceAll(String findString, String replaceString,
        boolean matchCase, boolean direction) {
        javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                    .getCurrentEditor();
        edit.getCaret().setSelectionVisible(true);

        edit.setCaretPosition(0);

        if (findString.equals("")) {
            return;
        }

        for (; find(findString, matchCase, direction);
                edit.replaceSelection(replaceString)) {
            ;
        }

        edit.setCaretPosition(0);
    }

    public boolean find(String findString, boolean matchCase,
        boolean downDirection) {
        boolean found = false;
        javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                    .getCurrentEditor();
        edit.getCaret().setSelectionVisible(true);

        int position = edit.getCaretPosition();
        int direction = 1;
        Document openedDocument = edit.getDocument();
        int taille = openedDocument.getLength();
        final int len = findString.length();

        if (!downDirection) {
            position = edit.getSelectionStart() - len;
            direction = -1;
        }

        for (int counter = position; counter < (taille + 1);
                counter += direction) {
            String checkText;

            try {
                checkText = openedDocument.getText(counter, len);
            } catch (BadLocationException e) {
                javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                       .getFrame(),
                    MsgUtils.getInstance().getString("msg.textNotFound"));

                return found;
            }

            if ((!matchCase && checkText.equalsIgnoreCase(findString)) ||
                    (matchCase && checkText.equals(findString))) {
                edit.setCaretPosition(counter);
                edit.moveCaretPosition(counter + len);
                found = true;

                return found;
            }
        }

        if (!found) {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                MsgUtils.getInstance().getString("msg.textNotFound"));
        }

        return found;
    }

    public boolean replace(String findString, String replaceString,
        boolean matchCase, boolean direction) {
        javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                    .getCurrentEditor();
        edit.getCaret().setSelectionVisible(true);

        if ((edit.getSelectedText() != null) &&
                ((!matchCase &&
                edit.getSelectedText().equalsIgnoreCase(findString)) ||
                (matchCase && edit.getSelectedText().equals(findString)))) {
            edit.replaceSelection(replaceString);
        }

        return find(findString, matchCase, direction);
    }

    public void replace() {
        if (searchForm.getFindString().equals("")) {
            MsgUtils msg = MsgUtils.getInstance();
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                msg.getString("msg.enterRequiredInformation"),
                msg.getString("msg.error"),
                javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        } else {
            replace(searchForm.getFindString(), searchForm.getReplaceString(),
                searchForm.isMatchCase(), searchForm.isDownDirection());
        }
    }

    public void replaceall() {
        if (searchForm.getFindString().equals("")) {
            MsgUtils msg = MsgUtils.getInstance();
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                msg.getString("msg.enterRequiredInformation"),
                msg.getString("msg.error"),
                javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        } else {
            replaceAll(searchForm.getFindString(),
                searchForm.getReplaceString(), searchForm.isMatchCase(),
                searchForm.isDownDirection());
        }
    }

    public void close() {
        searchForm.setVisible(false);
    }

    public SearchFormView getSearchForm() {
        return searchForm;
    }

    public void setSearchForm(SearchFormView searchForm) {
        this.searchForm = searchForm;
    }
}
