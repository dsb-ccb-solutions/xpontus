/*
 *
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
 *
 */
package net.sf.xpontus.modules.gui.components;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.event.MouseEvent;

import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.MouseInputAdapter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class CurrentLineHighlighter {
    private static final String LINE_HIGHLIGHT = "linehilight";
    private static final String PREVIOUS_CARET = "previousCaret";
    private static Color col = new Color(255, 255, 204);
    private static Object obj;
    private static CaretListener caretListener = new CaretListener() {
            public void caretUpdate(CaretEvent e) {
                JTextComponent c = (JTextComponent) e.getSource();
                currentLineChanged(c);
            }
        };

    private static MouseInputAdapter mouseListener = new MouseInputAdapter() {
            public void mousePressed(MouseEvent e) {
                JTextComponent c = (JTextComponent) e.getSource();
                currentLineChanged(c);
            }

            public void mouseDragged(MouseEvent e) {
                JTextComponent c = (JTextComponent) e.getSource();
                currentLineChanged(c);
            }
        };

    private static Highlighter.HighlightPainter painter = new Highlighter.HighlightPainter() {
            public void paint(Graphics g, int p0, int p1, Shape bounds,
                JTextComponent c) {
                try {
                    Rectangle r = c.modelToView(c.getCaretPosition());
                    g.setColor(col);
                    g.fillRect(0, r.y, c.getWidth(), r.height);
                } catch (BadLocationException ignore) {
                }
            }
        };

    private CurrentLineHighlighter() {
    }

    public static void install(JTextComponent c) {
        try {
            obj = c.getHighlighter().addHighlight(0, 0, painter);
            c.putClientProperty(LINE_HIGHLIGHT, obj);
            c.putClientProperty(PREVIOUS_CARET,
                new Integer(c.getCaretPosition()));
            c.addCaretListener(caretListener);
            c.addMouseListener(mouseListener);
            c.addMouseMotionListener(mouseListener);
            c.repaint();
            System.out.println("Added highlighter");
        } catch (BadLocationException ignore) {
            System.out.println("Error installing highlighter");
        }
    }

    public static void uninstall(JTextComponent c) {
        c.putClientProperty(LINE_HIGHLIGHT, null);
        c.putClientProperty(PREVIOUS_CARET, null);
        c.getHighlighter().removeHighlight(obj);
        c.removeCaretListener(caretListener);
        c.removeMouseListener(mouseListener);
        c.removeMouseMotionListener(mouseListener);
        c.repaint();
    }

    private static void currentLineChanged(JTextComponent c) {
        try {
            int previousCaret = ((Integer) c.getClientProperty(PREVIOUS_CARET)).intValue();
            Rectangle prev = c.modelToView(previousCaret);
            Rectangle r = c.modelToView(c.getCaretPosition());
            c.putClientProperty(PREVIOUS_CARET,
                new Integer(c.getCaretPosition()));

            if (prev.y != r.y) {
                c.repaint(0, prev.y, c.getWidth(), r.height);
                c.repaint(0, r.y, c.getWidth(), r.height);
            }
        } catch (BadLocationException ignore) {
        }
    }
}
