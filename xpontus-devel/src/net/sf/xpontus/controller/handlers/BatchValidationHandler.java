/*
 * BatchValidationHandler.java
 *
 * Created on November 5, 2005, 4:11 PM
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

import com.sun.java.help.impl.SwingWorker;

import net.sf.xpontus.view.ConsoleOutputWindow;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.xerces.parsers.SAXParser;
import org.apache.xerces.parsers.XIncludeAwareParserConfiguration;
import org.apache.xerces.util.XMLGrammarPoolImpl;
import org.apache.xerces.xni.grammars.XMLGrammarPool;
import org.apache.xerces.xni.parser.XMLParserConfiguration;

import org.xml.sax.InputSource;

import java.awt.Toolkit;

import java.io.InputStreamReader;

import javax.swing.ProgressMonitor;
import javax.swing.Timer;


/**
 * A class which handles batch validation
 * @author Yves Zoundi
 */
public class BatchValidationHandler
  {
    public final static int ONE_SECOND = 1000;
    private static BatchValidationHandler instance;
    private int lengthOfTask;
    private int current = 0;
    private boolean done = false;
    public boolean canceled = false;
    private String statMessage = "Working...";
    private java.util.List files;
    private javax.swing.JDialog dialog;
    private ProgressMonitor progressMonitor;
    private javax.swing.Timer timer;
    private SAXParser parser;

    /**
     *
     * @param files A file list
     */
    private BatchValidationHandler(java.util.List files)
      {
        if (parser == null)
          {
            try
              {
                XMLParserConfiguration config = new XIncludeAwareParserConfiguration();
                XMLGrammarPool grammarPool = new XMLGrammarPoolImpl();
                final String GRAMMAR_POOL_PROPERTY = "http://apache.org/xml/properties/internal/grammar-pool";
                config.setProperty(GRAMMAR_POOL_PROPERTY, grammarPool);
                parser = new SAXParser(config);

                parser.setFeature("http://xml.org/sax/features/validation", true);
                parser.setFeature("http://xml.org/sax/features/namespaces", true);
                parser.setFeature("http://apache.org/xml/features/validation/schema",
                    true);
                parser.setFeature("http://apache.org/xml/features/validation/dynamic",
                    true);
              }
            catch (Exception ex)
              {
                ex.printStackTrace();
              }
          }

        this.files = files;
        this.lengthOfTask = files.size();
        progressMonitor = new ProgressMonitor(XPontusWindow.getInstance()
                                                           .getFrame(),
                XPontusWindow.getInstance().getI18nMessage("msg.validating"),
                XPontusWindow.getInstance().getI18nMessage("msg.validating"),
                0, lengthOfTask);
        progressMonitor.setProgress(0);
        progressMonitor.setMillisToDecideToPopup(ONE_SECOND);
        initTimer();
        timer.start();
        go();
      }

    public static BatchValidationHandler getInstance(java.util.List files)
      {
        if (instance == null)
          {
            instance = new BatchValidationHandler(files);
          }

        return instance;
      }

    private void initTimer()
      {
        timer = new Timer(ONE_SECOND,
                new java.awt.event.ActionListener()
                  {
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                      {
                        if (done || canceled)
                          {
                            Toolkit.getDefaultToolkit().beep();
                            progressMonitor.close();
                            timer.stop();
                          }
                      }
                  });
      }

    /**
     * Start the task.
     */
    public void go()
      {
        final SwingWorker worker = new SwingWorker()
              {
                public Object construct()
                  {
                    current = 0;
                    done = false;
                    canceled = false;
                    statMessage = null;

                    return new ActualTask();
                  }
              };

        worker.start();
      }

    /**
     * Find out how much work needs to be done.
     */
    public int getLengthOfTask()
      {
        return lengthOfTask;
      }

    /**
     * Find out how much has been done.
     */
    public int getCurrent()
      {
        return current;
      }

    public void stop()
      {
        canceled = true;
        statMessage = null;
      }

    /**
     * Find out if the task has completed.
     */
    public boolean isDone()
      {
        return done;
      }

    public boolean isCanceled()
      {
        return canceled;
      }

    /**
     * Returns the most recent status message, or null
     * if there is no current status message.
     */
    public String getMessage()
      {
        return statMessage;
      }

    /**
     * The actual long running task.  This runs in a SwingWorker thread.
     */
    class ActualTask
      {
        boolean error = false;
        long invalid = 0;

        ActualTask()
          {
            final int total = files.size();

            //Fake a long task,
            //making a random amount of progress every second.
            int i = 0;

            for (; i < total; i++)
              {
                if (canceled || done)
                  {
                    dialog.setVisible(false);

                    break;
                  }

                java.io.File _file = (java.io.File) files.get(i);

                try
                  {
                    parser.parse(new InputSource(
                            new InputStreamReader(
                                new java.io.FileInputStream(_file), "UTF-8")));
                    current = i;
                    statMessage = _file.getName();
                    progressMonitor.setProgress(current);
                    progressMonitor.setNote(statMessage);
                  }
                catch (Exception e)
                  {
                    invalid++;

                    String filename = _file.toURI().toString();
                    StringBuffer sb = new StringBuffer();
                    sb.append("*** " +
                        XPontusWindow.getInstance().getI18nMessage("msg.error") +
                        " - ");
                    sb.append(filename);
                    sb.append("\n");
                    sb.append(e.getLocalizedMessage());
                    sb.append("\n");
                    sb.append("\n");
                    XPontusWindow.getInstance()
                                 .append(ConsoleOutputWindow.MESSAGES_WINDOW,
                        sb.toString());
                  }
              }

            done = true;
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage(XPontusWindow.getInstance()
                                                              .getI18nMessage("msg.thereAre") +
                " " + invalid + " " +
                XPontusWindow.getInstance().getI18nMessage("msg.invalidFiles"));
          }
      }
  }
