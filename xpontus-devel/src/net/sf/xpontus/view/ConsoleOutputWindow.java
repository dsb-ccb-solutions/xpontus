/*
 * OutputWindow.java
 *
 * Created on February 4, 2007, 5:08 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view;

import com.vlsolutions.swing.docking.DockGroup;
import com.vlsolutions.swing.docking.DockKey;
import com.vlsolutions.swing.docking.DockTabbedPane;
import com.vlsolutions.swing.docking.Dockable;
import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;
import com.vlsolutions.swing.docking.DockingUtilities;
import com.vlsolutions.swing.docking.TabbedDockableContainer;

import net.sf.xpontus.core.controller.handlers.PopupListener;
import net.sf.xpontus.core.utils.MessageProvider;
import net.sf.xpontus.utils.JTextComponentPrintStream;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableModel;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;


/**
 *
 * @author mrcheeks
 */
public class ConsoleOutputWindow extends DockTabbedPane implements Dockable
{
    static private final long serialVersionUID = -3334686130847823494L;
    static public final int MESSAGES_WINDOW = 0;
    static public final int ERRORS_WINDOW = 1;
    static public final int XPATH_WINDOW = 2;
    
    private final DockKey outputKey = new DockKey("Output");
    private Dockable[] dockables = new Dockable[3];
    private DockGroup group = new DockGroup("outputWindow");
    public JTextArea[] textboxes = new JTextArea[2];
    private JTextComponentPrintStream[] printers = new JTextComponentPrintStream[2];
    public String[] titles = 
        {
            MessageProvider.getinstance().getMessage("console.messages.key"),
            MessageProvider.getinstance().getMessage("console.errors.key"),
            MessageProvider.getinstance().getMessage("console.xpath.key")
        };
    private JTable xpathResultsTable;

    /** Creates a new instance of OutputWindow */
    public ConsoleOutputWindow()
    {
       
        // outputKey.setDockGroup(group);
        outputKey.setResizeWeight(0.2f);
        Dimension dim = new Dimension(600, 150);
        this.setMinimumSize(dim);
        this.setPreferredSize(dim);
        initComponents();
    }

    public Dockable[] getDockables()
    {
        return dockables;
    }

    /**
     * @param model
     */
    public void setResultsModel(TableModel model)
    {
        xpathResultsTable.setModel(model);
        xpathResultsTable.revalidate();
        xpathResultsTable.repaint();
    }

    public JTextComponentPrintStream getPrinter(int id)
    {
        return printers[id];
    }

    /**
     *
     */
    private void initComponents()
    {
        DockingDesktop desktop = XPontusWindow.getInstance().getDockingDesktop();
        this.installDocking(desktop);
        this.getDockKey().setDockableState(DockableState.STATE_DOCKED);
        
        xpathResultsTable = new JTable();
        xpathResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        xpathResultsTable.getSelectionModel()
                         .addListSelectionListener(new RowSelectionListener());

        for (int i = 0; i < 2; i++)
        {
            textboxes[i] = new JTextArea()
                    {
                        public void append(String str)
                        {
                            super.append(str);
                            setCaretPosition(getDocument().getLength());
                        }
                    };
            printers[i] = new JTextComponentPrintStream(textboxes[i]);
            textboxes[i].setEditable(false);
            textboxes[i].setLineWrap(true);
            createMessagesPopupListener(textboxes[i]);
            dockables[i] = new OutputWindowDockable( 
                        textboxes[i], titles[i]);
        }

        
        dockables[2] = new OutputWindowDockable(
                    new JScrollPane(xpathResultsTable), titles[2]);
 
 
        for (int i = 0; i < dockables.length; i++)
        {
//            desktop.registerDockable(dockables[i]);
            this.addTab(titles[i], dockables[i].getComponent());
//            this.addDockable(dockables[i], i);
//            dockables[i].getDockKey().setDockableState(DockableState.STATE_DOCKED);
        }
    }

