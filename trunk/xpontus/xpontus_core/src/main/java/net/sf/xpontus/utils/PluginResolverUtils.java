/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.utils;

import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.xpontus.plugins.SimplePluginDescriptor;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 *
 * @author mrcheeks
 */
public class PluginResolverUtils {

    private static PropertyDescriptor[] pd;

    public static SimplePluginDescriptor resolvePlugins(InputStream is) {
        SimplePluginDescriptor spd = new SimplePluginDescriptor();

        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            dbf.setValidating(false);

            DocumentBuilder db = dbf.newDocumentBuilder();
            db.setEntityResolver(NullEntityResolver.getInstance());

            Document doc = db.parse(is);

            Element root = doc.getDocumentElement();

            String version = root.getAttribute("version");

            if (version != null) {
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

            NodeList depsList = requiresNode.getElementsByTagName(
                    "import");

            StringBuilder depBuilder = new StringBuilder();

            for (int i = 0; i < depsList.getLength(); i++) {
                Element depNode = (Element) depsList.item(i);
                String depValue = depNode.getAttribute("plugin-id");
                depBuilder.append(depValue + "<br/>");
            }

            spd.setDependencies(depBuilder.toString());

            for (int i = 0; i < attributesList.getLength(); i++) {
                Element attributeNode = (Element) attributesList.item(i);
                String attributeId = attributeNode.getAttribute("id");
                String attributeValue = attributeNode.getAttribute("value");

                if (attributeId.equals("Built-in")) {
                    spd.setBuiltin(attributeValue);
                } else if (attributeId.equals("Category")) {
                    spd.setCategory(attributeValue);
                } else if (attributeId.equals("Homepage")) {
                    spd.setHomepage(attributeValue);
                } else if (attributeId.equals("Description")) {
                    spd.setDescription(attributeValue);
                } else if (attributeId.equals("DisplayName")) {
                    spd.setDisplayname(attributeValue);
                } else if (attributeId.equals("License")) {
                    spd.setLicense(attributeValue);
                } else if (attributeId.equals("date")) {
                    spd.setDate(attributeValue);
                }
            }


        } catch (Exception err) {
        }

        if (spd.getLicense() == null) {
            spd.setLicense("UNKNOWN");
        }

        return spd;
    }

    public static PropertyDescriptor findDescriptor(String property) {
        for (PropertyDescriptor p : pd) {
            if (p.getName().equals(property)) {
                return p;
            }
        }

        return null;
    }

    private static void setProperty(Object bean, PropertyDescriptor p,
            String value) {
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
            setter.invoke(bean, new Object[]{result});
        } catch (IllegalAccessException e) {
            throw new SecurityException(e);
        } catch (InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
