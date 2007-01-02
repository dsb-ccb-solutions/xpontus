/*
 * SyntaxDocument.java
 *
 * Created on December 22, 2006, 1:14 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view.editor;

import javax.swing.JEditorPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;


/**
 *
 * @author Owner
 */
public class SyntaxDocument extends PlainDocument
  {
    private static final long serialVersionUID = 3121057580358543856L;
    private JEditorPane editor;
    private boolean tagCompletion = true;

    public SyntaxDocument(JEditorPane editor)
      {
        this.editor = editor;
      }

    private boolean isTagCompletion()
      {
        return tagCompletion;
      }

    /**
     * Inserts some content into the document. When the content is a '>'
     * character and it is the end of a start-tag and auto tag completion has
     * been enabled, a new end-tag will be created. When the content is a new
     * line character and auto indentation has been enabled, an indentation will
     * be added to the content.
     *
     * @see PlainDocument#insertString( int, String, AttributeSet)
     */
    public void insertString(int off, String str, AttributeSet set)
        throws BadLocationException
      {
        if (str.equals(">") && isTagCompletion())
          {
            int dot = editor.getCaret().getDot();

            StringBuffer endTag = new StringBuffer(str);

            String text = getText(0, off);
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
                        endTag.append("</");

                        for (int i = 1; (i < tag.length()) && !finished; i++)
                          {
                            char ch = tag.charAt(i);

                            if (!Character.isWhitespace(ch))
                              {
                                endTag.append(ch);
                              }
                            else
                              {
                                finished = true;
                              }
                          }

                        endTag.append(">");
                      }
                  }
              }

            str = endTag.toString();

            super.insertString(off, str, set);

            editor.getCaret().setDot(dot + 1);
          }
        else
          {
            super.insertString(off, str, set);
          }
      }
  }
