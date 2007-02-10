/*
 * ConfigurationModel.java
 *
 * Created on February 24, 2006, 10:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.core.model;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;


/**
 *
 * @author Yves Zoundi
 */
public abstract class ConfigurationModel{
    /** Creates a new instance of ConfigurationModel */
    public ConfigurationModel() {
    }

    
    /**
     *
     * @return the editor mapping url
     */
    public abstract String getMappingURL();

    /**
     *
     * @return the file to store the configuration properties
     */
    public abstract File getFileToSaveTo();

    /**
     *
     * @param mappingURL
     * @param location
     */
    public void save() {
        try {
            XStream xstream = new XStream(new DomDriver());
            OutputStream fos = new FileOutputStream(getFileToSaveTo());
            Writer writer = new OutputStreamWriter(fos, "UTF-8");
            xstream.toXML(this, writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     * @param mappingURL
     * @param location
     * @return
     */
    public Object load() {
        try {
            XStream xstream = new XStream(new DomDriver());
            InputStream is = new FileInputStream(getFileToSaveTo());
            Reader reader = new InputStreamReader(is);
            return xstream.fromXML(reader);
        } catch (Exception ex) {
            ex.printStackTrace();

            return null;
        }
    }
}
