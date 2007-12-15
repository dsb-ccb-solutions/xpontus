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

import com.sun.java.help.impl.SwingWorker;

import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XIncludeAwareParserConfiguration;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLParserConfiguration;

import org.xml.sax.InputSource;

import java.io.File;
import java.io.FileFilter;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Vector;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JFileChooser;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.ListModel;
import javax.swing.ProgressMonitor;


/**
 * Controller for the Batch Validation dialog
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.1
 */
public class BatchValidationController {
    // Methods names
    public static final String VALIDATE_FILES_METHOD = "validateFiles";
    public static final String ADD_FILE_METHOD = "addFile";
    public static final String REMOVE_FILE_METHOD = "removeFile";
    public static final String ADD_EXTENSION_METHOD = "addExtension";
    public static final String REMOVE_EXTENSION_METHOD = "removeExtension";
    public static final String CLOSE_WINDOW_METHOD = "closeWindow";
    private Log log = LogFactory.getLog(BatchValidationController.class);

    // private members
    private BatchValidationDialogView view;
    private JFileChooser chooser;

    /**
     *
     */
    public BatchValidationController() {
        chooser = new JFileChooser();
        chooser.setMultiSelectionEnabled(true);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    /**
     *
     * @param aThis
     */
    public BatchValidationController(BatchValidationDialogView aThis) {
        this();
        this.view = aThis;
    }

    /**
     *
     * @return
     */
    public BatchValidationDialogView getView() {
        return view;
    }

    /**
     * Add a path
     */
    public void addFile() {
        int answer = chooser.showOpenDialog(view);

        if (answer == JFileChooser.APPROVE_OPTION) {
            File[] files = chooser.getSelectedFiles();

            for (int i = 0; i < files.length; i++) {
                File f = files[i];
                String path = f.getAbsolutePath();

                if (!pathAdded(path)) {
                    JList li = view.getPathList();
                    DefaultComboBoxModel m = (DefaultComboBoxModel) li.getModel();
                    m.addElement(path);
                }
            }
        }
    }

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
     *
     * @param view
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

    public void validateFiles() {
        final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    validateFiles2();

                    return null;
                }
            };

        worker.start();
    }

    /**
     * Validate the selected files
     */
    public void validateFiles2() {
        JList li = view.getPathList();
        final int paths = li.getModel().getSize();
        ListModel m = li.getModel();

        boolean recurse = view.getRecurseOption().isSelected();

        ListModel extensionListModel = view.getExtensionsList().getModel();

        final int nbExtensions = extensionListModel.getSize();

        if (nbExtensions == 0) {
            XPontusComponentsUtils.showErrorMessage(
                "Please add some file extensions");

            return;
        }

        String[] extensions = new String[nbExtensions];

        for (int i = 0; i < nbExtensions; i++) {
            extensions[i] = (extensionListModel.getElementAt(i).toString());
            System.out.println("extension:" + extensions[i]);
        }

        IOFileFilter suffixFilter = new WildcardFileFilter(extensions,
                IOCase.INSENSITIVE);

        List files = new Vector();

        for (int i = 0; i < paths; i++) {
            File filePath = new File(m.getElementAt(i).toString());

            if (filePath.isDirectory()) {
                if (recurse) {
                    System.out.println("recursive match");

                    Collection cl = FileUtils.listFiles(filePath, suffixFilter,
                            TrueFileFilter.INSTANCE);

                    File[] fl = FileUtils.convertFileCollectionToFileArray(cl);

                    files.addAll(Arrays.asList(fl));
                } else {
                    files.addAll(Arrays.asList(filePath.listFiles(
                                (FileFilter) suffixFilter)));
                }
            } else {
                files.add(filePath);
            }
        }

        final int nbFiles = files.size();

        if (nbFiles == 0) {
            XPontusComponentsUtils.showErrorMessage("No files found!");

            return;
        }

        log.info("There are " + nbFiles + " to validate");

        final ProgressMonitor pm = new ProgressMonitor(view, "Progression", "",
                0, nbFiles);
        pm.setMillisToDecideToPopup(1000);

        XMLParserConfiguration config = new XIncludeAwareParserConfiguration();
        XMLGrammarPool grammarPool = new XMLGrammarPoolImpl();
        final String GRAMMAR_POOL_PROPERTY = "http://apache.org/xml/properties/internal/grammar-pool";
        config.setProperty(GRAMMAR_POOL_PROPERTY, grammarPool);

        File current_file = null;

        SAXParser parser = null;

        OutputDockable console = (OutputDockable) DefaultXPontusWindowImpl.getInstance()
                                                                          .getConsole()
                                                                          .getDockables()
                                                                          .get(ConsoleOutputWindow.MESSAGES_WINDOW);

        try {
            parser = new SAXParser(config);

            parser.setFeature("http://xml.org/sax/features/validation", true);
            parser.setFeature("http://xml.org/sax/features/namespaces", true);
            parser.setFeature("http://apache.org/xml/features/validation/schema",
                true);
            parser.setFeature("http://apache.org/xml/features/validation/dynamic",
                true);
        } catch (Exception err) {
            log.fatal(err.getMessage());
        }

        for (int i = 0; i < nbFiles; i++) {
            final File m_file = (File) files.get(i);
            current_file = m_file;

            final int pos = i;

            pm.setProgress(pos + 1);
            pm.setNote(m_file.getName());

            String message = m_file.getName() + " is valid";

            try {
                parser.parse(new InputSource(FileUtils.openInputStream(m_file)));
            } catch (Exception exe) {
                message = m_file.getName() + " is invalid";
            }

            console.println(message);
        }
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

    private boolean pathAdded(String path) {
        JList li = view.getPathList();
        DefaultComboBoxModel m = (DefaultComboBoxModel) li.getModel();
        boolean found = false;

        for (int i = 0; i < m.getSize(); i++) {
            found = m.getElementAt(i).equals(path);

            if (found) {
                break;
            }
        }

        return found;
    }
}
