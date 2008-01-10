/*
 * Db4oSettingsImpl.java
 *
 * Created on 16-Aug-2007, 7:45:30 AM
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
 */
package net.sf.xpontus.plugins.settings.db4o;

import com.db4o.Db4o;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;



/**
 * Class description
 * @author Yves Zoundi
 */
public class Db4oSettingsImpl  {
    private ObjectContainer db;
    private String databaseFileName = "";

    public Db4oSettingsImpl() {
    }

    public void init() {
        this.db = Db4o.openFile(this.databaseFileName);
    }

    public void start() {
    }

    public void shutdown() {
        this.db.close();
    }

    public void save(Object bean) {
        db.set(bean);
        db.commit();
    }

    public synchronized Object get(Class objectClass) {
        Object m_object = db.get(objectClass).next();

        return m_object;
    }

    public synchronized void empty() {
        ObjectSet result = db.get(new Object());

        while (result.hasNext()) {
            db.delete(result.next());
        }
    }

    public void remove(Object bean) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Object getSingleObject(Class beanClass) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
