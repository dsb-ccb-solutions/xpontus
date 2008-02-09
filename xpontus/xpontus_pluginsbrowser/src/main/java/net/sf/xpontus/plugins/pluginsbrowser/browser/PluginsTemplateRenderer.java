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
package net.sf.xpontus.plugins.pluginsbrowser.browser;

import biz.source_code.miniTemplator.*;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.util.Iterator;
import java.util.Map;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PluginsTemplateRenderer {
    //     m.put("category", category);
    //            m.put("built-in", builtin);
    //            m.put("id", id);
    //            m.put("homepage", homepage); 
    //            m.put("displayname", displayname);
    //            m.put("description", description);
    //            m.put("version", version);
    public PluginsTemplateRenderer() {
    }

    public String renderTemplate(Map<String, String> pluginInformation) {
        try {
            InputStream is = getClass()
                                 .getResourceAsStream("template.st");
            Reader isr = new InputStreamReader(is);
            Reader m_reader = new BufferedReader(isr);
            MiniTemplator t = new MiniTemplator(m_reader);
            Iterator<String> it = pluginInformation.keySet().iterator();

            while (it.hasNext()) {
                String m_key = it.next();
                String m_value = pluginInformation.get(m_key);
                t.setVariable(m_key, m_value);
            }

            if (!pluginInformation.containsKey("vendor")) {
                t.setVariable("vendor", "Yves Zoundi");
            }

            return t.generateOutput();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public String renderTemplate(SimplePluginDescriptor spd) {
        try {
            InputStream is = getClass().getResourceAsStream("template.st");
            Reader isr = new InputStreamReader(is);
            Reader m_reader = new BufferedReader(isr);
            MiniTemplator t = new MiniTemplator(m_reader);

            t.setVariable("vendor", spd.getAuthor());
            t.setVariable("built-in", spd.getBuiltin());
            t.setVariable("category", spd.getCategory());
            t.setVariable("description", spd.getDescription());
            t.setVariable("displayname", spd.getDisplayname());
            t.setVariable("homepage", spd.getHomepage());
            t.setVariable("id", spd.getId());
            t.setVariable("version", spd.getVersion());

            return t.generateOutput();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
