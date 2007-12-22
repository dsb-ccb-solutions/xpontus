/*
 * SettingsModuleIF.java
 *
 * Created on 25 mai 2007, 15:44
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


/**
 *
 * @author Yves Zoundi
 */
public interface SettingsModuleIF {
    /**
     *
     */
    public void init();

    /**
     *
     */
    public void start();

    /**
     *
     */
    public void shutdown();

    /**
     *
     * @param bean
     */
    public void save(Object bean);
    
    
    /**
     * 
     * @param bean
     */
    public void remove(Object bean);

    /**
     *
     * @param beanClass
     * @return
     */
    public Object getSingleObject(Class beanClass);
}
