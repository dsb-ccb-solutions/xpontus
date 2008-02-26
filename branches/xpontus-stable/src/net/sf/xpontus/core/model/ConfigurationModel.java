/*
 * ConfigurationModel.java
 *
 * Created on February 24, 2006, 10:41 PM
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
public abstract class ConfigurationModel {
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
