/*
 *
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
 *
 *
 */
package net.sf.xpontus.plugins.completion;

import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Component;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

import javax.swing.AbstractListModel;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JWindow;
import javax.swing.ListSelectionModel;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Yves Zoundi
 */
public class CompletionWindow extends JWindow implements KeyListener,
    ListSelectionListener, FocusListener, MouseListener {
    private static CompletionWindow INSTANCE;
    private JList list;
    private CompletionListModel model;
    private JScrollPane scroll;
    private int maxHeight = -1;
    private JTextComponent jtc;
    protected String m_key = "";
    protected long m_time = 0;
    public int CHAR_DELTA = 1000;
    int pos = 0;

    public CompletionWindow() {
        super((Frame) XPontusComponentsUtils.getTopComponent()
                                            .getDisplayComponent());
        initComponents();
    }

    public CompletionListModel getCompletionListModel() {
        return model;
    }

    public static CompletionWindow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new CompletionWindow();
        }

        return INSTANCE;
    }

    private void closeWindow() {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    setVisible(false);

                    jtc.requestFocusInWindow();
                    jtc.requestFocus();
                    jtc.grabFocus();
                }
            });
    }

    private void initComponents() {
        model = new CompletionListModel();
        list = new JList(model);
        list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        list.addFocusListener(this);
        list.addListSelectionListener(this);
        list.addMouseListener(this);
        scroll = new JScrollPane(list);
        getContentPane().add(scroll);
        setSize(350, 120);
        list.addKeyListener(this);
        list.getInputMap().clear();
        scroll.getInputMap().clear();
        this.maxHeight = (int) Toolkit.getDefaultToolkit().getScreenSize()
                                      .getHeight();
        list.setFocusable(true);
        this.setFocusable(true);
    }

    public void showWindow(JTextComponent jtc) {
        this.jtc = jtc;
        list.setVisibleRowCount(7);

        try {
            Rectangle caretBounds = jtc.modelToView(jtc.getCaretPosition());
            Point p = new Point(jtc.getLocationOnScreen());
            p.translate(caretBounds.x, caretBounds.y + caretBounds.height);

            if ((p.getY() + scroll.getHeight()) > Toolkit.getDefaultToolkit()
                                                             .getScreenSize()
                                                             .getHeight()) {
                p.translate(0,
                    (int) (-scroll.getHeight() - caretBounds.getHeight()));
            }

            int outOfSight = (int) ((p.getX() + scroll.getWidth()) -
                Toolkit.getDefaultToolkit().getScreenSize().getWidth());

            if (outOfSight > 0) {
                p.translate(-outOfSight, 0);
            }

            final int t = jtc.getCaretPosition() + 1;
            pos = t;
            setLocation(p);
        } catch (BadLocationException ble) {
        }

        setVisible(true);

        this.requestFocus();

        list.grabFocus();
    }

    private void insertSelection() {
        String m_selection = ((String) list.getSelectedValue());

        int position = jtc.getCaretPosition();

        int diff = (position + 1) - pos;

        if (diff > 0) {
            m_selection = m_selection.substring(position - pos + 1);
        }

        insertText(m_selection, true);
    }

    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            insertSelection();
            closeWindow();
        } else if (e.getKeyCode() == KeyEvent.VK_ESCAPE) {
            closeWindow();
        } else if (e.getKeyCode() == KeyEvent.VK_DOWN) {
            if (model.getSize() > 0) {
                list.setSelectedIndex((list.getSelectedIndex()) % model.getSize());
            }
        } else if (e.getKeyCode() == KeyEvent.VK_UP) {
            if (model.getSize() > 0) {
                list.setSelectedIndex((model.getSize() +
                    list.getSelectedIndex()) % model.getSize());
            }
        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
            closeWindow();
        } else {
            if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED) {
                char ch = e.getKeyChar();
                int k = lookup(ch);

                if (k != -1) {
                    list.setSelectedIndex(k);
                    list.ensureIndexIsVisible(k);
                    insertText(Character.toString(ch), false);
                } else {
                    insertText(Character.toString(ch), true);
                }
            }
        }
    }

    private void insertText(String ch, boolean dispose) {
        try {
            jtc.getDocument().insertString(jtc.getCaretPosition(), ch, null);

            if (dispose) {
                closeWindow();
            }
        } catch (BadLocationException ex) {
        }
    }

    public int lookup(char ch) {
        int index = -1;

        if ((m_time + CHAR_DELTA) < System.currentTimeMillis()) {
            m_key = "";
        }

        m_time = System.currentTimeMillis();

        m_key += Character.toLowerCase(ch);

        for (int k = 0; k < model.getSize(); k++) {
            String str = ((String) model.getElementAt(k)).toLowerCase();

            if (str.startsWith(m_key)) {
                index = k;

                break;
            }
        }

        return index;
    }

    public void keyReleased(KeyEvent e) {
    }

    public void keyTyped(KeyEvent e) {
    }

    public void valueChanged(ListSelectionEvent e) {
        list.ensureIndexIsVisible(list.getSelectedIndex());
    }

    public void focusGained(FocusEvent e) {
    }

    public void focusLost(FocusEvent e) {
        closeWindow();
    }

    public void mouseClicked(MouseEvent e) {
        if (e.getClickCount() == 2) {
            insertSelection();
        }
    }

    public void mousePressed(MouseEvent e) {
    }

    public void mouseReleased(MouseEvent e) {
    }

    public void mouseEntered(MouseEvent e) {
    }

    public void mouseExited(MouseEvent e) {
    }

    public class CompletionListModel extends AbstractListModel {
        private ArrayList data;

        public CompletionListModel() {
            data = new ArrayList();
        }

        public Object getElementAt(int index) {
            return data.get(index);
        }

        public int getSize() {
            return data.size();
        }

        public void updateData(Collection liste) {
            Object[] sort = liste.toArray();

            // must ensure the object implements comparable
            Arrays.sort(sort);
            data.clear();

            for (int i = 0; i < sort.length; i++) {
                data.add(sort[i].toString());
            }

            fireContentsChanged(list, 0, sort.length);

            list.revalidate();
            list.setSelectedIndex(0);
            list.ensureIndexIsVisible(0);
        }
    }
}
