/*
 * SchemaGeneratorHandler.java
 *
 * Created on July 28, 2006, 6:07 AM
 *
 *
 *  Copyright (C) 2005 Yves Zoundi
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
package net.sf.xpontus.plugins.schema_converter_plugin;

import com.thaiopensource.relaxng.edit.SchemaCollection;
import com.thaiopensource.relaxng.input.InputFormat;
import com.thaiopensource.relaxng.input.dtd.DtdInputFormat;
import com.thaiopensource.relaxng.input.parse.compact.CompactParseInputFormat;
import com.thaiopensource.relaxng.input.parse.sax.SAXParseInputFormat;
import com.thaiopensource.relaxng.input.xml.XmlInputFormat;
import com.thaiopensource.relaxng.output.LocalOutputDirectory;
import com.thaiopensource.relaxng.output.OutputDirectory;
import com.thaiopensource.relaxng.output.OutputFormat;
import com.thaiopensource.relaxng.output.dtd.DtdOutputFormat;
import com.thaiopensource.relaxng.output.rnc.RncOutputFormat;
import com.thaiopensource.relaxng.output.rng.RngOutputFormat;
import com.thaiopensource.relaxng.output.xsd.XsdOutputFormat;

import com.thaiopensource.util.UriOrFile;

import com.thaiopensource.xml.sax.ErrorHandlerImpl;

import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentContainer;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.text.StrBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.xml.sax.SAXParseException;

import java.awt.Toolkit;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

import javax.swing.JFileChooser;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Yves Zoundi
 */
public class SchemaGeneratorHandler {
    private static final String DEFAULT_OUTPUT_ENCODING = "UTF-8";
    private static final int DEFAULT_LINE_LENGTH = 72;
    private static final int DEFAULT_INDENT = 2;
    public static final String SAVE_FILE_TO_DISK_METHOD = "saveFileToDisk";
    public static final String USE_EXTERNAL_DOCUMENT_METHOD = "useExternalDocument";
    public static final String OPEN_IN_EDITOR_METHOD = "openInEditor";
    public static final String USE_CURRENT_DOCUMENT_METHOD = "useCurrentDocument";
    public static final String CLOSE_METHOD = "close";
    public static final String GENERATE_METHOD = "generate";
    public static final String INPUT_METHOD = "input";
    public static final String OUTPUT_METHOD = "output";
    private Log logger = LogFactory.getLog(SchemaGeneratorHandler.class);
    private SchemaGeneratorView view;
    private JFileChooser chooser;

    /** Creates a new instance of SchemaGeneratorHandler
     * @param view
     */
    public SchemaGeneratorHandler(SchemaGeneratorView view) {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.view = view;
    }

    public void saveFileToDisk() {
        view.getOutputButton().setEnabled(true);
    }

    public void useExternalDocument() {
        view.getInputButton().setEnabled(view.getModel().isUseExternalDocument());
    }

    public void useCurrentDocument() {
        view.getInputButton()
            .setEnabled(view.getUseCurrentDocumentOption().isSelected());

        if (!view.getUseCurrentDocumentOption().isSelected()) {
            view.getModel().setInputURI("");
        }
    }

    public void close() {
        view.setVisible(false);
    }

    public void generate() {
        Thread worker = new Thread() {
                public void run() {
                    generateSchema();
                }
            };

        worker.setPriority(Thread.MIN_PRIORITY);
        worker.start();
    }

