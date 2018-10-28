/*
 * SwitchWindowAction
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
 *
 *
 */
package net.sf.xpontus.actions.impl;

import com.vlsolutions.swing.docking.DockingUtilities;
import com.vlsolutions.swing.docking.TabbedDockableContainer;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;


/**
 * Action to switch between tabs like Firefox (control Page Up - Control Page Down)
 * @version 0.0.2
 * @author Yves Zoundi
 */
public class SwitchWindowAction extends AbstractAction
{
    private static final long serialVersionUID = -3065024162067083579L;
    private boolean goForward = false;

    /**
     * @param name
     * @param goForward
     */
    public SwitchWindowAction(String name, boolean goForward)
    {
        super(name);
        this.goForward = goForward;
    }

    /* (non-Javadoc)
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        DocumentTabContainer docContainer = DefaultXPontusWindowImpl.getInstance()
                                                                    .getDocumentTabContainer();

        IDocumentContainer editorContainer = (IDocumentContainer) docContainer.getCurrentDockable();

        TabbedDockableContainer container = DockingUtilities.findTabbedDockableContainer(editorContainer);

        // check whether a document is opened (ie. no tabs)
        if (container == null)
        {
            Toolkit.getDefaultToolkit().beep();

            return;
        }

        // total of documents opened
        final int nbDocuments = container.getTabCount();

        if ((nbDocuments == 0) || (nbDocuments < 1))
        {
            Toolkit.getDefaultToolkit().beep();

            return;
        }

        int index = container.indexOfDockable(editorContainer);

        int pos = 0;

        // control Page up  - select the next window (left to right)
        if (goForward)
        {
            if (index != (nbDocuments - 1))
            {
                pos = index + 1;
            }
        }
        // select the previous window (right to left)
        else
        {
            pos = (index == 0) ? (nbDocuments - 1) : (index - 1);
        }

        // get the document to select
        IDocumentContainer toSelect = (IDocumentContainer) container.getDockableAt(pos);

        // give the document the input focus
        container.setSelectedDockable(toSelect);
        toSelect.getEditorComponent().requestFocus();
        toSelect.getEditorComponent().grabFocus();
    }
}
