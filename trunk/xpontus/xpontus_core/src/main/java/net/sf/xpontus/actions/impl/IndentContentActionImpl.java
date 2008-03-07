/*
 * IndentContentAction.java
 *
 * Created on 8-Aug-2007, 5:53:52 PM
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
 */
package net.sf.xpontus.actions.impl;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusFileConstantsIF;
import net.sf.xpontus.controllers.impl.ModificationHandler;
import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.plugins.indentation.IndentationPluginIF;
import net.sf.xpontus.properties.PropertiesHolder;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Toolkit;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.text.JTextComponent;


/**
 * Format a document
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class IndentContentActionImpl extends DefaultDocumentAwareActionImpl {
    /**
     * The alias of this class in the IOC container
     */
    public static final String BEAN_ALIAS = "action.indent";
    private boolean hasplugins = true;
    private Hashtable table = new Hashtable();

    public IndentContentActionImpl() {
    }

    public void run() {
        if (!hasplugins) {
            return;
        }

        DocumentTabContainer container = DefaultXPontusWindowImpl.getInstance()
                                                                 .getDocumentTabContainer();

        IDocumentContainer dc = (IDocumentContainer) container.getCurrentDockable();

        ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                              .getConsole();

        OutputDockable odk = (OutputDockable) console.getDockableById(MessagesWindowDockable.DOCKABLE_ID);

        Vector v = container.getEditorsAsVector();

        if (v.size() == 0) {
            return;
        }

        JTextComponent jtc = container.getCurrentEditor();

        String contentType = jtc.getClientProperty(XPontusConstantsIF.CONTENT_TYPE)
                                .toString();

        Hashtable ht = (Hashtable) PropertiesHolder.getPropertyValue(XPontusConstantsIF.XPONTUS_INDENTATION_ENGINES);

        if ((ht == null) || (ht.size() == 0)) {
            String msg = "Did you installed some plugins in the category (Indentation)?";

            getLogger().info(msg);

            XPontusComponentsUtils.showWarningMessage(msg);
            hasplugins = false;
            setEnabled(false);
            this.setDescription(
                "No indenter engines registered. Please install some plugins");

            return;
        }

        jtc.putClientProperty(XPontusFileConstantsIF.FILE_LOCKED, Boolean.TRUE);

        if (ht.containsKey(contentType)) {
            dc.getStatusBar().setMessage("Formatting document...");

            try {
                Hashtable m_ht = (Hashtable) ht.get(contentType);

                IndentationPluginIF indenter = null;

                if (!table.containsKey(contentType)) {
                    ClassLoader cl = (ClassLoader) m_ht.get(XPontusConstantsIF.CLASS_LOADER);
                    String m_class = m_ht.get(XPontusConstantsIF.OBJECT_CLASSNAME)
                                         .toString();
                    Class m_obj = cl.loadClass(m_class);
                    indenter = (IndentationPluginIF) m_obj.newInstance();
                    table.put(contentType, indenter);
                } else {
                    indenter = (IndentationPluginIF) table.get(contentType);
                }

                indenter.run();
                jtc.putClientProperty(XPontusFileConstantsIF.FILE_LOCKED, null);

                ModificationHandler handler = (ModificationHandler) jtc.getClientProperty(XPontusConstantsIF.MODIFICATION_HANDLER);
                handler.setModified(true);

                dc.getStatusBar().setMessage("Formatting succeeded...");
                odk.println("Indentation succeeded");
                jtc.setCaretPosition(0);
            } catch (Exception ex) {
                dc.getStatusBar().setMessage("Formatting failed...");
                odk.println(ex.getMessage(), OutputDockable.RED_STYLE);
                getLogger().error(ex.getLocalizedMessage());
            } finally {
                Toolkit.getDefaultToolkit().beep();
                console.setFocus(MessagesWindowDockable.DOCKABLE_ID);
            }
        } else {
            odk.println("no indenter engine for content type:" + contentType,
                OutputDockable.RED_STYLE);
            getLogger()
                .warn("no indenter engine for content type:" + contentType);
        }
    }
}