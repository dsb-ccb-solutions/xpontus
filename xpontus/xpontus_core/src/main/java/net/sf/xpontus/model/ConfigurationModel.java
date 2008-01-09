/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.model;

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
 * @author Propriétaire
 */
public abstract class ConfigurationModel {
    /** Creates a new instance of ConfigurationModel */
    public ConfigurationModel() {
    } 

    /**
     *
     * @return the file to store the configuration properties
     */
    public abstract File getFileToSaveTo();

    /**
     *
     *  save a configuration
     *
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
     * load a configuration
     * @return the deserialized object loaded from a file
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
