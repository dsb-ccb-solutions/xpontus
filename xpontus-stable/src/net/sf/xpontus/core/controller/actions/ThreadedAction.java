/*
 * ThreadedCommandAction.java
 *
 * Created on February 11, 2006, 8:47 PM
 *
 *
 *  Copyright (C) 2005 Yves Zoundi
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
package net.sf.xpontus.core.controller.actions;

import com.sun.java.help.impl.SwingWorker;

import java.awt.event.ActionEvent;


/**
 * An action which executes itself in a separate thread
 * @author Yves Zoundi
 */
public abstract class ThreadedAction extends BaseAction {
    /** Creates a new instance of ThreadedAction */
    public ThreadedAction() {
    }

    /**
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent event) {
        this.setEvent(event);

        final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    execute();

                    return null;
                }
            };

        worker.start();
    }
}
