/*
 * Copyright (C) 2005-2008 Yves Zoundi <yveszoundi at users dot sf dot net>
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

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.plugins.SimplePluginDescriptor;

import org.apache.commons.io.IOUtils;

import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.Velocity;
import org.apache.velocity.context.Context;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.io.Writer;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;


/**
 * Velocity template renderer utility class
 * @author Yves Zoundi
 */
public class PluginsTemplateRenderer
{
    private static final String TEMPLATE_VENDOR_KEY = "vendor";
    private static final String TEMPLATE_DATE_KEY = "date";
    private static final String TEMPLATE_ID_KEY = "id";
    private static final String TEMPLATE_CATEGORY_KEY = "category";
    private static final String TEMPLATE_DESCRIPTION_KEY = "description";
    private static final String TEMPLATE_VERSION_KEY = "version";
    private static final String TEMPLATE_LICENSE_KEY = "license";
    private static final String TEMPLATE_DEPENDENCIES_KEY = "dependencies";
    private static final String TEMPLATE_DISPLAYNAME_KEY = "displayname";
    private static final String TEMPLATE_HOMEPAGE_KEY = "homepage";
    private static final String TEMPLATE_BUILTIN_KEY = "built-in";

    public PluginsTemplateRenderer()
    {
    }

    public String renderTemplate(Map<String, String> pluginInformation)
    {
        Writer writer = null;

        try
        {
            Context context = new VelocityContext();

            writer = new StringWriter();

            Set<Entry<String, String>> pluginInfoSet = pluginInformation.entrySet();

            for (Entry<String, String> entry : pluginInfoSet)
            {
                context.put(entry.getKey(), entry.getValue());
            }

            if (!pluginInformation.containsKey(TEMPLATE_VENDOR_KEY))
            {
                context.put(TEMPLATE_VENDOR_KEY, "Yves Zoundi");
            }
            else
            {
                if (pluginInformation.get(TEMPLATE_VENDOR_KEY) == null)
                {
                    context.put(TEMPLATE_VENDOR_KEY, "Yves Zoundi");
                }
                else if (pluginInformation.get(TEMPLATE_VENDOR_KEY).trim()
                                              .equals(""))
                {
                    context.put(TEMPLATE_VENDOR_KEY, "Yves Zoundi");
                }
            }

            InputStream is = getClass()
                                 .getResourceAsStream("template.vm");
            Velocity.evaluate(context, writer, "browser_template_renderer",
                new InputStreamReader(is));

            return writer.toString();
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
        finally
        {
            IOUtils.closeQuietly(writer);
        }
    }

    public String renderTemplate(SimplePluginDescriptor spd)
    {
        Writer writer = null;

        try
        {
            Context context = new VelocityContext();

            writer = new StringWriter();

            String vendor = spd.getAuthor();

            if (vendor == null)
            {
                vendor = "Yves Zoundi";
            }

            context.put(TEMPLATE_VENDOR_KEY, vendor);
            context.put(TEMPLATE_DATE_KEY, spd.getDate());
            context.put(TEMPLATE_BUILTIN_KEY, spd.getBuiltin());
            context.put(TEMPLATE_CATEGORY_KEY, spd.getCategory());
            context.put(TEMPLATE_DESCRIPTION_KEY, spd.getDescription());
            context.put(TEMPLATE_DISPLAYNAME_KEY, spd.getDisplayname());
            context.put(TEMPLATE_HOMEPAGE_KEY, spd.getHomepage());
            context.put(TEMPLATE_ID_KEY, spd.getId());
            context.put(TEMPLATE_VERSION_KEY, spd.getVersion());
            context.put(TEMPLATE_LICENSE_KEY, spd.getLicense());
            context.put(TEMPLATE_DEPENDENCIES_KEY, spd.getDependencies());

            InputStream is = getClass().getResourceAsStream("template.vm");

            Velocity.evaluate(context, writer, "browser_template_renderer",
                new InputStreamReader(is));

            return writer.toString();
        }
        catch (Exception e)
        {
            return e.getMessage();
        }
        finally
        {
            IOUtils.closeQuietly(writer);
        }
    }

    public String renderMissingFeatureTemplate()
    {
        Writer writer = null;

        try
        {
            Context context = new VelocityContext();

            writer = new StringWriter();

            context.put("pluginsdir",
                XPontusConfigurationConstantsIF.XPONTUS_PLUGINS_DIR.getAbsolutePath());

            InputStream is = getClass().getResourceAsStream("missing.vm");
            Velocity.evaluate(context, writer, "missing_template_renderer",
                new InputStreamReader(is));

            return writer.toString();
        }
        catch (Exception e)
        {
            e.printStackTrace();

            return null;
        }
        finally
        {
            IOUtils.closeQuietly(writer);
        }
    }
}
