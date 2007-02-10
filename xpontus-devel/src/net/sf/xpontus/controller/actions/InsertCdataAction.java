/*
 * InsertCdataAction.java
 *
 * Created on 1 octobre 2005, 12:14
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
import net.sf.xpontus.view.XPontusWindow;


/**
 * Action to insert cdata
 * @author Yves Zoundi
 */
public class InsertCdataAction extends BaseAction
  {
    /** Creates a new instance of InsertCdataAction */
    public InsertCdataAction()
      {
      }

    public void execute()
      {
        javax.swing.JEditorPane editor;
        editor = XPontusWindow.getInstance().getCurrentEditor();

        int pos = editor.getSelectionStart();

        try
          {
            editor.getDocument().insertString(pos, "<![CDATA[  ]]>", null);
            editor.setCaretPosition(pos + 10);
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
      }
  }
