package net.sf.xpontus.view;

import net.sf.xpontus.core.controller.handlers.PopupListener;
import net.sf.xpontus.core.utils.MessageProvider;

import javax.swing.AbstractAction;
import javax.swing.JEditorPane;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
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
 * @author Yves Zoundi
 *
 */
public class ConsoleOutputWindow extends JTabbedPane
  {
    static private final long serialVersionUID = -3334686130847823494L;
    static public final int MESSAGES_WINDOW = 0;
    static public final int ERRORS_WINDOW = 1;
    static public final int XPATH_WINDOW = 2;
    public JTextArea[] textboxes = new JTextArea[2];
    public String[] titles = 
        {
            MessageProvider.getinstance().getMessage("console.messages.key"),
            MessageProvider.getinstance().getMessage("console.errors.key"),
            MessageProvider.getinstance().getMessage("console.xpath.key")
        };
    private JTable xpathResultsTable;

    public ConsoleOutputWindow()
      {
        initComponents();
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

    /**
     *
     */
    private void initComponents()
      {
        xpathResultsTable = new JTable();
        xpathResultsTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        xpathResultsTable.setBackground(getBackground());
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
            textboxes[i].setEditable(false);
            textboxes[i].setLineWrap(true);
            addTab(titles[i], new JScrollPane(textboxes[i]));
            createMessagesPopupListener(textboxes[i]);
          }

        addTab(titles[2], new JScrollPane(xpathResultsTable));
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
        this.setSelectedComponent(getComponentAt(i));
        this.getComponentAt(i).requestFocus();
      }

    /**
     * @param textBoxId
     * @param msg
     */
    public void println(int textBoxId, String msg)
      {
        textboxes[textBoxId].append(msg + "\n");
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
