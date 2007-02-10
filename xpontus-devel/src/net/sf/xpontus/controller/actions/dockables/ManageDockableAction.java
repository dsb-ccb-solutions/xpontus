/*
 * ManageDockableAction.java
 *
 * Created on 7 février 2007, 16:03
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.controller.actions.dockables;

import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenuItem;
import net.sf.xpontus.core.controller.actions.BaseAction;
import net.sf.xpontus.view.OutlineViewDockable;
import net.sf.xpontus.view.XPontusWindow;

/**
 *
 * @author YVZOU
 */
public class ManageDockableAction implements ActionListener{
    
    /**
     * Creates a new instance of ManageDockableAction
     */
    public ManageDockableAction() {
    }

    public void actionPerformed(ActionEvent e) {
        
        JCheckBoxMenuItem source = (JCheckBoxMenuItem)e.getSource();
        
        DockingDesktop desktop = XPontusWindow.getInstance().getDockingDesktop();
        
        Dockable dockable = null;
        
        if(source.getText().equalsIgnoreCase("Outline")){
            dockable = XPontusWindow.getInstance().getOutlineDockable();
        }
        else  if(source.getText().equalsIgnoreCase("Messages")){
            dockable = XPontusWindow.getInstance().getConsole().getDockables()[0];
        }
        else  if(source.getText().equalsIgnoreCase("XPath")){
            dockable = XPontusWindow.getInstance().getConsole().getDockables()[1];
        }
        else  if(source.getText().equalsIgnoreCase("Errors")){
            dockable = XPontusWindow.getInstance().getConsole().getDockables()[2];
        }
       
         
        if(desktop.getDockableState(dockable).getState() == DockableState.STATE_CLOSED){
            desktop.addDockable(dockable);
        }
        else if(desktop.getDockableState(dockable).getState() == DockableState.STATE_DOCKED){
            desktop.close(dockable);
        }
        
        
    }
    
}
