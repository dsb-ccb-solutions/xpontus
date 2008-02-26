/*
 * EditorOptionModel.java
 *
 * Created on February 24, 2006, 10:43 PM
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.model.options;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import com.sun.java.help.impl.SwingWorker;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;
import net.sf.xpontus.view.PaneForm;
import net.sf.xpontus.view.XPontusWindow;
import net.sf.xpontus.view.editor.LineView;

import java.awt.Font;

import java.beans.PropertyChangeListener;

import java.io.File;

import javax.swing.JEditorPane;
import javax.swing.UIManager;


/**
 * The model for the editor's preferences
 * @author Yves Zoundi
 */
public class EditorOptionModel extends ConfigurationModel {
    private boolean showLineNumbers = true;
    private Long tabSize = new Long(4);
    private String fontName = "Monospaced";
    private String fontSize = 12 + "";
    private int cursorRate = 500;
    private ExtendedPropertyChangeSupport changeSupport;

    /** Creates a new instance of EditorOptionModel */
    public EditorOptionModel() {
        changeSupport = new ExtendedPropertyChangeSupport(this);
    }

    /**
     *
     * @return
     */
    public String getMappingURL() {
        return "/net/sf/xpontus/model/mappings/EditorModel.xml";
    }

    /**
     *
     * @return
     */
    public File getFileToSaveTo() {
        return XPontusConstants.EDITOR_PREF;
    }

    /**
     *
     * @return
     */
    public boolean isShowLineNumbers() {
        return showLineNumbers;
    }

    /**
     *
     * @param newValue
     */
    public void setShowLineNumbers(boolean newValue) {
        boolean oldValue = showLineNumbers;
        showLineNumbers = newValue;
        changeSupport.firePropertyChange("showLineNumbers", oldValue, newValue);

        final boolean b = newValue;
        final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    PaneForm paneform = XPontusWindow.getInstance().getPane();

                    if (b) {
                        for (int i = 0; i < paneform.getTabCount(); i++) {
                            javax.swing.JScrollPane sp = (javax.swing.JScrollPane) paneform.getComponentAt(i);

                            if (sp.getRowHeader() == null) {
                                javax.swing.JEditorPane edit = paneform.getEditorAt(i);
                                sp.setRowHeaderView(new LineView(edit));
                                sp.repaint();
                            } else if (sp.getRowHeader().getComponentCount() == 0) {
                                javax.swing.JEditorPane edit = paneform.getEditorAt(i);
                                sp.setRowHeaderView(new LineView(edit));
                                sp.repaint();
                            }
                        }
                    } else {
                        for (int i = 0; i < paneform.getTabCount(); i++) {
                            javax.swing.JScrollPane sp = (javax.swing.JScrollPane) paneform.getComponentAt(i);
                            sp.setRowHeaderView(null);
                            sp.repaint();
                        }
                    }

                    return null;
                }
            };

        worker.start();
    }

    /**
     *
     * @return
     */
    public Long getTabSize() {
        return tabSize;
    }

    /**
     *
     * @param tabSize
     */
    public void setTabSize(Long newValue) {
        Long oldValue = tabSize;
        tabSize = newValue;
        changeSupport.firePropertyChange("tabSize", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public int getCursorRate() {
        return cursorRate;
    }

    /**
     *
     * @param newValue
     */
    public void setCursorRate(int newValue) {
        int oldValue = cursorRate;
        cursorRate = newValue;
        changeSupport.firePropertyChange("cursorRate", oldValue, newValue);
    }

    /**
     *
     * @param x
     */
    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }

    /**
     *
     * @param x
     */
    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }

    /**
     *
     * @return
     */
    public String getFontName() {
        return fontName;
    }

    /**
     *
     * @param newValue
     */
    public void setFontName(String newValue) {
        String oldValue = fontName;
        fontName = newValue;
        changeSupport.firePropertyChange("fontName", oldValue, newValue);
        UIManager.put("EditorPane.font",
            new Font(fontName, Font.PLAIN, Integer.parseInt(fontSize)));

        updateFont();
    }

    /**
     *
     * @return
     */
    public String getFontSize() {
        return fontSize;
    }

    private void updateFont() {
        PaneForm pane = XPontusWindow.getInstance().getPane();

        for (int i = 0; i < pane.getTabCount(); i++) {
            JEditorPane edit = pane.getEditorAt(i);
            edit.setFont(new Font(fontName, Font.PLAIN,
                    Integer.parseInt(fontSize)));
            edit.repaint();
        }
    }

    /**
     *
     * @param newValue
     */
    public void setFontSize(String newValue) {
        String oldValue = fontSize;
        fontSize = newValue;
        changeSupport.firePropertyChange("fontSize", oldValue, newValue);
        UIManager.put("EditorPane.font",
            new Font(fontName, Font.PLAIN, Integer.parseInt(fontSize)));
        updateFont();
    }
}
