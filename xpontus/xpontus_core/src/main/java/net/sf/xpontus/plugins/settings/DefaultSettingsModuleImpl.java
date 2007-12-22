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

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;

import com.db4o.config.Configuration;

import net.sf.xpontus.constants.XPontusConstantsIF;


/**
 *
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DefaultSettingsModuleImpl implements SettingsModuleIF {
    private static DefaultSettingsModuleImpl INSTANCE;
    private ObjectContainer container;
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
        XPontusConstantsIF.XPONTUS_PLUGINS_DATA_DIR.mkdirs();
        XPontusConstantsIF.XPONTUS_PREFERENCES_DIR.mkdirs();
        XPontusConstantsIF.XPONTUS_DATABASE_CONFIG_DIR.mkdirs();
        XPontusConstantsIF.XPONTUS_PLUGINS_DIR.mkdirs();
    }

    public void start() {
        Configuration config = Db4o.newConfiguration();
        config.lockDatabaseFile(false);
        config.unicode(true);

        container = Db4o.openFile(config, dbFile);
    }

    public void shutdown() {
        container.close();
    }

    /**
     *
     * @param bean
     */
    public void save(Object bean) {
        container.set(bean);
        container.commit();
    }
    
    public void remove(Object bean){
        container.delete(bean);
        container.commit();
    }

    public ObjectSet getObjectList(Class beanClass) {
        ObjectSet o = container.get(beanClass);

        return o;
    }

    /**
     *
     * @param beanClass
     * @return
     */
    public Object getSingleObject(Class beanClass) {
        ObjectSet o = container.get(beanClass);

        if (o != null) {
            return o.next();
        } else {
            return null;
        }
    }
}
