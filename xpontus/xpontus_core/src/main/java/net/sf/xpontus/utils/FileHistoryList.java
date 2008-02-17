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
package net.sf.xpontus.utils;

import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;

import java.util.List;
import java.util.Vector;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class FileHistoryList {
    private static Vector<String> files = new Vector<String>();
    private static final int HISTORY_LIMIT_SIZE = 10;
    private static final Log logger = LogFactory.getLog(FileHistoryList.class);
    static int count = 0;

    public static void init() {
        count++;

        if (count > 1) {
            System.out.println("called init again");

            return;
        }

        logger.info("Loading file history list...");

        File history = XPontusConfigurationConstantsIF.RECENT_FILES_HISTORY_FILE;

        try {
            if (!history.exists()) {
                history.createNewFile();
            }

            InputStream is = FileUtils.openInputStream(history);
            Reader reader = new InputStreamReader(is, "UTF-8");
            BufferedReader m_reader = new BufferedReader(reader);
            String line = null;

            while ((line = m_reader.readLine()) != null) {
                files.add(line);
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    public static void removeEntry(String path) {
        files.remove(path);
    }

    public static void save() {
        logger.info("Storing file history list...");

        File history = XPontusConfigurationConstantsIF.RECENT_FILES_HISTORY_FILE;

        try {
            if (!history.exists()) {
                history.createNewFile();
            }

            OutputStream bos = FileUtils.openOutputStream(history);
            Writer m_writer = new OutputStreamWriter(bos, "UTF-8");

            for (String s : files) {
                m_writer.write(s + "\n");
            }

            m_writer.flush();
            m_writer.close();
            bos.flush();
            bos.close();
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    /**
     *
     * @return
     */
    public static List<String> getFileHistoryList() {
        return files;
    }

    /**
     *
     * @param path
     */
    public static void addFile(String path) {
        if (!files.contains(path)) {
            files.add(path);

            if (files.size() > HISTORY_LIMIT_SIZE) {
                files.remove(0);
            }
        }
    }
}
