/*
 * AutoSuggestor.java
 *
 * Created on February 10, 2007, 7:48 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package test.completion;

/**
 *
 * @author Owner
 */
import java.awt.*;
import java.awt.event.*;
 
import javax.swing.*;
import javax.swing.event.*;
 
public class AutoSuggestor extends JFrame {
 
    private JEditorPane edit=new JEditorPane();
    String typedText="";
    int startWordPosition=0;
 
    Suggestor suggestor=new Suggestor(this);
    boolean userChanges=true;
 
    String[] wordList=new String[] {"112","113","111","124","123","1114","12345"};
 
    //Constructor
    public AutoSuggestor() {
        super ();
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
 
        this.getContentPane().add(new JScrollPane(edit));
        edit.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) {
                if (userChanges)
                processInsert(e);
            }
 
            public void removeUpdate(DocumentEvent e) {
                if (userChanges)
                processRemove(e);
            }
 
            public void changedUpdate(DocumentEvent e) {
            }
        });
    }
 
    public static void main(String args[]) {
        AutoSuggestor gI = new AutoSuggestor();
        gI.setSize(300,300);
        gI.setVisible(true);
 
    }
 
    protected void processRemove(DocumentEvent e) {
        int startOffset=e.getOffset();
        int len=e.getLength();
        try {
            if (startWordPosition<startOffset) {
                typedText=edit.getDocument().getText(startWordPosition,startOffset); 
                showSuggestor(startOffset);
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    protected void processInsert(DocumentEvent e) {
        int startOffset=e.getOffset();
        int len=e.getLength();
        try {
            String text=edit.getDocument().getText(startOffset,len);
            if (text.equals(" ")) {
                startWordPosition=startOffset+1;
                typedText="";
                suggestor.hide();
            }
            else {
                if (text.indexOf(' ')==-1) {
                    typedText=edit.getDocument().getText(startWordPosition,startOffset-startWordPosition+len); 
                    int pos=edit.getCaretPosition();
                    showSuggestor(pos);
                }
            }
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
 
    protected DefaultListModel getSuggestions() {
        int cnt=wordList.length;
        DefaultListModel result=new DefaultListModel();
        for (int i=0; i<cnt; i++) {
            if (wordList[i].indexOf(typedText)==0) {
                result.addElement(wordList[i]);
            }
        }
        return result;
    }
 
    protected void showSuggestor(int pos) {
        DefaultListModel suggestions=getSuggestions();
 
        if (suggestions.size()>0) {
            suggestor.list.setModel(suggestions);
            try {
                Rectangle rect=edit.getUI().modelToView(edit,pos);
                Point p=new Point((int)rect.getX(),(int)(rect.getY()+rect.getHeight()));
                SwingUtilities.convertPointToScreen(p,edit);
                if (!suggestor.isVisible()) {
                    suggestor.setLocation(p);
                    suggestor.show();
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            suggestor.processSelection=true;
                            edit.requestFocus();
                        }
                    });
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
            }
        }
        else {
            suggestor.hide();
        }
    }
 
    class Suggestor extends JWindow {
        public JList list=new JList();
        public boolean processSelection=true;
 
        public Suggestor(Window owner) {
            super(owner);
            setSize(100,100);
            list.setFocusable(false);
            JScrollPane scroll=new JScrollPane(list);
            scroll.setFocusable(false);
            this.getContentPane().add(scroll);
 
            list.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    SwingUtilities.invokeLater(new Runnable() {
                        public void run() {
                            int index=list.getSelectedIndex();
                            String word=(String)list.getModel().getElementAt(index);
                            try {
                                if (processSelection) {
                                    userChanges=false;
                                    edit.getDocument().remove(startWordPosition,edit.getCaretPosition()-startWordPosition);
                                    edit.getDocument().insertString(startWordPosition,word+" ",null);
                                    startWordPosition+=word.length()+1;
                                    typedText="";
                                    hide();
                                    edit.requestFocus();
                                    processSelection=false;
                                    userChanges=true;
                                }
                            }
                            catch (Exception ex) {
                                ex.printStackTrace();
                            }
                        }
                    });
                }
 
                public void mousePressed(MouseEvent e) {
                }
 
                public void mouseReleased(MouseEvent e) {
                }
 
                public void mouseEntered(MouseEvent e) {
                }
 
                public void mouseExited(MouseEvent e) {
                }
            });
        }
    }
}