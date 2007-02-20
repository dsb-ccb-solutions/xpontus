package net.sf.xpontus.view.tabbedpane;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;

import javax.swing.JComponent;
import javax.swing.plaf.ComponentUI;
import javax.swing.plaf.basic.BasicGraphicsUtils;
import javax.swing.text.View;


public class CloseTabPaneEclipseUI extends CloseTabPaneUI {
    private static final Color whiteColor = Color.white;
    private static final Color transparent = new Color(0, 0, 0, 0);
    private static final Color lightBlue = new Color(130, 200, 250, 50);
    private static final Color lightWhite = new Color(200, 200, 200, 50);
    private static final Color selectedColor = new Color(15, 70, 180);

    public static ComponentUI createUI(JComponent c) {
        return new CloseTabPaneEclipseUI();
    }

    protected void paintFocusIndicator(Graphics g, int tabPlacement,
        Rectangle[] rects, int tabIndex, Rectangle iconRect,
        Rectangle textRect, boolean isSelected) {
    }

    protected void paintTabBorder(Graphics g, int tabPlacement, int tabIndex,
        int x, int y, int w, int h, boolean isSelected) {
        g.setColor(shadow);

        g.drawLine(x, y + 2, x, (y + h) - 1); // left highlight
        g.drawLine(x + 1, y + 1, x + 1, y + 1); // top-left highlight
        g.drawLine(x + 2, y, (x + w) - 3, y); // top highlight              
        g.drawLine((x + w) - 1, y + 2, (x + w) - 1, (y + h) - 1);
        g.drawLine((x + w) - 2, y + 1, (x + w) - 2, y + 1); // top-right shadow

        if (isSelected) {
            //Do the highlights
            g.setColor(lightHighlight);
            g.drawLine(x + 2, y + 2, x + 2, (y + h) - 1);
            g.drawLine(x + 3, y + 1, (x + w) - 3, y + 1);
            g.drawLine((x + w) - 3, y + 2, (x + w) - 3, y + 2);
            g.drawLine((x + w) - 2, y + 2, (x + w) - 2, (y + h) - 1);
        }
    }

    protected void paintContentBorderTopEdge(Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h) {
        g.setColor(shadow);
        g.drawLine(x, y, (x + w) - 2, y);
    }

    protected void paintContentBorderLeftEdge(Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h) {
        g.setColor(shadow);

        g.drawLine(x, y, x, (y + h) - 3);
    }

    protected void paintContentBorderBottomEdge(Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h) {
        g.setColor(shadow);
        g.drawLine(x + 1, (y + h) - 3, (x + w) - 2, (y + h) - 3);
        g.drawLine(x + 1, (y + h) - 2, (x + w) - 2, (y + h) - 2);
        g.setColor(shadow.brighter());
        g.drawLine(x + 2, (y + h) - 1, (x + w) - 1, (y + h) - 1);
    }

    protected void paintContentBorderRightEdge(Graphics g, int tabPlacement,
        int selectedIndex, int x, int y, int w, int h) {
        g.setColor(shadow);

        g.drawLine((x + w) - 3, y + 1, (x + w) - 3, (y + h) - 3);
        g.drawLine((x + w) - 2, y + 1, (x + w) - 2, (y + h) - 3);
        g.setColor(shadow.brighter());
        g.drawLine((x + w) - 1, y + 2, (x + w) - 1, (y + h) - 2);
    }

    protected void paintTabBackground(Graphics g, int tabPlacement,
        int tabIndex, int x, int y, int w, int h, boolean isSelected) {
        if (isSelected) {
            GradientPaint leftGradient;
            GradientPaint rightGradient;

            int delta = 10;
            int delta2 = 8;

            if (isCloseEnabled()) {
                delta += (BUTTONSIZE + WIDTHDELTA);
                delta2 += BUTTONSIZE;
            }

            if (tabPane.isEnabledAt(tabIndex)) {
                leftGradient = new GradientPaint(x, y, selectedColor,
                        x + (w / 2), y, lightBlue);

                rightGradient = new GradientPaint(x + (w / 2), y, lightBlue,
                        x + w + delta, y, transparent);
            } else {
                leftGradient = new GradientPaint(x, y, shadow, x + (w / 2), y,
                        lightWhite);

                rightGradient = new GradientPaint(x + (w / 2), y, lightWhite,
                        x + w + delta, y, transparent);
            }

            Graphics2D g2 = (Graphics2D) g;
            g2.setPaint(leftGradient);
            g2.fillRect(x + 2, y + 2, w / 2, h - 2);
            g2.setPaint(rightGradient);
            g2.fillRect(x + 2 + (w / 2), y + 2, (w / 2) - delta2, h - 2);
        }
    }

