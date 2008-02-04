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
package net.sf.xpontus.controllers.impl;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.model.DocumentationModel;
import net.sf.xpontus.modules.gui.components.DocumentationView;
import net.sf.xpontus.plugins.gendoc.DocConfiguration;
import net.sf.xpontus.plugins.gendoc.IDocumentationPluginIF;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.io.File;

import java.util.Hashtable;

import javax.swing.JFileChooser;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DocumentationControllerImpl {
    public static final String INPUT_METHOD = "selectInput";
    public static final String OUTPUT_METHOD = "selectOutput";
    public static final String CSS_METHOD = "selectCss";
    public static final String CLOSE_METHOD = "closeWindow";
    public static final String HANDLE_METHOD = "handle";
    private DocumentationView view;
    private JFileChooser chooser;

    public DocumentationControllerImpl(DocumentationView view) {
        this();
        this.view = view;
    }

    /**
     *
     */
    public DocumentationControllerImpl() {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.setMultiSelectionEnabled(false);
    }

    /**
     *
     */
    public void selectInput() {
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (isOpenedDialog()) {
            view.getModel().setInput(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     *
     */
    public void selectOutput() {
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

        if (isOpenedDialog()) {
            view.getModel()
                .setOutput(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void selectCss() {
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

        if (isOpenedDialog()) {
            view.getModel().setCss(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    /**
     *
     * @return
     */
    public boolean isOpenedDialog() {
        int answer = chooser.showOpenDialog(XPontusComponentsUtils.getTopComponent()
                                                                  .getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION) {
            return true;
        }

        return false;
    }

    /**
     *
     * @return
     */
    public DocumentationView getView() {
        return view;
    }

    /**
     *
     * @param view
     */
    public void setView(DocumentationView view) {
        this.view = view;
    }

    /**
     *
     */
    public void handle() {
        String type = view.getModel().getType();

        if ((type == null) || type.trim().equals("")) {
            XPontusComponentsUtils.showErrorMessage(
                "Please install some plugins");

            return;
        }

        Hashtable t = (Hashtable) DocConfiguration.getInstane().getEngines()
                                                  .get(type);
        ClassLoader loader = (ClassLoader) t.get(XPontusConstantsIF.CLASS_LOADER);
        String classname = t.get(XPontusConstantsIF.OBJECT_CLASSNAME).toString();

        try {
            IDocumentationPluginIF p = (IDocumentationPluginIF) Class.forName(classname,
                    true, loader).newInstance();
            p.handle(view.getModel());
        } catch (Exception e) {
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