    private void generateSchema() {
        view.setVisible(false);

        ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                              .getConsole();
        MessagesWindowDockable mconsole = (MessagesWindowDockable) console.getDockableById(MessagesWindowDockable.DOCKABLE_ID);

        DocumentContainer container = (DocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                  .getDocumentTabContainer()
                                                                                  .getCurrentDockable();

        try {
            SchemaGenerationModel model = view.getModel();
            InputFormat inFormat = null;
            OutputFormat of = null;

            if (model.getInputType().equalsIgnoreCase("rng")) {
                inFormat = new SAXParseInputFormat();
            } else if (model.getInputType().equalsIgnoreCase("rnc")) {
                inFormat = new CompactParseInputFormat();
            } else if (model.getInputType().equalsIgnoreCase("dtd")) {
                inFormat = new DtdInputFormat();
            } else if (model.getInputType().equalsIgnoreCase("xml")) {
                inFormat = new XmlInputFormat();
            }

            if (model.getOutputType().equalsIgnoreCase("dtd")) {
                of = new DtdOutputFormat();
            } else if (model.getOutputType().equalsIgnoreCase("rng")) {
                of = new RngOutputFormat();
            } else if (model.getOutputType().equalsIgnoreCase("xsd")) {
                of = new XsdOutputFormat();
            } else if (model.getOutputType().equalsIgnoreCase("rnc")) {
                of = new RncOutputFormat();
            }

            ErrorHandlerImpl eh = new ErrorHandlerImpl();

            SchemaCollection sc = null;

            if (!view.getModel().isUseExternalDocument()) {
                JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                             .getDocumentTabContainer()
                                                             .getCurrentEditor();

                if (jtc == null) {
                    XPontusComponentsUtils.showErrorMessage(
                        "No document opened!!!");

                    return;
                }

                InputStream m_inputStream = new ByteArrayInputStream(jtc.getText()
                                                                        .getBytes());
                String suffixe = model.getOutputType().toLowerCase();
                File tmp = File.createTempFile("schemageneratorhandler",
                        "." + suffixe);
                OutputStream m_outputStream = FileUtils.openOutputStream(tmp);

                IOUtils.copy(m_inputStream, m_outputStream);

                m_inputStream.close();
                m_outputStream.close();

                try {
                    sc = inFormat.load(UriOrFile.toUri(tmp.getAbsolutePath()),
                            new String[0], model.getOutputType().toLowerCase(),
                            eh);
                } catch (Exception ife) {
                   StrBuilder stb = new StrBuilder();
                    stb.append("\nError loading input document!\n");
                    stb.append("Maybe the input type is invalid?\n");
                    stb.append(
                        "Please check again the input type list or trying validating your document\n");
                    throw new Exception(stb.toString());
                }

                tmp.deleteOnExit();
            } else {
                try {
                    sc = inFormat.load(UriOrFile.toUri(
                                view.getModel().getInputURI()), new String[0],
                            model.getOutputType().toLowerCase(), eh);
                } catch (Exception ife) {
                    StrBuilder stb = new StrBuilder();
                    stb.append("\nError loading input document!\n");
                    stb.append("Maybe the input type is invalid?\n");
                    stb.append(
                        "Please check again the input type list or trying validating your document\n");
                    throw new Exception(stb.toString());
                }
            }

            container.getStatusBar().setMessage("Generating schema...");

            OutputDirectory od = new LocalOutputDirectory(sc.getMainUri(),
                    new File(view.getModel().getOutputURI()),
                    model.getOutputType().toLowerCase(),
                    DEFAULT_OUTPUT_ENCODING, DEFAULT_LINE_LENGTH, DEFAULT_INDENT);
            of.output(sc, od, new String[0],
                model.getInputType().toLowerCase(), eh);

            mconsole.println("Schema generated sucessfully!");

            container.getStatusBar().setMessage("Schema generated sucessfully!");
        } catch (Exception ex) {
            ex.printStackTrace();

            StringBuffer sb = new StringBuffer();
            sb.append("Error generating schema\n");

            if (ex instanceof SAXParseException) {
                SAXParseException spe = (SAXParseException) ex;
                sb.append("Error around line " + spe.getLineNumber());
                sb.append(", column " + spe.getColumnNumber());
                sb.append("\n");
            }

            container.getStatusBar().setMessage("Schema generation failed!");

            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            PrintStream ps = new PrintStream(bos);
            ex.printStackTrace(ps);

            String errStackTrace = new String(bos.toByteArray());
            sb.append(errStackTrace);
            mconsole.println(sb.toString(), OutputDockable.RED_STYLE);
            logger.error(sb.toString());

            // close the open streams
            try {
                ps.flush();
                ps.close();
                bos.flush();
                bos.close();
            } catch (IOException ioe) {
                logger.error(ioe.getMessage());

                // what can we do with that... nothing
            }
        } finally {
            console.setFocus(MessagesWindowDockable.DOCKABLE_ID);
            Toolkit.getDefaultToolkit().beep();
        }
    }

    public void input() {
        chooser.setDialogTitle("Select input file");

        int answer = chooser.showOpenDialog(DefaultXPontusWindowImpl.getInstance()
                                                                    .getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION) {
            view.getModel()
                .setInputURI(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public void output() {
        chooser.setDialogTitle("Select output file");

        int answer = chooser.showSaveDialog(DefaultXPontusWindowImpl.getInstance()
                                                                    .getDisplayComponent());

        if (answer == JFileChooser.APPROVE_OPTION) {
            view.getModel()
                .setOutputURI(chooser.getSelectedFile().getAbsolutePath());
        }
    }

    public boolean transformationIsValid() {
        return false;
    }
}
