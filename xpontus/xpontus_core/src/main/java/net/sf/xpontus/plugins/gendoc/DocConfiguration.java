/*
 *
 *
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
package net.sf.xpontus.plugins.gendoc;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


/**
 * Configuration holder for dtd engines
 * @author Yves Zoundi
 * @version 0.0.1
 */
public class DocConfiguration {
    private static DocConfiguration INSTANCE;
    private Map engines = new HashMap();

    private DocConfiguration() {
    }

    /**
     *
     * @return
     */
    public static DocConfiguration getInstane() {
        if (INSTANCE == null) {
            INSTANCE = new DocConfiguration();
        }

        return INSTANCE;
    }

    /**
     *
     * @param engines
     */
    public void setEngines(Map engines) {
        this.engines = engines;
    }

    /**
     *
     * @return
     */
    public Map getEngines() {
        return engines;
    }

    /**
     *
     * @return
     */
    public String[] getEnginesNames() {
        String[] names = new String[engines.size()];
        Iterator it = engines.keySet().iterator();

        for (int i = 0; it.hasNext(); i++) {
            names[i] = it.next().toString();
        }

        return names;
    }
}
