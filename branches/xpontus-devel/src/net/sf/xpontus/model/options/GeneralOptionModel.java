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

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;

import java.beans.PropertyChangeListener;

import java.io.File;


/**
 *
 * @author Yves Zoundi
 */
public class GeneralOptionModel extends ConfigurationModel
  {
    private boolean showSplashScreen = true;
    private boolean confirmOnExitOption = true;
    private String language = "English";
    private boolean antialias = false;
    private String iconSet = "Eclipse";
    private String theme = "Plastic";

    /** Creates a new instance of GeneralOptionModel */
    public GeneralOptionModel()
      {
        super();
      }

    /**
     *
     * @return
     */
    public File getFileToSaveTo()
      {
        return XPontusConstants.GENERAL_PREF;
      }

    /**
     *
     * @return
     */
    public boolean isShowSplashScreen()
      {
        return showSplashScreen;
      }

    /**
     *
     * @param newValue
     */
    public void setShowSplashScreen(boolean newValue)
      {
        boolean oldValue = showSplashScreen;
        showSplashScreen = newValue;
        changeSupport.firePropertyChange("showSplashScreen", oldValue, newValue);
      }

    /**
     *
     * @return
     */
    public String getIconSet()
      {
        return iconSet;
      }

    /**
     *
     * @param newValue
     */
    public void setIconSet(String newValue)
      {
        String oldValue = iconSet;
        iconSet = newValue;
        changeSupport.firePropertyChange("iconSet", oldValue, newValue);
      }

    /**
     *
     * @return
     */
    public String getTheme()
      {
        return theme;
      }

    /**
     *
     * @param newValue
     */
    public void setTheme(String newValue)
      {
        String oldValue = theme;
        theme = newValue;
        changeSupport.firePropertyChange("theme", oldValue, newValue);
      }

    /**
     *
     * @return
     */
    public boolean isConfirmOnExitOption()
      {
        return confirmOnExitOption;
      }

    /**
     *
     * @param newValue
     */
    public void setConfirmOnExitOption(boolean newValue)
      {
        boolean oldValue = confirmOnExitOption;
        confirmOnExitOption = newValue;
        changeSupport.firePropertyChange("confirmOnExitOption", oldValue,
            newValue);
      }

    /**
     *
     * @return
     */
    public boolean isAntialias()
      {
        return antialias;
      }

    /**
     *
     * @param newValue
     */
    public void setAntialias(boolean newValue)
      {
        boolean oldValue = antialias;
        antialias = newValue;
        changeSupport.firePropertyChange("antialias", oldValue, newValue);
        System.setProperty("swing.aatext", "true");
      }

    /**
     *
     * @return
     */
    public String getLanguage()
      {
        return language;
      }

    /**
     *
     * @param newValue
     */
    public void setLanguage(String newValue)
      {
        String oldValue = language;
        language = newValue;
        changeSupport.firePropertyChange("language", oldValue, newValue);
      }
  }
