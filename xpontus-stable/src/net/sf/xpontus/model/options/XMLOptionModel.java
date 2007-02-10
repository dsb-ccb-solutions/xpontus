/*
 * XMLOptionModel.java
 *
 * Created on February 25, 2006, 9:19 PM
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
public class XMLOptionModel extends ConfigurationModel {
    private String xsltProcessor = "Saxon 6.5.4";
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
    
    
    public void addPropertyChangeListener(PropertyChangeListener x) {
        changeSupport.addPropertyChangeListener(x);
    }
    
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
     * @param xsltProcessor
     */
    public void setXsltProcessor(String newValue) {
        String oldValue = xsltProcessor;
        xsltProcessor = newValue;
        changeSupport.firePropertyChange("xsltProcessor", oldValue, newValue);
        if(newValue.equals("Saxon 6.5.5")){
            System.setProperty("javax.xml.transform.TransformerFactory",
                    "com.icl.saxon.TransformerFactoryImpl") ;
        } else if(newValue.equals("Saxon-B 8.5.1")){
            System.setProperty("javax.xml.transform.TransformerFactory",
                    "net.sf.saxon.TransformerFactoryImpl") ;
        } else if(newValue.equals("Jd.xslt 1.5.5")){
           System.setProperty("javax.xml.transform.TransformerFactory",
                    "jd.xml.xslt.trax.TransformerFactoryImpl");
        }
        else{
             System.setProperty("javax.xml.transform.TransformerFactory",
                    "org.apache.xalan.processor.TransformerFactoryImpl") ;
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
     * @param xmlEncoding
     */
    public void setXmlEncoding(String newValue) {
        String oldValue = xmlEncoding;
        xmlEncoding = newValue;
        changeSupport.firePropertyChange("xmlEncoding", oldValue, newValue);
    }
    
}
