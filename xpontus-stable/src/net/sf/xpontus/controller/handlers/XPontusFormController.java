/*
 * XPontusFormController.java
 *
 * Created on 18 juillet 2005, 02:29
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
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

import com.jgoodies.looks.plastic.Plastic3DLookAndFeel;

import com.sun.corba.se.spi.activation.Server;

import net.sf.xpontus.core.utils.IconUtils;
import net.sf.xpontus.core.utils.L10nHelper;
import net.sf.xpontus.core.utils.WindowUtilities;
import net.sf.xpontus.model.options.EditorOptionModel;
import net.sf.xpontus.model.options.GeneralOptionModel;
import net.sf.xpontus.model.options.XMLOptionModel;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.awt.Font;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.ServerSocket;
import java.net.Socket;

import java.util.Properties;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;


/**
 * Main controller/Launcher of the application
 * @author Yves Zoundi
 */
public class XPontusFormController
{
    private static XPontusWindow INSTANCE;
    public static final int PORT = 9876;
    private static final String EOT = ".";

    /** The server socket instance. */
    private static ServerSocket server;
    private static String theme = null;
    private static Log logger = LogFactory.getLog(XPontusFormController.class);

    static
    {
        System.setProperty("org.xml.sax.driver",
            "org.apache.xerces.parsers.SAXParser");
        System.setProperty("apple.laf.useScreenMenuBar", "true");
    }

    private static void openFiles(String[] args)
    {
        if (args != null)
        {
            for (int i = 0; i < args.length; i++)
            {
                String fileName = args[i];
                File f = new File(fileName);

                if (f.exists())
                {
                    INSTANCE.getPane().createEditorFromFile(f);
                }
                else
                {
                    System.err.println("File not found: " + fileName);
                }
            }
        }
    }

