/*
 * UserTemplateListModel.java
 *
 * Created on July 26, 2006, 5:55 PM
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
package net.sf.xpontus.model;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;

import java.io.File;

import java.util.Vector;


/**
 *
 * @author Yves Zoundi
 */
public class UserTemplateListModel extends ConfigurationModel
  {
    private Vector templateList;

    /** Creates a new instance of ScenarioListModel */
    public UserTemplateListModel()
      {
        templateList = new Vector();
      }

    public File getFileToSaveTo()
      {
        return XPontusConstants.SCENARIO_FILE;
      }

    public Vector getTemplateList()
      {
        return templateList;
      }

    public void setTemplateList(Vector templateList)
      {
        this.templateList = templateList;
      }
  }
