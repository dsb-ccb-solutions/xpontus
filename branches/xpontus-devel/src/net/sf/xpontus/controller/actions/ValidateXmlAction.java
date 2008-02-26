/*
 * ValidateXmlAction.java
 *
 * Created on 2 octobre 2005, 16:53
 *
 * Copyright (C) 2005 Yves Zoundi
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

import net.sf.xpontus.core.controller.actions.ThreadedAction;
import net.sf.xpontus.view.*;

import org.apache.xerces.parsers.SAXParser;

import org.xml.sax.InputSource;

import java.io.InputStreamReader;


/**
 * Action to validate an xml document
 * @author Yves Zoundi
 */
public class ValidateXmlAction extends ThreadedAction
  {
    private SAXParser parser;

    /** Creates a new instance of ValidateXmlAction */
    public ValidateXmlAction()
      {
        parser = new SAXParser();
      }

    public void execute()
      {
        try
          {
            StringBuffer buff = new StringBuffer();
            buff.append("\n");
            buff.append(XPontusWindow.getInstance()
                                     .getI18nMessage("msg.validating"));

            XPontusWindow.getInstance()
                         .append(ConsoleOutputWindow.MESSAGES_WINDOW,
                buff.toString());
            XPontusWindow.getInstance().getStatusBar()
                         .setOperationMessage(buff.toString());
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://xml.org/sax/features/namespaces", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema",
                true);
            parser.setFeature("http://apache.org/xml/features/validation/dynamic",
                true);

            javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                        .getCurrentEditor();
            byte[] bt = edit.getText().getBytes();
            java.io.InputStream is = new java.io.ByteArrayInputStream(bt);
            InputStreamReader isr = new InputStreamReader(is);
            parser.parse(new InputSource(isr));
            isr.close();
            is.close();
            XPontusWindow.getInstance()
                         .append(ConsoleOutputWindow.MESSAGES_WINDOW,
                XPontusWindow.getInstance()
                             .getI18nMessage("msg.validationFinished"));
            XPontusWindow.getInstance().getStatusBar()
                         .setOperationMessage(XPontusWindow.getInstance()
                                                           .getI18nMessage("msg.validDocument"));
          }
        catch (Exception e)
          {
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage("Error see messages window!");
            XPontusWindow.getInstance()
                         .append(ConsoleOutputWindow.ERRORS_WINDOW,
                e.getMessage());
          }
      }
  }
