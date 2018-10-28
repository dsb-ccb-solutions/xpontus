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
package net.sf.xpontus.actions.impl;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.DocumentContainerChangeEvent;
import net.sf.xpontus.utils.GrammarPoolHolder;

import org.apache.commons.lang.text.StrBuilder;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.util.SymbolTable;
import org.apache.xerces.xni.grammars.XMLGrammarPool;

import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.awt.Toolkit;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.swing.text.JTextComponent;


/**
 * Single document validation
 * @author Yves Zoundi
 * @version 0.0.1
 */
public class SimpleValidationActionImpl
    extends XPontusDocumentAwareThreadedActionImpl
{
    private static final long serialVersionUID = -6516772117153749764L;
    public static final String BEAN_ALIAS = "action.validate";
    private static final String FEATURE_ENTITY_RESOLVER2 = "http://xml.org/sax/features/use-entity-resolver2";
    private static final String FEATURE_VALIDATION_SCHEMA = "http://apache.org/xml/features/validation/schema";
    private static final String FEATURE_VALIDATION = "http://xml.org/sax/features/validation";
    private static final String FEATURE_NAMESPACES = "http://xml.org/sax/features/namespaces";
    private static final String FEATURE_HONOUR_SCHEMALOCATIONS = "http://apache.org/xml/features/honour-all-schemaLocations";
    private static final String FEATURE_VALIDATION_FULLSCHEMA_CHECK = "http://apache.org/xml/features/validation/schema-full-checking";
    private static final String FEATURE_VALIDATION_DYNAMIC = "http://apache.org/xml/features/validation/dynamic";
    private SAXParser saxParser;
    private ValidationHandler validationHandler;

    /**
     *
     */
    public SimpleValidationActionImpl()
    {
    }

    public void run()
    {
        try
        {
            JTextComponent textComponent = DefaultXPontusWindowImpl.getInstance()
                                                                   .getDocumentTabContainer()
                                                                   .getCurrentEditor();

            IDocumentContainer container = (IDocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                        .getDocumentTabContainer()
                                                                                        .getCurrentDockable();
            container.getStatusBar().setMessage("Validating document...");

            InputStream is = new ByteArrayInputStream(textComponent.getText()
                                                                   .getBytes());

            if (saxParser == null)
            {
                SymbolTable table = GrammarPoolHolder.getInstance()
                                                     .getSymbolTable();
                XMLGrammarPool pool = GrammarPoolHolder.getInstance()
                                                       .getGrammarPool();
                saxParser = new SAXParser(table, pool);

                saxParser.setFeature(FEATURE_ENTITY_RESOLVER2, true);
                saxParser.setFeature(FEATURE_VALIDATION_SCHEMA, true);
                saxParser.setFeature(FEATURE_VALIDATION, true);
                saxParser.setFeature(FEATURE_NAMESPACES, true);
                saxParser.setFeature(FEATURE_HONOUR_SCHEMALOCATIONS, true);
                saxParser.setFeature(FEATURE_VALIDATION_FULLSCHEMA_CHECK, true);
                saxParser.setFeature(FEATURE_VALIDATION_DYNAMIC, true);

                validationHandler = new ValidationHandler();
                saxParser.setErrorHandler(validationHandler);
            }

            validationHandler.reset();

            CharsetDetector detector = new CharsetDetector();
            detector.setText(is);

            saxParser.parse(new InputSource(detector.detect().getReader()));

            is.close();

            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();
            OutputDockable odk = (OutputDockable) console.getDockables()
                                                         .get(ConsoleOutputWindow.MESSAGES_WINDOW);

            if (validationHandler.getErrors().length() == 0)
            {
                container.getStatusBar().setMessage("Document is valid!");
                odk.println("Document is valid!");
            }
            else
            {
                container.getStatusBar().setMessage("Document is invalid!");
                odk.println("Document is invalid! " +
                    validationHandler.getErrors(), OutputDockable.RED_STYLE);
            }
        }
        catch (Exception e)
        {
            StrBuilder details = new StrBuilder();

            if (e instanceof SAXParseException)
            {
                SAXParseException sxpe = (SAXParseException) e;
                details.append("Error occurred around line:")
                       .append(sxpe.getLineNumber()).append(",column:")
                       .append(sxpe.getColumnNumber()).appendNewLine();
            }

            details.append(e.getMessage());

            ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                  .getConsole();
            OutputDockable odk = (OutputDockable) console.getDockables()
                                                         .get(ConsoleOutputWindow.MESSAGES_WINDOW);
            IDocumentContainer container = (IDocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                        .getDocumentTabContainer()
                                                                                        .getCurrentDockable();
            container.getStatusBar().setMessage("Document is invalid!");

            odk.println(details.toString(), OutputDockable.RED_STYLE);
        }
        finally
        {
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void onNotify(DocumentContainerChangeEvent evt)
    {
        if (evt.getSource() == null)
        {
            setEnabled(false);
        }
        else
        {
            setEnabled(true);
        }
    }

    public static class ValidationHandler implements ErrorHandler
    {
        private StrBuilder errors = new StrBuilder();

        public String getErrors()
        {
            return errors.toString();
        }

        public void reset()
        {
            errors = new StrBuilder();
        }

        public void warning(SAXParseException err) throws SAXException
        {
            reportError(err);
        }

        private void reportError(SAXParseException err)
        {
            errors.appendNewLine().append("Error occurred around line:")
                  .append(err.getLineNumber()).append(",column:")
                  .append(err.getColumnNumber()).appendNewLine()
                  .append(err.getMessage()).appendNewLine();
        }

        public void error(SAXParseException err) throws SAXException
        {
            reportError(err);
        }

        public void fatalError(SAXParseException err) throws SAXException
        {
            reportError(err);
        }
    }
}
