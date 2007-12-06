/*
 * SimpleValidationAction.java
 *
 * Created on 2007-07-26, 14:19:47 
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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
 */
package net.sf.xpontus.plugins.validation.simplexmlvalidation;


import net.sf.xpontus.actions.impl.XPontusThreadedActionImpl;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.text.JTextComponent;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.1
 */
public class SimpleValidationAction extends XPontusThreadedActionImpl {
    public SimpleValidationAction() {
        setName("Validate XML");
        setDescription("XML Validation");
    }

    public void run() {
        try {
            JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                         .getDocumentTabContainer()
                                                         .getCurrentEditor();

            InputStream is = new ByteArrayInputStream(jtc.getText().getBytes());

            DocumentBuilder db = DocumentBuilderFactory.newInstance()
                                                       .newDocumentBuilder();

            db.parse(is);

            is.close();
            
            DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("The document is valid!!");
        } catch (Exception ex) {
             DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage(ex.getMessage());
        }
    }
}
