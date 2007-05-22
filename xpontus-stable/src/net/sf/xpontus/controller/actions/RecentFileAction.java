/*
 * RecentFileAction.java
 *
 * Created on May 14, 2007, 6:50 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.controller.actions;

import net.sf.xpontus.core.controller.actions.BaseAction;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.Properties;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.event.EventListenerList;


/**
 *
 * @author mrcheeks
 */
public class RecentFileAction extends BaseAction
{
    private Vector files;
    private String type;
    private int size;
    private int used;
    private EventListenerList listenerList = new EventListenerList();

    /** Creates a new instance of RecentFileAction */
    public RecentFileAction()
    {
        this(null);
    }

    /**
     * Create a recent file list with a given type and size
     * @param type The prefix to use
     * @param size the maximum number of files to remember
     */
    public RecentFileAction(String type, int size)
    {
        files = new Vector(size);
        this.size = size;
        this.type = type;
        used = 0;
    }

    public RecentFileAction(String type)
    {
        this(type, 4);
    }

    public void addActionListener(ActionListener l)
    {
        listenerList.add(ActionListener.class, l);
    }

    public void removeActionListener(ActionListener l)
    {
        listenerList.remove(ActionListener.class, l);
    }

    /**
     * An action event is fired when the user selects one of the
     * files from a menu. The "actionCommand" in the event will be
     * set to the name of the selected file.
     */
    protected void fireActionPerformed(ActionEvent e)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2)
        {
            if (listeners[i] == ActionListener.class)
            {
                ((ActionListener) listeners[i + 1]).actionPerformed(e);
            }
        }
        System.out.println();
        System.out.println("fired action performed in recent files list action");
        System.out.println();
        
        
        
    }

    public void execute()
    {
        fireActionPerformed(getEvent());
    }

    /**
     * Save the recent file list in a Properties set
     * @param props The Properties set to save the files in
     */
    public void save(Properties props)
    {
        String key = "RecentFile_" + ((type != null) ? (type + "_") : "");

        for (int i = 0; i < used; i++)
        {
            props.put(key + i, files.elementAt(i).toString());
        }

        props.put(key + "Used", String.valueOf(used));
    }

    /**
     * Load the recent file list from a Properties set
     * @param props The Properties set to load from
     */
    public void load(Properties props)
    {
        String key = "RecentFile_" + ((type != null) ? (type + "_") : "");
        files.removeAllElements();
        used = Integer.parseInt(props.getProperty(key + "Used", "0"));

        for (int i = 0; i < used; i++)
        {
            String value = props.getProperty(key + i);

            if (value == null)
            {
                break;
            }

            files.addElement(value);
        }
    }

    /**
     * Add a file to the list
     * @param f The file to add
     */
    public void add(File f)
    {
        try
        {
            add(f.getCanonicalPath());
        }
        catch (java.io.IOException x)
        {
            add(f.getAbsolutePath());
        }
    }

    /**
     * Remove a file from the list
     * @param f Remove a file from the list
     */
    public void remove(File f)
    {
        remove(f.getName());
    }

    /**
     * Add a file to the list
     * @param name The name of the file to add
     */
    public void add(String name)
    {
        int pos = files.indexOf(name);

        if (pos > 0)
        {
            files.removeElementAt(pos);
            files.insertElementAt(name, 0);
        }
        else if (pos != 0)
        {
            if (used == size)
            {
                files.removeElementAt(size - 1);
            }
            else
            {
                used++;
            }

            files.insertElementAt(name, 0);
        }
    }

    /**
     * Remove a file from the list
     * @param name The name of the file to remove
     */
    public void remove(String name)
    {
        if (files.removeElement(name))
        {
            used--;
        }
    }

    /**
     * Adds the recent file list to a menu.
     * The files will be added at the end of the menu, with a
     * separator before the files (if there are >0 files in the list)
     */
    public void buildMenu(JMenu menu)
    {
        if (used > 0)
        {
            menu.addSeparator();

            for (int i = 0; i < used; i++)
            {
                JMenuItem item = new JMenuItem(String.valueOf(i + 1) + " " +
                        files.elementAt(i));
                item.setActionCommand(files.elementAt(i).toString());

                if (size < 9)
                {
                    item.setMnemonic(Character.forDigit(i + 1, 10));
                }

                item.addActionListener(this);
                menu.add(item);
            }
        }
    }
}
