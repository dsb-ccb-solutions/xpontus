/*
 * CheckXMLActionImpl.java
 *
 * Created on 19-Aug-2007, 9:34:47 AM 
 *
 * Copyright (C) 2005-2008 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 */
package net.sf.xpontus.actions.impl;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;





import javax.swing.text.JTextComponent;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DocumentContainer;
import net.sf.xpontus.modules.gui.components.OutputDockable;


/**
 * Check if the XML document is well-formed
 * @author Yves Zoundi
 */
public class CheckXMLActionImpl extends XPontusThreadedActionImpl {
    
    public static final String BEAN_ALIAS = "action.checkxml";
    
    public CheckXMLActionImpl() {
    }

     public void run() {
        try {
            JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                         .getDocumentTabContainer()
                                                         .getCurrentEditor();

            InputStream is = new ByteArrayInputStream(jtc.getText().getBytes());

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();

            dbf.setValidating(false);

            DocumentBuilder db = dbf.newDocumentBuilder();

            db.parse(is);

            is.close();

            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();
            OutputDockable odk = (OutputDockable) console.getDockables()
                                                         .get(ConsoleOutputWindow.MESSAGES_WINDOW);
            DocumentContainer container = (DocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                      .getDocumentTabContainer()
                                                                                      .getCurrentDockable();
            container.getStatusBar().setMessage("Document  well formed");
            odk.println("Document  well formed");
        } catch (Exception e) {
            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();
            OutputDockable odk = (OutputDockable) console.getDockables()
                                                         .get(ConsoleOutputWindow.MESSAGES_WINDOW);
            DocumentContainer container = (DocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                      .getDocumentTabContainer()
                                                                                      .getCurrentDockable();
            container.getStatusBar().setMessage("Document not well formed");
            odk.println(e.getMessage());
        }
    }
}
