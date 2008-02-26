/*
 * XMLOptionModel.java
 *
 * Created on February 25, 2006, 9:19 PM
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
 * the xml's option preferences
 * @author Yves Zoundi
 */
public class XMLOptionModel extends ConfigurationModel {
    private String xsltProcessor = "Saxon 6.5.5";
    private String xmlEncoding = "UTF-8";
    private ExtendedPropertyChangeSupport changeSupport;

    /** Creates a new instance of XMLOptionModel */
    public XMLOptionModel() {
        changeSupport = new ExtendedPropertyChangeSupport(this);
    }

    public String getMappingURL() {
        return "/net/sf/xpontus/model/mappings/XMLModel.xml";
    }

    public File getFileToSaveTo() {
        return XPontusConstants.XML_PREF;
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
    public String getXsltProcessor() {
        return xsltProcessor;
    }

    /**
     *
     * @param newValue
     *
     */
    public void setXsltProcessor(String newValue) {
        String oldValue = xsltProcessor;
        xsltProcessor = newValue;
        changeSupport.firePropertyChange("xsltProcessor", oldValue, newValue);

        if (newValue.equals("Saxon 6.5.5")) {
            System.setProperty("javax.xml.transform.TransformerFactory",
                "com.icl.saxon.TransformerFactoryImpl");
        } else if (newValue.equals("Saxon-B 8.5.1")) {
            System.setProperty("javax.xml.transform.TransformerFactory",
                "net.sf.saxon.TransformerFactoryImpl");
        } else if (newValue.equals("Jd.xslt 1.5.5")) {
            System.setProperty("javax.xml.transform.TransformerFactory",
                "jd.xml.xslt.trax.TransformerFactoryImpl");
        } else {
            System.setProperty("javax.xml.transform.TransformerFactory",
                "org.apache.xalan.processor.TransformerFactoryImpl");
        }
    }

    /**
     *
     * @return
     */
    public String getXmlEncoding() {
        return xmlEncoding;
    }

    /**
     *
     *
     * @param newValue
     */
    public void setXmlEncoding(String newValue) {
        String oldValue = xmlEncoding;
        xmlEncoding = newValue;
        changeSupport.firePropertyChange("xmlEncoding", oldValue, newValue);
    }
}
