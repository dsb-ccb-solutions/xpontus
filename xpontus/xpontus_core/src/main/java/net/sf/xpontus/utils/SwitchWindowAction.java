/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.utils;

import com.vlsolutions.swing.docking.DockingUtilities;
import com.vlsolutions.swing.docking.TabbedDockableContainer;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentTabContainer;
import net.sf.xpontus.modules.gui.components.IDocumentContainer;

import java.awt.Toolkit;
import java.awt.event.ActionEvent;

import java.util.Vector;

import javax.swing.AbstractAction;


/**
 *
 * @author mrcheeks
 */
public class SwitchWindowAction extends AbstractAction {
    private boolean nextWindow = false;

    public SwitchWindowAction(String name, boolean nextWindow) {
        super(name);
        this.nextWindow = nextWindow;
    }

    public void actionPerformed(ActionEvent e) {
        DocumentTabContainer docContainer = DefaultXPontusWindowImpl.getInstance()
                                                                    .getDocumentTabContainer();

        Vector<IDocumentContainer> v = docContainer.getEditorsAsVector();
        final int msize = v.size();
        final int msize2 = v.size() - 1;

        if ((msize == 0) || (msize < 1)) {
            Toolkit.getDefaultToolkit().beep();

            return;
        }

        int index = v.indexOf(docContainer.getCurrentDockable());

        int pos = 0;

        if (nextWindow) {
            if (index != msize2) {
                pos = index + 1;
            }
        } else {
            if (index == 0) {
                pos = msize2;
            } else {
                pos = index - 1;
            }
        }

        IDocumentContainer toSelect = v.get(pos);

        TabbedDockableContainer container = DockingUtilities.findTabbedDockableContainer(toSelect);

        if (container != null) {
            container.setSelectedDockable(toSelect);
            toSelect.getEditorComponent().requestFocus();
            toSelect.getEditorComponent().grabFocus();
        }
    }
}
