/*
 * EnvironmentModel.java
 *
 * Created on 2 octobre 2005, 16:25
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

import java.io.IOException;
import java.io.InputStream;

import java.util.Properties;


/**
 * A class holding the information about the application
 * @author Yves Zoundi
 */
public class InformationModel extends Properties {
    private InputStream inputStream;

    /**
     * default constructor
     */
    public InformationModel() {
    }

    /**
     * return the stream of the properties file
     * @return the stream of the properties file
     */
    public InputStream getInputStream() {
        return inputStream;
    }

    /**
     * set the stream of the properties file
     * @param inputStream the stream of the properties file
     */
    public void setInputStream(InputStream inputStream) {
        this.inputStream = inputStream;

        try {
            super.load(inputStream);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }
}