/*
 * OutputDockable.java
 *
 * Created on February 27, 2007, 7:35 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.view;

import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.Dockable;
import java.awt.Component;

/**
 *
 * @author mrcheeks
 */
public class OutputDockable implements Dockable {
        private Component comp;
        private String key;
        private DockKey _mkey;
        private int id;

        public int getId(){
            return id;
        }
        
        public OutputDockable(int id, String key, Component comp) {
            this.key = key;
            this.id = id;
            this.comp = comp;
            _mkey = new DockKey(key);
            _mkey.setDockGroup(ConsoleOutputWindow.group);
            _mkey.setResizeWeight(0.1f);
        }

        public DockKey getDockKey() {
            return _mkey;
        }

        public Component getComponent() {
            return comp;
        }
    }