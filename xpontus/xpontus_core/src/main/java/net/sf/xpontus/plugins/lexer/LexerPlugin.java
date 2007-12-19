/*
 * LexerPlugin.java
 *
 * Created on 20-Jul-2007, 1:03:34 PM
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
package net.sf.xpontus.plugins.lexer;

import java.util.Arrays;
import net.sf.xpontus.constants.LexerPropertiesConstantsIF;
import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.plugins.XPontusPlugin;
import net.sf.xpontus.properties.PropertiesHolder;
import net.sf.xpontus.utils.MimeTypesProvider;

import org.java.plugin.PluginManager;
import org.java.plugin.registry.Extension;
import org.java.plugin.registry.ExtensionPoint;
import org.java.plugin.registry.PluginDescriptor;
import org.java.plugin.registry.PluginRegistry;

import java.util.Collection;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;


/**
 * Lexer plugin for different file types
 * @author Yves Zoundi
 */
public class LexerPlugin extends XPontusPlugin {
    public static final String EXTENSION_POINT_NAME = "lexerpluginif";
    public static final String PLUGIN_IDENTIFIER = "plugin.core.lexer";
    private Map lexerMap = new HashMap();

    public LexerPlugin() {
    }

    /**
     *
     *
     * @param lexer
     * @param loader
     */
    private void addLexer(LexerPluginIF lexer, ClassLoader loader) {
        Hashtable t = new Hashtable(); 
        
        t.put(LexerPropertiesConstantsIF.CLASS_LOADER, loader);
        t.put(LexerPropertiesConstantsIF.LEXER_CLASSNAME,
            lexer.getLexerClassName());
        t.put(LexerPropertiesConstantsIF.LEXER_DESCRIPTION,
            lexer.getDescription());
        t.put(LexerPropertiesConstantsIF.LEXER_NAME, lexer.getName());
        t.put(LexerPropertiesConstantsIF.CONTENT_TYPE, lexer.getMimeType());

        lexerMap.put(lexer.getMimeType(), t);

        String[] extensions = null;
        try{ 
            extensions = lexer.getSupportedExtensions();
        }
        catch(Exception e){ 
            e.printStackTrace();
        } 
        
        String mime = lexer.getMimeType();
        String mime_types = createMimeTypes(mime, extensions);

        MimeTypesProvider.getInstance().addMimeTypes(mime_types);
    }

    public void init() throws Exception {
        PluginManager manager = getManager();
        PluginRegistry registry = manager.getRegistry();
        ExtensionPoint themePluginExtPoint = registry.getExtensionPoint(getDescriptor()
                                                                            .getId(),
                EXTENSION_POINT_NAME);

        Collection plugins = themePluginExtPoint.getConnectedExtensions();

        LexerPluginIF mPlugin = new PlainLexerModuleImpl();
        addLexer(mPlugin, mPlugin.getClass().getClassLoader());

        for (Iterator it = plugins.iterator(); it.hasNext();) {
            Extension ext = (Extension) it.next();
            PluginDescriptor descriptor = ext.getDeclaringPluginDescriptor();
            ClassLoader classLoader = manager.getPluginClassLoader(descriptor);
            String className = ext.getParameter("class").valueAsString();
            Class cl = classLoader.loadClass(className);
            mPlugin = (LexerPluginIF) cl.newInstance();
            addLexer(mPlugin, classLoader);
        }

        PropertiesHolder.registerProperty(XPontusPropertiesConstantsIF.XPONTUS_LEXER_PROPERTY,
            lexerMap);
    }

    protected void doStart() throws Exception {
    }

    protected void doStop() throws Exception {
    }

    private String createMimeTypes(String mimeType, String[] supportedExtensions) {
        StringBuffer sb = new StringBuffer(mimeType + " ");

        for (int i = 0; i < supportedExtensions.length; i++) {
            sb.append(supportedExtensions[i]);

            if (i != (supportedExtensions.length - 1)) {
                sb.append(" ");
            }
        }

        return sb.toString();
    }
}
