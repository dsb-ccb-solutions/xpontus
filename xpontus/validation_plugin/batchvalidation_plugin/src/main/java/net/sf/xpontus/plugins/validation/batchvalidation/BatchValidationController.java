/*
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
package net.sf.xpontus.plugins.validation.batchvalidation;

import com.ibm.icu.text.CharsetDetector;


import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileContent;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.FileType;
import org.apache.commons.vfs.VFS;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XIncludeAwareParserConfiguration;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLParserConfiguration;

import org.xml.sax.InputSource;

import java.awt.Component;
import java.awt.Toolkit;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.InputStream;
import java.io.Reader;

import java.util.Arrays;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;


/**
 * Controller for the Batch Validation dialog
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.1
 */
public class BatchValidationController {
    /**
    * Method to validate files
    */
    public static final String VALIDATE_FILES_METHOD = "validateFiles";

    /**
     * Method to add a path to validate
     */
    public static final String ADD_FILE_METHOD = "addFile";

    /**
     * Method to remove a file/directory from the path list
     */
    public static final String REMOVE_FILE_METHOD = "removeFile";

    /**
     * Method to register an XML file extension
     */
    public static final String ADD_EXTENSION_METHOD = "addExtension";

    /**
     * Method to remove a file extension to consider
     */
    public static final String REMOVE_EXTENSION_METHOD = "removeExtension";

    /**
     * Method name to close the validation dialog
     */
    public static final String CLOSE_WINDOW_METHOD = "closeWindow";

    // private members
    private Log log = LogFactory.getLog(BatchValidationController.class);
    private BatchValidationDialogView view;
    private SAXParser parser;
    private BatchValidationErrorHandler errorHandler;
    private JFileChooser chooser;

