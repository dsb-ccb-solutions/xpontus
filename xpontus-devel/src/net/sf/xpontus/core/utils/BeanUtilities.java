/*
 * BeanUtilities.java
 *
 * Created on  décembre , :
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.core.utils;

import java.beans.PropertyDescriptor;

import java.lang.reflect.Method;

import java.util.Arrays;
import java.util.List;


/**
 *
 * @author mrcheeks
 */
public class BeanUtilities
  {
    public static void copyProperties(Object source, Object target)
      {
        //     copyProperties(source, target, null);
      }

    /**
     * Copy the property values of the given source bean into the given target bean,
     * ignoring the given ignoreProperties.
     * @param source the source bean
     * @param target the target bean
     * @param ignoreProperties array of property names to ignore
     * @throws IllegalArgumentException if the classes of source and target do not match
     */

    //   public static void copyProperties(Object source, Object target, String[] ignoreProperties)
    //       throws IllegalArgumentException {
    //     List ignoreList = (ignoreProperties != null) ? Arrays.asList(ignoreProperties) : null;
    //    
    //     for (int i = 0; i < sourceBw.getPropertyDescriptors().length; i++) {
    //       PropertyDescriptor sourceDesc = sourceBw.getPropertyDescriptors()[i];
    //       String name = sourceDesc.getName();
    //       if (ignoreProperties == null || (!ignoreList.contains(name))) {
    //         PropertyDescriptor targetDesc = targetBw.getPropertyDescriptor(name);
    //         if (targetDesc.getWriteMethod() != null && targetDesc.getReadMethod() != null) {
    //           values.addPropertyValue(new PropertyValue(name, sourceBw.getPropertyValue(name)));
    //         }
    //       }
    //     }
    //     targetBw.setPropertyValues(values);
    //   }
    // 
    //   /**
    //    * Retrieve the JavaBeans <code>PropertyDescriptor</code>s of a given class.
    //    * @param clazz the Class to retrieve the PropertyDescriptors for
    //    * @return an array of <code>PropertyDescriptors</code> for the given class
    //    * @throws BeansException if PropertyDescriptor look fails
    //    */
    //   public static PropertyDescriptor[] getPropertyDescriptors(Class clazz) throws BeansException {
    //     CachedIntrospectionResults cr = CachedIntrospectionResults.forClass(clazz);
    //     return cr.getBeanInfo().getPropertyDescriptors();
    //   }
    // 
    //   /**
    //    * Find a JavaBeans <code>PropertyDescriptor</code> for the given method,
    //    * with the method either being the read method or the write method for
    //    * that bean property.
    //    * @param method the method to find a corresponding PropertyDescriptor for
    //    * @return the corresponding PropertyDescriptor, or null if none
    //    * @throws BeansException if PropertyDescriptor look fails
    //    */
    //   public static PropertyDescriptor findPropertyForMethod(Method method) throws BeansException {
    //     Assert.notNull(method, "method must not be null");
    //     PropertyDescriptor[] pds = getPropertyDescriptors(method.getDeclaringClass());
    //     for (int i =0 ; i < pds.length; i++) {
    //       if (method.equals(pds[i].getReadMethod()) || method.equals(pds[i].getWriteMethod())) {
    //         return pds[i];
    //       }
    //     }
    //     return null;
    //   }
  }
