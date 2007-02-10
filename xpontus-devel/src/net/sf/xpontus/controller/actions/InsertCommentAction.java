/*
 * InsertCommentAction.java
 *
 * Created on June 3, 2006, 1:15 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.view.XPontusWindow;


/**
 *
 * @author Yves Zoundi
 */
public class InsertCommentAction extends BaseAction
  {
    /** Creates a new instance of InsertCdataAction */
    public InsertCommentAction()
      {
      }

    public void execute()
      {
        javax.swing.JEditorPane editor;
        editor = XPontusWindow.getInstance().getCurrentEditor();

        int pos = editor.getSelectionStart();

        try
          {
            editor.getDocument().insertString(pos, "<!--  -->", null);
            editor.setCaretPosition(pos + 5);
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
      }
  }
