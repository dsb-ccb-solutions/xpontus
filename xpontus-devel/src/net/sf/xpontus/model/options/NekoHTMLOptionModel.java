/*
 * NekoHTMLOptionModel.java
 *
 * Created on July 12, 2006, 5:16 PM
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
public class NekoHTMLOptionModel extends ConfigurationModel
  {
    private String lower_attr = "lower";
    private String lower_elements = "lower";
    private boolean balance_tags = true;
    private boolean override_doctype = false;
    private String doctype = "transitional";

    /** Creates a new instance of NekoHTMLOptionModel */
    public NekoHTMLOptionModel()
      {
        super();
      }

    /**
     *
     * @return
     */
    public File getFileToSaveTo()
      {
        return XPontusConstants.JTIDY_PREF;
      }

    public String getLower_attr()
      {
        return lower_attr;
      }

    public void setLower_attr(String newValue)
      {
        String oldValue = lower_attr;
        lower_attr = newValue;
        changeSupport.firePropertyChange("lower_attr", oldValue, newValue);
      }

    public String getLower_elements()
      {
        return lower_elements;
      }

    public void setLower_elements(String newValue)
      {
        String oldValue = lower_elements;
        lower_elements = newValue;
        changeSupport.firePropertyChange("lower_elements", oldValue, newValue);
      }

    public boolean isBalance_tags()
      {
        return balance_tags;
      }

    public void setBalance_tags(boolean newValue)
      {
        boolean oldValue = balance_tags;
        balance_tags = newValue;
        changeSupport.firePropertyChange("balance_tags", oldValue, newValue);
      }

    public boolean isOverride_doctype()
      {
        return override_doctype;
      }

    public void setOverride_doctype(boolean newValue)
      {
        boolean oldValue = override_doctype;
        override_doctype = newValue;
        changeSupport.firePropertyChange("override_doctype", oldValue, newValue);
      }

    public String getDoctype()
      {
        return doctype;
      }

    public void setDoctype(String newValue)
      {
        String oldValue = doctype;
        doctype = newValue;
        changeSupport.firePropertyChange("doctype", oldValue, newValue);
      }
  }
