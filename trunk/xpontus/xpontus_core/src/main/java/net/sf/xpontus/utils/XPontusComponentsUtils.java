/*
 * XPontusComponentsUtils.java
 *
 * Created on July 1, 2007, 12:58 PM
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
package net.sf.xpontus.utils;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.XPontusTopComponentIF;

import java.awt.Component;

import javax.swing.JOptionPane;


/**
 * Helper class to retrieve for top components
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class XPontusComponentsUtils {
    public static int ERROR_MESSAGE_TYPE = JOptionPane.ERROR_MESSAGE;
    public static final int INFORMATION_MESSAGE_TYPE = JOptionPane.INFORMATION_MESSAGE;
    private static String TOP_COMPONENT_FACTORY_METHOD = "getInstance";

    /** Creates a new instance of XPontusComponentsUtils */
    private XPontusComponentsUtils() {
    }

    /**
     * Set the main window of this application
     * @param method The method name to create the main window
     */
    public static void setTopComponentFactoryMethod(String method) {
        TOP_COMPONENT_FACTORY_METHOD = method;
    }

    /**
     * Show an information dialog
     * @param message A message
     */
    public static void showInformationMessage(String message) {
        Component component = getTopComponent().getDisplayComponent();
        JOptionPane.showMessageDialog(component, message, "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Display an error dialog
     * @param message A message
     */
    public static void showErrorMessage(String message) {
        Component component = getTopComponent().getDisplayComponent();
        JOptionPane.showMessageDialog(component, message, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     * Show a warning dialog
     * @param message a message
     */
    public static void showWarningMessage(String message) {
        Component component = getTopComponent().getDisplayComponent();
        JOptionPane.showMessageDialog(component, message, "Warning",
            JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Returns the main window
     * @return The default component(The main window)
     */
    public static XPontusTopComponentIF getTopComponent() {
        return DefaultXPontusWindowImpl.getInstance();
    }
}
