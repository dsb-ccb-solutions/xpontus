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

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;

import org.apache.commons.io.IOUtils;

import org.apache.xerces.parsers.SAXParser;

import org.xml.sax.InputSource;

import java.io.Reader;

import javax.swing.text.JTextComponent;


/**
 * Check if the XML document is well-formed
 * @author Yves Zoundi
 */
public class CheckXMLActionImpl extends XPontusThreadedActionImpl {
    public CheckXMLActionImpl() {
    }

    public void run() {
        DocumentTabContainer dtc = DefaultXPontusWindowImpl.getInstance()
                                                           .getDocumentTabContainer();
        JTextComponent jtc = dtc.getCurrentEditor();

        try {
            // create and configure a new sax parser
            SAXParser parser = new SAXParser();
            
            // encoding detection of the input text
            CharsetDetector detect = new CharsetDetector();
            
            // deprecated method but whatever
            detect.setText(IOUtils.toByteArray(jtc.getText()));

            Reader reader = detect.detect().getReader();

            // parse the XML document
            parser.parse(new InputSource(reader));

            DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                    .setMessage("XML document is well formed!");
        } catch (Exception e) {
            DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                    .setMessage("Error checking XML...!");
            getLogger().error(e.getMessage());
        }
    }
}
