/*
 *
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
 *
 *
 */
package net.sf.xpontus.utils;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;

import org.apache.commons.io.IOCase;
import org.apache.commons.io.filefilter.WildcardFileFilter;
import org.apache.commons.lang.text.StrBuilder;

import java.awt.Component;
import java.awt.Font;
import java.awt.Window;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetAdapter;
import java.awt.dnd.DropTargetDropEvent;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;

import java.net.URL;

import java.util.Iterator;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;


/**
 * Hopefully his class will be doing lots of usefull stuff
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class GUIUtils {
    // the constructor should not be called
    private GUIUtils() {
    }

    public static FileFilter createFilter(final String description,
        final String[] patterns) {
        FileFilter m_filter = new FileFilter() {
                public boolean accept(File f) {
                    WildcardFileFilter a = new WildcardFileFilter(patterns,
                            IOCase.INSENSITIVE);

                    if (f.isDirectory()) {
                        return true;
                    }

                    return a.accept(f);
                }

                public String getDescription() {
                    return description;
                }
            };

        return m_filter;
    }

    public static void installDefaultFilters(JFileChooser chooser) {
        chooser.addChoosableFileFilter(createFilter("XML Files",
                new String[] { "*.xml", "*.xhtml" }));
        chooser.addChoosableFileFilter(createFilter("XSL stylesheets",
                new String[] { "*.xsl", "*.xslt" }));
        chooser.addChoosableFileFilter(createFilter("HTML Files",
                new String[] { "*.html", "*.htm" }));
        chooser.setFileFilter(chooser.getAcceptAllFileFilter());
    }

    /**
     * Return a font
     * @param fontString the font information
     * @return a font from a string descriptor
     */
    public static Font getFontFromString(String fontString) {
        String[] m_table = fontString.split(",");

        String m_family = m_table[0];
        int m_style = Integer.parseInt(m_table[1]);
        int m_size = Integer.parseInt(m_table[2]);

        Font m_font = new Font(m_family, m_style, m_size);

        return m_font;
    }

    /**
     * Returns a String from a font
     * @param m_font The font to convert
     * @return a String from a font
     */
    public static String fontToString(Font m_font) {
        StrBuilder sb = new StrBuilder();
        sb.append(m_font.getFamily());
        sb.append(",");
        sb.append("" + m_font.getStyle());
        sb.append(",");
        sb.append(m_font.getSize());

        return sb.toString();
    }

    /**
     * This function calls Component.setFocusableWindowState (in java >= 1.4) to  keep GUI
     * consistent with java 1.3.x
     *
     * @param aObj TODO: DOCUMENT ME!
     * @param aFlag TODO: DOCUMENT ME!
     */
    public static final void setFocusableWindowState(Window aObj, boolean aFlag) {
        try {
            //try to call setFocusableWindowState (true) on java 1.4 while staying compatible with Java 1.3
            aObj.getClass()
                .getMethod("setFocusableWindowState",
                new Class[] { Boolean.TYPE }).invoke(aObj,
                new Object[] { aFlag ? Boolean.TRUE : Boolean.FALSE });
        } catch (java.lang.NoSuchMethodException ex) {
        } catch (java.lang.IllegalAccessException ex) {
        } catch (java.lang.reflect.InvocationTargetException ex) {
        }
    }

    public static void installDragAndDropSupport(Component component) {
        new DropTarget(component,
            new DropTargetAdapter() {
                public void drop(DropTargetDropEvent e) {
                    try {
                        Transferable t = e.getTransferable();
                        e.acceptDrop(e.getDropAction());

                        DataFlavor[] flavors = e.getCurrentDataFlavors();
                        URL fileURL = null;
                        java.util.List fileList = null;
                        String dndString = null;
                        DataFlavor urlFlavor = null;
                        DataFlavor listFlavor = null;
                        DataFlavor stringFlavor = null;

                        if (flavors != null) {
                            for (int i = 0; i < flavors.length; i++) {
                                // System.out.println(flavors[i]);
                                if (flavors[i].getRepresentationClass() == java.net.URL.class) {
                                    urlFlavor = flavors[i];
                                } else if (flavors[i].getRepresentationClass() == java.util.List.class) {
                                    listFlavor = flavors[i];
                                } else if (flavors[i].getRepresentationClass() == java.lang.String.class) {
                                    stringFlavor = flavors[i];
                                }
                            }
                        } else {
                            System.out.println("flavors == null");

                            return;
                        }

                        if (stringFlavor != null) {
                            try {
                                Object obj = e.getTransferable()
                                              .getTransferData(stringFlavor);

                                if (obj instanceof String) {
                                    dndString = (String) obj;
                                }
                            } catch (Throwable tr) {
                            }

                            if (dndString != null) {
                                StringReader reader = new StringReader(dndString.trim());
                                BufferedReader mReader = new BufferedReader(reader);

                                String line = null;

                                while ((line = mReader.readLine()) != null) {
                                    URL url = new URL(line.trim());
                                    System.out.println("String");

                                    if (url.getProtocol().equals("file")) {
                                        DefaultXPontusWindowImpl.getInstance()
                                                                .getDocumentTabContainer()
                                                                .createEditorFromFile(new File(
                                                url.getPath()));
                                    } else {
                                        DefaultXPontusWindowImpl.getInstance()
                                                                .getDocumentTabContainer()
                                                                .createEditorFromURL(url);
                                    }
                                }
                            }
                        }

                        try {
                            Object obj = e.getTransferable().getTransferData(urlFlavor);

                            if (obj instanceof URL) {
                                fileURL = (URL) obj;
                            }
                        } catch (Throwable tr) {
                        }

                        if (fileURL != null) {
                            URL url = fileURL;
                            System.out.println("file");
                            DefaultXPontusWindowImpl.getInstance()
                                                    .getDocumentTabContainer()
                                                    .createEditorFromURL(url);
                        }

                        try {
                            Object obj = e.getTransferable().getTransferData(listFlavor);

                            if (obj instanceof java.util.List) {
                                fileList = (java.util.List) obj;
                            }
                        } catch (Throwable tr) {
                        }

                        if (fileList != null) {
                            Iterator it = fileList.iterator();

                            while (it.hasNext()) {
                                Object file = it.next();

                                if (file instanceof File) {
                                    DefaultXPontusWindowImpl.getInstance()
                                                            .getDocumentTabContainer()
                                                            .createEditorFromFile((File) file);
                                }
                            }
                        }

                        try {
                            e.dropComplete(true);
                        } catch (Throwable tr) {
                            System.out.println("???? " + t);
                        }
                    } catch (Exception ex) {
                        //                    ex.printStackTrace();
                    }
                }
            });
    }
}
