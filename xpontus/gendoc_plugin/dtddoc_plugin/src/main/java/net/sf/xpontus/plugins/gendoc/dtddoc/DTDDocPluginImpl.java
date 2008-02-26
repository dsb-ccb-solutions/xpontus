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
package net.sf.xpontus.plugins.gendoc.dtddoc;

import DTDDoc.*;

import net.sf.xpontus.model.DocumentationModel;
import net.sf.xpontus.plugins.gendoc.IDocumentationPluginIF;

import java.io.File;

import java.util.HashSet;
import java.util.Set;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class DTDDocPluginImpl implements IDocumentationPluginIF {
    public final String TYPE = "DTD";

    public String getType() {
        return TYPE;
    }

    public void handle(DocumentationModel model) throws Exception {
        DTDCommenter commenter = new DTDCommenter(new SystemLogger());
        File dest = new File(model.getOutput());
        File src = new File(model.getInput());
        Set scan = new HashSet();
        String t = model.getTitle();
        scan.add(src);
        commenter.commentDTDs(scan, src.getParentFile(), dest, true, true,
            true, t, null);
    }
}
