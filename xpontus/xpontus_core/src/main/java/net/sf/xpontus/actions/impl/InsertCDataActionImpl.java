/*
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
 */
package net.sf.xpontus.actions.impl;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;

import javax.swing.text.JTextComponent;


/**
 * Insert a CDATA section
 * @author Yves Zoundi
 * @version 0.0.2
 */
public class InsertCDataActionImpl extends DefaultDocumentAwareActionImpl
{
    private static final long serialVersionUID = 462880528241084121L;
    public static final String BEAN_ALIAS = "action.insertcdata";

    public InsertCDataActionImpl()
    {
    }

    public void run()
    {
        DocumentTabContainer documentTabContainer = DefaultXPontusWindowImpl.getInstance()
                                                                            .getDocumentTabContainer();

        JTextComponent textComponent = documentTabContainer.getCurrentEditor();
        int pos = textComponent.getSelectionStart();

        try
        {
            textComponent.getDocument().insertString(pos, "<![CDATA[  ]]>", null);
            textComponent.setCaretPosition(pos + 10);
            textComponent.requestFocus();
            textComponent.grabFocus();
        }
        catch (Exception e)
        {
            getLogger().warn(e.getMessage(), e);
        }
    }
}
