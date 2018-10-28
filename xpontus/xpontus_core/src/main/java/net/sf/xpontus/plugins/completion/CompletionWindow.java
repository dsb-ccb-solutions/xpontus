/*
 * CompletionWindow.java
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
 */
package net.sf.xpontus.plugins.completion;

import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 * @version 0.0.3
 * Code completion window
 * @author Yves Zoundi
 */
public class CompletionWindow extends JWindow implements KeyListener,
    ListSelectionListener, FocusListener, MouseListener
{
    private static final long serialVersionUID = 6201420460065287340L;
    private static final Log LOG = LogFactory.getLog(CompletionWindow.class);
    private static CompletionWindow INSTANCE;
    private JList completionList;
    private CompletionListModel completionListModel;
    private JScrollPane scrollPane;
    private JTextComponent textEditor;
    protected String m_key = "";
    protected long m_time = 0;
    public int CHAR_DELTA = 1000;
    int pos = 0;

    public CompletionWindow()
    {
        super((Frame) XPontusComponentsUtils.getTopComponent()
                                            .getDisplayComponent());
        initComponents();
    }

    /**
     * Method getCompletionListModel returns the completionListModel of this CompletionWindow object.
     *
     * @return the completionListModel (type CompletionListModel) of this CompletionWindow object.
     */
    public CompletionListModel getCompletionListModel()
    {
        return completionListModel;
    }

    /**
     * Single instance of this class
     *
     * @return the single instance of this class
     */
    public static synchronized CompletionWindow getInstance()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new CompletionWindow();
        }

        return INSTANCE;
    }

    private void closeWindow()
    {
        SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    setVisible(false);

                    textEditor.requestFocusInWindow();
                    textEditor.requestFocus();
                    textEditor.grabFocus();
                }
            });
    }

    private void initComponents()
    {
        completionListModel = new CompletionListModel();
        completionList = new JList(completionListModel);
        completionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        completionList.addFocusListener(this);
        completionList.addListSelectionListener(this);
        completionList.addMouseListener(this);
        scrollPane = new JScrollPane(completionList);
        getContentPane().add(scrollPane);
        setSize(350, 120);
        completionList.addKeyListener(this);
        completionList.getInputMap().clear();
        scrollPane.getInputMap().clear();
        completionList.setFocusable(true);
        this.setFocusable(true);
    }

    /**
     * Show the completion window
     *
     * @param textEditor The text editor component
     */
    public void showWindow(JTextComponent textEditor)
    {
        this.textEditor = textEditor;
        completionList.setVisibleRowCount(7);

        try
        {
            Rectangle caretBounds = textEditor.modelToView(textEditor.getCaretPosition());
            Point p = new Point(textEditor.getLocationOnScreen());
            p.translate(caretBounds.x, caretBounds.y + caretBounds.height);

            if ((p.getY() + scrollPane.getHeight()) > Toolkit.getDefaultToolkit()
                                                                 .getScreenSize()
                                                                 .getHeight())
            {
                p.translate(0,
                    (int) (-scrollPane.getHeight() - caretBounds.getHeight()));
            }

            int outOfSight = (int) ((p.getX() + scrollPane.getWidth()) -
                Toolkit.getDefaultToolkit().getScreenSize().getWidth());

            if (outOfSight > 0)
            {
                p.translate(-outOfSight, 0);
            }

            final int t = textEditor.getCaretPosition() + 1;
            pos = t;
            setLocation(p);
        }
        catch (BadLocationException ble)
        {
            LOG.debug(ble.getMessage(), ble);
        }

        setVisible(true);

        this.requestFocus();

        completionList.grabFocus();
    }

    private void insertSelection()
    {
        String m_selection = ((String) completionList.getSelectedValue());

        int position = textEditor.getCaretPosition();

        int diff = (position + 1) - pos;
        boolean goodCompletion = false;

        if (diff > 0)
        {
            try
            {
                String text_diff = textEditor.getDocument()
                                             .getText(position, diff);

                if (m_selection.length() < diff)
                {
                    goodCompletion = false;
                }
                else
                {
                    String completion_diff = m_selection.substring(0, diff);

                    if (text_diff.equals(completion_diff))
                    {
                        goodCompletion = true;
                    }
                }
            }
            catch (Exception e)
            {
                LOG.debug(e.getMessage(), e);
            }
        }

        // check if we need to correct some text already entered

        // if the first letters entered are identitical to the selected item insert directly
        if (goodCompletion)
        {
            m_selection = m_selection.substring(position - pos + 1);
            insertText(m_selection, true);
        }
        else
        {
            // else correct the entered text and insert the completion
            insertAndFixText(pos, diff, m_selection, true);
        }
    }

    public void keyPressed(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            insertSelection();
            closeWindow();
        }
        else if (e.getKeyCode() == KeyEvent.VK_ESCAPE)
        {
            closeWindow();
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN)
        {
            if (completionListModel.getSize() > 0)
            {
                completionList.setSelectedIndex((completionList.getSelectedIndex()) % completionListModel.getSize());
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_UP)
        {
            if (completionListModel.getSize() > 0)
            {
                completionList.setSelectedIndex((completionListModel.getSize() +
                    completionList.getSelectedIndex()) % completionListModel.getSize());
            }
        }
        else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE)
        {
            closeWindow();
        }
        else
        {
            if (e.getKeyChar() != KeyEvent.CHAR_UNDEFINED)
            {
                char ch = e.getKeyChar();
                int k = lookup(ch);

                if (k != -1)
                {
                    completionList.setSelectedIndex(k);
                    completionList.ensureIndexIsVisible(k);
                    insertText(Character.toString(ch), false);
                }
                else
                {
                    insertText(Character.toString(ch), true);
                }
            }
        }
    }

    /**
     * Remove the text the user was typing and insert the completion item
     *
     * @param startOffset The position where the user triggered code completion
     * @param len of type The amount of the text to remove
     * @param completion The completion item to insert
     * @param dispose close the completion window
     */
    private void insertAndFixText(int startOffset, int len, String completion,
        boolean dispose)
    {
        try
        {
            // remove the text which has already been entered
            textEditor.getDocument().remove(startOffset - 1, len);

            // insert the completion
            textEditor.getDocument().insertString(pos - 1, completion, null);

            // close the completion window
            if (dispose)
            {
                closeWindow();
            }
        }
        catch (BadLocationException ex)
        {
            LOG.debug(ex.getMessage(), ex);
        }
    }

    /**
     * insert some text in the editor component
     *
     * @param ch the string to insert
     * @param dispose close the completion window
     */
    private void insertText(String ch, boolean dispose)
    {
        try
        {
            textEditor.getDocument()
                      .insertString(textEditor.getCaretPosition(), ch, null);

            if (dispose)
            {
                closeWindow();
            }
        }
        catch (BadLocationException ex)
        {
            LOG.debug(ex.getMessage(), ex);
        }
    }

    /**
     * Select a completion element as the user press a key
     *
     * @param ch of type char
     * @return int
     */
    public int lookup(char ch)
    {
        int index = -1;

        if ((m_time + CHAR_DELTA) < System.currentTimeMillis())
        {
            m_key = "";
        }

        m_time = System.currentTimeMillis();

        m_key += Character.toLowerCase(ch);

        for (int k = 0; k < completionListModel.getSize(); k++)
        {
            String str = ((String) completionListModel.getElementAt(k)).toLowerCase();

            if (str.startsWith(m_key))
            {
                index = k;

                break;
            }
        }

        return index;
    }

    public void keyReleased(KeyEvent e)
    {
    }

    public void keyTyped(KeyEvent e)
    {
    }

    public void valueChanged(ListSelectionEvent e)
    {
        completionList.ensureIndexIsVisible(completionList.getSelectedIndex());
    }

    public void focusGained(FocusEvent e)
    {
    }

    public void focusLost(FocusEvent e)
    {
        closeWindow();
    }

    public void mouseClicked(MouseEvent e)
    {
        if (e.getClickCount() == 2)
        {
            insertSelection();
        }
    }

    public void mousePressed(MouseEvent e)
    {
    }

    public void mouseReleased(MouseEvent e)
    {
    }

    public void mouseEntered(MouseEvent e)
    {
    }

    public void mouseExited(MouseEvent e)
    {
    }

    public class CompletionListModel extends AbstractListModel
    {
        private static final long serialVersionUID = 3061505335047555230L;
        private ArrayList<Object> data;

        public CompletionListModel()
        {
            data = new ArrayList<Object>();
        }

        public Object getElementAt(int index)
        {
            return data.get(index);
        }

        public int getSize()
        {
            return data.size();
        }

        /**
         * Update the completion list items
         *
         * @param completionItems a collection of completion items
         */
        public void updateData(Collection<Object> completionItems)
        {
            Object[] sort = completionItems.toArray();

            // must ensure the object implements comparable
            Arrays.sort(sort);

            // remove the existing completion elements
            data.clear();

            // add the new completion elements
            for (Object item : sort)
            {
                data.add(item.toString());
            }

            // notify the list the content has been updated
            fireContentsChanged(completionList, 0, sort.length);

            completionList.revalidate();
            completionList.setSelectedIndex(0);
            completionList.ensureIndexIsVisible(0);
        }
    }
}
