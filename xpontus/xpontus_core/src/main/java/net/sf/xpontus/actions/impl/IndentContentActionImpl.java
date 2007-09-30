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
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;
import net.sf.xpontus.plugins.indentation.IndentationPluginIF;
import net.sf.xpontus.properties.PropertiesHolder;

import java.util.Hashtable;
import java.util.Vector;

import javax.swing.text.JTextComponent;


/**
 * Class description
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class IndentContentActionImpl extends XPontusThreadedActionImpl {
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

        Vector v = container.getEditorsAsVector();

        if (v.size() == 0) {
            return;
        }

        JTextComponent jtc = container.getCurrentEditor();

        String contentType = jtc.getClientProperty(XPontusConstantsIF.CONTENT_TYPE)
                                .toString();

        Hashtable ht = (Hashtable) PropertiesHolder.getPropertyValue(XPontusConstantsIF.XPONTUS_INDENTATION_ENGINES);

        if ((ht == null) || (ht.size() == 0)) {
            getLogger()
                .info("Did you forget to call the init method of the indentationplugin??");
            hasplugins = false;
            setEnabled(false);
            this.setDescription(
                "No indenter engines registered. Please install some plugins");

            return;
        }

        System.out.println("content type:" + contentType);
        
        if (ht.containsKey(contentType)) {
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
            } catch (Exception ex) {
                getLogger().error(ex.getLocalizedMessage());
            }
        }
        else{
            System.out.println("no indenter engine for content type:" + contentType);
        }
    }
}
