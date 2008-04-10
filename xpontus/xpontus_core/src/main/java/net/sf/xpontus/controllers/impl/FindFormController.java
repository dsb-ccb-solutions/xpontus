package net.sf.xpontus.controllers.impl;


/*
 * SearchFormController.java
 *
 * Created on 01/07/2005, 17:46
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
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.SearchFormView;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


/**
 * Controller for the find/replace dialog
 * @author Yves Zoundi
 */
public class FindFormController {
    public static final String FIND_METHOD = "find";
    public static final String REPLACE_ALL_METHOD = "replaceAll";
    public static final String REPLACE_METHOD = "replace";
    public static final String CLOSE_METHOD = "close";
    private SearchFormView searchForm;

    /** Creates a new instance of SearchFormController */
    public FindFormController(SearchFormView form) {
        this.searchForm = form;
    }

    public FindFormController() {
    }

    public void find() {
        //System.out.println("search form is not null:" + (searchForm!=null));
        if (searchForm.getFindString() == null) {
            XPontusComponentsUtils.showWarningMessage(
                "Please enter required information!");

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
        JTextComponent edit = DefaultXPontusWindowImpl.getInstance()
                                                      .getDocumentTabContainer()
                                                      .getCurrentEditor();
        edit.getCaret().setSelectionVisible(true);

        edit.setCaretPosition(0);



        if (findString == null) {
            return;
        }

        this.searchForm.getReplaceList().addItem(replaceString);

        for (; find(findString, matchCase, direction);
                edit.replaceSelection(replaceString)) {
            ;
        }

        edit.setCaretPosition(0);
    }

    public boolean find(String findString, boolean matchCase,
        boolean downDirection) {
        boolean found = false;
        JTextComponent edit = DefaultXPontusWindowImpl.getInstance()
                                                      .getDocumentTabContainer()
                                                      .getCurrentEditor();
        edit.getCaret().setSelectionVisible(true);

        this.searchForm.getFindList().addItem(findString);

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
                XPontusComponentsUtils.showErrorMessage("Text not found");

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
            XPontusComponentsUtils.showErrorMessage("Text not found");
        }

        return found;
    }

    public boolean replace(String findString, String replaceString,
        boolean matchCase, boolean direction) {
        JTextComponent edit = DefaultXPontusWindowImpl.getInstance()
                                                      .getDocumentTabContainer()
                                                      .getCurrentEditor();
        this.searchForm.getReplaceList().addItem(replaceString);
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
        if (searchForm.getFindString() == null) {
            XPontusComponentsUtils.showWarningMessage(
                "Please enter required information!");

            return;
        } else {
            replace(searchForm.getFindString(), searchForm.getReplaceString(),
                searchForm.isMatchCase(), searchForm.isDownDirection());
        }
    }

    public void replaceAll() {
        if (searchForm.getFindString() == null) {
            XPontusComponentsUtils.showWarningMessage(
                "Please enter required information!");

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
