/*
 * DefaultSettingsModuleImpl.java
 *
 * Created on May 26, 2007, 8:13 AM
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
 */
package net.sf.xpontus.plugins.settings;

import edu.ucla.loni.ccb.vfsbrowser.VFSBrowser;
import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.model.ConfigurationModel;
import net.sf.xpontus.plugins.scenarios.ScenarioListModel;
import net.sf.xpontus.properties.PropertiesHolder;

//import com.db4o.Db4o;
//import com.db4o.ObjectContainer;
//import com.db4o.ObjectSet;
//
//import com.db4o.config.Configuration;
import java.io.File;

import java.util.HashMap;
import java.util.Map;


/**
 *
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DefaultSettingsModuleImpl implements SettingsModuleIF {
    public static final String ROLE = DefaultSettingsModuleImpl.class.getName() +
        ".ROLE";
    private static DefaultSettingsModuleImpl INSTANCE;

    //    private ObjectContainer container;
    private String dbFile;

    /** Creates a new instance of DefaultSettingsModuleImpl */
    private DefaultSettingsModuleImpl() {
        dbFile = XPontusConstantsIF.XPONTUS_DATABASE_FILE.getAbsolutePath();
        start();
    }

    /**
     *
     * @return
     */
    public static DefaultSettingsModuleImpl getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DefaultSettingsModuleImpl();
        }

        return INSTANCE;
    }

    public void init() {
        
        
        File[] configsDirectories = {
                XPontusConstantsIF.XPONTUS_PLUGINS_DATA_DIR,
                XPontusConstantsIF.XPONTUS_PREFERENCES_DIR,
                XPontusConstantsIF.XPONTUS_DATABASE_CONFIG_DIR,
                XPontusConstantsIF.XPONTUS_PLUGINS_DIR,
                XPontusConstantsIF.XPONTUS_CACHE_DIR
            };

        for (int i = 0; i < configsDirectories.length; i++) {
            if (!configsDirectories[i].exists()) {
                configsDirectories[i].mkdirs();
            }
        }

        Map map = new HashMap();
        map.put(ROLE, this);
        PropertiesHolder.registerProperty(XPontusSettings.KEY, map);
    }

    public void start() {
        ScenarioListModel slm = new ScenarioListModel();

        if (!slm.getFileToSaveTo().exists()) {
            slm.save();
        }
    }

    public void shutdown() {
    }

    /**
     *
     * @param bean
     */
    public void save(ConfigurationModel bean) {
        bean.save();
    }

    /**
     *
     * @param beanClass
     * @return
     */
    public Object load(ConfigurationModel bean) {
        return bean.load();
    }
}