    /**
     * Default constructor
     */
    public BatchValidationController() {
        chooser = new JFileChooser();
        chooser.setFileHidingEnabled(true);
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    /**
     * Set the validation dialog of this controller
     * @param aThis a validation dialog
     */
    public BatchValidationController(BatchValidationDialogView aThis) {
        this();
        this.view = aThis;
    }

    /**
     * Returns the view of this controller
     * @return The view of this controller
     */
    public BatchValidationDialogView getView() {
        return view;
    }

    /**
     * Add a path to validate
     */
    public void addFile() {
        int answer = chooser.showOpenDialog(view);

        if (answer == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();

            for (File f : files) {
                try {
                    FileObject fo = VFS.getManager().toFileObject(f);

                    if (!pathAdded(fo)) {
                        JList li = view.getPathList();
                        DefaultComboBoxModel m = (DefaultComboBoxModel) li.getModel();
                        m.addElement(fo);
                    }
                } catch (Exception err) {
                }
            }
        }
    }

    /**
     * Remove a path from the path list
     */
    public void removeFile() {
        JList li = view.getPathList();
        int index = li.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(DefaultXPontusWindowImpl.getInstance()
                                                                  .getDisplayComponent(),
                "Please select a path", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel m = (DefaultComboBoxModel) li.getModel();
            m.removeElementAt(index);
        }
    }

    /**
     * Set the validation dialog of this view
     * @param view The validation dialog
     */
    public void setView(BatchValidationDialogView view) {
        this.view = view;
    }

    /**
     * Add a file extension
     */
    public void addExtension() {
        JList li = view.getExtensionsList();
        DefaultComboBoxModel m = (DefaultComboBoxModel) li.getModel();
        String m_extension = view.getExtensionTF().getText();

        if (m_extension.trim().equals("")) {
        } else if (extensionExists(m_extension)) {
            JOptionPane.showMessageDialog(DefaultXPontusWindowImpl.getInstance()
                                                                  .getDisplayComponent(),
                "The extension already exists", "Error",
                JOptionPane.ERROR_MESSAGE);
        } else {
            m.addElement(m_extension);
        }
    }

    /**
     * Remove a file extension
     */
    public void removeExtension() {
        JList li = view.getExtensionsList();
        int index = li.getSelectedIndex();

        if (index == -1) {
            JOptionPane.showMessageDialog(DefaultXPontusWindowImpl.getInstance()
                                                                  .getDisplayComponent(),
                "Please select an extension from the list", "Error",
                JOptionPane.ERROR_MESSAGE);
        } else {
            DefaultComboBoxModel m = (DefaultComboBoxModel) li.getModel();
            m.removeElementAt(index);
        }
    }

    /**
     * Validate the selected paths
     */
    public void validateFiles() {
        Thread worker = new Thread() {
                public void run() {
                    view.enableControlButtons(false);
                    doValidateFiles();
                }
            };

        worker.setPriority(Thread.MIN_PRIORITY);
        worker.start();
    }

    /**
     * Validate the selected files
     */
    public void doValidateFiles() {
        JList li = view.getPathList();
        final int paths = li.getModel().getSize();
        ListModel m = li.getModel();

        boolean recurse = view.getRecurseOption().isSelected();

        ListModel extensionListModel = view.getExtensionsList().getModel();

        final int nbExtensions = extensionListModel.getSize();

        String[] extensions = new String[nbExtensions];

        for (int i = 0; i < nbExtensions; i++) {
            extensions[i] = (extensionListModel.getElementAt(i).toString());
        }

        if (nbExtensions == 0) {
            Toolkit.getDefaultToolkit().beep();
            XPontusComponentsUtils.showErrorMessage(
                "Please add some file extensions");

            view.enableControlButtons(true);

            return;
        }

        WildcardFileSelector filterSelector = new WildcardFileSelector(recurse,
                extensions);

        List<FileObject> files = new Vector<FileObject>();

        for (int i = 0; i < paths; i++) {
            FileObject fo = (FileObject) m.getElementAt(i);

            try {
                if (fo.getType().equals(FileType.FOLDER)) {
                    try {
                        FileObject[] fl = fo.findFiles(filterSelector);
                        files.addAll(Arrays.asList(fl));
                    } catch (Exception err) {
                        log.error("Error getting file list information from " +
                            fo.getName().getURI());
                        log.error(err.getMessage());
                    }
                } else {
                    files.add(fo);
                }
            } catch (Exception err) {
            }
        }

        final int nbFiles = files.size();

        if (nbFiles == 0) {
            Toolkit.getDefaultToolkit().beep();
            XPontusComponentsUtils.showErrorMessage("No files found!");
            view.enableControlButtons(true);

            return;
        }

        view.setVisible(false);

        log.info("There is(are) " + nbFiles + " to validate");

        Component mainWindow = DefaultXPontusWindowImpl.getInstance()
                                                       .getDisplayComponent();

         
        ProgressMonitorHandler pmh = new ProgressMonitorHandler(0, nbFiles);
        DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                .addOperationComponent(pmh);

        ConsoleOutputWindow outputWindow = DefaultXPontusWindowImpl.getInstance()
                                                                   .getConsole();
        OutputDockable console = (OutputDockable) outputWindow.getDockables()
                                                              .get(ConsoleOutputWindow.MESSAGES_WINDOW);

        if (parser == null) {
            try {
                XMLParserConfiguration config = new XIncludeAwareParserConfiguration();
                XMLGrammarPool grammarPool = new XMLGrammarPoolImpl();
                final String GRAMMAR_POOL_PROPERTY = "http://apache.org/xml/properties/internal/grammar-pool";
                config.setProperty(GRAMMAR_POOL_PROPERTY, grammarPool);

                parser = new SAXParser(config);
                errorHandler = new BatchValidationErrorHandler();

                parser.setFeature("http://xml.org/sax/features/validation", true);
                parser.setFeature("http://xml.org/sax/features/namespaces", true);
                parser.setFeature("http://apache.org/xml/features/validation/schema",
                    true);
                parser.setFeature("http://apache.org/xml/features/honour-all-schemaLocations",
                    true);
                parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking",
                    true);
                parser.setErrorHandler(errorHandler);
                parser.setFeature("http://apache.org/xml/features/validation/dynamic",
                    true);
            } catch (Exception err) {
                log.fatal(err.getMessage());
            }
        }

        errorHandler.reset();

        CharsetDetector detector = new CharsetDetector();

        for (int i = 0; i < files.size(); i++) {
            if (pmh.isCanceled()) {
                Toolkit.getDefaultToolkit().beep();
                view.enableControlButtons(true);

                int nbErrors = errorHandler.getNumberOfErrors();

                String strMessage = "There is(are) " + nbErrors +
                    " errors(s). Please consult the messages window";

                console.println("Batch Validation report",
                    OutputDockable.BLUE_STYLE);

                if (nbErrors == 0) {
                    console.println("All files are valid!");
                    DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                            .setMessage("All files are valid!");
                } else {
                    console.println("There is(are) " + nbErrors +
                        " errors(s)");
                    console.println(errorHandler.getErrorMessages(),
                        OutputDockable.RED_STYLE);

                    DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                            .setMessage(strMessage);
                }

                outputWindow.setFocus(MessagesWindowDockable.DOCKABLE_ID);

                return;
            }

            FileObject m_file = files.get(i);

            final int pos = i;

            pmh.updateProgress(pos + 1);

            try {
                errorHandler.setCurrentFile(m_file);

                FileContent fileContent = m_file.getContent();
                InputStream bis = new BufferedInputStream(fileContent.getInputStream());
                detector.setText(bis);

                Reader m_reader = detector.detect().getReader();
                parser.parse(new InputSource(m_reader));
            } catch (Exception exe) {
                log.error(exe.getMessage());
            }
        }

        int nbErrors = errorHandler.getNumberOfErrors();

        String strMessage = "There is(are) " + nbErrors +
            " errors(s). Please consult the messages window";

        console.println("Batch Validation report", OutputDockable.BLUE_STYLE);

        Toolkit.getDefaultToolkit().beep();

        if (nbErrors == 0) {
            console.println("All files are valid!");
            DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                    .setMessage("All files are valid!");
        } else {
            console.println("There is(are) " + nbErrors + " errors(s)");
            console.println(errorHandler.getErrorMessages(),
                OutputDockable.RED_STYLE);

            DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                    .setMessage(strMessage);
        }

        view.enableControlButtons(true);
        outputWindow.setFocus(MessagesWindowDockable.DOCKABLE_ID);

        DefaultXPontusWindowImpl.getInstance().getStatusBar()
                                .removeOperationComponent(pmh);
    }

    /**
     * Close the dialog window
     */
    public void closeWindow() {
        view.setVisible(false);
    }

    private boolean extensionExists(String m_extension) {
        JList li = view.getExtensionsList();
        DefaultComboBoxModel m = (DefaultComboBoxModel) li.getModel();
        boolean found = false;

        for (int i = 0; i < m.getSize(); i++) {
            found = m.getElementAt(i).equals(m_extension);

            if (found) {
                break;
            }
        }

        return found;
    }

    private boolean pathAdded(FileObject fo) {
        JList li = view.getPathList();
        DefaultComboBoxModel m = (DefaultComboBoxModel) li.getModel();
        boolean found = false;

        for (int i = 0; i < m.getSize(); i++) {
            found = m.getElementAt(i).equals(fo);

            if (found) {
                break;
            }
        }

        return found;
    }
}
