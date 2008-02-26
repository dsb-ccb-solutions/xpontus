package net.sf.xpontus.view.editor;

import java.awt.Color;
import java.awt.event.AdjustmentEvent;
import java.awt.event.AdjustmentListener;

import javax.swing.JScrollPane;
import javax.swing.border.LineBorder;
import javax.swing.text.JTextComponent;


public class LineView extends javax.swing.JLabel
{
    /**
         *
         */
    private static final long serialVersionUID = 8918688944159766510L;
    private javax.swing.JEditorPane edit;
    private java.awt.FontMetrics tpFontMetrics;
    private java.awt.Font fnt;
    private final int rhWidth = 50;

    /**
         *
         * @param edit
         *                the editor which has to display line numbers
         */
    public LineView(javax.swing.JEditorPane edit)
    {
        this.edit = edit;
        setForeground(Color.BLACK);
        fnt = edit.getFont();
        setFont(fnt);
        tpFontMetrics = getFontMetrics(fnt);
    }

    /**
         *
         * @return The preferred size of the line numbering column
         */
    public java.awt.Dimension getPreferredSize()
    {
        java.awt.Dimension dim;
        int w = rhWidth + 10;
        int h = (int) edit.getPreferredSize().getHeight();
        dim = new java.awt.Dimension(w, h);

        return dim;
    }

    /**
         *
         * @param g
         *                A graphic component
         */
    public void paintComponent(java.awt.Graphics g)
    {
        super.paintComponent(g);

        int h = g.getFontMetrics().getHeight();
        int i = h - tpFontMetrics.getDescent();
        int row = 1;

        while (i < getHeight())
        {
            String s = Integer.toString(row);
            g.drawString(s, (5 + rhWidth) - tpFontMetrics.stringWidth(s), i);
            i += h;
            row++;
        }
    }
}
