/*
 * ConfigurationHandler.java
 *
 * Created on 1 aout 2005, 17:46
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
package net.sf.xpontus.controller.handlers;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;
import net.sf.xpontus.model.ScenarioListModel;
import net.sf.xpontus.model.options.EditorOptionModel;
import net.sf.xpontus.model.options.GeneralOptionModel;
import net.sf.xpontus.model.options.JTidyOptionModel;
import net.sf.xpontus.model.options.TokenColorsOptionModel;
import net.sf.xpontus.model.options.XMLOptionModel;


/**
 * A class which checks for xpontus settings and initialize them
 * @author Yves Zoundi
 */
public class ConfigurationHandler {
    private final String TFACTORY = "javax.xml.parsers.TransformerFactory";
    private final String SAXON_IMPL = "com.icl.saxon.TransformerFactoryImpl";

    /**
     * Creates a new instance of ConfigurationHandler
     */
    public ConfigurationHandler() {
    }

    /**
     * init the configuration
     */
    public void init() {
        System.setProperty(TFACTORY, SAXON_IMPL);

        java.io.File file = null;
        ConfigurationModel model = null;

        if (!XPontusConstants.PREF_DIR.exists()) {
            XPontusConstants.PREF_DIR.mkdirs();
        }

        // save general preferences defaults
        file = XPontusConstants.GENERAL_PREF;

        if (!file.exists()) {
            model = new GeneralOptionModel();
            model.save();
        }

        // save editor preferences defaults
        file = XPontusConstants.EDITOR_PREF;

        if (!file.exists()) {
            model = new EditorOptionModel();
            model.save();
        }

        // save jtidy default properties
        file = XPontusConstants.JTIDY_PREF;

        if (!file.exists()) {
            model = new JTidyOptionModel();
            model.save();
        }

        // save jtidy default properties
        file = XPontusConstants.COLOR_PREF;

        if (!file.exists()) {
            model = new TokenColorsOptionModel();
            model.save();
        }

        // save jtidy default properties
        file = XPontusConstants.XML_PREF;

        if (!file.exists()) {
            model = new XMLOptionModel();
            model.save();
        }

        // save an empty scenario list if necessary
        file = XPontusConstants.SCENARIO_FILE;

        if (!file.exists()) {
            model = new ScenarioListModel();
            model.save();
        }
    }
}
