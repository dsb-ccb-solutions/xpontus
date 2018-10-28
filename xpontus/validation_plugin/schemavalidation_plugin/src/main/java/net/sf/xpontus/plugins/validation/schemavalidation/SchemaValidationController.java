/*
 *
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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
package net.sf.xpontus.plugins.validation.schemavalidation;

import com.sun.msv.verifier.ValidityViolation;

import java.awt.Toolkit;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.iso_relax.verifier.Schema;
import org.iso_relax.verifier.Verifier;
import org.iso_relax.verifier.VerifierFactory;

import org.xml.sax.InputSource;

import java.io.File;
import java.io.StringReader;

import javax.swing.JFileChooser;
import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import org.apache.commons.lang.text.StrBuilder;

/**
 *
 * @author Yves Zoundi
 */
public class SchemaValidationController {

    public static final String INPUT_METHOD = "selectInput";
    public static final String SCHEMA_METHOD = "selectSchema";
    public static final String HANDLE_METHOD = "handle";
    public static final String CLOSE_METHOD = "closeWindow";
    private SchemaValidationView view;
    private JFileChooser chooser;

    public SchemaValidationController(SchemaValidationView view) {
        this();
        this.view = view;
    }

    /**
     *
     */
    public SchemaValidationController() {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        chooser.setMultiSelectionEnabled(false);
    }

    /**
     *
     */
    public void selectInput() {
        int answer = chooser.showOpenDialog(XPontusComponentsUtils.getTopComponent().getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION) {
            view.getModel().setInput(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     *
     */
    public void selectSchema() {
        int answer = chooser.showOpenDialog(XPontusComponentsUtils.getTopComponent().getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION) {
            view.getModel().setSchema(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     *
     * @return
     */
    public SchemaValidationView getView() {
        return view;
    }

    /**
     *
     * @param view
     */
    public void setView(SchemaValidationView view) {
        this.view = view;
    }

    /**
     *
     */
    public void handle() {
        if (isValid()) {

            if (view.getModel().isUseCurrentDocument()) {
                if (DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentEditor() == null) {



                    XPontusComponentsUtils.showErrorMessage("You choose to perform validation against the opened document but no document seems to be opened...");



                    return;
                }
            }

            closeWindow();

            DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("Starting validation against schema...");

            ConsoleOutputWindow outputWindow = DefaultXPontusWindowImpl.getInstance().getConsole();
            OutputDockable console = (OutputDockable) outputWindow.getDockables().get(ConsoleOutputWindow.MESSAGES_WINDOW);


            try {
                VerifierFactory factory = new com.sun.msv.verifier.jarv.TheFactoryImpl();

                // compile a schema
                Schema schema = factory.compileSchema(new File(view.getModel().getSchema()));

                InputSource src = null;

                // parse the document
                if (view.getModel().isUseCurrentDocument()) {
                    String texte = DefaultXPontusWindowImpl.getInstance().getDocumentTabContainer().getCurrentEditor().getText();
                    StringReader sr = new StringReader(texte);
                    src = new InputSource(sr);
                } else {
                    src = new InputSource(new java.io.FileInputStream(
                            view.getModel().getInput()));
                }

                Verifier verifier = schema.newVerifier();
                boolean b = verifier.verify(src);

                String mValid = "The document is valid";

                if (!b) {
                    mValid = "The document is invalid";
                }

                console.println(mValid);
                DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage(mValid);

            } catch (ValidityViolation e) {
                StrBuilder msg = new StrBuilder();
                msg.append("The document is invalid!");
                msg.appendNewLine();
                msg.append("Error around line : " + e.getLineNumber() + ",column:" + e.getColumnNumber());
                msg.appendNewLine();
                msg.append(e.getMessage());
                console.println(msg.toString(), OutputDockable.RED_STYLE);
            } catch (Exception e) {
                console.println(e.getMessage(), OutputDockable.RED_STYLE);
            }
            finally{
                Toolkit.getDefaultToolkit().beep();
                DefaultXPontusWindowImpl.getInstance().getStatusBar().setMessage("Validation finished! (see the messages window)");
            }
        }
    }

    /**
     *
     */
    public void closeWindow() {
        view.setVisible(false);
    }

    /**
     *
     * @return
     */
    public boolean isValid() {
        SchemaValidationModel model = view.getModel();

        String input = model.getInput();
        String schema = model.getSchema();
        System.out.println("use current document:" +
                (model.isUseCurrentDocument()));

        StringBuffer sb = new StringBuffer();

        if (!model.isUseCurrentDocument() &&
                (input.trim().equals("") || !new File(input).exists())) {
            sb.append("* The input file is missing or doesn't exist\n");
        }

        if (schema.trim().equals("") || !new File(schema).exists()) {
            sb.append("* The schema file is missing or doesn't exist");
        }

        if (sb.length() > 0) {
            XPontusComponentsUtils.showErrorMessage(sb.toString());

            return false;
        }

        return true;
    }
}
