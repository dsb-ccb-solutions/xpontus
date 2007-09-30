/*
 * ConsoleOutputWindow.java
 *
 * Created on July 1, 2007, 12:22 PM
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

import com.vlsolutions.swing.docking.DockGroup;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockingUtilities;
import com.vlsolutions.swing.docking.TabbedDockableContainer;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * Console window
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class ConsoleOutputWindow {
    /**
     *
     */
    static public final int MESSAGES_WINDOW = 0;

    /**
     *
     */
    static public final int ERRORS_WINDOW = 1;

    /**
     *
     */
    static public final int XPATH_WINDOW = 2;
    public static DockGroup group = new DockGroup("outputWindow");
    private final DockKey outputKey = new DockKey("Output");
    List dockables = new ArrayList();

    /**
     *
     */
    public JTextArea[] textboxes = new JTextArea[2];

    /**
     *
     */
    public String[] titles = { "Messages", "Errors" };

    /** Creates a new instance of OutputWindow */
    public ConsoleOutputWindow() {
        initComponents();
    }

    /**
     *
     * @return
     */
    public List getDockables() {
        return dockables;
    }

    public void addDockable(Dockable dockable) {
        dockables.add(dockable);
    }

    /**
     *
     */
    private void initComponents() {
        for (int i = 0; i < 2; i++) {
            textboxes[i] = new JTextArea() {
                        public void append(String str) {
                            super.append(str);
                            setCaretPosition(getDocument().getLength());
                        }
                    };
            textboxes[i].setEditable(false);
            textboxes[i].setLineWrap(true);
            textboxes[i].setWrapStyleWord(true);

            addDockable(new OutputDockable(i, titles[i],
                    new JScrollPane(textboxes[i])));
        }
    }

    /**
     * @param i
     */
    public void setFocus(int i) {
        Dockable dockable = (Dockable) dockables.get(i);
        TabbedDockableContainer container = DockingUtilities.findTabbedDockableContainer(dockable);

        if (container != null) {
            container.setSelectedDockable(dockable);
            dockable.getComponent().requestFocus();
        }
    }

    /**
     *
     * @return
     */
    public DockKey getDockKey() {
        return outputKey;
    }
}
