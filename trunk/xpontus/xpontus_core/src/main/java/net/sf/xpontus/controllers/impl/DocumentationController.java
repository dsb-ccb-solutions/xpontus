/*
 * DocumentationController.java
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
package net.sf.xpontus.controllers.impl;

import com.sun.java.help.impl.SwingWorker;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.model.DocumentationModel;
import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentationView;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.plugins.gendoc.DocConfiguration;
import net.sf.xpontus.plugins.gendoc.IDocumentationPluginIF;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Toolkit;

import java.io.File;

import java.util.Hashtable;

import javax.swing.JFileChooser;


/**
 * @version 0.0.1
 * The controller for the documentation generator dialog
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DocumentationController {
    /**
     * The method name to select an input file
     */
    public static final String INPUT_METHOD = "selectInput";

    /**
     * The method name to select an output directory
     */
    public static final String OUTPUT_METHOD = "selectOutput";

    /**
     * The method name to select a css stylesheet
     */
    public static final String CSS_METHOD = "selectCss";

    /**
     * The method name to close the documentation generator dialog
     */
    public static final String CLOSE_METHOD = "closeWindow";

    /**
     * The method name to generate the documentation
     */
    public static final String HANDLE_METHOD = "handle";

    // private members
    private DocumentationView view;
    private JFileChooser chooser;

    /**
     * Create a controller for the specified documentation generator dialog
     * @param view The documentation generator dialog
     */
    public DocumentationController(DocumentationView view) {
        this();
        this.view = view;
    }

    /**
     * Default constructor
     */
    public DocumentationController() {
        chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(false);
    }

    /**
     * Select a schema
     */
    public void selectInput() {
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int answer = chooser.showOpenDialog(XPontusComponentsUtils.getTopComponent()
                                                                  .getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION) {
            view.getModel().setInput(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Select a output directory
     */
    public void selectOutput() {
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        int answer = chooser.showOpenDialog(XPontusComponentsUtils.getTopComponent()
                                                                  .getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION) {
            view.getModel().setOutput(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Select a css stylesheet
     */
    public void selectCss() {
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        int answer = chooser.showOpenDialog(XPontusComponentsUtils.getTopComponent()
                                                                  .getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION) {
            view.getModel().setCss(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     * Returns the documentation generator dialog of this controller
     * @return The documentation generator dialog of this controller
     */
    public DocumentationView getView() {
        return view;
    }

    /**
     * Sets the documentation generator dialog of this controller
     * @param view The documentation generator dialog
     */
    public void setView(DocumentationView view) {
        this.view = view;
    }

    /**
     * Generate the documentation for a specified schema
     */
    public void handle() {
        SwingWorker sw = new SwingWorker() {
                public Object construct() {
                    String type = view.getModel().getType();

                    ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                                          .getConsole();
                    console.setFocus(MessagesWindowDockable.DOCKABLE_ID);

                    MessagesWindowDockable mwd = (MessagesWindowDockable) console.getDockableById(MessagesWindowDockable.DOCKABLE_ID);

                    if ((type == null) || type.trim().equals("")) {
                        XPontusComponentsUtils.showErrorMessage(
                            "Please install some plugins");

                        return null;
                    }

                    Hashtable t = (Hashtable) DocConfiguration.getInstane()
                                                              .getEngines().get(type);
                    ClassLoader loader = (ClassLoader) t.get(XPontusConstantsIF.CLASS_LOADER);
                    String classname = t.get(XPontusConstantsIF.OBJECT_CLASSNAME)
                                        .toString();

                    try {
                        IDocumentationPluginIF p = (IDocumentationPluginIF) Class.forName(classname,
                                true, loader).newInstance();
                        p.handle(view.getModel());

                        mwd.println("Documentation generated successfully!");
                    } catch (Exception e) {
                        mwd.println(e.getMessage(), OutputDockable.RED_STYLE);
                    } finally {
                        console.setFocus(MessagesWindowDockable.DOCKABLE_ID);
                        Toolkit.getDefaultToolkit().beep();
                        view.setVisible(false);
                    }

                    return null;
                }
            };

        sw.start();
    }

    /**
     * Close the documentation generator dialog
     */
    public void closeWindow() {
        view.setVisible(false);
    }

    /**
     * Validation of user input
     * @return Whether or not all the information has been filled to generate the documentation
     */
    public boolean isValid() {
        DocumentationModel model = view.getModel();

        String input = model.getInput();
        String output = model.getOutput();

        StringBuffer sb = new StringBuffer();

        if (input.trim().equals("") || !new File(input).exists()) {
            sb.append("* The input directory is missing or doesn't exist");
        }

        if (output.trim().equals("") || !new File(input).exists()) {
            sb.append("* The output directory is missing or doesn't exist");
        }

        if (sb.length() > 0) {
            XPontusComponentsUtils.showErrorMessage(sb.toString());

            return false;
        }

        return true;
    }
}
