package test.completion;

import net.sf.xpontus.view.XPontusWindow;

import java.awt.*;
import java.awt.event.KeyEvent;

import java.util.Collection;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;


/**
 * A simple window for displaying a list of strings.  The window
 * disposes itself when you press ESCAPE.
 *
 * TODO: x/y
 * TODO: width/height
 * TODO: addActionListener() method
 *
 * @author ddjohnson
 * @version 1.0
 */
public class CalltipWindow extends JWindow
{
    private JList list;
    private StringListModel listModel;

    /**
     * Constructor.
     *
     * @see JWindow#JWindow(Frame)
     *
     * @param owner
     */
    public CalltipWindow(Frame owner)
    {
        super(owner);
        initComponents();
        initWindow();
    }

    public CalltipWindow()
    {
        initComponents();
        initWindow();
    }

    public void updateVisibility()
    {
        this.requestFocus();
        this.list.setSelectedIndex(0);
        this.list.requestFocus();
    }

    private void initComponents()
    {
        listModel = new StringListModel();
        list = new TipList(listModel);

        Container panel = new JPanel(new BorderLayout());
        panel.add(new JScrollPane(list), BorderLayout.CENTER);

        setContentPane(panel);
    }

    private void initWindow()
    {
        setSize(120, 90);
        setLocationRelativeTo(getOwner());
    }

    /**
     * Returns the {@link StringListModel}
     *
     * @return listModel
     */
    public StringListModel getListModel()
    {
        return listModel;
    }

    /**
     * Sets the contents of the window to the given collection of
     * strings.
     *
     * @param strings
     */
    public void setListContents(Collection strings)
    {
        listModel.clear();
        listModel.addAll(strings);
    }

    /**
     * Disposes the window and contents.
     *
     */
    public void closeWindow()
    {
        dispose();
    }

    class TipList extends JList
    {
        TipList(ListModel model)
        {
            super(model);
            enableEvents(AWTEvent.KEY_EVENT_MASK);
        }

        protected void processKeyEvent(KeyEvent e)
        {
            if (e.getID() == KeyEvent.KEY_PRESSED)
            {
                switch (e.getKeyCode())
                {
                case KeyEvent.VK_ENTER:

                    JEditorPane editor = XPontusWindow.getInstance()
                                                      .getCurrentEditor();
                    Document doc = editor.getDocument();
                    String str = this.getSelectedValue().toString();
                    int offset = editor.getCaretPosition();

                    try
                    {
                        doc.insertString(offset, str, null);
                    }
                    catch (BadLocationException ex)
                    {
                        ex.printStackTrace();
                    }

                    break;

                //setVisible(false);
                case KeyEvent.VK_ESCAPE:
                    closeWindow();

                    break;
                }
            }

            super.processKeyEvent(e);
        }
    }
}
