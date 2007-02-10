/*
 * HelpAction.java
 *
 * Created on 2 octobre 2005, 16:25
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
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.core.utils.HelperUtils;
import net.sf.xpontus.view.XPontusWindow;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;


/**
 * Action to open the help window
 * @author Yves Zoundi
 */
public class HelpAction extends BaseAction {
    private java.awt.event.ActionListener helpListener;
    private javax.help.HelpBroker broker;

    /** Creates a new instance of HelpAction */
    public HelpAction() {
    }

    private void init() {
        HelperUtils utils;
        utils = new HelperUtils();

        String helpset = "/net/sf/xpontus/help/jhelpset.hs";
        java.net.URL url = getClass().getResource(helpset);
        utils.setHelpURL(url);
        broker = utils.getHelpBroker();

        javax.swing.JComponent label = new javax.swing.JLabel() {
                java.util.HashMap map;

                private void setup() {
                    map = new java.util.HashMap();
                    map.put(RenderingHints.KEY_TEXT_ANTIALIASING,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                    map.put(RenderingHints.KEY_DITHERING,
                        RenderingHints.VALUE_DITHER_ENABLE);
                    map.put(RenderingHints.KEY_FRACTIONALMETRICS,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                }

                public void paintComponent(Graphics g) {
                    Graphics2D g2d = (Graphics2D) g;

                    if (map == null) {
                        setup();
                    }

                    RenderingHints hints = new RenderingHints(map);
                    g2d.setRenderingHints(hints);

                    super.paint(g2d);
                }
            };

        broker.setFont(label.getFont());

        java.util.List li = new java.util.ArrayList();

        java.util.Enumeration _e = broker.getHelpSet().getLocalMap().getAllIDs();

        while (_e.hasMoreElements()) {
            String elem = _e.nextElement().toString();
            li.add(elem);
        }

        java.util.Collections.sort(li);

        String firstId = li.get(0).toString();
        broker.setCurrentID(firstId.split(":")[1].split(",")[0].trim());

        helpListener = new javax.help.CSH.DisplayHelpFromSource(broker);
    }

    public void execute() {
        if (broker == null) {
            init();
        }

        helpListener.actionPerformed(getEvent());
    }
}
