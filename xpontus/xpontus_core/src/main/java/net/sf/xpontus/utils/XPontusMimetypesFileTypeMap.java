/*
 * XPontusMimetypesFileTypeMap.java
 *
 * Created on 2007-08-08, 10:45:53
 *
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
 */
package net.sf.xpontus.utils;

import org.apache.commons.collections.map.ListOrderedMap;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.concurrent.ConcurrentHashMap;

import javax.activation.FileTypeMap;


/**
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XPontusMimetypesFileTypeMap extends FileTypeMap
{
    private static final String DEFAULT_TYPE = "text/plain";
    private final Map<String, List<String>> contentTypesMap = new ConcurrentHashMap<String, List<String>>();
    private final Map<String, String> types = new ConcurrentHashMap<String, String>();
    private ListOrderedMap mimetypesList = new ListOrderedMap();

    public XPontusMimetypesFileTypeMap()
    {
    }

    public XPontusMimetypesFileTypeMap(String mimeTypeFileName)
        throws IOException
    {
        this();

        BufferedReader reader = new BufferedReader(new FileReader(
                    mimeTypeFileName));

        try
        {
            String line;

            while ((line = reader.readLine()) != null)
            {
                addMimeTypes(line);
            }

            reader.close();
        }
        catch (IOException e)
        {
            try
            {
                reader.close();
            }
            catch (IOException e1)
            {
                // ignore to allow original cause through
            }

            throw e;
        }
    }

    public XPontusMimetypesFileTypeMap(InputStream is)
    {
        this();

        try
        {
            loadStream(is);
        }
        catch (IOException e)
        {
            // ignore as the spec's signature says we can't throw IOException - doh!
        }
    }

    public Map<String, List<String>> getContentTypesMap()
    {
        return contentTypesMap;
    }

    public ListOrderedMap getTypes()
    {
        return mimetypesList;
    }

    private void loadStream(InputStream is) throws IOException
    {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        String line;

        while ((line = reader.readLine()) != null)
        {
            addMimeTypes(line);
        }
    }

    public synchronized void addMimeTypes(String mime_types)
    {
        int hashPos = mime_types.indexOf('#');

        if (hashPos != -1)
        {
            mime_types = mime_types.substring(0, hashPos);
        }

        StringTokenizer tok = new StringTokenizer(mime_types);

        if (!tok.hasMoreTokens())
        {
            return;
        }

        String contentType = tok.nextToken();

        while (tok.hasMoreTokens())
        {
            String fileType = tok.nextToken();
            types.put(fileType, contentType);

            if (!mimetypesList.containsKey(contentType))
            {
                mimetypesList.put(contentType, new ArrayList<String>());
            }

            List<String> mli = (List<String>) mimetypesList.get(contentType);
            mli.add("*." + fileType);

            List<String> li = new ArrayList<String>();

            if (!contentTypesMap.containsKey(contentType))
            {
                contentTypesMap.put(contentType, li);
            }
            else
            {
                li = contentTypesMap.get(contentType);
            }

            if (!li.contains(fileType))
            {
                li.add(fileType);
            }
        }
    }

    public String getContentType(File f)
    {
        return getContentType(f.getName());
    }

    public String getContentType(String filename)
    {
        int index = filename.lastIndexOf('.');

        if ((index == -1) || (index == (filename.length() - 1)))
        {
            return DEFAULT_TYPE;
        }

        String fileType = filename.substring(index + 1);
        String contentType = (String) types.get(fileType);

        return (contentType == null) ? DEFAULT_TYPE : contentType;
    }
}
