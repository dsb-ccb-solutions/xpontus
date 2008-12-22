/*
 * HelpActionImpl.java
 *
 * Created on Sep 2, 2007, 10:04:58 AM
 *
 * Copyright (C) 2005-2007 Yves Zoundi <yveszoundi at users dot sf dot net>
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

import net.sf.xpontus.utils.HelperUtils;

import java.awt.event.ActionListener;

import java.net.URL;

import javax.help.CSH;
import javax.help.HelpBroker;


/**
 * Action to display the help dialog
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class HelpActionImpl extends AbstractXPontusActionImpl
{
    private static final long serialVersionUID = -8866719178970983293L;
    public static final String BEAN_ALIAS = "action.help";
    private ActionListener helpListener;
    private HelpBroker broker;

    /** Creates a new instance of HelpAction */
    public HelpActionImpl()
    {
    }

    private void init()
    {
        HelperUtils utils;
        utils = new HelperUtils();

        String helpset = "/net/sf/xpontus/help/jhelpset.hs";
        URL url = getClass().getResource(helpset);
        utils.setHelpURL(url);
        broker = utils.getHelpBroker();
        broker.setCurrentID("id2475596");

        helpListener = new CSH.DisplayHelpFromSource(broker);
    }

    public void execute()
    {
        if (broker == null)
        {
            init();
        }

        helpListener.actionPerformed(getEvent());
    }
}
