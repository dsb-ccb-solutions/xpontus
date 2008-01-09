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

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.actions.impl.DefaultDocumentAwareActionImpl;
import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentContainer;
import net.sf.xpontus.modules.gui.components.OutputDockable;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import java.net.URL;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.1
 */
public class SimpleValidationAction extends DefaultDocumentAwareActionImpl {
    private SAXParser parser;
    private ValidationHandler handler;

    /**
     *
     */
    public SimpleValidationAction() {
        setName("Validate XML");
        setDescription("XML Validation");

        URL url = getClass().getResource("validate16.gif");
        this.putValue(Action.SMALL_ICON, new ImageIcon(url));
    }

    public void run() {
        try {
            JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                         .getDocumentTabContainer()
                                                         .getCurrentEditor();

            InputStream is = new ByteArrayInputStream(jtc.getText().getBytes());

            if (parser == null) {
                XMLGrammarPool pool = new XMLGrammarPoolImpl();
                SymbolTable table = new SymbolTable();
                parser = new SAXParser(table, pool);
                parser.setFeature("http://xml.org/sax/features/use-entity-resolver2",
                    true);
                parser.setFeature("http://apache.org/xml/features/validation/schema",
                    true);
                parser.setFeature("http://xml.org/sax/features/validation", true);
                parser.setFeature("http://xml.org/sax/features/namespaces", true);
                parser.setFeature("http://apache.org/xml/features/honour-all-schemaLocations",
                    true);
                parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking",
                    true);
                parser.setFeature("http://apache.org/xml/features/validation/dynamic",
                    true);

                handler = new ValidationHandler();
                parser.setErrorHandler(handler);
            }

            handler.reset();

            CharsetDetector detector = new CharsetDetector();
            detector.setText(is);

            parser.parse(new InputSource(detector.detect().getReader()));

            is.close();

            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();
            OutputDockable odk = (OutputDockable) console.getDockables()
                                                         .get(ConsoleOutputWindow.MESSAGES_WINDOW);
            DocumentContainer container = (DocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                      .getDocumentTabContainer()
                                                                                      .getCurrentDockable();

            if (handler.getErrors().length() == 0) {
                container.getStatusBar().setMessage("Document is valid!");
                odk.println("Document is valid!");
            } else {
                container.getStatusBar().setMessage("Document is invalid!");
                odk.println("Document is invalid! " + handler.getErrors(),
                    OutputDockable.RED_STYLE);
            }
        } catch (Exception e) {
            StringBuffer details = new StringBuffer();

            if (e instanceof SAXParseException) {
                SAXParseException err = (SAXParseException) e;
                int column = err.getColumnNumber();
                int line = err.getLineNumber();
                details.append("Error occurred around line:" + line +
                    ",column:" + column);
            }

            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();
            OutputDockable odk = (OutputDockable) console.getDockables()
                                                         .get(ConsoleOutputWindow.MESSAGES_WINDOW);
            DocumentContainer container = (DocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                      .getDocumentTabContainer()
                                                                                      .getCurrentDockable();
            container.getStatusBar().setMessage("Document is invalid!");

            String error = "" + details.toString() + "\n";

            odk.println(error + e.getMessage(), OutputDockable.RED_STYLE);
        }
    }

    public static class ValidationHandler implements ErrorHandler {
        private StringBuffer errors = new StringBuffer();

        public String getErrors() {
            return errors.toString();
        }

        public void reset() {
            errors = new StringBuffer();
        }

        public void warning(SAXParseException err) throws SAXException {
            reportError(err);
        }

        private void reportError(SAXParseException err) {
            int column = err.getColumnNumber();
            int line = err.getLineNumber();
            errors.append("\nError occurred around line:" + line + ",column:" +
                column);
            errors.append("\n" + err.getMessage() + "\n");
        }

        public void error(SAXParseException err) throws SAXException {
            reportError(err);
        }

        public void fatalError(SAXParseException err) throws SAXException {
            reportError(err);
        }
    }
}
