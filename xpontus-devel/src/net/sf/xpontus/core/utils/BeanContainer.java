/*
 * BeanContainer.java
 *
 * Created on 2 décembre 2006, 10:48
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.core.utils;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.io.IOUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;

import java.io.BufferedInputStream;
import java.io.InputStream;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import java.util.Hashtable;
import java.util.Iterator;


/**
 *
 * @author mrcheeks
 */
public class BeanContainer
  {
    private static BeanContainer container;
    private String propertiesFile;
    private ExtendedProperties properties;
    private Hashtable beanList;
    private final String BEAN_CLASS_PREFIX = ".(class)";

    /** Creates a new instance of BeanContainer */
    private BeanContainer()
      {
        beanList = new Hashtable();
        properties = new ExtendedProperties();
      }

    public Object getBean(String alias)
      {
        return beanList.get(alias);
      }

    public static BeanContainer getInstance()
      {
        if (container == null)
          {
            container = new BeanContainer();
          }

        return container;
      }

    public void load(String location)
      {
        setup(location);
      }

    public void setup(String location)
      {
        try
          {
            InputStream is = getClass()
                                 .getResourceAsStream(location);
            InputStream bis = new BufferedInputStream(is);

            properties.load(bis);

            IOUtils.closeQuietly(bis);
            IOUtils.closeQuietly(is);

            Iterator it = properties.getKeys();

            while (it.hasNext())
              {
                String property_key = it.next().toString();

                String[] keyTable = property_key.split("\\.");

                if (property_key.endsWith(BEAN_CLASS_PREFIX))
                  {
                    String beanClass = properties.getString(property_key);

                    Object bean = Class.forName(beanClass).newInstance();

                    StringBuffer actionCommand = new StringBuffer();
                    actionCommand.append(keyTable[0] + "." + keyTable[1]);

                    beanList.put(actionCommand.toString(), bean);

                    BeanInfo bi = Introspector.getBeanInfo(bean.getClass());

                    PropertyDescriptor[] pd = bi.getPropertyDescriptors();

                    final int len = pd.length;

                    for (int i = 0; i < len; i++)
                      {
                        PropertyDescriptor property = pd[i];

                        String name = property.getName();

                        if (name.endsWith("class"))
                          {
                            continue;
                          }

                        StringBuffer propToFind = new StringBuffer();
                        propToFind.append(keyTable[0]);
                        propToFind.append(".");
                        propToFind.append(keyTable[1]);
                        propToFind.append(".");
                        propToFind.append(name);

                        String propertyLookup = propToFind.toString();

                        String value = properties.getString(propertyLookup);

                        if (value != null)
                          {
                            setProperty(bean, property, value);
                          }
                      }
                  }
              }
          }
        catch (Exception ex)
          {
            ex.printStackTrace();
            System.exit(1);
          }
      }

    private void setProperty(Object bean, PropertyDescriptor p, String value)
      {
        Class propType = p.getPropertyType();

        PropertyEditor editor = PropertyEditorManager.findEditor(propType);

        // check the editor
        if (editor == null)
          {
            throw new IllegalArgumentException("Not found: " + propType);
          }

        Method setter = p.getWriteMethod();

        editor.setAsText(value);

        Object result = editor.getValue();

        try
          {
            setter.invoke(bean, new Object[] { result });
          }
        catch (IllegalAccessException e)
          {
            throw new SecurityException(e);
          }
        catch (InvocationTargetException e)
          {
            throw new RuntimeException(e);
          }
      }

    public String getPropertiesFile()
      {
        return propertiesFile;
      }

    public void setPropertiesFile(String propertiesFile)
      {
        this.propertiesFile = propertiesFile;
      }
  }
