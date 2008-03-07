/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.completion;

import net.sf.xpontus.syntax.ILexer;
import net.sf.xpontus.syntax.SyntaxDocument;
import net.sf.xpontus.syntax.Token;

import java.awt.Rectangle;
import java.awt.event.KeyEvent;

import java.text.Collator;

import java.util.Collections;
import java.util.List;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;


/**
 *
 * @author mrcheeks
 */
public class ContentAssistWindow {
    private static JList completionList;
    private static JPopupMenu completionMenu;
    private static String endTag = new String(); 

   

    public static String tagInside(Document doc, int off) {
        SyntaxDocument mDoc = (SyntaxDocument) doc;
        ILexer lexer = mDoc.getLexer();

        Element root = mDoc.getDefaultRootElement();

        int lineIndex = root.getElementIndex(off);
        Element lineElement = root.getElement(lineIndex);

        int startOffset = lineElement.getEndOffset();
        int endOffset = off;

        List tokens = mDoc.getTokenListForLine(lineIndex);

        Token currentToken = null;
        Token tagToken = null;

        boolean inside = false;

        for (int i = 0; i < tokens.size(); i++) {
            currentToken = (Token) tokens.get(i);

            if (currentToken.endColumn > off) {
                break;
            }
        }

        if (tagToken != null) {
            if (tagToken.kind == 15) {
                return tagToken.image;
            }
        }

        return null;
    }

    public static void complete(final JTextComponent editor,
        final CodeCompletionIF contentAssist, int off, final String str,
        final AttributeSet set) {
        List completionData = contentAssist.getCompletionList(off);

        final Document doc = editor.getDocument();

//        if (str.equals(">")) {
//            completeEndTag(editor, off, str, set);
//        } else if (str.equals(" ")) {
//            String tagCompletionName = null; //tagInside(editor.getDocument(), off);
//
//            if (tagCompletionName != null) {
//                List attributeCompletion = contentAssist.getAttributesCompletionList(tagCompletionName);
//                completionData = attributeCompletion;
//
//                if ((completionData == null) || (completionData.size() == 0)) {
//                    return;
//                }
//
//                try {
//                    Collections.sort(completionData, new SimpleComparator());
//                    completionList = new javax.swing.JList(completionData.toArray());
//
//                    final int offset = off;
//
//                    completionList.addMouseListener(new java.awt.event.MouseAdapter() {
//                            public void mouseReleased(
//                                java.awt.event.MouseEvent e) {
//                                try {
//                                    if (e.getClickCount() == 2) {
//                                        doc.insertString(editor.getCaretPosition(),
//                                            completionList.getSelectedValue()
//                                                          .toString(), set);
//                                        completionMenu.setVisible(false);
//                                    }
//                                } catch (javax.swing.text.BadLocationException ex) {
//                                    ex.printStackTrace();
//                                }
//                            }
//                        });
//                    completionList.addKeyListener(new java.awt.event.KeyAdapter() {
//                            public void keyReleased(java.awt.event.KeyEvent e) {
//                                switch (e.getKeyCode()) {
//                                //                                case KeyEvent.VK_SPACE:
//                                //                                    completionMenu.setVisible(false);
//                                //
//                                //                                    break;
//                                case KeyEvent.VK_BACK_SPACE:
//                                    completionMenu.setVisible(false);
//
//                                    break;
//
//                                case KeyEvent.VK_ESCAPE:
//                                    completionMenu.setVisible(false);
//
//                                    break;
//
//                                case java.awt.event.KeyEvent.VK_ENTER:
//
//                                    try {
//                                        doc.insertString(editor.getCaretPosition(),
//                                            completionList.getSelectedValue()
//                                                          .toString(), null);
//
//                                        completionMenu.setVisible(false);
//                                    } catch (javax.swing.text.BadLocationException ex) {
//                                        ex.printStackTrace();
//                                    }
//
//                                    break;
//
//                                default:
//                                    break;
//                                }
//                            }
//                        });
//                    completionList.setSelectedIndex(0);
//
//                    javax.swing.JScrollPane completionPane = new javax.swing.JScrollPane(completionList);
//
//                    completionMenu = new javax.swing.JPopupMenu();
//                    completionMenu.add(completionPane);
//
//                    int dotPosition = editor.getCaretPosition();
//                    Rectangle popupLocation = editor.modelToView(dotPosition);
//
//                    completionList.setVisibleRowCount((completionData.size() > 10)
//                        ? 10 : completionData.size());
//                    completionMenu.show(editor, popupLocation.x, popupLocation.y);
//                    completionList.grabFocus();
//                } catch (Exception npe) {
//                }
//            }
//        } else {
//            if ((completionData == null) || (completionData.size() == 0)) {
//                return;
//            }
//
//            try {
//                Collections.sort(completionData, new SimpleComparator());
//                completionList = new javax.swing.JList(completionData.toArray());
//
//                final int offset = off;
//
//                completionList.addMouseListener(new java.awt.event.MouseAdapter() {
//                        public void mouseReleased(java.awt.event.MouseEvent e) {
//                            try {
//                                if (e.getClickCount() == 2) {
//                                    doc.insertString(editor.getCaretPosition(),
//                                        completionList.getSelectedValue()
//                                                      .toString(), set);
//                                    completionMenu.setVisible(false);
//                                }
//                            } catch (javax.swing.text.BadLocationException ex) {
//                                ex.printStackTrace();
//                            }
//                        }
//                    });
//                completionList.addKeyListener(new java.awt.event.KeyAdapter() {
//                        public void keyReleased(java.awt.event.KeyEvent e) {
//                            switch (e.getKeyCode()) {
//                            case KeyEvent.VK_SPACE:
//                                completionMenu.setVisible(false);
//
//                                break;
//
//                            case KeyEvent.VK_BACK_SPACE:
//                                completionMenu.setVisible(false);
//
//                                break;
//
//                            case KeyEvent.VK_ESCAPE:
//                                completionMenu.setVisible(false);
//
//                                break;
//
//                            case java.awt.event.KeyEvent.VK_ENTER:
//
//                                try {
//                                    doc.insertString(editor.getCaretPosition(),
//                                        completionList.getSelectedValue()
//                                                      .toString(), null);
//
//                                    completionMenu.setVisible(false);
//                                } catch (javax.swing.text.BadLocationException ex) {
//                                    ex.printStackTrace();
//                                }
//
//                                break;
//
//                            default:
//                                break;
//                            }
//                        }
//                    });
//                completionList.setSelectedIndex(0);
//
//                javax.swing.JScrollPane completionPane = new javax.swing.JScrollPane(completionList);
//
//                completionMenu = new javax.swing.JPopupMenu();
//                completionMenu.add(completionPane);
//
//                int dotPosition = editor.getCaretPosition();
//                Rectangle popupLocation = editor.modelToView(dotPosition);
//
//                completionList.setVisibleRowCount((completionData.size() > 10)
//                    ? 10 : completionData.size());
//                completionMenu.show(editor, popupLocation.x, popupLocation.y);
//                completionList.grabFocus();
//            } catch (Exception ex) {
//                java.util.logging.Logger.getLogger("global")
//                                        .log(java.util.logging.Level.SEVERE,
//                    ex.getMessage(), ex);
//            }
//        }
    }
}
