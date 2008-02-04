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
package net.sf.xpontus.modules.gui.components;

import java.io.File;

import java.net.URL;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public interface IPerspective {
    /**
     *
     * @return
     */
    public String getMimeType();

    /**
     *
     * @param f
     */
    public void addDocument(File f);

    /**
     *
     * @param url
     */
    public void addDocument(URL url);

    /**
     *
     * @return
     */
    public boolean canCopy();

    /**
     *
     * @return
     */
    public boolean canPaste();

    /**
     *
     * @return
     */
    public boolean canCut();

    /**
     *
     * @return
     */
    public boolean canRedo();

    /**
     *
     * @return
     */
    public boolean canUndo();

    /**
     *
     * @return
     */
    public boolean beforeLayout();

    /**
     *
     */
    public void dispose();

    /**
     *
     * @return
     */
    public boolean handleCopy();

    /**
     *
     * @return
     */
    public boolean selfHandlePaste();

    /**
     *
     * @return
     */
    public boolean selfHandleCut();

    /**
     *
     * @return
     */
    public boolean selfHandleRedo();

    /**
     *
     * @return
     */
    public boolean selfHandleUndo();
}
