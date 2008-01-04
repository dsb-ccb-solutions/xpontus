/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.plugins.vfs;

import java.awt.Component;

/**
 *
 * @author Propriétaire
 */
public interface VFSPluginImpl {

    public String getProtocol();
    
    public void showOpenDialog(Component c);
    
    public void showSaveDialog(Component c);
    
}
