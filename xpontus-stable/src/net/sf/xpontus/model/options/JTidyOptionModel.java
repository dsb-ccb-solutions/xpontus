/*
 * JTidyOptionModel.java
 *
 * Created on February 24, 2006, 6:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.model.options;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import java.beans.PropertyChangeListener;
import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.*;

import java.io.File;


/**
 *
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
    
    public boolean isWrapAttrOption() {
        return wrapAttrOption;
    }
    
    public void setWrapAttrOption(boolean newValue) {
        boolean oldValue = wrapAttrOption;
        wrapAttrOption = newValue;
        changeSupport.firePropertyChange("wrapAttrOption", oldValue, newValue);
    }
    
    public boolean isDropEmptyParasOption() {
        return dropEmptyParasOption;
    }
    
    public void setDropEmptyParasOption(boolean newValue) {
        boolean oldValue = dropEmptyParasOption;
        dropEmptyParasOption = newValue;
        changeSupport.firePropertyChange("dropEmptyParasOption", oldValue, newValue);
    }
    
    public boolean isIndentOption() {
        return indentOption;
    }
    
    public void setIndentOption(boolean newValue) {
        boolean oldValue = indentOption;
        indentOption = newValue;
        changeSupport.firePropertyChange("indentOption", oldValue, newValue);
    }
    
    public boolean isLogicalEmphasisOption() {
        return logicalEmphasisOption;
    }
    
    public void setLogicalEmphasisOption(boolean newValue) {
        boolean oldValue = logicalEmphasisOption;
        logicalEmphasisOption = newValue;
        changeSupport.firePropertyChange("logicalEmphasisOption", oldValue, newValue);
    }
    
    
    public boolean isUppercaseAttrOption() {
        return uppercaseAttrOption;
    }
    
    public void setUppercaseAttrOption(boolean newValue) {
        boolean oldValue = uppercaseAttrOption;
        uppercaseAttrOption = newValue;
        changeSupport.firePropertyChange("uppercaseAttrOption", oldValue, newValue);
    }
    
    public boolean isUppercaseTagsOption() {
        return uppercaseTagsOption;
    }
    
    public void setUppercaseTagsOption(boolean newValue) {
        boolean oldValue = uppercaseTagsOption;
        uppercaseTagsOption = newValue;
        changeSupport.firePropertyChange("uppercaseTagsOption", oldValue, newValue);
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }
       
}
