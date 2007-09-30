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


/**
 * Action to terminate the program
 * @author Yves Zoundi
 */
public class ExitActionImpl extends AbstractXPontusActionImpl {
    public static final String BEAN_ALIAS = "action.exit";

    /** Creates a new instance of ExitActionImpl */
    public ExitActionImpl() {
        setName("Exit");
        setDescription("Terminate the program");
    }

    // Terminate the program
    public void execute() {
        // terminate the program
        
        // exit without errors

        /** TODO
         * Add a queue for objects which needs to free resources
         *
         * The plugin manager must be shutdown
         * The opened files must be closed and maybe saved
         * The remote connections must be closed
         *
         * All the pending operations must terminate
         *
         */
        System.exit(0);
    }
}
