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
package net.sf.xpontus.plugins.gendoc.xsddoc;

import net.sf.xframe.xsddoc.Processor;

import net.sf.xpontus.model.DocumentationModel;
import net.sf.xpontus.plugins.gendoc.IDocumentationPluginIF;


/**
 *
 * @author Yves Zoundi
 */
public class XSDDocPluginImpl implements IDocumentationPluginIF {
    public final String TYPE = "XML Schema";

    public String getType() {
        return TYPE;
    }

    public void handle(DocumentationModel model) throws Exception {
        Processor processor = new Processor();
        processor.setOut(model.getOutput());
        processor.setDoctitle(model.getTitle());
        processor.setCss(model.getCss());
        System.out.println("Footer:" + model.getFooter());
        System.out.println("header:" + model.getHeader());
        processor.setFooter(model.getFooter());
        processor.setHeader(model.getHeader());
        processor.setSchemaLocation(model.getInput());
        processor.run();
    }
}