    /**
     * @param messagesTA
     */
    private void createMessagesPopupListener(final JTextComponent messagesTA)
    {
        javax.swing.JPopupMenu popup = new javax.swing.JPopupMenu();
        javax.swing.Action action;
        javax.swing.ActionMap map = messagesTA.getActionMap();

        action = map.get(DefaultEditorKit.copyAction);
        action.putValue(javax.swing.Action.NAME,
            XPontusWindow.getInstance().getI18nMessage("action.copy.name"));
        popup.add(action);

        action = map.get(DefaultEditorKit.selectAllAction);
        action.putValue(javax.swing.Action.NAME,
            XPontusWindow.getInstance().getI18nMessage("action.selectall.name"));
        popup.add(action);

        XPontusWindow form = XPontusWindow.getInstance();
        String clearKey = form.getI18nMessage("clear.key");
        action = new AbstractAction(clearKey)
                {
                    public void actionPerformed(java.awt.event.ActionEvent evt)
                    {
                        messagesTA.setText("");
                    }
                };
        popup.add(action);

        PopupListener listener = new PopupListener(popup);
        messagesTA.addMouseListener(listener);
    }

    /**
     * @param i
     */
    public void setFocus(int i)
    {
        TabbedDockableContainer container = DockingUtilities.findTabbedDockableContainer(this.getDockables()[i]);

        if (container != null)
        {
            container.setSelectedDockable(this.getDockables()[i]);
        }
    }

    /**
     * @param textBoxId
     * @param msg
     */
    public void println(int textBoxId, String msg)
    {
        printers[textBoxId].println(msg);
    }

    public DockKey getDockKey()
    {
        return outputKey;
    }

    public Component getComponent()
    {
        return this;
    }

    class OutputWindowDockable implements Dockable
    {
        private JComponent comp;
        private DockKey key;

        public OutputWindowDockable(JComponent comp, String Mkey)
        {
            this.comp = comp;
            key = new DockKey(Mkey);
            key.setCloseEnabled(true);
            //            key.setDockGroup(group);
//            key.setResizeWeight(0.2f);
//
//            Dimension dim = new Dimension(600, 150);
//            comp.setMinimumSize(dim);
//            comp.setPreferredSize(dim);
        }

        public DockKey getDockKey()
        {
            return key;
        }

        public Component getComponent()
        {
            return (Component)comp;
        }
    }

    private class RowSelectionListener implements ListSelectionListener
    {
        public void valueChanged(ListSelectionEvent e)
        {
            // Ignore extra messages.
            if (e.getValueIsAdjusting())
            {
                return;
            }

            ListSelectionModel lsm = (ListSelectionModel) e.getSource();

            if (!lsm.isSelectionEmpty())
            {
                int selectedRow = lsm.getMinSelectionIndex();
                String s = null;

                for (int i = 0; i < xpathResultsTable.getColumnCount(); i++)
                {
                    s = (xpathResultsTable.getValueAt(selectedRow, i)).toString();

                    String line = s.split(",")[0];
                    int pos = "line ".length();
                    int lineNumber = Integer.parseInt(line.substring(pos,
                                line.length()));

                    gotoLine(lineNumber);
                }
            }
        }

        /**
         * @param lineNumber
         */
        private void gotoLine(int lineNumber)
        {
            JEditorPane edit = XPontusWindow.getInstance().getCurrentEditor();
            Element element = edit.getDocument().getDefaultRootElement();

            if (element.getElement(lineNumber - 1) == null)
            {
                JOptionPane.showMessageDialog(XPontusWindow.getInstance()
                                                           .getFrame(),
                    XPontusWindow.getInstance().getI18nMessage("msg.noSuchLine"),
                    XPontusWindow.getInstance().getI18nMessage("msg.error"),
                    JOptionPane.WARNING_MESSAGE);

                return;
            }

            int pos = element.getElement(lineNumber - 1).getStartOffset();
            edit.setCaretPosition(pos);
            edit.moveCaretPosition(element.getElement(lineNumber - 1)
                                          .getEndOffset() - 1);
        }
    }
}
