/*
 * ConfigurationModel.java
 *
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 *
 *
 */
package net.sf.xpontus.model;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
    private transient Log log = LogFactory.getLog(ConfigurationModel.class);

    /** Creates a new instance of ConfigurationModel */
    public ConfigurationModel()
    {
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
    public void save()
    {
        OutputStream outputStream = null;

        Writer writer = null;

        try
        {
            XStream xstream = new XStream(new DomDriver());
            outputStream = new FileOutputStream(getFileToSaveTo());
            writer = new OutputStreamWriter(outputStream, "UTF-8");
            xstream.toXML(this, writer);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);
        }
        finally
        {
            if (writer != null)
            {
                IOUtils.closeQuietly(writer);
            }

            if (outputStream != null)
            {
                IOUtils.closeQuietly(outputStream);
            }
        }
    }

    /**
     * load a configuration
     * @return the deserialized object loaded from a file
     */
    public Object load()
    {
        InputStream inputStream = null;
        Reader reader = null;

        try
        {
            XStream xstream = new XStream(new DomDriver());
            inputStream = new FileInputStream(getFileToSaveTo());
            reader = new InputStreamReader(inputStream, "UTF-8");

            return xstream.fromXML(reader);
        }
        catch (Exception ex)
        {
            log.error(ex.getMessage(), ex);

            return null;
        }
        finally
        {
            if (reader != null)
            {
                IOUtils.closeQuietly(reader);
            }

            if (inputStream != null)
            {
                IOUtils.closeQuietly(inputStream);
            }
        }
    }
}
