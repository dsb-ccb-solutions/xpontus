/*
 * BatchValidationFormController.java
 *
 * Created on November 5, 2005, 3:17 PM
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

import net.sf.xpontus.view.*;
import net.sf.xpontus.view.BatchValidationForm;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.SuffixFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;


/**
 * Controller for the batch validation dialog actions
 * @author Yves Zoundi
 */
public class BatchValidationFormController
  {
    public static final String CANCEL_METHOD = "cancel";
    public static final String ADD_EXTENSION_METHOD = "addExtension";
    public static final String REMOVE_EXTENSION_METHOD = "removeExtension";
    public static final String SELECT_PATH_METHOD = "selectPath";
    public static final String VALIDATE_XML_METHOD = "validateXml";
    private java.io.File[] files;
    private javax.swing.JFileChooser chooser;
    private BatchValidationForm view;

    /** Creates a new instance of BatchValidationFormController */
    public BatchValidationFormController(BatchValidationForm view)
      {
        this.view = view;
        chooser = new javax.swing.JFileChooser();
        chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(true);
        chooser.setDialogTitle(XPontusWindow.getInstance()
                                            .getI18nMessage("msg.batchvalidation"));
      }

    public void cancel()
      {
        view.setVisible(false);
      }

    public void validateXml()
      {
        if (files == null)
          {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                XPontusWindow.getInstance().getI18nMessage("msg.noFileSelected"),
                XPontusWindow.getInstance().getI18nMessage("msg.error"),
                javax.swing.JOptionPane.ERROR_MESSAGE);

            return;
          }

        javax.swing.ListModel _listModel = view.getExtensionsList().getModel();
        final int taille = _listModel.getSize();
        String[] ext = new String[taille];

        for (int i = 0; i < taille; i++)
          {
            ext[i] = _listModel.getElementAt(i).toString();
          }

        java.io.File[] toProcess;
        final boolean recurse = view.isRecursive();

        IOFileFilter suffixFilter = new SuffixFileFilter(ext);
        IOFileFilter filter = null;

        if (recurse)
          {
            filter = TrueFileFilter.INSTANCE;
          }

        java.util.Collection col;
        final java.util.List fileList = new java.util.ArrayList();

        for (int i = 0; i < files.length; i++)
          {
            if (files[i].isDirectory())
              {
                col = FileUtils.listFiles(files[i], suffixFilter, filter);

                java.io.File[] _array = FileUtils.convertFileCollectionToFileArray(col);

                for (int j = 0; j < _array.length; j++)
                  {
                    fileList.add(_array[j]);
                  }
              }
            else
              {
                fileList.add(files[i]);
              }
          }

        view.setVisible(false);
        XPontusWindow.getInstance()
                     .append(ConsoleOutputWindow.MESSAGES_WINDOW,
            "-------------------");
        XPontusWindow.getInstance()
                     .append(ConsoleOutputWindow.MESSAGES_WINDOW,
            XPontusWindow.getInstance().getI18nMessage("msg.validating"));
        XPontusWindow.getInstance()
                     .append(ConsoleOutputWindow.MESSAGES_WINDOW,
            XPontusWindow.getInstance().getI18nMessage("msg.thereAre") + " " +
            fileList.size() + " " +
            XPontusWindow.getInstance().getI18nMessage("msg.filesToValidate"));

        BatchValidationHandler task = BatchValidationHandler.getInstance(fileList);

        //       task.go();
      }

    public boolean extensionExiste(String ext)
      {
        javax.swing.DefaultComboBoxModel model = view.getModel();

        for (int i = 0; i < model.getSize(); i++)
          {
            if (model.getElementAt(i).toString().equals(ext))
              {
                return true;
              }
          }

        return false;
      }

    public void addExtension()
      {
        String ext = view.getExtensionTF().getText().trim();

        if (ext.equals(""))
          {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                XPontusWindow.getInstance().getI18nMessage("msg.typeExtension"),
                XPontusWindow.getInstance().getI18nMessage("msg.error"),
                javax.swing.JOptionPane.ERROR_MESSAGE);

            return;
          }
        else
          {
            javax.swing.DefaultComboBoxModel model = view.getModel();

            if (!extensionExiste(ext))
              {
                model.addElement(ext);
              }
          }
      }

    public void removeExtension()
      {
        int i = view.getExtensionsList().getSelectedIndex();

        if (i == -1)
          {
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                XPontusWindow.getInstance().getI18nMessage("msg.extensionSelect"),
                XPontusWindow.getInstance().getI18nMessage("msg.error"),
                javax.swing.JOptionPane.WARNING_MESSAGE);

            return;
          }

        javax.swing.DefaultComboBoxModel model = view.getModel();
        model.removeElementAt(i);
      }

    public void selectPath()
      {
        int answer = chooser.showOpenDialog(view);

        if (answer == javax.swing.JFileChooser.APPROVE_OPTION)
          {
            files = chooser.getSelectedFiles();

            StringBuffer sb = new StringBuffer();

            for (int i = 0; i < files.length; i++)
              {
                sb.append(files[i].getAbsolutePath());
                sb.append(" ");
              }

            view.getPathTF().setText(sb.toString());
          }
      }
  }
