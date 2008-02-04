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

import java.lang.reflect.Method;

import javax.swing.JOptionPane;


/**
 * Helper class to retrieve for top components
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class XPontusComponentsUtils {
    public static int ERROR_MESSAGE_TYPE = JOptionPane.ERROR_MESSAGE;
    public static final int INFORMATION_MESSAGE_TYPE = JOptionPane.INFORMATION_MESSAGE;
    private static Object m_obj = null;
    private static String TOP_COMPONENT_FACTORY_METHOD = "getInstance";

    /** Creates a new instance of XPontusComponentsUtils */
    private XPontusComponentsUtils() {
    }

    /**
     *
     * @param method
     */
    public static void setTopComponentFactoryMethod(String method) {
        TOP_COMPONENT_FACTORY_METHOD = method;
    }

    /**
     *
     * @param message
     */
    public static void showInformationMessage(String message) {
        Component component = getTopComponent().getDisplayComponent();
        JOptionPane.showMessageDialog(component, message, "Information",
            JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     *
     * @param message
     */
    public static void showErrorMessage(String message) {
        Component component = getTopComponent().getDisplayComponent();
        JOptionPane.showMessageDialog(component, message, "Error",
            JOptionPane.ERROR_MESSAGE);
    }

    /**
     *
     * @param message
     */
    public static void showWarningMessage(String message) {
        Component component = getTopComponent().getDisplayComponent();
        JOptionPane.showMessageDialog(component, message, "Warning",
            JOptionPane.WARNING_MESSAGE);
    }

    /**
     *
     * @return
     */
    public static XPontusTopComponentIF getTopComponent() {
        return DefaultXPontusWindowImpl.getInstance();
    }
}