    protected void paintText(Graphics g, int tabPlacement, Font font,
        FontMetrics metrics, int tabIndex, String title, Rectangle textRect,
        boolean isSelected) {
        g.setFont(font);

        View v = getTextViewForTab(tabIndex);

        if (v != null) {
            // html
            v.paint(g, textRect);
        } else {
            // plain text
            int mnemIndex = tabPane.getDisplayedMnemonicIndexAt(tabIndex);

            if (tabPane.isEnabled() && tabPane.isEnabledAt(tabIndex)) {
                if (isSelected) {
                    g.setColor(whiteColor);
                } else {
                    g.setColor(tabPane.getForegroundAt(tabIndex));
                }

                BasicGraphicsUtils.drawStringUnderlineCharAt(g, title,
                    mnemIndex, textRect.x, textRect.y + metrics.getAscent());
            } else { // tab disabled
                g.setColor(tabPane.getBackgroundAt(tabIndex).brighter());
                BasicGraphicsUtils.drawStringUnderlineCharAt(g, title,
                    mnemIndex, textRect.x, textRect.y + metrics.getAscent());
                g.setColor(tabPane.getBackgroundAt(tabIndex).darker());
                BasicGraphicsUtils.drawStringUnderlineCharAt(g, title,
                    mnemIndex, textRect.x - 1,
                    (textRect.y + metrics.getAscent()) - 1);
            }
        }
    }

    protected CloseTabPaneUI.ScrollableTabButton createScrollableTabButton(
        int direction) {
        return new ScrollableTabButton(direction);
    }

    protected class ScrollableTabButton extends CloseTabPaneUI.ScrollableTabButton {
        public ScrollableTabButton(int direction) {
            super(direction);
            setRolloverEnabled(true);
        }

        public Dimension getPreferredSize() {
            return new Dimension(16, calculateMaxTabHeight(0));
        }

        public void paint(Graphics g) {
            Color origColor;
            boolean isPressed;
            boolean isRollOver;
            boolean isEnabled;
            int w;
            int h;
            int size;

            w = getSize().width;
            h = getSize().height;
            origColor = g.getColor();
            isPressed = getModel().isPressed();
            isRollOver = getModel().isRollover();
            isEnabled = isEnabled();

            g.setColor(getBackground());
            g.fillRect(0, 0, w, h);

            g.setColor(shadow);

            // Using the background color set above
            if (direction == WEST) {
                g.drawLine(0, 0, 0, h - 1); //left
                g.drawLine(w - 1, 0, w - 1, 0); //right
            } else {
                g.drawLine(w - 2, h - 1, w - 2, 0); //right
            }

            g.drawLine(0, 0, w - 2, 0); //top

            if (isRollOver) {
                //do highlights or shadows
                Color color1;
                Color color2;

                if (isPressed) {
                    color2 = whiteColor;
                    color1 = shadow;
                } else {
                    color1 = whiteColor;
                    color2 = shadow;
                }

                g.setColor(color1);

                if (direction == WEST) {
                    g.drawLine(1, 1, 1, h - 1); //left
                    g.drawLine(1, 1, w - 2, 1); //top
                    g.setColor(color2);
                    g.drawLine(w - 1, h - 1, w - 1, 1); //right
                } else {
                    g.drawLine(0, 1, 0, h - 1);
                    g.drawLine(0, 1, w - 3, 1); //top
                    g.setColor(color2);
                    g.drawLine(w - 3, h - 1, w - 3, 1); //right
                }
            }

            //g.drawLine(0, h - 1, w - 1, h - 1); //bottom

            // If there's no room to draw arrow, bail
            if ((h < 5) || (w < 5)) {
                g.setColor(origColor);

                return;
            }

            if (isPressed) {
                g.translate(1, 1);
            }

            // Draw the arrow
            size = Math.min((h - 4) / 3, (w - 4) / 3);
            size = Math.max(size, 2);
            paintTriangle(g, (w - size) / 2, (h - size) / 2, size, direction,
                isEnabled);

            // Reset the Graphics back to it's original settings
            if (isPressed) {
                g.translate(-1, -1);
            }

            g.setColor(origColor);
        }
    }
}
