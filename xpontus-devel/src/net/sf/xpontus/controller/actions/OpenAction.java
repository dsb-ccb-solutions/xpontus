/*
 * OpenAction.java
 *
 * Created on 18 juillet 2005, 02:55
 *
 *  Copyright (C) 2005 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.controller.actions;

import com.vlsolutions.swing.docking.DockableState;
import com.vlsolutions.swing.docking.DockingDesktop;

import net.sf.xpontus.core.controller.actions.ThreadedAction;
import net.sf.xpontus.view.EditorContainer;
import net.sf.xpontus.view.PaneForm;
import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.collections.ExtendedProperties;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import java.util.Iterator;

import javax.swing.filechooser.FileFilter;

/**
 * Action to open one or multiple files
 * 
 * @author Yves Zoundi
 */
public class OpenAction extends ThreadedAction {
	private javax.swing.JFileChooser chooser;

	EditorContainer current = null;

	int pos = 0;

	/** Creates a new instance of OpenAction */
	public OpenAction() {
		chooser = new javax.swing.JFileChooser();
		chooser.setMultiSelectionEnabled(true);
		chooser.setFileSelectionMode(javax.swing.JFileChooser.FILES_ONLY);
		initFilters();
	}

	/**
	 * 
	 */
	public void initFilters() {
		ExtendedProperties props = new ExtendedProperties();

		String syntaxDef = "/net/sf/xpontus/conf/syntax.properties";

		InputStream is = getClass().getResourceAsStream(syntaxDef);

		try {
			props.load(is);
		} catch (IOException e) {
			e.printStackTrace();
		}

		IOUtils.closeQuietly(is);

		Iterator it = props.getKeys();

		while (it.hasNext()) {
			String key = it.next().toString();

			if (key.endsWith(".parser") && !(key.startsWith("plain"))) {
				String extension = key.split("\\.")[0];
				FileFilter m_filter = createFileFilter(extension);
				chooser.addChoosableFileFilter(m_filter);
			}
		}

		chooser.setAcceptAllFileFilterUsed(true);
	}

	/**
	 * @param extension
	 * @return
	 */
	private FileFilter createFileFilter(final String extension) {
		FileFilter m_filter = new FileFilter() {
			public boolean accept(File f) {
				return f.getName().endsWith(extension) || f.isDirectory();
			}

			public String getDescription() {
				// TODO Auto-generated method stub
				return extension.toUpperCase() + " files";
			}
		};

		return m_filter;
	}

	/**
	 * @param dir
	 */
	public void setFileDir(String dir) {
		chooser.setCurrentDirectory(new File(dir));
	}

	public void execute() {
		int answer = chooser.showOpenDialog(XPontusWindow.getInstance()
				.getFrame());

		if (answer == javax.swing.JFileChooser.APPROVE_OPTION) {
			final java.io.File[] selectedFiles = chooser.getSelectedFiles();

			for (int i = 0; i < selectedFiles.length; i++) {
				  XPontusWindow.getInstance().getTabContainer().createEditorFromFile(selectedFiles[i]);

			}

		}
	}
}