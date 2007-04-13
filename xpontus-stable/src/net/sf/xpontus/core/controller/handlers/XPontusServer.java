/*
 * XPontusServer.java
 *
 * Created on April 12, 2007, 6:40 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.core.controller.handlers;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import net.sf.xpontus.controller.handlers.XPontusFormController;

/**
 *
 * @author mrcheeks
 */
public class XPontusServer { 
    
	
	/**
	 * The port on which the server listens for files to open in the editor.
	 * TODO: this is also worth implementing a commandline switch or should#
	 * get an entry in the settings resource bundle.
	 */
	public static final int PORT = 9876;
	
	/**
	 * The marker for the end of a transmission between client and server 
	 * sockets.
	 */
	private static final String EOT = ".";
	
	/** The server socket instance. */
	private ServerSocket server;
	
	/** The actual editor instance. */
	private XPontusFormController editor;
	
	/**
	 * Creates a new server with an editor. The files are opened in the editor.
	 * The server starts listenening for socket connections to receive file 
	 * names to open in the editor.	 
	 *
	 * @param args the file names (or other arguments as "--hide")
	 */
	private XPontusServer(String args[]) {
		boolean show = true;
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				if ("--hide".equals(args[i])) {
					show = false;
				}
			}
		}
                
		listen();
	}
	
	/**
	 * Converts an array of strings into an array of files ignoring non-existing 
	 * files.
	 *
	 * @param args the arguments
	 * 
	 * @param files the files
	 */
	private static File[] convertArgsToFiles(String[] args) {
		List files = new ArrayList();
		if (args != null) {
			for (int i = 0; i < args.length; i++) {
				String fileName = args[i];
				File f = null;//new File(Editor.replaceBackslashBySlash(fileName)).getAbsoluteFile();
				if (f.exists()) {
					files.add(f);
				}
				else {
					System.err.println("File not found: " + fileName);
				}
			}
		}
		return (File[]) files.toArray(new File[files.size()]);
	}
	
	/**
	 * Causes the server to start its server socket and to listen for client
	 * connections. If a client is accepted the server reads its input stream 
	 * line by line until the end marker is found or the socket is closed.
	 * The lines are interpreted as file names and finally forwarded to the 
	 * editor for opening.
	 */
	private void listen() {
		try {
			if(server != null) {
				System.err.println("WARNING: Server already running");
			}
			else {
				server = new ServerSocket(PORT);
				do {
					Socket socket = server.accept();
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					while(!socket.isClosed() && !socket.isInputShutdown()) {
						sleep(100);
						if(!in.ready())
							continue;
						try {
							String line = in.readLine();
							if(EOT.equals(line))
								break;
//							editor.openFile(new File(line).getAbsoluteFile());
						}
						catch(RuntimeException e) {
							e.printStackTrace();
						}
					}
//					editor.setVisible(true);
//					editor.setExtendedState(0);
//					editor.toFront();
				} while(true);
			}
		}
		catch(Exception e) {
			try {
				server.close();
			}
			catch(IOException x) { }
			server = null;
			e.printStackTrace();
		}
	}
	
	/**
	 * Cause the calling thread to sleep some time.
	 *
	 * @param millis time in milliseconds
	 */
	private static void sleep(long delay) {
		try {
			Thread.sleep(delay);
		}
		catch(InterruptedException e) { }
	}
	
	/**
	 * Tries to attach the provided arguments to a running server socket and 
	 * returns true if the server socket has accepted the transmission.
	 *
	 * @param args the arguments for the server
	 *
	 * @return true if a server has accepted the arguments, false otherwise
	 *		in particular if no server was found.
	 */
	public static boolean attach(String args[]) {
		try {
			Socket socket = new Socket("localhost", PORT);
			BufferedWriter out = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
			for(int i = 0; i < args.length; i++) {
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
		catch (IOException e) {
		}
		
		return false;
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
	 *		otherwise which means that a new server was started if 
	 *		<code>startServerIfNeeded</code> is set to true or that .
	 */
	public static boolean attach(String args[], boolean startServerIfNeeded) {
		if(!attach(args)) {
			if (startServerIfNeeded) {
				System.err.println("NBEdit server not yet started - starting now ...");
				new XPontusServer(args);
			}
			return false;
		}
		return true;
	}	

	/**
	 * Forwards to {@link attach(String args[], true)}
	 *
	 * @param args the arguments for the server
	 */
	public static void main(String args[]) {
		attach(args, true);
	}	
} 
