/*
 * DocumentationToolModel.java
 *
 * Created on July 16, 2006, 5:19 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.model;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import net.sf.xpontus.core.model.ConfigurationModel;

import java.beans.PropertyChangeListener;

import java.io.File;


/**
 *
 * @author Yves Zoundi
 */
public class DocumentationToolModel extends ConfigurationModel
  {
    private String inputURI;
    private String outputURI;
    private String docTitle = "Documentation";
    private String inputType = "XSL";
    private boolean useCurrentDocument = true;

    /** Creates a new instance of DocumentationToolModel */
    public DocumentationToolModel()
      {
        super();
      }

    public File getFileToSaveTo()
      {
        return null;
      }

    public String getInputURI()
      {
        return inputURI;
      }

    public void setInputURI(String newValue)
      {
        String oldValue = inputURI;
        inputURI = newValue;
        changeSupport.firePropertyChange("inputURI", oldValue, newValue);
      }

    public String getOutputURI()
      {
        return outputURI;
      }

    public void setOutputURI(String newValue)
      {
        String oldValue = outputURI;
        outputURI = newValue;
        changeSupport.firePropertyChange("outputURI", oldValue, newValue);
      }

    public String getDocTitle()
      {
        return docTitle;
      }

    public void setDocTitle(String newValue)
      {
        String oldValue = docTitle;
        docTitle = newValue;
        changeSupport.firePropertyChange("docTitle", oldValue, newValue);
      }

    public String getInputType()
      {
        return inputType;
      }

    public void setInputType(String newValue)
      {
        String oldValue = inputType;
        inputType = newValue;
        changeSupport.firePropertyChange("inputType", oldValue, newValue);
      }

    public boolean isUseCurrentDocument()
      {
        return useCurrentDocument;
      }

    public void setUseCurrentDocument(boolean newValue)
      {
        boolean oldValue = useCurrentDocument;
        useCurrentDocument = newValue;
        changeSupport.firePropertyChange("useCurrentDocument", oldValue,
            newValue);
      }
  }
