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
package net.sf.xpontus.plugins.preview;

import net.sf.xpontus.plugins.scenarios.DetachableScenarioModel;


/**
 * Plugin interface for transformation results preview
 * @author Yves Zoundi
 */
public interface PreviewPluginIF {
    /**
     * Return the mimetype of this plugin
     * @return the mimetype of this plugin
     */
    public String getMimeType();

    /**
     * @return
     */
    public String getName();

    /**
     * Preview the result of a transformation
     * @param model The transformation profile model
     */
    public void preview(DetachableScenarioModel model);
}
