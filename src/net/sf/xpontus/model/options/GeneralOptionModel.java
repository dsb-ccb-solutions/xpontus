/*
 * GeneralOptionModel.java
 *
 * Created on February 24, 2006, 10:56 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.model.options;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;
import java.beans.PropertyChangeListener;
import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;

import java.io.File;


/**
 *
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
    
    public boolean isShowSplashScreen() {
        return showSplashScreen;
    }
    
    public void setShowSplashScreen(boolean newValue) {
        boolean oldValue = showSplashScreen;
        showSplashScreen = newValue;
        changeSupport.firePropertyChange("showSplashScreen", oldValue, newValue);
    }
    
    
    public String getIconSet() {
        return iconSet;
    }
    
    public void setIconSet(String newValue) {
        String oldValue = iconSet;
        iconSet = newValue;
        changeSupport.firePropertyChange("iconSet", oldValue, newValue);
    }
    
    public String getTheme() {
        return theme;
    }
    
    public void setTheme(String newValue) {
        String oldValue = theme;
        theme = newValue;
        changeSupport.firePropertyChange("theme", oldValue, newValue);
    }
    
    public boolean isConfirmOnExitOption() {
        return confirmOnExitOption;
    }
    
    public void setConfirmOnExitOption(boolean newValue) {
        boolean oldValue = confirmOnExitOption;
        confirmOnExitOption = newValue;
        changeSupport.firePropertyChange("confirmOnExitOption", oldValue, newValue);
    }
    
    
    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }
    
    public void removePropertyChangeListener(PropertyChangeListener x) {
        changeSupport.removePropertyChangeListener(x);
    }
}
