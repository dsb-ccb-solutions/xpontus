/*
 * RecentFileListActionListener.java
 *
 * Created on May 21, 2007, 8:00 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.controller.handlers;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Vector;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.view.PaneForm;
import net.sf.xpontus.view.XPontusWindow;

/**
 *
 * @author mrcheeks
 */
public class RecentFileListActionListener {
    
    private List recentFiles = new Vector();
    
    /** Creates a new instance of RecentFileListActionListener */
    public RecentFileListActionListener() {
        File history = new File(XPontusConstants.CONF_DIR, "history.txt");
        if(!history.exists()){
            try {
                FileWriter writer = new FileWriter(history);
                writer.write("");
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        } 
    }
 
    public void loadList() throws Exception{
        File history = new File(XPontusConstants.CONF_DIR, "history.txt");
        FileReader reader = new FileReader(history);
        BufferedReader f = new BufferedReader(reader);
        String line = null;
        
        while( (line = f.readLine())!=null){
            if(!line.trim().equals("")){
                File file = new File(line);
                if(file.exists()){
                    addFile(file);
                }
            }
        }
        
        f.close();
        reader.close();
    }
    
    public void saveList() throws Exception{
        File history = new File(XPontusConstants.CONF_DIR, "history.txt");
        
        FileWriter writer = new FileWriter(history); 
        
        for(int i=0;i<recentFiles.size();i++){
            writer.write(recentFiles.get(i).toString() + "\n");
        }
        
        writer.close();
        
        
    }
     
    
    public void addFile(final File file){ 
        
        if(recentFiles.contains(file.getAbsolutePath())){
            return;
        } 
        
        recentFiles.add(file.getAbsolutePath());
        
        // recent files menu
       final JMenu menu = XPontusWindow.getInstance().getRecentFileMenu();
        
        int menuItems = menu.getItemCount();
        
        if(menuItems == 10){
            menu.remove(menuItems - 1);
            recentFiles.remove(recentFiles.size() - 1);
        }
        
        // create a new menu item
        final JMenuItem item = new JMenuItem(file.getName());
        item.setToolTipText(file.getAbsolutePath());
        
        ActionListener l = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                PaneForm form = XPontusWindow.getInstance().getPane();
                if(!file.exists()){
                    menu.remove(item);
                    return;
                }
                form.createEditorFromFile(file);
            }
        };
        item.addActionListener(l);
        
        menu.add(item); 
        
    }
    
    
    
}
