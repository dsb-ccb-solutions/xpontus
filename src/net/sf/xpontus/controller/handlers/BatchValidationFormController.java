/*
 * BatchValidationFormController.java
 *
 * Created on November 5, 2005, 3:17 PM
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
import net.sf.xpontus.view.BatchValidationForm;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;


/**
 * Controller for the batch validation dialog actions
 * @author Yves Zoundi
 */
public class BatchValidationFormController extends BaseController {
    private java.io.File[] files;
    private javax.swing.JFileChooser chooser;
    private BatchValidationForm view;
    private MsgUtils _msg;

    /** Creates a new instance of BatchValidationFormController
     * @param view the window associated to this controller
     */
    public BatchValidationFormController(BatchValidationForm view) {
        this.view = view;
        _msg = MsgUtils.getInstance();
        chooser = new javax.swing.JFileChooser();
        chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle(_msg.getString("msg.batchvalidation"));
    }

    /**
     * close the view
     */
    public void cancel() {
        view.setVisible(false);
    }

    /**
     * validate a set of xml documents
     */
    public void validate() {
        if (files == null) {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                _msg.getString("msg.noFileSelected"),
                _msg.getString("msg.error"),
                javax.swing.JOptionPane.ERROR_MESSAGE);

            return;
        }

        javax.swing.ListModel _listModel = view.getExtensionsList().getModel();
        final int taille = _listModel.getSize();
        String[] ext = new String[taille];

        for (int i = 0; i < taille; i++) {
            ext[i] = _listModel.getElementAt(i).toString();
        }

        java.io.File[] toProcess;
        final boolean recurse = view.isRecursive();

        IOFileFilter suffixFilter = new SuffixFileFilter(ext);
        IOFileFilter filter = null;

        if (recurse) {
            filter = TrueFileFilter.INSTANCE;
        }

        java.util.Collection col;
        final java.util.List fileList = new java.util.ArrayList();

        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                col = FileUtils.listFiles(files[i], suffixFilter, filter);

                java.io.File[] _array = FileUtils.convertFileCollectionToFileArray(col);

                for (int j = 0; j < _array.length; j++) {
                    fileList.add(_array[j]);
                }
            } else {
                fileList.add(files[i]);
            }
        }

        view.setVisible(false);
        XPontusWindow.getInstance().append("-------------------");
        XPontusWindow.getInstance().append(_msg.getString("msg.validating"));
        XPontusWindow.getInstance()
                     .append(_msg.getString("msg.thereAre") + " " +
            fileList.size() + " " + _msg.getString("msg.filesToValidate"));

        BatchValidationHandler task = new BatchValidationHandler(fileList);

        //       task.go();
    }

    /**
     * check if the xml extension mapping has already been added
     * @param ext the file extension
     * @return if the extension already exists
     */
    public boolean extensionExiste(String ext) {
        javax.swing.DefaultComboBoxModel model = view.getModel();

        for (int i = 0; i < model.getSize(); i++) {
            if (model.getElementAt(i).toString().equals(ext)) {
                return true;
            }
        }

        return false;
    }

    /**
     * add a xml extension mapping
     */
    public void addExtension() {
        String ext = view.getExtensionTF().getText().trim();

        if (ext.equals("")) {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                _msg.getString("msg.typeExtension"),
                _msg.getString("msg.error"),
                javax.swing.JOptionPane.ERROR_MESSAGE);

            return;
        } else {
            javax.swing.DefaultComboBoxModel model = view.getModel();

            if (!extensionExiste(ext)) {
                model.addElement(ext);
            }
        }
    }

    /**
     * remove a xml extension mapping
     */
    public void removeExtension() {
        int i = view.getExtensionsList().getSelectedIndex();

        if (i == -1) {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                _msg.getString("msg.extensionSelect"),
                _msg.getString("msg.error"),
                javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
        }

        javax.swing.DefaultComboBoxModel model = view.getModel();
        model.removeElementAt(i);
    }

    /**
     * select a directory path
     */
    public void selectPath() {
        int answer = chooser.showOpenDialog(view);

        if (answer == javax.swing.JFileChooser.APPROVE_OPTION) {
            files = chooser.getSelectedFiles();

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < files.length; i++) {
                sb.append(files[i].getAbsolutePath());
                sb.append(" ");
            }

            view.getPathTF().setText(sb.toString());
        }
    }
}
