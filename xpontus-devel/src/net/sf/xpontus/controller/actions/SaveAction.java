/*
 * SaveAction.java
 *
 * Created on 18 juillet 2005, 02:53
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
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.view.PaneForm;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.io.FilenameUtils;

import javax.swing.text.Document;


/**
 * Action to save a document
 * @author Yves Zoundi
 */
public class SaveAction extends BaseAction
  {
    javax.swing.JFileChooser chooser;

    /** Creates a new instance of SaveAction */
    public SaveAction()
      {
        chooser = new javax.swing.JFileChooser();
        chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
      }

    /**
     *
     * @param editor
     * @param ask
     * @throws java.lang.Exception
     */
    public void saveEditor(javax.swing.JEditorPane editor, boolean ask)
        throws Exception
      {
        PaneForm pane = XPontusWindow.getInstance().getPane();
        String _val_ = XPontusWindow.getInstance()
                                    .getI18nMessage("action.save.name");
        boolean filenew = editor.getClientProperty("FILE_NEW")
                                .equals(Boolean.TRUE);
        boolean modified = editor.getClientProperty("FILE_MODIFIED")
                                 .equals(Boolean.TRUE);

        if (!filenew)
          {
            //            System.out.println("file is not new");
            if (modified)
              {
                //                System.out.println("file is modified");
                if (ask)
                  {
                    //                    System.out.println("asked to save");
                    java.io.File file = (java.io.File) editor.getClientProperty(
                            "FILE");
                    String str = null;

                    if (file == null)
                      {
                        str = editor.getClientProperty("FILE_PATH").toString();
                      }
                    else
                      {
                        str = file.getName();
                      }

                    chooser.setDialogTitle(_val_ + " " + str + "?");

                    int rep = javax.swing.JOptionPane.showConfirmDialog(XPontusWindow.getInstance()
                                                                                     .getFrame(),
                            _val_ + file.getAbsolutePath() + "?", _val_ + "?",
                            javax.swing.JOptionPane.YES_NO_OPTION);

                    if (rep == javax.swing.JOptionPane.YES_OPTION)
                      {
                        save(file);
                        editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);
                      }
                  }
                else
                  {
                    java.io.File file = (java.io.File) editor.getClientProperty(
                            "FILE");
                    save(file);
                    editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);
                  }
              }
          }
        else
          {
            //            System.out.println("file is  new");
            //                System.out.println("file is modified");
            chooser.setDialogTitle(_val_ +
                editor.getClientProperty("FILE_PATH") + "?");

            if (chooser.showSaveDialog(XPontusWindow.getInstance().getFrame()) == javax.swing.JFileChooser.APPROVE_OPTION)
              {
                java.io.File file = chooser.getSelectedFile();

                if (file.exists())
                  {
                    int answer = javax.swing.JOptionPane.showConfirmDialog(XPontusWindow.getInstance()
                                                                                        .getFrame(),
                            "Erase");

                    if (answer == javax.swing.JOptionPane.YES_OPTION)
                      {
//                        int ouvert = pane.isOpen(file);
//
//                        if (ouvert != -1)
//                          {
//                            pane.remove(ouvert);
//                          }
//
//                        save(file);
                      }
                    else
                      {
                        return;
                      }
                  }
                else
                  {
                    save(file);
                  }
              }
          }
      }

    public void save(int index, boolean ask) throws Exception
      {
        //        System.out.println("index " + index);
        javax.swing.JEditorPane editor = null;
        PaneForm pane = XPontusWindow.getInstance().getPane();
//        editor = pane.getEditorAt(index);

        String _val_ = XPontusWindow.getInstance()
                                    .getI18nMessage("action.save.name");

        boolean filenew = editor.getClientProperty("FILE_NEW")
                                .equals(Boolean.TRUE);
        boolean modified = editor.getClientProperty("FILE_MODIFIED")
                                 .equals(Boolean.TRUE);

        if (!filenew)
          {
            //            System.out.println("file is not new");
            if (modified)
              {
                //                System.out.println("filemodified:" + editor.getClientProperty("FILE_MODIFIED"));
                //                System.out.println("file is modified");
                if (ask)
                  {
                    //                    System.out.println("asked to save");
                    java.io.File file = (java.io.File) editor.getClientProperty(
                            "FILE");
                    chooser.setDialogTitle(_val_ + " " + file.getName() + "?");

                    int rep = javax.swing.JOptionPane.showConfirmDialog(XPontusWindow.getInstance()
                                                                                     .getFrame(),
                            _val_ + " " + file.getAbsolutePath() + "?",
                            "Confirm", javax.swing.JOptionPane.YES_NO_OPTION);

                    if (rep == javax.swing.JOptionPane.YES_OPTION)
                      {
                        save(file);
                        editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);
                      }
                  }
                else
                  {
                    java.io.File file = (java.io.File) editor.getClientProperty(
                            "FILE");
                    save(file);
                    editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);
                  }
              }
          }
        else
          {
            //              System.out.println("filenew");
            //            System.out.println("file is  new");
            //                System.out.println("file is modified");
//            chooser.setDialogTitle(_val_ + " " + pane.getToolTipTextAt(index) +
//                "?");

            if (chooser.showSaveDialog(XPontusWindow.getInstance().getFrame()) == javax.swing.JFileChooser.APPROVE_OPTION)
              {
                java.io.File file = chooser.getSelectedFile();

                if (file.exists())
                  {
                    int answer = javax.swing.JOptionPane.showConfirmDialog(XPontusWindow.getInstance()
                                                                                        .getFrame(),
                            "Erase");

                    if (answer == javax.swing.JOptionPane.YES_OPTION)
                      {
//                        int ouvert = pane.isOpen(file);
//
//                        if (ouvert != -1)
//                          {
//                            pane.remove(ouvert);
//                          }
//
//                        save(file);
                      }
                    else
                      {
                        return;
                      }
                  }
                else
                  {
                    save(file);
                  }
              }
          }
      }

    public void execute()
      {
        try
          {
//            PaneForm pane = XPontusWindow.getInstance().getPane();
//            int index = pane.getSelectedIndex();
//            save(index, false);
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
      }

    public void save(java.io.File file)
      {
        javax.swing.JEditorPane editor = XPontusWindow.getInstance()
                                                      .getCurrentEditor();

        Document _doc = editor.getDocument();

        //                TokenMarker tk1 = (TokenMarker) editor.getClientProperty("TOKEN_MARKER");
        //                
        try
          {
            //            if (tk1!=null ) {
            //                if(tk1.getClass() == XMLTokenMarker.class){
            //                XMLOptionModel m1 = new XMLOptionModel();
            //                m1 = (XMLOptionModel) m1.load();
            //                editor.write(new java.io.OutputStreamWriter(
            //                        new java.io.FileOutputStream(file), m1.getXmlEncoding()));
            //                }
            //                else{
            //                    editor.write(new java.io.FileWriter(file));
            //                }
            //            } else {
            //                editor.write(new java.io.FileWriter(file));
            //            }
            editor.write(new java.io.FileWriter(file));
            editor.putClientProperty("FILE", file);
            editor.putClientProperty("FILE_PATH", file.getAbsolutePath());
            editor.putClientProperty("FILE_MODIFIED", Boolean.FALSE);
            editor.putClientProperty("FILE_NEW", Boolean.FALSE);

            String extension = FilenameUtils.getExtension(file.getName());
            //            TokenMarker tk = KitInfo.getInstance().getTokenMarker(extension);
            //            editor.putClientProperty("TOKEN_MARKER", tk);
            //           _doc.setTokenMarker(tk);
            XPontusWindow.getInstance().setMessage(file.getAbsolutePath());

//            int i = XPontusWindow.getInstance().getPane().getSelectedIndex();
//            XPontusWindow.getInstance().getPane()
//                         .setToolTipTextAt(i, file.getAbsolutePath());
//            XPontusWindow.getInstance().getPane().setTitleAt(i, file.getName());
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
      }
  }
