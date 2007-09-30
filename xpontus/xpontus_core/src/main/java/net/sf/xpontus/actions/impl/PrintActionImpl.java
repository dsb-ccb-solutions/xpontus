/*
 * PrintActionImpl.java
 *
 * Created on July 1, 2007, 11:31 AM
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

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.RepaintManager;


/**
 * Action to print a document
 * @author Yves Zoundi
 */
public class PrintActionImpl extends XPontusThreadedActionImpl
    implements Printable {
    public static final String BEAN_ALIAS = "action.print";
    private Component componentToBePrinted;

    /** Creates a new instance of PrintAction */
    public PrintActionImpl() {
    }

    /**
     * Print the document in a thread
     */
    public void run() {
        DefaultXPontusWindowImpl frame = (DefaultXPontusWindowImpl) XPontusComponentsUtils.getTopComponent();
        componentToBePrinted = frame.getDocumentTabContainer().getCurrentEditor();
        print();
    }

    /**
     * show the print dialog
     */
    public void print() {
        PrinterJob printJob = PrinterJob.getPrinterJob();
        printJob.setPrintable(this);

        if (printJob.printDialog()) {
            try {
                printJob.print();
            } catch (PrinterException pe) {
                getLogger().info("Error printing: " + pe.getMessage());
            }
        }
    }

    /**
     * Print a page
     * @param g The graphics
     * @param pageFormat The page format
     * @param pageIndex The page index
     * @return whether the page exists
     */
    public int print(Graphics g, PageFormat pageFormat, int pageIndex) {
        if (pageIndex > 0) {
            return (NO_SUCH_PAGE);
        } else {
            Graphics2D g2d = (Graphics2D) g;
            g2d.translate(pageFormat.getImageableX(), pageFormat.getImageableY());
            disableDoubleBuffering(componentToBePrinted);
            componentToBePrinted.paint(g2d);
            enableDoubleBuffering(componentToBePrinted);

            return (PAGE_EXISTS);
        }
    }

    /**
     *
     * @param c
     */
    public static void disableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(false);
    }

    /**
     *
     * @param c
     */
    public static void enableDoubleBuffering(Component c) {
        RepaintManager currentManager = RepaintManager.currentManager(c);
        currentManager.setDoubleBufferingEnabled(true);
    }
}
