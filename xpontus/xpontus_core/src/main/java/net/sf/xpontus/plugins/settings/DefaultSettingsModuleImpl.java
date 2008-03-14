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

import com.vlsolutions.swing.docking.DefaultDockableContainerFactory;
import com.vlsolutions.swing.docking.DockableContainerFactory;
import com.vlsolutions.swing.docking.TabbedDockableContainer;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.model.ConfigurationModel;
import net.sf.xpontus.plugins.scenarios.ScenarioListModel;
import net.sf.xpontus.properties.PropertiesHolder;
import net.sf.xpontus.utils.FileHistoryList;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;

import java.awt.Font;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.swing.UIManager;
import net.sf.xpontus.utils.GUIUtils;


/**
 *
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DefaultSettingsModuleImpl implements SettingsModuleIF {
    public static final String ROLE = DefaultSettingsModuleImpl.class.getName() +
        ".ROLE";
    private static DefaultSettingsModuleImpl INSTANCE;
    public static File XPONTUS_SYSTEM_PLUGIN_DIR;

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
        Properties m_props = PropertiesConfigurationLoader.load(XPontusConfigurationConstantsIF.EDITOR_PREFERENCES_FILE);
        
         
        
        String[] f = m_props.get("EditorPane.Font").toString().split(",");
        String family = f[0].trim();
        String style1 = f[1].trim();
        int style = Integer.parseInt(style1);
        int size = Integer.parseInt(f[2].trim());
        Font m_font = new Font(family, style, size);
        

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

        String[] locations = {
                "/net/sf/xpontus/configuration/editorPanel.properties",
                "/net/sf/xpontus/configuration/general.properties",
                "/net/sf/xpontus/configuration/mimetypes.properties"
            };

        try {
            for (String loc : locations) {
                String outName = FilenameUtils.getName(loc);
                File output = new File(XPontusConstantsIF.XPONTUS_PREFERENCES_DIR,
                        outName);

                if (!output.exists()) {
                    InputStream is = getClass().getResourceAsStream(loc);

                    OutputStream out = new FileOutputStream(output);
                    IOUtils.copy(is, out);
                }

                if (!outName.equals("mimetypes.properties")) {
                    Properties m_properties = PropertiesConfigurationLoader.load(output);
                    Iterator it = m_properties.keySet().iterator();

                    while (it.hasNext()) {
                        Object m_key = it.next();
                        Object m_value = m_properties.get(m_key);
                        System.out.println("cle:" + m_key + ",value:" +
                            m_value);
                         
                            XPontusConfig.put(m_key, m_value); 
                        
                    }
                }
            }
        } catch (Exception err) {
            err.printStackTrace();
        }

        XPontusConfig.put("EditorPane.Font", m_font);
        System.out.println("Default font:" + GUIUtils.fontToString((Font)XPontusConfig.getValue("EditorPane.Font")));
         
        Map map = new HashMap();
        map.put(ROLE, this);
        PropertiesHolder.registerProperty(XPontusSettings.KEY, map);

        DockableContainerFactory.setFactory(new XPontusDockableContainerFactory());

        FileHistoryList.init();

        initDefaultSettings();
    }

    public void start() {
        ScenarioListModel slm = new ScenarioListModel();

        if (!slm.getFileToSaveTo().exists()) {
            slm.save();
        }
    }

    public void shutdown() {
        FileHistoryList.save();
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

    private void initDefaultSettings() {
        Properties props = null;

        props = PropertiesConfigurationLoader.load(XPontusConstantsIF.GENERAL_PREFERENCES_FILE);

        Iterator it = props.keySet().iterator();

        while (it.hasNext()) {
            Object key = it.next();
            Object value = props.get(key);
            UIManager.put(key, value);
        }
    }

    public class XPontusDockableContainerFactory
        extends DefaultDockableContainerFactory {
        public TabbedDockableContainer createTabbedDockableContainer() {
            return new DockTabbedPane2();
        }
    }
}
