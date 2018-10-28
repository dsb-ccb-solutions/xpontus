/*
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
 *
 *
 */
package net.sf.xpontus.plugins.outline.html;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusMimeConstantsIF;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.OutlineViewDockable;
import net.sf.xpontus.plugins.outline.OutlinePluginIF;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.Reader;

import javax.swing.SwingUtilities;
import javax.swing.text.Document;


/**
 *
 * @author Yves Zoundi
 */
public class HTMLOutlinePluginImpl implements OutlinePluginIF
{
    private static final Log LOG = LogFactory.getLog(HTMLOutlinePluginImpl.class);

    /**
     * Returns the content type supported by this plugin implementation
     * @return
     */
    public String getContentType()
    {
        return XPontusMimeConstantsIF.TEXT_HTML;
    }

    public void updateOutline(final Document doc)
    {
        try
        {
            byte[] b = doc.getText(0, doc.getLength()).getBytes();
            CharsetDetector detector = new CharsetDetector();
            detector.setText(new ByteArrayInputStream(b));

            Reader reader = detector.detect().getReader();

            final HtmlTreeBuilder treeBuilder = new HtmlTreeBuilder();
            treeBuilder.parse(reader);

            final OutlineViewDockable outline = DefaultXPontusWindowImpl.getInstance()
                                                                        .getOutline();

            SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        doc.putProperty(XPontusConstantsIF.OUTLINE_INFO,
                            treeBuilder.getRootNode());
                        outline.updateAll(treeBuilder.getRootNode());
                    }
                });
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
    }
}
