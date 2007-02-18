/*
 * ValidateXmlAction.java
 *
 * Created on 2 octobre 2005, 16:53
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
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

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.constants.GrammarCachingPoolProvider;
import net.sf.xpontus.core.controller.actions.ThreadedAction;
import net.sf.xpontus.utils.EncodingHelper;
import net.sf.xpontus.utils.MsgUtils;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.util.*;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.grammars.Grammar;
import org.apache.xerces.xni.grammars.XMLGrammarDescription;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

import org.syntax.jedit.SyntaxDocument;

import org.xml.sax.InputSource;

import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * Action to validate an xml document
 * @author Yves Zoundi
 */
public class ValidateXmlAction extends ThreadedAction {
    private SAXParser parser;

    /** Creates a new instance of ValidateXmlAction */
    public ValidateXmlAction() {
        GrammarCachingPoolProvider provider = GrammarCachingPoolProvider.getInstance();

        parser = new SAXParser(provider.getSymbolTable(),
                provider.getGrammarPool());
    }

    public void execute() {
        MsgUtils _msg = MsgUtils.getInstance();

        try {
            StringBuffer buff = new StringBuffer();
            buff.append("\n");
            buff.append(_msg.getString("msg.validating"));

            XPontusWindow.getInstance().append(buff.toString());
            XPontusWindow.getInstance().getStatusBar()
                         .setOperationMessage(buff.toString());
            parser.setFeature("http://xml.org/sax/features/use-entity-resolver2",
                true);
            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://xml.org/sax/features/namespaces", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema",
                true);
            parser.setFeature("http://apache.org/xml/features/honour-all-schemaLocations",
                true);
            parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking",
                true);
            parser.setFeature("http://apache.org/xml/features/validation/dynamic",
                true);

            javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                        .getCurrentEditor();

            byte[] bt = edit.getText().getBytes();
            java.io.InputStream is = new java.io.ByteArrayInputStream(bt);
            parser.parse(new InputSource(EncodingHelper.getReader(is)));
            XPontusWindow.getInstance()
                         .append(_msg.getString("msg.validationFinished"));
            XPontusWindow.getInstance().getStatusBar()
                         .setOperationMessage(_msg.getString(
                    "msg.validDocument"));
        } catch (Exception e) {
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage("Error see messages window!");
            XPontusWindow.getInstance().append(e.getMessage());
        }
    }
}
