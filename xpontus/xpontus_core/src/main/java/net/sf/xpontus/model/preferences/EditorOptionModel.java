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
package net.sf.xpontus.model.preferences;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.model.ConfigurationModel;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import java.io.File;


/**
 *
 * @author Yves Zoundi
 */
public class EditorOptionModel extends ConfigurationModel {
    private boolean showLineNumbers = true;
    private Long tabSize = new Long(4);
    private String fontName = "Dialog";
    private String fontSize = 12 + "";
    private int cursorRate = 500;
    private PropertyChangeSupport changeSupport;

    /** Creates a new instance of EditorOptionModel */
    public EditorOptionModel() {
        changeSupport = new PropertyChangeSupport(this);
    }

    /**
     *
     * @return
     */
    public File getFileToSaveTo() {
        return XPontusConfigurationConstantsIF.GENERAL_PREFERENCES_FILE;
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
     * @param newValue 
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
    }

    /**
     *
     * @return
     */
    public String getFontSize() {
        return fontSize;
    }

    /**
     *
     * @param newValue
     */
    public void setFontSize(String newValue) {
        String oldValue = fontSize;
        fontSize = newValue;
        changeSupport.firePropertyChange("fontSize", oldValue, newValue);
    }
}
