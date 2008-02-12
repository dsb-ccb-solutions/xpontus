/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.modules.gui.components;

import com.vlsolutions.swing.docking.DockKey;

import java.awt.Component;

import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.MutableAttributeSet;


/**
 *
 * @author Propriétaire
 */
public class MessagesWindowDockable extends OutputDockable {
    public static final String DOCKABLE_ID = "DEFAULT_MESSAGES_WINDOW";
    private JTextPane m_pane;
    private DockKey m_key;
    private JScrollPane scrollPane;

    public MessagesWindowDockable() {
        m_pane = new JTextPane();
        m_pane.setEditable(false);
        scrollPane = new JScrollPane(m_pane);
        m_key = new DockKey(DOCKABLE_ID, "Messages");
        m_key.setResizeWeight(0.1f);
    }

    public void println(final String message) {
        println(message, OutputDockable.BLACK_STYLE);
    }

    public void println(final String message, final int style) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    try {
                        Document doc = m_pane.getDocument();
                        MutableAttributeSet s = null;

                        switch (style) {
                        case OutputDockable.BLUE_STYLE:
                            s = getBlueStyle();

                            break;

                        case OutputDockable.RED_STYLE:
                            s = getRedStyle();

                            break;

                        default:
                            s = getBlackStyle();

                            break;
                        }

                        doc.insertString(doc.getLength(), message + "\n", s);
                        m_pane.setCaretPosition(doc.getLength());
                    } catch (BadLocationException ex) {
                        ex.printStackTrace();
                    }
                }
            });
    }

    public DockKey getDockKey() {
        return m_key;
    }

    public Component getComponent() {
        return scrollPane;
    }

    public String getId() {
        return DOCKABLE_ID;
    }
}
