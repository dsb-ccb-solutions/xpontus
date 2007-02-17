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

import net.sf.xpontus.constants.GrammarCachingPoolProvider;
import net.sf.xpontus.utils.EncodingHelper;
import net.sf.xpontus.utils.MsgUtils;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.xerces.parsers.SAXParser;

import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import java.awt.Toolkit;

import javax.swing.ProgressMonitor;
import javax.swing.Timer;


/**
 * A class which handles batch validation
 * @author Yves Zoundi
 */
public class BatchValidationHandler
{
    public final static int ONE_SECOND = 1000;
    private int lengthOfTask;
    private int current = 0;
    private boolean done = false;
    public boolean canceled = false;
    private String statMessage = "Working...";
    private java.util.List files;
    private javax.swing.JDialog dialog;
    private ProgressMonitor progressMonitor;
    private javax.swing.Timer timer;
    private MsgUtils _msg;

    /**
     *
     * @param files A file list
     */
    public BatchValidationHandler(java.util.List files)
    {
        this.files = files;
        _msg = MsgUtils.getInstance();
        this.lengthOfTask = files.size();
        progressMonitor = new ProgressMonitor(XPontusWindow.getInstance()
                                                           .getFrame(),
                _msg.getString("msg.validating"),
                _msg.getString("msg.validating"), 0, lengthOfTask);
        progressMonitor.setProgress(0);
        progressMonitor.setMillisToDecideToPopup(ONE_SECOND);
        initTimer();
        timer.start();
        go();
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
            SAXParser parser = null;

            try
            {
                GrammarCachingPoolProvider provider = GrammarCachingPoolProvider.getInstance();

                parser = new SAXParser(provider.getSymbolTable(),
                        provider.getGrammarPool());
                parser.setFeature("http://xml.org/sax/features/use-entity-resolver2",
                    true);
                parser.setFeature("http://xml.org/sax/features/validation", true);
                parser.setFeature("http://xml.org/sax/features/namespaces", true);
                parser.setFeature("http://apache.org/xml/features/validation/schema",
                    true);
                parser.setFeature("http://apache.org/xml/features/honour-all-schemaLocations",
                    true);
                parser.setFeature("http://apache.org/xml/features/validation/schema-full-checking",
                    true);
                parser.setFeature("http://apache.org/xml/features/validation/dynamic",
                    true);
            }
            catch (Exception ee)
            {
                ee.printStackTrace();
            }

            final int total = files.size();

            //Fake a long task,
            //making a random amount of progress every second.
            int i = 0;

            for (; i < total; i++)
            {
                if (canceled || done)
                {
                    dialog.setVisible(false);

                    return;
                }

                java.io.File _file = (java.io.File) files.get(i);

                try
                {
                    parser.parse(new InputSource(EncodingHelper.getReader(
                                new java.io.FileInputStream(_file))));
                    current = i;
                    statMessage = _file.getName();
                    progressMonitor.setNote(statMessage);
                    progressMonitor.setProgress(current);
                }
                catch (Exception e)
                {
                    invalid++;

                    String filename = _file.toURI().toString();
                    StringBuffer sb = new StringBuffer();
                    sb.append("*** " + _msg.getString("msg.error") + " - ");
                    sb.append(filename);
                    sb.append("\n");

                    StringBuffer errorMsg = new StringBuffer();

                    if (e instanceof SAXParseException)
                    {
                        SAXParseException spe = (SAXParseException) e;
                        errorMsg.append("line:" + spe.getLineNumber() +
                            ",column:" + spe.getColumnNumber() + "\n");
                    }

                    sb.append(e.getLocalizedMessage());
                    sb.append("\n");
                    sb.append(errorMsg.toString());
                    XPontusWindow.getInstance().append(sb.toString());
                }
            }

            done = true;
            javax.swing.JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                                   .getFrame(),
                _msg.getString("msg.thereAre") + " " + invalid + " " +
                _msg.getString("msg.invalidFiles"));
        }
    }
}
