/*
 * IDocumentContainer.java
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
package net.sf.xpontus.modules.gui.components;

import com.vlsolutions.swing.docking.Dockable;

import org.apache.commons.vfs.FileObject;

import javax.swing.text.JTextComponent;


/**
 *
 * Interface for document containers
 * @version 0.0.1
 * @author Yves Zoundi
 */
public interface IDocumentContainer extends Dockable {
    /**
     *
     * @return
     */
    public JTextComponent getEditorComponent();

    /**
     *
     * @return
     */
    public JStatusBar getStatusBar();

    /**
     *
     */
    public void completeSetup();

    /**
     *
     * @param url
     */
    public void setup(java.net.URL url);

    /**
     *
     * @param url
     */
    public void setup(java.io.File url);

    /**
     * Method setupFromTemplate ...
     *
     * @param templateFileName
     * @param templatePath
     */
    public void setupFromTemplate(String templateFileName, String templatePath);

    /**
     *
     */
    public void setup();

    /**
     *
     * @param fo
     */
    public void setup(FileObject fo);
}
