/*
 * KitInfo.java
 *
 * Created on 6 septembre 2005, 22:49
 *
 * Copyright (C) 2005 Yves Zoundi
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
package net.sf.xpontus.view.editor;


import java.io.File;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.syntax.jedit.tokenmarker.TokenMarker;


/**
 * A singleton class to select the syntax highlighting class to use
 * @author Yves Zoundi
 */
public class KitInfo {
    private static KitInfo _KitInfoInstance;
    java.util.Properties props;
    
    private KitInfo() {
        init();
    }
    
    /**
     *
     * @return the single instance of this class
     */
    public static KitInfo getInstance() {
        if (_KitInfoInstance == null) {
            _KitInfoInstance = new KitInfo();
        }
        
        return _KitInfoInstance;
    }
    
    /** init the syntax properties*/
    private void init() {
        props = new java.util.Properties();
        
        try {
            java.io.InputStream is = getClass().getResourceAsStream("syntax.sr");
            props.load(is);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
     public TokenMarker getTokenMarker(File ext) {
       return getTokenMarker(getExtension(ext.getAbsolutePath()));
    }
     
     public TokenMarker getTokenMarker(String ext) {
        TokenMarker mk = null;
        
        if (ext == null) {
            return mk;
        }
        
        if (props.getProperty(ext) == null) {
            
            return mk;
        }
        
        try {
            Object _instance = Class.forName(props.getProperty(ext))
            .newInstance();
            mk = (TokenMarker) _instance;
            System.out.println("token marker class:" + mk.getClass().getName());
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return mk;
    }
     
    /**
     *
     * @param filename A filename
     * @return The filename extension
     */
    public String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }
    
}