    // test the server inside the documentation tool and documentation inside
    // if the server is idupdated verify the document
    /**
     * Causes the server to start its server socket and to listen for client
     * connections. If a client is accepted the server reads its input stream
     * line by line until the end marker is found or the socket is closed.
     * The lines are interpreted as file names and finally forwarded to the
     * editor for opening.
     */
    private static void listen()
    {
        try
        {
            if (server != null)
            {
                System.err.println("WARNING: Server already running");
            }
            else
            {
                server = new ServerSocket(PORT);

                do
                {
                    Socket socket = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                                socket.getInputStream()));

                    while (!socket.isClosed() && !socket.isInputShutdown())
                    {
                        sleep(100);

                        if (!in.ready())
                        {
                            continue;
                        }

                        try
                        {
                            String line = in.readLine();

                            if (EOT.equals(line))
                            {
                                break;
                            }

                            INSTANCE.getPane()
                                    .createEditorFromFile(new File(line));
                        }
                        catch (RuntimeException e)
                        {
                            e.printStackTrace();
                        }
                    }

                    // show the editor frame
                    INSTANCE.getFrame().setExtendedState(JFrame.NORMAL);
                    INSTANCE.getFrame().toFront();
                    INSTANCE.getFrame().setVisible(true);
                }
                while (true);
            }
        }
        catch (Exception e)
        {
            System.out.println("error in listen function");
            e.printStackTrace();

            try
            {
                server.close();
            }
            catch (IOException x)
            {
                System.out.println("error in listen function");
            }

            server = null;
            e.printStackTrace();
        }
    }

    /**
     * Cause the calling thread to sleep some time.
     *
     * @param millis time in milliseconds
     */
    private static void sleep(long delay)
    {
        try
        {
            Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {
        }
    }

    public static boolean attach(String[] args)
    {
        try
        {
            Socket socket = new Socket("localhost", PORT);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream()));

            for (int i = 0; i < args.length; i++)
            {
                out.write((new File(args[i])).getAbsolutePath());
                out.newLine();
            }

            out.write(EOT);
            out.newLine();
            out.flush();
            out.close();
            socket.close();

            return true;
        }
        catch (IOException e)
        {
            //            e.printStackTrace();
            //            System.out.println("Error in attach function ");
            return false;
        }
    }

    /**
     * Tries to attach the provided arguments to a running server socket. If
     * there is no accepting server a new server instance will be started.
     * Returns true if another server socket has accepted the transmission,
     * false otherwise. In this case and if <code>startServerIfNeeded</code> is
     * set to true a new server will be started with the provided arguments.
     *
     * @param args the arguments for the server
     * @param startServerIfNeeded true to start a new server if ther is none yet
     *
     * @return true if another server has accepted the arguments, false
     *                otherwise which means that a new server was started if
     *                <code>startServerIfNeeded</code> is set to true or that .
     */
    public static boolean attach(String[] args, boolean startServerIfNeeded)
    {
        if (!attach(args))
        {
            if (startServerIfNeeded)
            {
                System.err.println(
                    "XPontus Server not yet started - starting now ...");

                try
                {
                    initApp(args);
                }
                catch (Exception e)
                {
                }
            }

            // l ne faut surtout
            return false;
        }

        return true;
    }

    private void server(String[] args)
    {
        boolean show = true;

        if (args != null)
        {
            for (int i = 0; i < args.length; i++)
            {
                if ("--hide".equals(args[i]))
                {
                    show = false;
                }
            }
        }
    }

    public static void main(String[] args)
    {
        attach(args, true);
    }

    /**
     * Main method of the class
     * @param argv Command line arguments
     * @throws java.lang.Exception
     */
    public static final void initApp(final String[] argv)
        throws Exception
    {
        System.setProperty("apple.laf.useScreenMenuBar", "true");
        System.setProperty("Plastic.defaultTheme", "ExperienceBlue");

        new ConfigurationHandler().init();

        Properties themeProperties = new Properties();
        String loc = "/net/sf/xpontus/conf/theme.properties";
        InputStream is = XPontusFormController.class.getResourceAsStream(loc);
        themeProperties.load(is);

        GeneralOptionModel m = new GeneralOptionModel();
        GeneralOptionModel model1 = (GeneralOptionModel) m.load();

        if (model1 == null)
        {
            model1 = new GeneralOptionModel();
        }

        final String _theme = model1.getIconSet(); // m1.getTheme();
        IconUtils.getInstance().setStyle(_theme);

        String look = model1.getTheme();

        String lf = themeProperties.getProperty(look);

        if (lf == null)
        {
            if (look.equals("Java"))
            {
                lf = UIManager.getCrossPlatformLookAndFeelClassName();
            }
            else
            {
                lf = UIManager.getSystemLookAndFeelClassName();
            }

            UIManager.setLookAndFeel(lf);
        }
        else
        {
            UIManager.setLookAndFeel(new Plastic3DLookAndFeel());
        }

        EditorOptionModel em = new EditorOptionModel();
        EditorOptionModel emodel1 = (EditorOptionModel) em.load();
        String fontSize = emodel1.getFontSize();
        String fontName = emodel1.getFontName();

        UIManager.put("EditorPane.font",
            new Font(fontName, Font.PLAIN, Integer.parseInt(fontSize)));

        XMLOptionModel em2 = new XMLOptionModel();
        XMLOptionModel emodel2 = (XMLOptionModel) em2.load();
        String newValue = emodel2.getXsltProcessor();

        if (newValue.equals("Saxon 6.5.5"))
        {
            System.setProperty("javax.xml.transform.TransformerFactory",
                "com.icl.saxon.TransformerFactoryImpl");
        }
        else if (newValue.equals("Saxon-B 8.5.1"))
        {
            System.setProperty("javax.xml.transform.TransformerFactory",
                "net.sf.saxon.TransformerFactoryImpl");
        }
        else if (newValue.equals("Jd.xslt 1.5.5"))
        {
            System.setProperty("javax.xml.transform.TransformerFactory",
                "jd.xml.xslt.trax.TransformerFactoryImpl");
        }
        else
        {
            System.setProperty("javax.xml.transform.TransformerFactory",
                "org.apache.xalan.processor.TransformerFactoryImpl");
        }

        is.close();
        themeProperties = null;

        String i18nfile = "net.sf.xpontus.i18n.application";

        L10nHelper.registerLocalizationFile(i18nfile);

        String conf = "/net/sf/xpontus/conf/configuration.xml";

        ApplicationContext context = new ClassPathXmlApplicationContext(conf);

        INSTANCE = XPontusWindow.getInstance();

        INSTANCE.setApplicationContext(context);

        INSTANCE.initActions();

        INSTANCE.getFrame().setTitle("XPontus XML Editor 1.0.0-rc2");

        logger.info("building main window");

        javax.swing.ImageIcon frameicon;
        loc = "/net/sf/xpontus/icons/Sun/icone.png";
        frameicon = IconUtils.getInstance().getIcon(loc);
        INSTANCE.getFrame().setIconImage(frameicon.getImage());
        INSTANCE.getFrame().pack();
        WindowUtilities.centerOnScreen(INSTANCE.getFrame());

        INSTANCE.getFrame().setVisible(true);

        SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    try
                    {
                        if (argv.length > 0)
                        {
                            for (int i = 0; i < argv.length; i++)
                            {
                                File f = new File(argv[i]);

                                if (f.exists())
                                {
                                    INSTANCE.getPane().createEditorFromFile(f);
                                }
                            }
                        }
                    }
                    catch (Exception err)
                    {
                    }
                }
            });
        listen();
    }
}
