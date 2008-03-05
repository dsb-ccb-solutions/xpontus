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
package net.sf.xpontus.plugins.browser;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.controllers.impl.XPontusPluginManager;
import net.sf.xpontus.plugins.SimplePluginDescriptor;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import org.java.plugin.registry.PluginDescriptor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import java.io.File;
import java.io.InputStream;
import java.io.OutputStream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.net.URL;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class AvailablePluginsResolver extends AbstractPluginsResolver {
    private Map<String, SimplePluginDescriptor> pluginsMap = new HashMap<String, SimplePluginDescriptor>();
    private List<String> installed = new Vector<String>();
    private PropertyDescriptor[] pd;

    public AvailablePluginsResolver() {
        init();
    }

    public PropertyDescriptor findDescriptor(String property) {
        for (PropertyDescriptor p : pd) {
            if (p.getName().equals(property)) {
                return p;
            }
        }

        return null;
    }

    public synchronized Map<String, SimplePluginDescriptor> getPluginDescriptorsMap() {
        return pluginsMap;
    }

    private void init() {
        try {
            BeanInfo bi = Introspector.getBeanInfo(SimplePluginDescriptor.class);

            pd = bi.getPropertyDescriptors();
        } catch (IntrospectionException ex) {
            Logger.getLogger(AvailablePluginsResolver.class.getName())
                  .log(Level.SEVERE, null, ex);
        }

        Object[] descriptors = XPontusPluginManager.getPluginManager()
                                                   .getRegistry()
                                                   .getPluginDescriptors()
                                                   .toArray();

        for (int i = 0; i < descriptors.length; i++) {
            PluginDescriptor pds = (PluginDescriptor) descriptors[i];
            installed.add(pds.getId());
        }
    }

    private void setProperty(Object bean, PropertyDescriptor p, String value) {
        Class propType = p.getPropertyType();

        PropertyEditor editor = PropertyEditorManager.findEditor(propType);

        // check the editor
        if (editor == null) {
            throw new IllegalArgumentException("Not found: " + propType);
        }

        Method setter = p.getWriteMethod();

        editor.setAsText(value);

        Object result = editor.getValue();

        try {
            setter.invoke(bean, new Object[] { result });
        } catch (IllegalAccessException e) {
            throw new SecurityException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public void resolvePlugins(String url) throws Exception {
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder db = dbf.newDocumentBuilder();
        Document doc = db.parse(new File(url));
        Node root = doc.getFirstChild();

        NodeList plugins = root.getChildNodes();

        final int total = plugins.getLength();

        for (int i = 0; i < total; i++) {
            Node node = plugins.item(i);

            if (node instanceof Element) {
                String id = node.getAttributes().getNamedItem("id")
                                .getNodeValue();

                if (!installed.contains(id)) {
                    SimplePluginDescriptor spd = new SimplePluginDescriptor();

                    setProperty(spd, findDescriptor("id"), id);

                    NodeList pluginInfo = node.getChildNodes();

                    for (int j = 0; j < pluginInfo.getLength(); j++) {
                        Node n = pluginInfo.item(j);

                        if (n instanceof Element) {
                            setProperty(spd, findDescriptor(n.getNodeName()),
                                n.getTextContent());
                        }
                    }

                    if (spd.getAuthor() == null) {
                        spd.setAuthor("Yves Zoundi");
                    }

                    if (spd.getLicense() == null) {
                        spd.setLicense("UNKNOWN");
                    }
                }
            }
        }
    }

    public void resolvePlugins() {
        try {
            File cacheDir = XPontusConfigurationConstantsIF.XPONTUS_CACHE_DIR;
            File pluginsFile = new File(cacheDir, "plugins.xml");

            if (!pluginsFile.exists()) {
                URL pluginURL = new URL(
                        "http://xpontus.sourceforge.net/snapshot/plugins.xml");
                InputStream is = pluginURL.openStream();
                OutputStream os = FileUtils.openOutputStream(pluginsFile);
                IOUtils.copy(is, os);
                IOUtils.closeQuietly(os);
                IOUtils.closeQuietly(is);
            }

            resolvePlugins(pluginsFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void reload() {
    }
}
