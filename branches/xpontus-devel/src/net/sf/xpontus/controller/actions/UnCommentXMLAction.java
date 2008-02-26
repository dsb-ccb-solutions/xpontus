/*
 * UncommentXMLAction.java
 *
 * Created on June 3, 2006, 12:30 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *
 * @author Yves Zoundi
 */
public class UnCommentXMLAction
  {
    private Log log = LogFactory.getLog(UnCommentXMLAction.class);

    /**
     * Creates a new instance of UncommentXMLAction
     */
    public UnCommentXMLAction()
      {
      }

    public void execute()
      {
        javax.swing.JEditorPane editor;
        editor = XPontusWindow.getInstance().getCurrentEditor();

        String texte = editor.getSelectedText();
        int debut = texte.indexOf("<!--");
        int fin = texte.lastIndexOf("-->");

        if (texte == null)
          {
            return;
          }

        try
          {
            final int taille = texte.length();

            for (int i = 0; i < taille; i++)
              {
              }

            //            editor.getDocument().remove(end, " -->", null);
            //            editor.getDocument().insertString(debut, "<!-- ", null);
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
      }
  }
//  JEditorPane editor = XPontusWindow.getInstance().getCurrentEditor();
//
//        try {
//            String text = editor.getSelectedText();
//            String content1 = text.replaceAll("<!--", "");
//            String content = content1.replaceAll("-->", "");
//            editor.replaceSelection(content);
//        } catch (Exception e) {
//            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
//                                                                   .getFrame(),
//                e.getMessage());
//        }
//    }
//}
