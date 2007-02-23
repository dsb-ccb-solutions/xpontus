/*
 * SearchFormController.java
 *
 * Created on 1 août 2005, 17:46
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
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

import java.awt.Toolkit;

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

    /**
     * create a controller with a search dialog
     * @param searchForm the search dialog
     */
    public FindFormController(SearchFormView searchForm) {
        setSearchForm(searchForm);
    }

    /**
     * find a string
     */
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
     * replace a string by another by looking in the whole document from the start
     * @param findString the string to look for
     * @param replaceString the string replacement
     * @param matchCase a case sensitive search
     * @param direction the search direction
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

    /**
     * Find a string
     * @param findString a string to look for
     * @param matchCase a case sensitive search
     * @param downDirection the direction of the search
     * @return if the text has been found
     */
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

                Toolkit.getDefaultToolkit().beep();

                return found;
            }
        }

        if (!found) {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                MsgUtils.getInstance().getString("msg.textNotFound"));
        }

        Toolkit.getDefaultToolkit().beep();

        return found;
    }

    /**
     * Replace a string
     * @param findString  the string to look for
     * @param replaceString the string replacement
     * @param matchCase a case sensitive search
     * @param direction  the search direction
     * @return if the text has been found and replaced
     */
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

    /**
     * replace a string
     */
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

    /**
     * replace all in the document
     */
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

    /**
     *  close the search dialog
     */
    public void close() {
        searchForm.setVisible(false);
    }

    /**
     * get the view of this controller
     * @return the view of this controller
     */
    public SearchFormView getSearchForm() {
        return searchForm;
    }

    /**
     * set the view of this controller
     * @param searchForm the view of this controller
     */
    public void setSearchForm(SearchFormView searchForm) {
        this.searchForm = searchForm;
    }
}
