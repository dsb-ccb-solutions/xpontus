/*
 * SyntaxInfoFactory.java
 *
 * Created on December 22, 2006, 1:07 AM
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
package net.sf.xpontus.syntax;

import net.sf.xpontus.constants.LexerPropertiesConstantsIF;
import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.plugins.color.PlainColorProviderImpl;
import net.sf.xpontus.plugins.lexer.LexerPluginIF;
import net.sf.xpontus.plugins.lexer.PlainLexerModuleImpl;
import net.sf.xpontus.properties.PropertiesHolder;
import net.sf.xpontus.utils.MimeTypesProvider;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Hashtable;
import java.util.Map;


/**
 * A class responsible for providing the right syntax coloring parser
 * @author Yves Zoundi
 */
public class SyntaxSupportFactory {
    private static Log log = LogFactory.getLog(SyntaxSupportFactory.class);

    /** Creates a new instance of SyntaxInfoFactory */
    private SyntaxSupportFactory() {
    }

    /**
     * @param path
     * @return
     */
    public static SyntaxSupport getSyntax(String path) {
        LexerPluginIF lexer = null;
        IColorProvider colorer = null;

        MimeTypesProvider p = MimeTypesProvider.getInstance();
        Map map = (Map) PropertiesHolder.getPropertyValue(XPontusPropertiesConstantsIF.XPONTUS_LEXER_PROPERTY);

        String mimeType = p.getMimeType(path);

        System.out.println("syntaxSupport path:" + path);
        System.out.println("syntax support mime type:" + mimeType);

        try {
            if (map.containsKey(mimeType)) {
                Hashtable v = (Hashtable) map.get(mimeType);
                ClassLoader loader = (ClassLoader) v.get(LexerPropertiesConstantsIF.CLASS_LOADER);
                String lexerClass = (String) v.get(LexerPropertiesConstantsIF.LEXER_CLASSNAME);
                lexer = (LexerPluginIF) loader.loadClass(lexerClass)
                                              .newInstance();
                colorer = lexer.getColorer();
            } else {
                lexer = new PlainLexerModuleImpl();
                colorer = new PlainColorProviderImpl();
            }
        } catch (Exception e) {
            log.error("Error:" + SyntaxSupportFactory.class.getName() + "," +
                e.getMessage());
            lexer = new PlainLexerModuleImpl();
            colorer = new PlainColorProviderImpl();
        }

        SyntaxSupport _info = new SyntaxSupport(lexer, colorer);

        return _info;
    }
}
