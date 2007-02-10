/*
 * ContentAssistWindow.java
 *
 * Created on February 9, 2007, 5:18 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.codecompletion.xml;

import java.awt.Point;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import java.util.List;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Yves Zoundi
 */
public class ContentAssistWindow
{
    private static JList completionList;
    private static JPopupMenu completionMenu;
    private static String endTag = new String();

    public static void completeEndTag(JTextComponent editor, int off,
        String str, AttributeSet set)
    {
        final Document doc = editor.getDocument();

        int dot = editor.getCaret().getDot(); 
        
         endTag = new String(str);

        String text = null;

        try
        {
            text = doc.getText(0, off);
        }
        catch (BadLocationException ex)
        {
            ex.printStackTrace();
        }

        int startTag = text.lastIndexOf('<', off);
        int prefEndTag = text.lastIndexOf('>', off);

        // If there was a start tag and if the start tag is not empty
        // and
        // if the start-tag has not got an end-tag already.
        if ((startTag > 0) && (startTag > prefEndTag) &&
                (startTag < (text.length() - 1)))
        {
            String tag = text.substring(startTag, text.length());
            char first = tag.charAt(1);

            if ((first != '/') && (first != '!') && (first != '?') &&
                    !Character.isWhitespace(first))
            {
                boolean finished = false;
                char previous = tag.charAt(tag.length() - 1);

                if ((previous != '/') && (previous != '-'))
                {
                    endTag+=("</");

                    for (int i = 1; (i < tag.length()) && !finished; i++)
                    {
                        char ch = tag.charAt(i);

                        if (!Character.isWhitespace(ch))
                        {
                            endTag+=(ch);
                        }
                        else
                        {
                            finished = true;
                        }
                    }

                    endTag+=(">");
                }
            }
        }

        str = endTag;

        try
        {
            doc.insertString(off, str, set);
        }
        catch (BadLocationException ex)
        {
            ex.printStackTrace();
        }

        editor.getCaret().setDot(dot + 1);
    }

    // Tries to find out if the line finishes with an element start
    private static boolean isStartElement(String line)
    {
        boolean result = false;

        int first = line.lastIndexOf("<");
        int last = line.lastIndexOf(">");

        if (last < first)
        { // In the Tag
            result = true;
        }
        else
        {
            int firstEnd = line.lastIndexOf("</");
            int lastEnd = line.lastIndexOf("/>");

            // Last Tag is not an End Tag
            if ((firstEnd != first) && ((lastEnd + 1) != last))
            {
                result = true;
            }
        }

        return result;
    }

    public static void complete(final JTextComponent editor,
        List completionData, int off, String str, AttributeSet set)
    {
        final Document doc = editor.getDocument();

        if (str.equals(">"))
        {
            completeEndTag(editor, off, str, set);

            return;
        }

        if (completionData.size() == 0)
        {
            try
            {
                doc.insertString(off, str, null);
            }
            catch (BadLocationException ex)
            {
                ex.printStackTrace();
            }

            return;
        }

        if (completionData.size() > 0)
        {
            completionList = new JList(completionData.toArray());

            final int offset = off;

            completionList.addMouseListener(new MouseAdapter()
                {
                    public void mouseReleased(MouseEvent e)
                    {
                        try
                        {
                            doc.insertString(editor.getCaretPosition(),
                                completionList.getSelectedValue().toString(),
                                null);
                        }
                        catch (BadLocationException ex)
                        {
                            ex.printStackTrace();
                        }

                        completionMenu.setVisible(false);
                    }
                });

            completionList.addKeyListener(new KeyAdapter()
                {
                    public void keyReleased(KeyEvent e)
                    {
                        switch (e.getKeyCode())
                        {
                        case KeyEvent.VK_ENTER:

                            try
                            {
                                doc.insertString((editor.getCaretPosition()),
                                    completionList.getSelectedValue().toString(),
                                    null);
                                completionMenu.setVisible(false);
                            }
                            catch (BadLocationException ex)
                            {
                                ex.printStackTrace();
                            }

                            break;

                        case KeyEvent.VK_ESCAPE:
                            completionMenu.setVisible(false);

                            break;

                        case KeyEvent.VK_SPACE:
                            completionMenu.setVisible(false);

                            break;

                        case 16:

                            try
                            {
                                doc.insertString(editor.getCaretPosition(),
                                    "" + e.getKeyChar(), null);
                            }
                            catch (BadLocationException ex)
                            {
                                ex.printStackTrace();
                            }

                            break;

                        case KeyEvent.VK_BACK_SPACE:

                           
                                // recorder.deleteCharAt(recorder.length() - 1);
                                try
                                {
                                    doc.remove(editor.getCaretPosition(), 1);
                                }
                                catch (BadLocationException ex)
                                {
                                    ex.printStackTrace();
                                } 

                            break;

                        default: 

                            try
                            {
                                if (Character.isLetter(e.getKeyChar()) ||
                                        Character.isDigit(e.getKeyChar()))
                                {
                                    doc.insertString(editor.getCaretPosition(),
                                        ("" + e.getKeyChar()), null);
                                }
                            }
                            catch (BadLocationException ex)
                            {
                                ex.printStackTrace();
                            }

                            break;
                        }
                    }
                });
            completionList.setSelectedIndex(0);

            JScrollPane completionPane = new JScrollPane(completionList);
            completionMenu = new JPopupMenu();
            completionMenu.add(completionPane);

            Point point = editor.getCaret().getMagicCaretPosition();
            
            System.out.println("point not null:" + point!=null);
            completionMenu.show(editor,
                point.x, point.y);
            completionList.grabFocus();
        }
    }
}
