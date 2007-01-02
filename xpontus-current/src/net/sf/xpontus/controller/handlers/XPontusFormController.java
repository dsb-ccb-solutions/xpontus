/*
 * XPontusFormController.java
 *
 * Created on 18 juillet 2005, 02:29
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
package net.sf.xpontus.controller.handlers;

import net.sf.xpontus.core.utils.BeanContainer;
import net.sf.xpontus.core.utils.IconUtils;
import net.sf.xpontus.core.utils.WindowUtilities;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 * Main controller/Launcher of the application
 * @author Yves Zoundi
 */
public class XPontusFormController
  {
    private static String theme = null;
    private static Log logger = LogFactory.getLog(XPontusFormController.class);

    static
      {
        System.setProperty("org.xml.sax.driver",
            "org.apache.xerces.parsers.SAXParser");
      }

    /** Creates a new instance of XPontusFormController */
    public XPontusFormController()
      {
        ConfigurationHandler.init();

        String conf = "/net/sf/xpontus/conf/xpontus.properties";

        BeanContainer ctx = BeanContainer.getInstance();

        ctx.setup(conf);

        XPontusWindow form = XPontusWindow.getInstance();

        form.setApplicationContext(ctx);

        form.init();

        form.initWindow();

        form.initActions();

        form.getFrame().setTitle("XPontus XML Editor 1.0.0rc3");

        //        new XPontusPluginLoader().boot();
        javax.swing.ImageIcon frameicon;
        String loc = "/net/sf/xpontus/icons/Sun/icone.png";
        frameicon = IconUtils.getInstance().getIcon(loc);
        form.getFrame().setIconImage(frameicon.getImage());
        form.getFrame().pack();

        WindowUtilities.centerOnScreen(form.getFrame());

        form.getFrame().setVisible(true);
      }

    /**
     * Main method of the class
     * @param argv Command line arguments
     */
    public static void main(String[] argv) throws Exception
      {
        new XPontusFormController();
      }
  }
