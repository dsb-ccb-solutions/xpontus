/*
 * BaseController.java
 *
 * Created on 2 octobre 2005, 16:25
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.core.controller.handlers;

import java.lang.reflect.Method;


/**
 * A class which executes method using java reflection API
 * @author Yves Zoundi
 */
public class BaseController {
    /**
     * Create a new instance of BaseController
     */
    public BaseController() {
    }

    /**
     * Call a method on an object of type BaseController
     * @param method The method name to call
     */
    public void invokeAction(String method) {
        invokeAction(method, new Object[] {  });
    }

    /**
     * Call a method with parameters
     * @param method The method name to call
     * @param parameters The method parameters
     */
    public void invokeAction(String method, Object[] parameters) {
        int taille = parameters.length;
        Class[] cls = new Class[taille];

        if (parameters.length == 0) {
            cls = new Class[] {  };
        }

        for (int j = 0; j < taille; j++)
            cls[j] = parameters[j].getClass();

        try {
            Method aMethod = getClass()
                                 .getDeclaredMethod(method, cls);
            aMethod.invoke(this, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Call a method on an object
     * @param o The object on which to call the method
     * @param method The method name to call
     */
    public void invokeObjectAction(Object o, String method) {
        invokeObjectAction(o, method, new Object[] {  });
    }

    /**
     * Call a method on an object with parameters
     * @param o The object on which to call the method
     * @param method The method name to call
     * @param parameters The method parameters
     */
    public void invokeObjectAction(Object o, String method, Object[] parameters) {
        int taille = parameters.length;
        Class[] cls = new Class[taille];

        for (int j = 0; j < taille; j++)
            cls[j] = parameters[j].getClass();

        try {
            Method aMethod = getClass().getDeclaredMethod(method, cls);
            aMethod.invoke(o, parameters);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
