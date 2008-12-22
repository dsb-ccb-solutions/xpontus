/*
 * PrintActionImpl.java
 *
 * Created on July 1, 2007, 11:31 AM
 *
 *  Copyright (C) 2005-2007 Yves Zoundi <yveszoundi at users dot sf dot net>
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
import net.sf.xpontus.utils.PrintUtilities;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import javax.swing.JEditorPane;


/**
 * Action to print a document
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PrintActionImpl extends DefaultDocumentAwareActionImpl
{
    private static final long serialVersionUID = -4689493792699685503L;
    public static final String BEAN_ALIAS = "action.print";

    /** Creates a new instance of PrintAction */
    public PrintActionImpl()
    {
    }

    /**
     * Print the document in a thread
     */
    public void run()
    {
        DefaultXPontusWindowImpl frame = (DefaultXPontusWindowImpl) XPontusComponentsUtils.getTopComponent();
        new PrintUtilities().print((JEditorPane) frame.getDocumentTabContainer()
                                                      .getCurrentEditor());
    }
}
