/*
 * PluginResolverUtils.java
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

import net.sf.xpontus.plugins.SimplePluginDescriptor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import java.beans.PropertyDescriptor;

import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;


/**
 *
 * @author Yves Zoundi
 * @version 0.0.1
 */
public class PluginResolverUtils
{
    private static PropertyDescriptor[] pd;

    public static SimplePluginDescriptor resolvePlugins(InputStream is)
    {
        SimplePluginDescriptor spd = new SimplePluginDescriptor();

        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setEntityResolver(NullEntityResolver.getInstance());

            Document doc = db.parse(is);

            Element root = doc.getDocumentElement();

            String version = root.getAttribute("version");

            if (version != null)
            {
                spd.setVersion(version);
            }

            String id = root.getAttribute("id");
            spd.setId(id);

            String vendor = root.getAttribute("vendor");
            spd.setVersion((vendor != null) ? vendor : "Unknown");

            Element attributesNode = (Element) root.getElementsByTagName(
                    "attributes").item(0);

            NodeList attributesList = attributesNode.getElementsByTagName(
                    "attribute");

            Element requiresNode = (Element) root.getElementsByTagName(
                    "requires").item(0);

            NodeList depsList = requiresNode.getElementsByTagName("import");

            StringBuilder depBuilder = new StringBuilder();

            for (int i = 0; i < depsList.getLength(); i++)
            {
                Element depNode = (Element) depsList.item(i);
                String depValue = depNode.getAttribute("plugin-id");
                depBuilder.append(depValue + "<br/>");
            }

            spd.setDependencies(depBuilder.toString());

            for (int i = 0; i < attributesList.getLength(); i++)
            {
                Element attributeNode = (Element) attributesList.item(i);
                String attributeId = attributeNode.getAttribute("id");
                String attributeValue = attributeNode.getAttribute("value");

                if (attributeId.equals("Built-in"))
                {
                    spd.setBuiltin(attributeValue);
                }
                else if (attributeId.equals("Category"))
                {
                    spd.setCategory(attributeValue);
                }
                else if (attributeId.equals("Homepage"))
                {
                    spd.setHomepage(attributeValue);
                }
                else if (attributeId.equals("Description"))
                {
                    spd.setDescription(attributeValue);
                }
                else if (attributeId.equals("DisplayName"))
                {
                    spd.setDisplayname(attributeValue);
                }
                else if (attributeId.equals("License"))
                {
                    spd.setLicense(attributeValue);
                }
                else if (attributeId.equals("date"))
                {
                    spd.setDate(attributeValue);
                }
            }
        }
        catch (Exception err)
        {
        }

        if (spd.getLicense() == null)
        {
            spd.setLicense("UNKNOWN");
        }

        return spd;
    }

    public static PropertyDescriptor findDescriptor(String property)
    {
        for (PropertyDescriptor p : pd)
        {
            if (p.getName().equals(property))
            {
                return p;
            }
        }

        return null;
    }
}
