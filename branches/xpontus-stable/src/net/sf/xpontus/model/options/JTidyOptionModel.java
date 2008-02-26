/*
 * JTidyOptionModel.java
 *
 * Created on February 24, 2006, 6:33 PM
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
import net.sf.xpontus.core.model.*;

import java.beans.PropertyChangeListener;

import java.io.File;


/**
 * the jtidy preferences model
 * @author Yves Zoundi
 */
public class JTidyOptionModel extends ConfigurationModel {
    private boolean wrapAttrOption = true;
    private boolean dropEmptyParasOption = true;
    private boolean indentOption = true;
    private boolean logicalEmphasisOption = true;
    private boolean uppercaseAttrOption = true;
    private boolean uppercaseTagsOption = true;
    private ExtendedPropertyChangeSupport changeSupport;

    /** Creates a new instance of JTidyOptionModel */
    public JTidyOptionModel() {
        changeSupport = new ExtendedPropertyChangeSupport(this);
    }

    public String getMappingURL() {
        return "/net/sf/xpontus/model/mappings/JTidyModel.xml";
    }

    public File getFileToSaveTo() {
        return XPontusConstants.JTIDY_PREF;
    }

    /**
     *
     * @return
     */
    public boolean isWrapAttrOption() {
        return wrapAttrOption;
    }

    /**
     *
     * @param newValue
     */
    public void setWrapAttrOption(boolean newValue) {
        boolean oldValue = wrapAttrOption;
        wrapAttrOption = newValue;
        changeSupport.firePropertyChange("wrapAttrOption", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public boolean isDropEmptyParasOption() {
        return dropEmptyParasOption;
    }

    /**
     *
     * @param newValue
     */
    public void setDropEmptyParasOption(boolean newValue) {
        boolean oldValue = dropEmptyParasOption;
        dropEmptyParasOption = newValue;
        changeSupport.firePropertyChange("dropEmptyParasOption", oldValue,
            newValue);
    }

    /**
     *
     * @return
     */
    public boolean isIndentOption() {
        return indentOption;
    }

    /**
     *
     * @param newValue
     */
    public void setIndentOption(boolean newValue) {
        boolean oldValue = indentOption;
        indentOption = newValue;
        changeSupport.firePropertyChange("indentOption", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public boolean isLogicalEmphasisOption() {
        return logicalEmphasisOption;
    }

    /**
     *
     * @param newValue
     */
    public void setLogicalEmphasisOption(boolean newValue) {
        boolean oldValue = logicalEmphasisOption;
        logicalEmphasisOption = newValue;
        changeSupport.firePropertyChange("logicalEmphasisOption", oldValue,
            newValue);
    }

    /**
     *
     * @return
     */
    public boolean isUppercaseAttrOption() {
        return uppercaseAttrOption;
    }

    /**
     *
     * @param newValue
     */
    public void setUppercaseAttrOption(boolean newValue) {
        boolean oldValue = uppercaseAttrOption;
        uppercaseAttrOption = newValue;
        changeSupport.firePropertyChange("uppercaseAttrOption", oldValue,
            newValue);
    }

    /**
     *
     * @return
     */
    public boolean isUppercaseTagsOption() {
        return uppercaseTagsOption;
    }

    /**
     *
     * @param newValue
     */
    public void setUppercaseTagsOption(boolean newValue) {
        boolean oldValue = uppercaseTagsOption;
        uppercaseTagsOption = newValue;
        changeSupport.firePropertyChange("uppercaseTagsOption", oldValue,
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
