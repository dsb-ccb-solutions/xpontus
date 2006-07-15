/*
 * FontSelection.java
 *
 * Created on February 13, 2006, 6:01 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Rectangle;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.Arrays;
import java.util.Vector;

import javax.swing.JPanel;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;


/**
 *
 * @author Yves Zoundi
 */

/*
 * The Apache Software License, Version 1.1
 *
 * Copyright (c) 2003 Thomas Zander zander@kde.org  All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 * 1. Redistributions of source code must retain the above copyright
 *    notice, this list of conditions and the following disclaimer.
 *
 * 2. Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in
 *    the documentation and/or other materials provided with the
 *    distribution.
 *
 * 3. The end-user documentation included with the redistribution, if
 *    any, must include the following acknowlegement:
 *       "This product includes software developed by the
 *        Apache Software Foundation (http://www.apache.org/)."
 *    Alternately, this acknowlegement may appear in the software itself,
 *    if and wherever such third-party acknowlegements normally appear.
 *
 * 4. The names "uic", "UICompiler", and "Apache Software Foundation"
 *    must not be used to endorse or promote products derived
 *    from this software without prior written permission. For written
 *    permission, please contact apache@apache.org.
 *
 * 5. Products derived from this software may not be called "Apache"
 *    nor may "Apache" appear in their names without prior written
 *    permission of the Apache Group.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL THE APACHE SOFTWARE FOUNDATION OR
 * ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
 * SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
 * LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF
 * USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND
 * ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT
 * OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 * ====================================================================
 *
 * This software consists of voluntary contributions made by many
 * individuals on behalf of the Apache Software Foundation.  For more
 * information on the Apache Software Foundation, please see
 * <http://www.apache.org/>.
 */

/**
 * This widget shows a list of installed fonts and allows the user to choose one.
 * <p><img src="/images/fontselector.png">
 * @since 1.1
 */
public class FontSelection extends JPanel {
    private static Rectangle formerLocation = null;
    private Font selectedFont;
    private float selectedSize = 12;
    private int selectedStyle = Font.PLAIN;
    private Vector familyNames;
    private Vector defaultFontSizes;
    private Vector defaultStyles;
    private boolean passSizeSignals = false;
    javax.swing.JList familyList;
    javax.swing.JList sizeList;
    javax.swing.JList styleList;
    javax.swing.JLabel previewLabel;
    javax.swing.JTextField fontSizeEdit;

