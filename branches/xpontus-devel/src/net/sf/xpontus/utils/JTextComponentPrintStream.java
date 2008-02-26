/*
 * JTextComponentPrintStream.java
 *
 * Created on February 10, 2007, 8:38 AM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.utils;

import java.awt.Rectangle;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;

/**
 *
 * @author Yves Zoundi
 */
public class JTextComponentPrintStream extends PrintStream {
    private JTextArea textArea;
    private StringBuffer line;

    public JTextComponentPrintStream(JTextArea textArea) {
        super(new ByteArrayOutputStream(0));
        this.textArea = textArea;
        line = new StringBuffer();
    }

    private void scrollToEnd() {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    if (isAdjusting()) {
                        return;
                    } else {
                        int height = textArea.getHeight();
                        textArea.scrollRectToVisible(new Rectangle(0,
                                height - 1, 1, height));
                        textArea.setCaretPosition(textArea.getDocument()
                                                          .getLength() - 1);

                        return;
                    }
                }
            });
    }

    private boolean isAdjusting() {
        return false;
    }

    public void close() {
    }

    public synchronized void flush() {
        if (line.length() > 0) {
            textArea.append(line.toString());
            line = new StringBuffer();
        }
    }

    public synchronized void write(byte[] b) {
        line.append(b);
    }

    public synchronized void write(byte[] b, int off, int len) {
        line.append(new String(b, off, len));
    }

    public synchronized void write(int c) {
        line.append(c);
    }

    public synchronized boolean checkError() {
        return false;
    }

    public synchronized void print(Object obj) {
        line.append(obj);
    }

    public synchronized void print(String s) {
        line.append(s);
    }

    public synchronized void print(boolean b) {
        line.append(b);
    }

    public synchronized void print(char c) {
        line.append(c);
    }

    public synchronized void print(char[] s) {
        line.append(s);
    }

    public synchronized void print(double d) {
        line.append(d);
    }

    public synchronized void print(float f) {
        line.append(f);
    }

    public synchronized void print(int i) {
        line.append(i);
    }

    public synchronized void print(long l) {
        line.append(l);
    }

    public synchronized void println() {
        line.append('\n');
        flush();
    }

    public synchronized void println(Object x) {
        if (line.length() > 0) {
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        } else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }

        scrollToEnd();
    }

    public synchronized void println(String x) {
        if (line.length() > 0) {
            textArea.append(line.append(x).toString());
            textArea.append("\n");
            line = new StringBuffer();
        } else {
            textArea.append(x);
            textArea.append("\n");
        }

        scrollToEnd();
    }

    public synchronized void println(boolean x) {
        if (line.length() > 0) {
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        } else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
            scrollToEnd();
        }
    }

    public synchronized void println(char x) {
        if (line.length() > 0) {
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        } else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }

        scrollToEnd();
    }

    public synchronized void println(char[] x) {
        if (line.length() > 0) {
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        } else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }

        scrollToEnd();
    }

    public synchronized void println(double x) {
        if (line.length() > 0) {
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        } else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }

        scrollToEnd();
    }

    public synchronized void println(float x) {
        if (line.length() > 0) {
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        } else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }

        scrollToEnd();
    }

    public synchronized void println(int x) {
        if (line.length() > 0) {
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        } else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }

        scrollToEnd();
    }

    public synchronized void println(long x) {
        if (line.length() > 0) {
            textArea.append(line.append(String.valueOf(x)).toString());
            textArea.append("\n");
            line = new StringBuffer();
        } else {
            textArea.append(String.valueOf(x));
            textArea.append("\n");
        }

        scrollToEnd();
    }

    protected void setError() {
    }
}

