/*
 * UserTemplateModel.java
 *
 * Created on July 23, 2006, 2:14 PM
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
package net.sf.xpontus.model.templates;

import net.sf.xpontus.core.model.ConfigurationModel;

import java.io.File;


/**
 * User template model
 * @author Yves Zoundi
 */
public class UserTemplateModel extends ConfigurationModel
  {
    private int position;
    private String name;
    private String fileURI;

    /**
     * Creates a new instance of UserTemplateModel
     */
    public UserTemplateModel()
      {
      }

    /**
     * The template URI
     * @return The destination file
     */
    public File getFileToSaveTo()
      {
        return null;
      }

    /**
     * The position of the template in the template list
     * @return The position of the template in the template list
     */
    public int getPosition()
      {
        return position;
      }

    /**
     * Set the position of the template in the template list
     * @param newValue The position of the template in the template list
     */
    public void setPosition(int newValue)
      {
        int oldValue = position;
        position = newValue;
        changeSupport.firePropertyChange("position", oldValue, newValue);
      }

    /**
     * The template name
     * @return The template name
     */
    public String getName()
      {
        return name;
      }

    /**
     * The template's name
     * @param newValue The template's name
     */
    public void setName(String newValue)
      {
        String oldValue = name;
        name = newValue;
        changeSupport.firePropertyChange("name", oldValue, newValue);
      }

    /**
     * The template file URI
     * @return The template file URI
     */
    public String getFileURI()
      {
        return fileURI;
      }

    /**
     * Set the template file URI
     * @param newValue the template file URI
     */
    public void setFileURI(String newValue)
      {
        String oldValue = fileURI;
        fileURI = newValue;
        changeSupport.firePropertyChange("fileURI", oldValue, newValue);
      }
  }
