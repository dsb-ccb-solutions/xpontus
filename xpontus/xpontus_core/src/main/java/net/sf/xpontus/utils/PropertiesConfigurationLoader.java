/*
 *
 *
 * Copyright (C) 2005-2008 Yves Zoundi
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
package net.sf.xpontus.utils;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.Iterator;
import java.util.Properties;


/**
 * <p>
 *  Utility class to load some properties
 *  It will be usefull to read and save some configurations
 * </p>
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PropertiesConfigurationLoader {
    private static Log log = LogFactory.getLog(PropertiesConfigurationLoader.class);

    private PropertiesConfigurationLoader() {
    }

    public static void save(File propertiesFile, Properties props) {
        try {
            OutputStream bos = FileUtils.openOutputStream(propertiesFile);
            props.store(bos, null);
            IOUtils.closeQuietly(bos);
        } catch (Exception e) {
            log.error(e.getMessage());
        }
    }

    public static Properties load(File propertiesFile) {
        Properties properties = new Properties();

        try {
            InputStream is = FileUtils.openInputStream(propertiesFile);
            properties.load(is);
            IOUtils.closeQuietly(is);
        } catch (IOException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return properties;
    }
}
