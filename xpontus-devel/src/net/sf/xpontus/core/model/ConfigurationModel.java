/*
 * ConfigurationModel.java
 *
 * Created on February 24, 2006, 10:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.core.model;

import com.jgoodies.binding.beans.ExtendedPropertyChangeSupport;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import org.apache.commons.io.IOUtils;

import java.beans.PropertyChangeListener;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
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
public abstract class ConfigurationModel
  {
    protected ExtendedPropertyChangeSupport changeSupport;

    /** Creates a new instance of ConfigurationModel */
    public ConfigurationModel()
      {
        changeSupport = new ExtendedPropertyChangeSupport(this);
      }

    /**
     *
     * @param x
     */
    public void addPropertyChangeListener(PropertyChangeListener x)
      {
        changeSupport.addPropertyChangeListener(x);
      }

    /**
     *
     * @param x
     */
    public void removePropertyChangeListener(PropertyChangeListener x)
      {
        changeSupport.removePropertyChangeListener(x);
      }

    /**
     *
     * @return the file to store the configuration properties
     */
    public abstract File getFileToSaveTo();

    /**
     */
    public void save()
      {
        try
          {
            XStream xstream = new XStream(new DomDriver());
            OutputStream fos = new FileOutputStream(getFileToSaveTo());
            BufferedOutputStream bos = new BufferedOutputStream(fos);
            Writer writer = new OutputStreamWriter(bos, "UTF-8");
            Writer bw = new BufferedWriter(writer);
            xstream.toXML(this, bw);
            IOUtils.closeQuietly(bw);
            IOUtils.closeQuietly(writer);
            IOUtils.closeQuietly(bos);
            IOUtils.closeQuietly(fos);
          }
        catch (Exception ex)
          {
            ex.printStackTrace();
          }
      }

    /**
     *
     *
     * @return
     */
    public Object load()
      {
        try
          {
            XStream xstream = new XStream(new DomDriver());
            InputStream is = new FileInputStream(getFileToSaveTo());
            InputStream bis = new BufferedInputStream(is);
            Reader reader = new InputStreamReader(bis);
            Reader br = new BufferedReader(reader);
            Object obj = xstream.fromXML(br);
            IOUtils.closeQuietly(br);
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(is);

            return obj;
          }
        catch (Exception ex)
          {
            ex.printStackTrace();

            return null;
          }
      }
  }
