/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package net.sf.xpontus.utils;

import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import java.beans.PropertyDescriptor;
import java.beans.PropertyEditor;
import java.beans.PropertyEditorManager;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method; 

/**
 *
 * @author mrcheeks
 */
public class PluginResolverUtils {
    
    private static PropertyDescriptor[] pd;
  
 
 public static PropertyDescriptor findDescriptor(String property) {
        for (PropertyDescriptor p : pd) {
            if (p.getName().equals(property)) {
                return p;
            }
        }

        return null;
    }
 
 private static void setProperty(Object bean, PropertyDescriptor p, String value) {
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
}
