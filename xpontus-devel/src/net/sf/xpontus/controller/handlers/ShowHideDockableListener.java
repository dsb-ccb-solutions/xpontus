/*
 * ShowHideDockableListener.java
 *
 * Created on 7 février 2007, 16:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.controller.handlers;

import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingConstants;
import com.vlsolutions.swing.docking.DockingDesktop;

import net.sf.xpontus.view.EditorTabContainer;
import net.sf.xpontus.view.XPontusWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;


/**
 *
 * @author YVZOU
 */
public class ShowHideDockableListener implements ActionListener
{
    /**
     * Creates a new instance of ShowHideDockableListener
     */
    public ShowHideDockableListener()
    {
    }

    public void actionPerformed(ActionEvent e)
    {
        JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();

        DockingDesktop desktop = XPontusWindow.getInstance().getDockingDesktop();

        Dockable dockable = null;

        if (source.getText().equalsIgnoreCase("Outline"))
        {
            dockable = XPontusWindow.getInstance().getOutlineDockable();
        }
        else if (source.getText().equalsIgnoreCase("Output"))
        {
            dockable = XPontusWindow.getInstance().getConsole();
        }

        if (desktop.getDockableState(dockable).getState() == DockableState.STATE_CLOSED)
        {
            EditorTabContainer tabContainer = XPontusWindow.getInstance()
                                                           .getTabContainer();
            Dockable paneDockable = tabContainer.getCurrentDockable();

            if (paneDockable == null)
            {
                paneDockable = XPontusWindow.getInstance().getPane();
            }

            if (source.getText().equalsIgnoreCase("Outline"))
            {
                desktop.split(paneDockable, dockable,
                    DockingConstants.SPLIT_LEFT);
            }
            else
            {
                desktop.split(paneDockable, dockable,
                    DockingConstants.SPLIT_BOTTOM);
            }
        }
        else
        {
            desktop.close(dockable);
        }
    }
}
