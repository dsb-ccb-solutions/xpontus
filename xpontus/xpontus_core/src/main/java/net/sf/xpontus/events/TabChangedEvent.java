/*
 * TabChangedEvent.java
 *
 * Created on 24 avril 2007, 15:20
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
package net.sf.xpontus.events;

import net.sf.xpontus.modules.gui.components.IDocumentContainer;

import java.util.EventObject;


/**
 * @author Yves Zoundi
 *
 */
public class TabChangedEvent extends EventObject
{
    private static final long serialVersionUID = 8017004140893384773L;
    private final IDocumentContainer documentContainer;

    /**
     * @param documentContainer
     */
    public TabChangedEvent(final IDocumentContainer documentContainer)
    {
        super(documentContainer);
        this.documentContainer = documentContainer;
    }

    /* (non-Javadoc)
     * @see java.util.EventObject#getSource()
     */
    public IDocumentContainer getSource()
    {
        return documentContainer;
    }
}
