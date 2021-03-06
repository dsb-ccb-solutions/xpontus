/*
 * DefaultXPontusWindowImpl.java
 *
 * Created on June 20, 2007, 9:39 AM
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
package net.sf.xpontus.modules.gui.components;

import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingConstants;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.toolbars.ToolBarContainer;

import net.sf.xpontus.actions.impl.ExitActionImpl;
import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.plugins.ioc.IOCPlugin;
import net.sf.xpontus.utils.GUIUtils;

import org.apache.commons.lang.SystemUtils;
import org.apache.commons.lang.text.StrBuilder;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import java.net.URL;

import javax.swing.JFrame;
import javax.swing.JMenuBar;
import javax.swing.plaf.basic.BasicMenuBarUI;


/**
 * The default main component of XPontus XML Editor
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class DefaultXPontusWindowImpl extends DefaultXPontusTopComponentImpl
{
    private static DefaultXPontusWindowImpl INSTANCE;
    private IOCPlugin iocContainer;
    private final JMenuBar menubar = new JMenuBar();
    private DockingDesktop desktop;
    private JStatusBar statusbar;
    private ToolBarContainer toolbar;
    private DocumentTabContainer tabContainer;
    private Dockable outlineDockable;
    private final JFrame frame = new JFrame();
    private Dockable pane;
    private ConsoleOutputWindow console;
    private String WINDOW_TITLE;

    /** Creates a new instance of DefaultXPontusWindowImpl */
    private DefaultXPontusWindowImpl()
    {
        frame.setJMenuBar(menubar);

        URL logoURL = getClass()
                          .getResource("/net/sf/xpontus/icons/icone.png");
        frame.setIconImage(Toolkit.getDefaultToolkit().createImage(logoURL));

        frame.addWindowListener(new WindowAdapter()
            {
                public void windowClosing(WindowEvent arg0)
                {
                    new ExitActionImpl().execute();
                }
            });

        StrBuilder b = new StrBuilder();

        b.append(XPontusConstantsIF.APPLICATION_NAME);
        b.append(" ");
        b.append(XPontusConstantsIF.APPLICATION_VERSION);
        WINDOW_TITLE = b.toString();

        frame.setTitle(WINDOW_TITLE);
        initComponents();
    }

    /**
     * Method getOutline returns the outline of this DefaultXPontusWindowImpl object.
     *
     * @return the outline (type OutlineViewDockable) of this DefaultXPontusWindowImpl object.
     */
    public OutlineViewDockable getOutline()
    {
        return (OutlineViewDockable) outlineDockable;
    }

    /**
     * Method getStatusBar returns the statusBar of this DefaultXPontusWindowImpl object.
     *
     * @return the statusBar (type JStatusBar) of this DefaultXPontusWindowImpl object.
     */
    public JStatusBar getStatusBar()
    {
        return statusbar;
    }

    /**
     *
     * @return
     */
    public JMenuBar getMenuBar()
    {
        return menubar;
    }

    /**
     * Method getToolBar returns the toolBar of this DefaultXPontusWindowImpl object.
     *
     * @return the toolBar (type ToolBarContainer) of this DefaultXPontusWindowImpl object.
     */
    public ToolBarContainer getToolBar()
    {
        return toolbar;
    }

    /**
     *
     * @return
     */
    public DocumentTabContainer getDocumentTabContainer()
    {
        return tabContainer;
    }

    /**
     *
     * @return
     */
    public ConsoleOutputWindow getConsole()
    {
        return console;
    }

    /**
     *
     * @return
     */
    public Dockable getDefaultPane()
    {
        return pane;
    }

    /**
     *
     * @return
     */
    public DockingDesktop getDesktop()
    {
        return desktop;
    }

    /**
     *
     * @return
     */
    public static synchronized DefaultXPontusWindowImpl getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new DefaultXPontusWindowImpl();
        }

        return INSTANCE;
    }

    private void initComponents()
    {
        desktop = new DockingDesktop();

        statusbar = new JStatusBar();

        // create the toolbar
        toolbar = ToolBarContainer.createDefaultContainer(true, true, true, true);

        // default pane
        pane = new DefaultPane();
        GUIUtils.installDragAndDropSupport(pane.getComponent());

        // add the pane to the desktop
        desktop.addDockable(pane);

        outlineDockable = new OutlineViewDockable();
        desktop.registerDockable(outlineDockable);

        desktop.split(pane, outlineDockable, DockingConstants.SPLIT_LEFT);

        console = new ConsoleOutputWindow();

        final int total = console.getDockables().size();

        for (int i = 0; i < total; i++)
        {
            desktop.registerDockable((Dockable) console.getDockables().get(i));
        }

        desktop.split(pane, (Dockable) console.getDockables().get(0),
            DockingConstants.SPLIT_BOTTOM);

        for (int i = 1; i < total; i++)
        {
            desktop.createTab((Dockable) console.getDockables().get(i - 1),
                (Dockable) console.getDockables().get(i), i);
        }

        tabContainer = new DocumentTabContainer(desktop);

        frame.getContentPane().add(desktop, BorderLayout.CENTER);
        frame.getContentPane().add(toolbar, BorderLayout.NORTH);
        frame.getContentPane().add(statusbar, BorderLayout.SOUTH);
    }

    /**
     *
     * @return
     */
    public String getName()
    {
        return WINDOW_TITLE;
    }

    /**
     *
     * @return
     */
    public Component getDisplayComponent()
    {
        return frame;
    }

    /** activate xpontus window */
    public void activateComponent()
    {
        if (SystemUtils.IS_OS_WINDOWS_VISTA)
        {
            menubar.setUI(new BasicMenuBarUI());
        }

        frame.pack();

        frame.setLocationRelativeTo(frame.getOwner());

        frame.setVisible(true);
    }

    /** deactivate the main window */
    public void deactivateComponent()
    {
        frame.setVisible(false);
    }

    public IOCPlugin getIOCContainer()
    {
        return this.iocContainer;
    }

    public void setIOCContainer(IOCPlugin container)
    {
        this.iocContainer = container;
    }
}
