/*
 * ExitActionImpl.java
 *
 * Created on June 30, 2007, 11:56 AM
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

import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;
import net.sf.xpontus.plugins.settings.DefaultSettingsModuleImpl;


/**
 * Action to terminate the program
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class ExitActionImpl extends AbstractXPontusActionImpl
{
    private static final long serialVersionUID = 2414515927696392864L;
    public static final String BEAN_ALIAS = "action.exit";

    /** Creates a new instance of ExitActionImpl */
    public ExitActionImpl()
    {
        setName("Exit");
        setDescription("Terminate the program");
    }

    /* (non-Javadoc)
     * @see net.sf.xpontus.actions.XPontusActionIF#execute()
     */
    public void execute()
    {
        // get all opened documents
        final IDocumentContainer[] editors = DefaultXPontusWindowImpl.getInstance()
                                                                     .getDocumentTabContainer()
                                                                     .getEditorsAsArray();

        // close all documents
        for (IDocumentContainer editor : editors)
        {
            DefaultXPontusWindowImpl.getInstance().getDesktop().close(editor);
        }

        // save settings 
        DefaultSettingsModuleImpl.getInstance().shutdown();

        // shutdown the plugin manager
        XPontusPluginManager.getPluginManager().shutdown();

        // terminate the application
        System.exit(0);
    }
}
