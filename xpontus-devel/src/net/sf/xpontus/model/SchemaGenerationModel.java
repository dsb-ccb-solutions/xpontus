/*
 * SchemaGenerationModel.java
 *
 * Created on July 28, 2006, 6:10 AM
 *
 *
 *  Copyright (C) 2005 Yves Zoundi
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
package net.sf.xpontus.model;

import net.sf.xpontus.core.model.ConfigurationModel;

import java.io.File;

import java.util.Observable;


/**
 *
 * @author Yves Zoundi
 */
public class SchemaGenerationModel extends ConfigurationModel
{
    private String inputURI = "";
    private String outputURI = "";
    private String inputType = "XML";
    private String outputType = "DTD";
    private boolean openInEditor = true;
    private boolean useExternalDocument = false;

    /** Creates a new instance of SchemaGenerationModel */
    public SchemaGenerationModel()
    {
        super();
    }

    /**
     *
     * @return
     */
    public File getFileToSaveTo()
    {
        return null;
    }

    /**
     * The input path
     * @return The input path
     */
    public String getInputURI()
    {
        return inputURI;
    }

    /**
     * Set the input path
     * @param newValue The input path
     */
    public void setInputURI(String newValue)
    {
        String oldValue = inputURI;
        inputURI = newValue;
        changeSupport.firePropertyChange("inputURI", oldValue, newValue);
    }

    /**
     * Return the output path
     * @return The output path
     */
    public String getOutputURI()
    {
        return outputURI;
    }

    /**
     * Set the output path
     * @param newValue the output path
     */
    public void setOutputURI(String newValue)
    {
        String oldValue = outputURI;
        outputURI = newValue;
        changeSupport.firePropertyChange("outputURI", oldValue, newValue);
    }

    /**
     * The input type(xml, xsd, rng,etc.)
     * @return The input type
     */
    public String getInputType()
    {
        return inputType;
    }

    /**
     * Set the input type(xml, xsd, rng,etc.)
     * @param inputType the input type(xml, xsd, rng,etc.)
     */
    public void setInputType(String newValue)
    {
        String oldValue = inputType;
        inputType = newValue;
        changeSupport.firePropertyChange("inputType", oldValue, newValue);
    }

    /**
     * Set the output type
     * @return The output type
     */
    public String getOutputType()
    {
        return outputType;
    }

    /**
     * Return the output type
     * @param outputType The output type
     */
    public void setOutputType(String newValue)
    {
        String oldValue = outputType;
        outputType = newValue;
        changeSupport.firePropertyChange("outputType", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public boolean isOpenInEditor()
    {
        return openInEditor;
    }

    /**
     *
     * @param newValue
     */
    public void setOpenInEditor(boolean newValue)
    {
        boolean oldValue = openInEditor;
        openInEditor = newValue;
        changeSupport.firePropertyChange("openInEditor", oldValue, newValue);
    }

    /**
     *
     * @return
     */
    public boolean isUseExternalDocument()
    {
        return useExternalDocument;
    }

    /**
     *
     * @param newValue
     */
    public void setUseExternalDocument(boolean newValue)
    {
        boolean oldValue = useExternalDocument;
        useExternalDocument = newValue;
        changeSupport.firePropertyChange("useExternalDocument", oldValue,
            newValue);
    }
}
