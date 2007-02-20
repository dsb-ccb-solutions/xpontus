/*
 * XPontusTabbedPaneUI.java
 *
 * Created on February 18, 2007, 9:20 PM
 *
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.view.ui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Insets;
import java.awt.LayoutManager;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.AbstractAction;
import javax.swing.JButton;
import javax.swing.plaf.basic.BasicTabbedPaneUI;
import javax.swing.plaf.basic.BasicTabbedPaneUI.TabbedPaneLayout;


/**
 *
 * @author Yves Zoundi
 */
public class XPontusTabbedPaneUI extends BasicTabbedPaneUI {
    //override to return our layoutmanager
    protected LayoutManager createLayoutManager() {
        return new TestPlafLayout();
    }

    //add 40 to the tab size to allow room for the close button and 8 to the height
    protected Insets getTabInsets(int tabPlacement, int tabIndex) {
        //note that the insets that are returned to us are not copies.
        Insets defaultInsets = (Insets) super.getTabInsets(tabPlacement,
                tabIndex).clone();

        defaultInsets.right += 20;

        defaultInsets.top += 0;

        defaultInsets.bottom += 2;

        return defaultInsets;
    }

    class TestPlafLayout extends TabbedPaneLayout {
        //a list of our close buttons
        java.util.ArrayList closeButtons = new java.util.ArrayList();

        public void layoutContainer(Container parent) {
            super.layoutContainer(parent);

            //ensure that there are at least as many close buttons as tabs
            while (tabPane.getTabCount() > closeButtons.size()) {
                closeButtons.add(new CloseButton(closeButtons.size()));
            }

            Rectangle rect = new Rectangle();

            int i;

            for (i = 0; i < tabPane.getTabCount(); i++) {
                rect = getTabBounds(i, rect);

                JButton closeButton = (JButton) closeButtons.get(i);

                //shift the close button 3 down from the top of the pane and 20 to the left
                closeButton.setLocation((rect.x + rect.width) - 20, rect.y + 5);

                closeButton.setSize(15, 15);

                tabPane.add(closeButton);
            }

            for (; i < closeButtons.size(); i++) {
                //remove any extra close buttons
                tabPane.remove((JButton) closeButtons.get(i));
            }
        }

        // implement UIResource so that when we add this button to the 

        // tabbedpane, it doesn't try to make a tab for it!
        class CloseButton extends JButton implements javax.swing.plaf.UIResource {
            public CloseButton(int index) {
                super(new CloseButtonAction(index));
                setToolTipText("Close this tab");

                //remove the typical padding for the button
                setMargin(new Insets(0, 0, 0, 0));

                addMouseListener(new MouseAdapter() {
                        public void mouseEntered(MouseEvent e) {
                            setForeground(new Color(255, 0, 0));
                        }

                        public void mouseExited(MouseEvent e) {
                            setForeground(new Color(0, 0, 0));
                        }
                    });
            }
        }

        class CloseButtonAction extends AbstractAction {
            int index;

            public CloseButtonAction(int index) {
                super("x");

                this.index = index;
            }

            public void actionPerformed(ActionEvent e) {
                tabPane.remove(index);
            }
        } // End of CloseButtonAction
    }
}
