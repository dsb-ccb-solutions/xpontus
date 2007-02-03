/*
 * CloseOtherAction.java
 *
 * Created on November 4, 2005, 10:16 AM
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

import java.awt.Component;

import java.util.List;
import java.util.Vector;

import javax.swing.JEditorPane;
import javax.swing.JScrollPane;
import javax.swing.JViewport;


/**
 * Action to close all documents but the selected one(not included for now in the application)
 * @author Yves Zoundi
 */
public class CloseOtherAction extends BaseAction
  {
    /** Creates a new instance of CloseOtherAction */
    public CloseOtherAction()
      {
      }

    public void execute()
      {
        // check files to save
        checkFilesToSave();
      }

    /**
     * close all tabs but the selected one
     */
    public void checkFilesToSave()
      {
        PaneForm pane = XPontusWindow.getInstance().getPane();
        JEditorPane edit = pane.getCurrentEditor();

        SaveAction action = null;
        action = (SaveAction) pane.getApplicationContext().getBean("action.save");

        List componentList = new Vector();
        List editorList = new Vector();

        try
          {
            for (int i = 0; i < pane.getTabCount(); i++)
              {
                if (pane.getEditorAt(i) != null)
                  {
                    if (pane.getEditorAt(i) == edit)
                      {
                        continue;
                      }
                    else
                      {
                        JScrollPane scrollPane = (JScrollPane) pane.getComponentAt(i);
                        JViewport viewPort = scrollPane.getViewport();
                        JEditorPane editor = (JEditorPane) viewPort.getComponent(0);
                        componentList.add(scrollPane);
                        editorList.add(editor);
                      }
                  }
              }

            for (int i = 0; i < componentList.size(); i++)
              {
                action.saveEditor((JEditorPane) editorList.get(i), true);
                pane.remove((Component) componentList.get(i));
              }
          }
        catch (Exception e)
          {
            e.printStackTrace();
          }
      }
  }
