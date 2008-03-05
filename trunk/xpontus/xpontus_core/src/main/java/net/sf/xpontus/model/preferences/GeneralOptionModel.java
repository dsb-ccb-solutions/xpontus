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

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;


/**
 *
 * Model for the general preferences
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class GeneralOptionModel {
    private boolean showSplash = true;
    private boolean showConfirmDialogOnExit = true;
    private String defaultTheme;
    private String defaultIconSet;
    private String toolBarStyle;
    private String menuBarStyle;
    private transient PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener l) {
        this.pcs.addPropertyChangeListener(l);
    }

    public void addPropertyChangeListener(String propertyName,
        PropertyChangeListener l) {
        this.pcs.addPropertyChangeListener(propertyName, l);
    }

    public PropertyChangeListener[] getPropertyChangeListeners() {
        return this.pcs.getPropertyChangeListeners();
    }

    public void removePropertyChangeListener(PropertyChangeListener l) {
        this.pcs.removePropertyChangeListener(l);
    }

    public void removePropertyChangeListener(String propertyName,
        PropertyChangeListener l) {
        this.pcs.removePropertyChangeListener(propertyName, l);
    }

    /**
     *
     * @return
     */
    public String getDefaultIconSet() {
        return defaultIconSet;
    }

    /**
     *
     * @param defaultIconSet
     */
    public void setDefaultIconSet(String defaultIconSet) {
        this.pcs.firePropertyChange("defaultIconSet", this.defaultIconSet,
            this.defaultIconSet = defaultIconSet);
    }

    /**
     *
     * @return
     */
    public boolean isShowConfirmDialogOnExit() {
        return showConfirmDialogOnExit;
    }

    /**
     *
     * @param showConfirmDialogOnExit
     */
    public void setShowConfirmDialogOnExit(boolean showConfirmDialogOnExit) {
        this.pcs.firePropertyChange("showConfirmDialogOnExit",
            this.showConfirmDialogOnExit,
            this.showConfirmDialogOnExit = showConfirmDialogOnExit);
    }

    /**
     *
     * @return
     */
    public String getDefaultTheme() {
        return defaultTheme;
    }

    /**
     *
     * @param defaultTheme
     */
    public void setDefaultTheme(String defaultTheme) {
        this.pcs.firePropertyChange("defaultTheme", this.defaultTheme,
            this.defaultTheme = defaultTheme);
    }

    /**
     *
     * @return
     */
    public String getMenuBarStyle() {
        return menuBarStyle;
    }

    /**
     *
     * @param menuBarStyle
     */
    public void setMenuBarStyle(String menuBarStyle) {
        this.pcs.firePropertyChange("menuBarStyle", this.menuBarStyle,
            this.menuBarStyle = menuBarStyle);
    }

    /**
     *
     * @return
     */
    public boolean isShowSplash() {
        return showSplash;
    }

    /**
     *
     * @param showSplash
     */
    public void setShowSplash(boolean showSplash) {
        this.pcs.firePropertyChange("showSplash", this.showSplash,
            this.showSplash = showSplash);
    }

    /**
     *
     * @return
     */
    public String getToolBarStyle() {
        return toolBarStyle;
    }

    /**
     *
     * @param toolBarStyle
     */
    public void setToolBarStyle(String toolBarStyle) {
        this.pcs.firePropertyChange("toolBarStyle", this.toolBarStyle,
            this.toolBarStyle = toolBarStyle);
    }
}
