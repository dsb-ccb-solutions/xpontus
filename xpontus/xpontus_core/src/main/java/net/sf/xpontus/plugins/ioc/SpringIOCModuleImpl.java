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
package net.sf.xpontus.plugins.ioc;

import org.springframework.beans.factory.support.PropertiesBeanDefinitionReader;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;

import org.springframework.context.support.GenericApplicationContext;

import org.springframework.core.io.ClassPathResource;


/**
 * Spring Framework IOC Container implementation
 * @author Yves Zoundi
 */
public class SpringIOCModuleImpl implements IOCPluginIF {
    private GenericApplicationContext ctx;

    /**
     * Default constructor
     */
    public SpringIOCModuleImpl() {
        ctx = new GenericApplicationContext();
    }

    /**
     * Load beans via an XML configuration file
     * @param resource The location of the xml configuration in the classpath
     * @param loader The class loader to use for beans loading
     */
    public void initializeXMLBeans(String resource, ClassLoader loader) {
        XmlBeanDefinitionReader xmlReader = new XmlBeanDefinitionReader(ctx);
        xmlReader.loadBeanDefinitions(new ClassPathResource(resource, loader));
        ctx.refresh();
    }

    /**
     * Get a bean from the IOC Container
     * @param alias The bean's unique ID
     * @return The bean corresponding to that alias
     */
    public Object getBean(String alias) {
        return ctx.getBean(alias);
    }

    /**
     * Initialize beans from properties file
     * @param resource The location of the properties file in the classpath
     * @param loader The class loader to be able to load beans
     */
    public void initializePropertiesBeans(String resource, ClassLoader loader) {
        PropertiesBeanDefinitionReader propReader = new PropertiesBeanDefinitionReader(ctx);
        propReader.loadBeanDefinitions(new ClassPathResource(resource, loader));
        ctx.refresh();
    }
}
