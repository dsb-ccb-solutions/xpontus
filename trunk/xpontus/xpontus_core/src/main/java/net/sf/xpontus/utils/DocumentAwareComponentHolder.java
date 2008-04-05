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
package net.sf.xpontus.utils;

import com.sun.java.help.impl.SwingWorker;

import net.sf.xpontus.actions.DocumentAwareComponentIF;

import java.util.List;
import java.util.Vector;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DocumentAwareComponentHolder {
    private static DocumentAwareComponentHolder INSTANCE;
    private List<DocumentAwareComponentIF> componentList = new Vector<DocumentAwareComponentIF>();

    /**
     * @return
     */
    public static DocumentAwareComponentHolder getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new DocumentAwareComponentHolder();
        }

        return INSTANCE;
    }

    /**
     * @param evt
     */
    public void notifyComponents(final DocumentContainerChangeEvent evt) {
        SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    for (DocumentAwareComponentIF dai : componentList) {
                        dai.onNotify(evt);
                    }

                    return null;
                }
            };

        worker.start();
    }

    /**
     * @param dac
     */
    public void register(DocumentAwareComponentIF dac) {
        componentList.add(dac);
    }
}
