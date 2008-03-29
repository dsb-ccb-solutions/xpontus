/*
 *
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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
package net.sf.xpontus.utils;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.JEditorPane;
import javax.swing.text.Document;
import javax.swing.text.View;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PrintUtilities implements Printable {
    int currentPage = -1;
    JEditorPane printPane;
    double pageEndY = 0;
    double pageStartY = 0;
    boolean scaleWidthToFit = true;
    PageFormat pFormat;
    PrinterJob pJob;

    public PrintUtilities() {
        pFormat = new PageFormat();
        pJob = PrinterJob.getPrinterJob();
    }

    private Document getDocument() {
        if (printPane != null) {
            return printPane.getDocument();
        } else {
            return null;
        }
    }

    private boolean getScaleWidthToFit() {
        return scaleWidthToFit;
    }

    private void pageDialog() {
        pFormat = pJob.pageDialog(pFormat);
    }

    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
        double scale = 1.0;
        Graphics2D graphics2D;
        View rootView;

        graphics2D = (Graphics2D) graphics;
        printPane.setSize((int) pageFormat.getImageableWidth(),
            Integer.MAX_VALUE);
        printPane.validate();

        rootView = printPane.getUI().getRootView(printPane);

        if ((scaleWidthToFit) &&
                (printPane.getMinimumSize().getWidth() > pageFormat.getImageableWidth())) {
            scale = pageFormat.getImageableWidth() / printPane.getMinimumSize()
                                                              .getWidth();
            graphics2D.scale(scale, scale);
        }

        graphics2D.setClip((int) (pageFormat.getImageableX() / scale),
            (int) (pageFormat.getImageableY() / scale),
            (int) (pageFormat.getImageableWidth() / scale),
            (int) (pageFormat.getImageableHeight() / scale));

        if (pageIndex > currentPage) {
            currentPage = pageIndex;
            pageStartY += pageEndY;
            pageEndY = graphics2D.getClipBounds().getHeight();
        }

        graphics2D.translate(graphics2D.getClipBounds().getX(),
            graphics2D.getClipBounds().getY());

        Rectangle allocation = new Rectangle(0, (int) -pageStartY,
                (int) (printPane.getMinimumSize().getWidth()),
                (int) (printPane.getPreferredSize().getHeight()));

        if (printView(graphics2D, allocation, rootView)) {
            return Printable.PAGE_EXISTS;
        } else {
            pageStartY = 0;
            pageEndY = 0;
            currentPage = -1;

            return Printable.NO_SUCH_PAGE;
        }
    }

    public void print(JEditorPane pane) {
        //pane = (JComponent)new JEditorPane();
        setDocument(pane);
        printDialog();
    }

    private void printDialog() {
        if (pJob.printDialog()) {
            pJob.setPrintable(this, pFormat);

            try {
                pJob.print();
            } catch (PrinterException printerException) {
                pageStartY = 0;
                pageEndY = 0;
                currentPage = -1;
                System.out.println("Error Printing Document");
            }
        }
    }

    private boolean printView(Graphics2D graphics2D, Shape allocation, View view) {
        boolean pageExists = false;
        Rectangle clipRectangle = graphics2D.getClipBounds();
        Shape childAllocation;
        View childView;

        if (view.getViewCount() > 0) {
            for (int i = 0; i < view.getViewCount(); i++) {
                childAllocation = view.getChildAllocation(i, allocation);

                if (childAllocation != null) {
                    childView = view.getView(i);

                    if (printView(graphics2D, childAllocation, childView)) {
                        pageExists = true;
                    }
                }
            }
        } else {
            if (allocation.getBounds().getMaxY() >= clipRectangle.getY()) {
                pageExists = true;

                if ((allocation.getBounds().getHeight() > clipRectangle.getHeight()) &&
                        (allocation.intersects(clipRectangle))) {
                    view.paint(graphics2D, allocation);
                } else {
                    if (allocation.getBounds().getY() >= clipRectangle.getY()) {
                        if (allocation.getBounds().getMaxY() <= clipRectangle.getMaxY()) {
                            view.paint(graphics2D, allocation);
                        } else {
                            if (allocation.getBounds().getY() < pageEndY) {
                                pageEndY = allocation.getBounds().getY();
                            }
                        }
                    }
                }
            }
        }

        return pageExists;
    }

    private void setContentType(String type) {
        printPane.setContentType(type);
    }

    private void setDocument(JEditorPane pane) {
        printPane = new JEditorPane();
        setDocument(pane.getContentType(), pane.getDocument());
    }

    private void setDocument(String type, Document document) {
        setContentType(type);
        printPane.setDocument(document);
    }

    private void setScaleWidthToFit(boolean scaleWidth) {
        scaleWidthToFit = scaleWidth;
    }
}
