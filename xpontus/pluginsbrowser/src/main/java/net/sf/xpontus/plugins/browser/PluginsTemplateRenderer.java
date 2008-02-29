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
package net.sf.xpontus.plugins.browser;
 
import org.apache.velocity.app.Velocity; 
import org.apache.velocity.context.Context;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;


import java.util.Iterator;
import java.util.Map;
import org.apache.velocity.VelocityContext;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PluginsTemplateRenderer {
    public PluginsTemplateRenderer() {
    }

    public String renderTemplate(Map<String, String> pluginInformation) {
        try {
            Context context = new VelocityContext();

            Writer writer = new StringWriter();

            Iterator<String> it = pluginInformation.keySet().iterator();

            while (it.hasNext()) {
                String m_key = it.next();
                String m_value = pluginInformation.get(m_key);
                context.put(m_key, m_value);
            }

            if (!pluginInformation.containsKey("vendor")) {
                context.put("vendor", "Yves Zoundi");
            }
            else{
                if(pluginInformation.get("vendor") == null){
                    context.put("vendor", "Yves Zoundi");
                }
                else if(pluginInformation.get("vendor").trim().equals("")){
                    context.put("vendor", "Yves Zoundi");
                }
            }

            InputStream is = getClass().getResourceAsStream("template.vm");
            Velocity.evaluate(context, writer, "browser_template_renderer",
                new InputStreamReader(is));

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }

    public String renderTemplate(SimplePluginDescriptor spd) {
        try {
            Context context = new VelocityContext();

            Writer writer = new StringWriter();

            String vendor = spd.getAuthor();

            if (vendor == null) {
                vendor = "Yves Zoundi";
            }

            context.put("vendor", vendor);
            context.put("built-in", spd.getBuiltin());
            context.put("category", spd.getCategory());
            context.put("description", spd.getDescription());
            context.put("displayname", spd.getDisplayname());
            context.put("homepage", spd.getHomepage());
            context.put("id", spd.getId());
            context.put("version", spd.getVersion());

            InputStream is = getClass().getResourceAsStream("template.vm");
            Velocity.evaluate(context, writer, "browser_template_renderer",
                new InputStreamReader(is));

            return writer.toString();
        } catch (Exception e) {
            e.printStackTrace();

            return null;
        }
    }
}
