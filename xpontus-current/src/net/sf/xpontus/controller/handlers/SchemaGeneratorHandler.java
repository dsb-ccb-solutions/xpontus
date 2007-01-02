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
package net.sf.xpontus.controller.handlers;

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

import net.sf.xpontus.model.SchemaGenerationModel;
import net.sf.xpontus.view.SchemaGeneratorView;
import net.sf.xpontus.view.XPontusWindow;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


/**
 *
 * @author Yves Zoundi
 */
public class SchemaGeneratorHandler
  {
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
    private SchemaGeneratorView view;
    private JFileChooser chooser;

    /** Creates a new instance of SchemaGeneratorHandler */
    public SchemaGeneratorHandler(SchemaGeneratorView view)
      {
        chooser = new JFileChooser();
        chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        this.view = view;
      }

    public void saveFileToDisk()
      {
        view.getOutputButton().setEnabled(true);
      }

    public void useExternalDocument()
      {
        view.getInputButton().setEnabled(true);
      }

    public void openInEditor()
      {
        view.getOutputButton()
            .setEnabled(view.getOpenInEditorOption().isSelected());
        view.getModel().setOutputURI("");
      }

    public void useCurrentDocument()
      {
        view.getInputButton()
            .setEnabled(view.getUseCurrentDocumentOption().isSelected());
        view.getModel().setInputURI("");
      }

    public void close()
      {
        view.setVisible(false);
      }

    public void generate()
      {
        try
          {
            SchemaGenerationModel model = view.getModel();
            InputFormat inFormat = null;
            OutputFormat of = null;

            if (model.getInputType().equalsIgnoreCase("rng"))
              {
                inFormat = new SAXParseInputFormat();
              }
            else if (model.getInputType().equalsIgnoreCase("rnc"))
              {
                inFormat = new CompactParseInputFormat();
              }
            else if (model.getInputType().equalsIgnoreCase("dtd"))
              {
                inFormat = new DtdInputFormat();
              }
            else if (model.getInputType().equalsIgnoreCase("xml"))
              {
                inFormat = new XmlInputFormat();
              }

            if (model.getOutputType().equalsIgnoreCase("dtd"))
              {
                of = new DtdOutputFormat();
              }
            else if (model.getOutputType().equalsIgnoreCase("rng"))
              {
                of = new RngOutputFormat();
              }
            else if (model.getOutputType().equalsIgnoreCase("xsd"))
              {
                of = new XsdOutputFormat();
              }
            else if (model.getOutputType().equalsIgnoreCase("rnc"))
              {
                of = new RncOutputFormat();
              }

            ErrorHandlerImpl eh = new ErrorHandlerImpl();

            SchemaCollection sc = inFormat.load(UriOrFile.toUri(""), null,
                    model.getOutputType().toLowerCase(), eh);
            OutputDirectory od = new LocalOutputDirectory(sc.getMainUri(),
                    new File(""), model.getOutputType().toLowerCase(),
                    DEFAULT_OUTPUT_ENCODING, DEFAULT_LINE_LENGTH, DEFAULT_INDENT);
            of.output(sc, od, new String[] {  },
                model.getInputType().toLowerCase(), eh);
          }
        catch (Exception ex)
          {
            JOptionPane.showMessageDialog(XPontusWindow.getInstance().getFrame(),
                ex.getMessage());
          }
      }

    public void input()
      {
        chooser.setDialogTitle("Select input file");

        int answer = chooser.showOpenDialog(XPontusWindow.getInstance()
                                                         .getFrame());

        if (answer == JFileChooser.APPROVE_OPTION)
          {
            view.getModel()
                .setInputURI(chooser.getSelectedFile().toURI().toString());
          }
      }

    public void output()
      {
        chooser.setDialogTitle("Select output file");

        int answer = chooser.showSaveDialog(XPontusWindow.getInstance()
                                                         .getFrame());

        if (answer == JFileChooser.APPROVE_OPTION)
          {
            view.getModel()
                .setOutputURI(chooser.getSelectedFile().toURI().toString());
          }
      }

    public boolean transformationIsValid()
      {
        return false;
      }
  }
