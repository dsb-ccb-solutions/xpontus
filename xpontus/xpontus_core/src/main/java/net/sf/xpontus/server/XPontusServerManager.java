/*
 * XPontusServerManager.java
 *
 * Created on Aug 12, 2007, 4:54:39 PM
 *
 * Copyright (C) 2005-2008 Yves Zoundi
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
package net.sf.xpontus.server;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

import java.net.ServerSocket;
import java.net.Socket;


/**
 *
 * @author Yves Zoundi
 */
public class XPontusServerManager {
    public static final int PORT = 9876;
    private static final String EOT = ".";

    /** The server socket instance. */
    private static ServerSocket server;
    private static String theme = null;
    private static Log logger = LogFactory.getLog(XPontusServerManager.class);

    public XPontusServerManager() {
    }

    /**
     * Cause the calling thread to sleep some time.
     *
     * @param millis time in milliseconds
     */
    private static void sleep(long delay) {
        try {
            Thread.sleep(delay);
        } catch (InterruptedException e) {
        }
    }

    public static boolean attach(String[] args) {
        try {
            Socket socket = new Socket("localhost", PORT);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                        socket.getOutputStream()));

            for (int i = 0; i < args.length; i++) {
                out.write((new File(args[i])).getAbsolutePath());
                out.newLine();
            }

            out.write(EOT);
            out.newLine();
            out.flush();
            out.close();
            socket.close();

            return true;
        } catch (IOException e) {
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
    public static boolean attach(String[] args, boolean startServerIfNeeded) {
        if (!attach(args)) {
            if (startServerIfNeeded) {
                System.err.println(
                    "XPontus Server not yet started - starting now ...");

                try {
                    //                    initApp(args);
                    listen();
                } catch (Exception e) {
                }
            }

            // l ne faut surtout
            return false;
        }

        return true;
    }

    public static void main(String[] args) {
        attach(args, true);
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
    private static void listen() {
        try {
            if (server != null) {
                System.err.println("WARNING: Server already running");
            } else {
                server = new ServerSocket(PORT);

                do {
                    Socket socket = server.accept();
                    BufferedReader in = new BufferedReader(new InputStreamReader(
                                socket.getInputStream()));

                    while (!socket.isClosed() && !socket.isInputShutdown()) {
                        sleep(100);

                        if (!in.ready()) {
                            continue;
                        }

                        try {
                            String line = in.readLine();

                            if (EOT.equals(line)) {
                                break;
                            }

                            //                            INSTANCE.getPane()
                            //                                    .createEditorFromFile(new File(line));
                        } catch (RuntimeException e) {
                            e.printStackTrace();
                        }
                    }
                } while (true);
            }
        } catch (Exception e) {
            try {
                server.close();
            } catch (IOException x) {
            }

            server = null;
            e.printStackTrace();
        }
    }

    private static void openFiles(String[] args) {
        if (args != null) {
            for (int i = 0; i < args.length; i++) {
                String fileName = args[i];
                File f = new File(fileName);

                //                if (f.exists())
                //                {
                //                    INSTANCE.getPane().createEditorFromFile(f);
                //                }
                //                else
                //                {
                //                    System.err.println("File not found: " + fileName);
                //                }
            }
        }
    }
}
