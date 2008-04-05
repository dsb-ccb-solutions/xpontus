/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.modules.gui.components;

import javax.swing.JComboBox;
import javax.swing.SwingUtilities;


public class MemoryComboBox extends JComboBox {
    public static int NO_MAX = -1;
    private int _maxMemoryCount = NO_MAX;

    /**
     * 
     */
    public MemoryComboBox() {
        this(NO_MAX);
    }

    /**
     * @param maxMemoryCount
     */
    public MemoryComboBox(int maxMemoryCount) {
        super();
        setMaxMemoryCount(maxMemoryCount);
    }

    /**
     * @param value
     */
    public void setMaxMemoryCount(int value) {
        _maxMemoryCount = (value > NO_MAX) ? value : NO_MAX;
    }

    /* (non-Javadoc)
     * @see javax.swing.JComboBox#addItem(java.lang.Object)
     */
    public void addItem(Object item) {
        if (item != null) {
            if (!item.toString().trim().equals("")) {
                removeItem(item);
                insertItemAt(item, 0);
                setSelectedIndex(0);

                if ((_maxMemoryCount > NO_MAX) &&
                        (getItemCount() > _maxMemoryCount)) {
                    removeItemAt(getItemCount() - 1);
                }
            }
        }
    }

    /* (non-Javadoc)
     * @see javax.swing.JComboBox#insertItemAt(java.lang.Object, int)
     */
    public void insertItemAt(Object anObject, int index) {
        super.insertItemAt(anObject, index);

        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    while ((_maxMemoryCount > NO_MAX) &&
                            (getItemCount() > _maxMemoryCount)) {
                        removeItemAt(0);
                    }
                }
            });
    }
}
