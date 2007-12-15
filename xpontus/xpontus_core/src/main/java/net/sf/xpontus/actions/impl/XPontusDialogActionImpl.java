/*
 * XPontusDialogActionImpl.java
 *
 * Created on May 25, 2007, 5:59 PM
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
package net.sf.xpontus.actions.impl;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;


/**
 * Event handler which display a dialog on top of a main window
 * @author Yves Zoundi
 */
public class XPontusDialogActionImpl extends AbstractXPontusActionImpl
    implements Runnable {
    private String dialogClassName;
    private ClassLoader windowClassLoader;

    public ClassLoader getWindowClassLoader() {
        return windowClassLoader;
    }

    public void setWindowClassLoader(ClassLoader windowClassLoader) {
        this.windowClassLoader = windowClassLoader;
    }
    
    private JDialog dialog;
    private Log log = LogFactory.getLog(XPontusDialogActionImpl.class);

    /**
     * Creates a new instance of XPontusDialogActionImpl
     */
    public XPontusDialogActionImpl() {
    }

    /**
     * Create a dialog and display it
     */
    public void execute() {
        initComponents();
        System.out.print("dialog not null:" + (dialog!=null));
          System.out.print("frame not null:" + (DefaultXPontusWindowImpl.getInstance().getDisplayComponent()!=null));
        dialog.setLocationRelativeTo(DefaultXPontusWindowImpl.getInstance().getDisplayComponent());
        SwingUtilities.invokeLater(this);
    }

    private void initComponents() {
//        System.out.println("init components");
        if (dialog == null) {
            try {
                 if(windowClassLoader!=null){
//                     System.out.println("classloader");
                     dialog = (JDialog) Class.forName(dialogClassName, true, windowClassLoader).newInstance();
                 }
                 else{
//                     System.out.println("no classloader");
//                     System.out.println(dialogClassName);
                     dialog = (JDialog) Class.forName(dialogClassName).newInstance();
                    
                 }
                
                log.info("Created dialog for class:" + this.dialogClassName);
            } catch (Exception ex) {
                log.error(ex.getMessage());
            }
        }
    }

    /**
     * Implementation of <code>java.lang.Runnable</code> interface
     */
    public void run() {
        dialog.setVisible(true);
    }

    /**
     * Returns the class name of the dialog to create
     * @return The class name of the dialog to create
     */
    public String getDialogClassName() {
        return dialogClassName;
    }

    /**
     * The class name of the dialog to create
     * @param dialogClassName The dialog class name
     */
    public void setDialogClassName(String dialogClassName) {
        this.dialogClassName = dialogClassName;
    }
}
