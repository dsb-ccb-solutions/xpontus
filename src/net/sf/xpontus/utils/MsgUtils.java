/*
 * MsgUtils.java
 *
 * Created on November 12, 2005, 1:46 PM
 *
 *  Copyright (C) 2005 Yves Zoundi
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
package net.sf.xpontus.utils;


/**
 * A class to display some i18n messages
 * @author Yves Zoundi
 */
public class MsgUtils {
    private static MsgUtils _instance;
    private java.util.ResourceBundle _res;

    /** Creates a new instance of MsgUtils */
    private MsgUtils() {
        try {
            java.util.Locale locale = java.util.Locale.getDefault();
            _res = java.util.ResourceBundle.getBundle("net.sf.xpontus.i18n.textMessages",
                    locale);
        } catch (Exception e) {
            _res = java.util.ResourceBundle.getBundle("net.sf.xpontus.i18n.textMessages",
                    java.util.Locale.ENGLISH);
        }
    }

    /**
     * The single instance of this class
     * @return The static instance of this class
     */
    public static MsgUtils getInstance() {
        if (_instance == null) {
            _instance = new MsgUtils();
        }

        return _instance;
    }

    /**
     *
     * @param msg The message key
     * @return The i18n value of the message key
     */
    public String getString(String msg) {
        return _res.getString(msg);
    }
}
