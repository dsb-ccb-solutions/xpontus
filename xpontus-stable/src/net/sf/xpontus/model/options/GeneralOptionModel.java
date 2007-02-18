/*
 * GeneralOptionModel.java
 *
 * Created on February 24, 2006, 10:56 PM
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

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;

import java.beans.PropertyChangeListener;

import java.io.File;


/**
 * the model for general preferences
 * @author Yves Zoundi
 */
public class GeneralOptionModel extends ConfigurationModel {
    private boolean showSplashScreen = true;
    private boolean confirmOnExitOption = true;
    private String iconSet = "Eclipse";
    private String theme = "Plastic";
    private ExtendedPropertyChangeSupport changeSupport;

    /** Creates a new instance of GeneralOptionModel */
    public GeneralOptionModel() {
        changeSupport = new ExtendedPropertyChangeSupport(this);
    }

    public String getMappingURL() {
        return "/net/sf/xpontus/model/mappings/GeneralModel.xml";
    }

    public File getFileToSaveTo() {
        return XPontusConstants.GENERAL_PREF;
    }

    /**
     *
     * @return
     */
    public boolean isShowSplashScreen() {
        return showSplashScreen;
    }

    /**
     *
     * @param newValue
     */
    public void setShowSplashScreen(boolean newValue) {
        boolean oldValue = showSplashScreen;
        showSplashScreen = newValue;
        changeSupport.firePropertyChange("showSplashScreen", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public String getIconSet() {
        return iconSet;
    }

    /**
     *
     * @param newValue
     */
    public void setIconSet(String newValue) {
        String oldValue = iconSet;
        iconSet = newValue;
        changeSupport.firePropertyChange("iconSet", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public String getTheme() {
        return theme;
    }

    /**
     *
     * @param newValue
     */
    public void setTheme(String newValue) {
        String oldValue = theme;
        theme = newValue;
        changeSupport.firePropertyChange("theme", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public boolean isConfirmOnExitOption() {
        return confirmOnExitOption;
    }

    /**
     *
     * @param newValue
     */
    public void setConfirmOnExitOption(boolean newValue) {
        boolean oldValue = confirmOnExitOption;
        confirmOnExitOption = newValue;
        changeSupport.firePropertyChange("confirmOnExitOption", oldValue,
            newValue);
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
}