    /**
     * Default constructor which fills all lists with the fonts present on the system.
     */
    public FontSelection() {
        String[] fonts = GraphicsEnvironment.getLocalGraphicsEnvironment()
                                            .getAvailableFontFamilyNames();
        familyNames = new Vector(Arrays.asList(fonts));
        familyList.setListData(familyNames);
        defaultFontSizes = new Vector(Arrays.asList(
                    new Object[] {
                        new Integer(4), new Integer(5), new Integer(6),
                        new Integer(7), new Integer(8), new Integer(9),
                        new Integer(10), new Integer(11), new Integer(12),
                        new Integer(13), new Integer(14), new Integer(15),
                        new Integer(16), new Integer(17), new Integer(18),
                        new Integer(19), new Integer(20), new Integer(22),
                        new Integer(24), new Integer(26), new Integer(28),
                        new Integer(32), new Integer(48), new Integer(64)
                    }));
        sizeList.setListData(defaultFontSizes);

        defaultStyles = new Vector(Arrays.asList(
                    new String[] { "Plain", "Bold", "Bold/Italic", "Italic" }));
        styleList.setListData(defaultStyles);

        previewLabel.setMinimumSize(new Dimension(200, 100));
        previewLabel.setText("The Quick Brown Fox Jumps Over The Lazy Dog");

        familyList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent ae) {
                    if (ae.getValueIsAdjusting()) {
                        return;
                    }

                    familySelectedSlot((String) familyList.getSelectedValue());
                }
            });
        styleList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent ae) {
                    if (ae.getValueIsAdjusting()) {
                        return;
                    }

                    styleSelectedSlot((String) styleList.getSelectedValue());
                }
            });
        sizeList.addListSelectionListener(new ListSelectionListener() {
                public void valueChanged(ListSelectionEvent ae) {
                    if (ae.getValueIsAdjusting()) {
                        return;
                    }

                    sizeSelectedSlot((sizeList.getSelectedValue() == null)
                        ? null : sizeList.getSelectedValue().toString());
                }
            });
        fontSizeEdit.setDocument(new javax.swing.text.PlainDocument() {
                public void insertString(int offs, String str, AttributeSet a)
                    throws BadLocationException {
                    if (str == null) {
                        return;
                    }

                    try {
                        Integer.parseInt(str);
                        super.insertString(offs, str, a);
                    } catch (java.lang.NumberFormatException e) {
                    }
                }
            });
        fontSizeEdit.getDocument().addDocumentListener(new DocumentListener() {
                public void insertUpdate(DocumentEvent event) {
                    sizeSelectedSlot(fontSizeEdit.getText());
                }

                public void removeUpdate(DocumentEvent event) {
                    sizeSelectedSlot(fontSizeEdit.getText());
                }

                public void changedUpdate(DocumentEvent event) {
                }
            });
        fontSizeEdit.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        passSizeSignals = true;
    }

    public static Font selectFontDialog(Window parent, final Font selected) {
        final FontSelection content = new FontSelection();
        content.setFont(selected);

        javax.swing.JDialog diag = new javax.swing.JDialog();
        javax.swing.JPanel buttonPanel = new javax.swing.JPanel();
        java.awt.LayoutManager l = new java.awt.FlowLayout(java.awt.FlowLayout.CENTER);
        javax.swing.JButton button = new javax.swing.JButton("OK");
        javax.swing.JButton cancelButton = new javax.swing.JButton("OK");
        diag.add(content, BorderLayout.CENTER);

        if (selected != null) {
            content.setFont(selected);
        }

        diag.setLocationRelativeTo(parent);

        return null;
    }

    /**
     * Method called by the GUi code.
     */
    public void familySelectedSlot(String name) {
        selectedFont = Font.decode(name);
        selectedFont = selectedFont.deriveFont(selectedStyle, selectedSize);
        previewLabel.setFont(selectedFont);
    }

    /**
     * Method called by the GUi code.
     */
    public void sizeSelectedSlot(String size) {
        if (!passSizeSignals) {
            return;
        }

        if ((size == null) || (size.length() == 0)) {
            return;
        }

        passSizeSignals = false;

        sizeList.setSelectedValue(new Integer(size), true);
        selectedSize = (float) Integer.parseInt(size);
        selectedFont = selectedFont.deriveFont(selectedSize);
        previewLabel.setFont(selectedFont);

        if (!fontSizeEdit.getText().equals(size)) {
            fontSizeEdit.setText(size);
        }

        passSizeSignals = true;
    }

    /**
     * Method called by the GUi code.
     */
    public void styleSelectedSlot(String style) {
        switch (defaultStyles.indexOf(style)) {
        case 1:
            selectedStyle = Font.BOLD;

            break;

        case 2:
            selectedStyle = Font.BOLD | Font.ITALIC;

            break;

        case 3:
            selectedStyle = Font.ITALIC;

            break;

        default:
            selectedStyle = Font.PLAIN;
        }

        selectedFont = selectedFont.deriveFont(selectedStyle);
        previewLabel.setFont(selectedFont);
    }

    /**
     * Set a font which will then be selected in the lists.
     * @param newFont the font that is to be selected. The family, size and style will be
     *  read from the font object.
     */
    public void setFont(Font newFont) {
        if (familyNames == null) {
            return;
        }

        if (newFont == null) {
            return;
        }

        selectedFont = newFont;

        String size = String.valueOf(selectedFont.getSize());
        String family = selectedFont.getName();
        familyList.setSelectedValue(family, true);

        if (defaultFontSizes.contains(size)) {
            sizeList.setSelectedValue(new Integer(size), true);
        } else {
            sizeSelectedSlot(size);
        }

        int style = 0;

        if (newFont.isBold()) {
            if (newFont.isItalic()) {
                style = 2;
            } else {
                style = 1;
            }
        } else if (newFont.isItalic()) {
            style = 3;
        }

        styleList.setSelectedIndex(style);
    }

    /**
     * Return the currently selected font.
     * @return the currently selected font.
     */
    public Font getFont() {
        return selectedFont;
    }
}
