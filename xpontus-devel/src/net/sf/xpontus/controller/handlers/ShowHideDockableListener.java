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

import net.sf.xpontus.view.ConsoleOutputWindow;
import net.sf.xpontus.view.EditorTabContainer;
import net.sf.xpontus.view.XPontusWindow;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JCheckBoxMenuItem;
import net.sf.xpontus.view.OutputDockable;


/**
 *
 * @author YVZOU
 */
public class ShowHideDockableListener implements ActionListener {
    /**
     * Creates a new instance of ShowHideDockableListener
     */
    public ShowHideDockableListener() {
    }

    public void actionPerformed(ActionEvent e) {
        JCheckBoxMenuItem source = (JCheckBoxMenuItem) e.getSource();

        DockingDesktop desktop = XPontusWindow.getInstance().getDockingDesktop();

        Dockable dockable = null;

        ConsoleOutputWindow console = XPontusWindow.getInstance().getConsole();

        if (source.getText().equalsIgnoreCase("Outline")) {
            dockable = XPontusWindow.getInstance().getOutlineDockable();
        } else if (source.getText().equalsIgnoreCase("Messages")) {
            dockable = console.getDockables()[0];
        } else if (source.getText().equalsIgnoreCase("Errors")) {
            dockable = console.getDockables()[1];
        } else if (source.getText().equalsIgnoreCase("XPath")) {
            dockable = console.getDockables()[2];
        }

        if (desktop.getDockableState(dockable).getState() == DockableState.STATE_CLOSED) {
            EditorTabContainer tabContainer = XPontusWindow.getInstance()
                                                           .getTabContainer();
            Dockable paneDockable = tabContainer.getCurrentDockable();

            if (paneDockable == null) {
                paneDockable = XPontusWindow.getInstance().getPane();
            }

            if (source.getText().equalsIgnoreCase("Outline")) {
                desktop.split(paneDockable, dockable,
                    DockingConstants.SPLIT_LEFT);
            } else {
                Dockable cons = null;
                for(int i=0;i<console.getDockables().length;i++){
                    if(console.getDockables()[i].getDockKey().getDockableState() == DockableState.STATE_DOCKED){
                        cons = console.getDockables()[i];
                        break;
                    }
                }
                if(cons == null){
                    desktop.split(paneDockable, dockable, DockingConstants.SPLIT_BOTTOM);
                }
                else{
                    int position = ((OutputDockable)dockable).getId();
                    int _position = ((OutputDockable)cons).getId();
                    if(position > _position){
                        position = position - _position;
                    }
                    else{
                        position = _position - position;
                    }
                    desktop.createTab(cons, dockable, position);
                }

                
            }
        } else {
            desktop.close(dockable);
        }
    }
}
