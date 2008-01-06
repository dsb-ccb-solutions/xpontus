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

import com.ibm.icu.text.CharsetDetector;

import java.io.Reader;
import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentContainer;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.DocumentContainerChangeEvent;

import org.apache.xerces.parsers.SAXParser;

import org.xml.sax.InputSource;

import javax.swing.text.JTextComponent;
import org.xml.sax.SAXParseException;


/**
 * Check if the XML document is well-formed
 * @author Yves Zoundi
 */
public class CheckXMLActionImpl extends XPontusDocumentAwareThreadedActionImpl {
    public static final String BEAN_ALIAS = "action.checkxml";

    public CheckXMLActionImpl() {
    }

    public void run() {
        JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                     .getDocumentTabContainer()
                                                     .getCurrentEditor();

        try {
            //            ((PlainDocument) jtc.getDocument()).readLock();

            //            parser.setFeature("http://xml.org/sax/features/namespaces", true);
            CharsetDetector d = new CharsetDetector();
            d.setText(jtc.getText().getBytes());

            SAXParser parser = new SAXParser();

            parser.setFeature("http://xml.org/sax/features/validation", false);

            Reader m_reader = d.detect().getReader();
            
            parser.parse(new InputSource(m_reader));
            
            m_reader.close();

            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();
            OutputDockable odk = (OutputDockable) console.getDockables()
                                                         .get(ConsoleOutputWindow.MESSAGES_WINDOW);
            DocumentContainer container = (DocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                      .getDocumentTabContainer()
                                                                                      .getCurrentDockable();
            container.getStatusBar().setMessage("Document well formed");
            odk.println("Document well formed");
        } catch (Exception e) {
            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();
            OutputDockable odk = (OutputDockable) console.getDockables()
                                                         .get(ConsoleOutputWindow.MESSAGES_WINDOW);
            DocumentContainer container = (DocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                      .getDocumentTabContainer()
                                                                                      .getCurrentDockable();
 

            StringBuffer err = new StringBuffer();
            
            if(e instanceof SAXParseException){
                SAXParseException spe = (SAXParseException) e;
                err.append( "Line:" + spe.getLineNumber() + ",Column:" + spe.getColumnNumber() + "\n");
            }
            container.getStatusBar().setMessage("Document not well formed");
            odk.println("Error:\n" + err.toString() + e.getMessage());
        } finally {
            //            ((PlainDocument) jtc.getDocument()).readUnlock();
        }
    }

    public void onNotify(DocumentContainerChangeEvent evt) {
        setEnabled(evt != null);
    }
}
