/*
 * NekoHtmlAction.java
 *
 * Created on July 4, 2006, 2:01 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.ThreadedAction;
import net.sf.xpontus.model.options.NekoHTMLOptionModel;
import net.sf.xpontus.view.*;

import org.apache.xerces.parsers.DOMParser;
import org.apache.xerces.xni.parser.XMLDocumentFilter;

import org.apache.xml.serialize.OutputFormat;
import org.apache.xml.serialize.XMLSerializer;

import org.cyberneko.html.HTMLConfiguration;
import org.cyberneko.html.filters.Purifier;

import org.w3c.dom.Document;

import org.xml.sax.InputSource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.event.UndoableEditEvent;
import javax.swing.event.UndoableEditListener;


/**
 *
 * @author Yves Zoundi
 */
public class NekoHtmlAction extends ThreadedAction
  {
    private final String OVERRIDE_NAMESPACES = "http://cyberneko.org/html/features/override-namespaces";
    private final String ELEM_PROP = "http://cyberneko.org/html/properties/names/elems";
    private final String ATTRS_PROP = "http://cyberneko.org/html/properties/names/attrs";
    private final String NS_PROP = "http://xml.org/sax/features/namespaces";
    private final String BALANCE_PROP = "http://cyberneko.org/html/features/balance-tags";
    private final String FILTERS_PROP = "http://cyberneko.org/html/properties/filters";
    private final String OVERRIDE_PROP = "http://cyberneko.org/html/features/override-doctype";
    private final String PUBID_PROP = "http://cyberneko.org/html/properties/doctype/pubid";
    private final String SYSID_PROP = "http://cyberneko.org/html/properties/doctype/sysid";
    private final String INSERT_NAMESPACES = "http://cyberneko.org/html/features/insert-namespaces";
    private final String NAMESPACES_URI = "http://cyberneko.org/html/properties/namespaces-uri";

    /** Creates a new instance of NekoHtmlAction */
    public NekoHtmlAction()
      {
      }

    public void execute()
      {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);

        NekoHTMLOptionModel _model = new NekoHTMLOptionModel();
        _model = (NekoHTMLOptionModel) _model.load();

        try
          {
            HTMLConfiguration config = new HTMLConfiguration();
            config.setProperty(ELEM_PROP, _model.getLower_elements());
            config.setProperty(ATTRS_PROP, _model.getLower_attr());
            config.setFeature(NS_PROP, true);
            config.setFeature(BALANCE_PROP, true);
            config.setFeature(OVERRIDE_PROP, _model.isOverride_doctype());

            if (_model.isOverride_doctype())
              {
                if (_model.getDoctype().equals("transitionnal"))
                  {
                    config.setProperty(PUBID_PROP,
                        "-//W3C//DTD XHTML 1.0 Transitional//EN");
                    config.setProperty(SYSID_PROP,
                        "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd");
                    config.setProperty(NAMESPACES_URI,
                        "http://www.w3.org/1999/xhtml");
                  }
                else if (_model.getDoctype().equals("transitionnal"))
                  {
                  }
                else
                  {
                  }
              }

            String log = "Unable to format! see Messages Window";
            final javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                              .getCurrentEditor();
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage(XPontusWindow.getInstance()
                                                              .getI18nMessage("msg.formatting"));

            byte[] bt = edit.getText().getBytes();

            InputStream in = new ByteArrayInputStream(bt);
            final InputStream _backup = new ByteArrayInputStream(bt);

            ByteArrayOutputStream out = new ByteArrayOutputStream();

            Purifier purifier = new Purifier();
            XMLDocumentFilter[] filters = new XMLDocumentFilter[] { purifier };

            DOMParser parser = new DOMParser(config);
            parser.setProperty(FILTERS_PROP, filters);

            InputSource source = new InputSource(new InputStreamReader(in));

            parser.parse(source);

            Document htmlD = parser.getDocument();
            OutputFormat format = new OutputFormat(htmlD, "UTF-8", true);

            XMLSerializer serial = new XMLSerializer(out, format);
            serial.serialize(htmlD);
            out.close();

            if (new String(out.toByteArray()).trim().equals(""))
              {
              }
            else
              {
                byte[] bs = out.toByteArray();
                InputStream ax = new ByteArrayInputStream(bs);
                InputStreamReader isr = new InputStreamReader(ax, "UTF-8");
                edit.getDocument().remove(0, edit.getDocument().getLength());
                edit.read(isr, null);
                log = XPontusWindow.getInstance()
                                   .getI18nMessage("msg.formattingDone");
              }

            javax.swing.text.Document _doc = edit.getDocument();

            edit.repaint();
            edit.putClientProperty("FILE_MODIFIED", Boolean.TRUE);

            javax.swing.undo.UndoManager _undo = new javax.swing.undo.UndoManager();
            edit.putClientProperty("UNDO_MANAGER", _undo);

            edit.getDocument().addUndoableEditListener(new UndoableEditListener()
                  {
                    public void undoableEditHappened(UndoableEditEvent event)
                      {
                        ((javax.swing.undo.UndoManager) edit.getClientProperty(
                            "UNDO_MANAGER")).addEdit(event.getEdit());
                      }
                  });

            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);

            XPontusWindow.getInstance()
                         .append(ConsoleOutputWindow.MESSAGES_WINDOW,
                sw.toString());
          }
        catch (Exception e)
          {
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage("Error see messages window!");
            XPontusWindow.getInstance()
                         .append(ConsoleOutputWindow.ERRORS_WINDOW,
                sw.toString());
          }
      }
  }
