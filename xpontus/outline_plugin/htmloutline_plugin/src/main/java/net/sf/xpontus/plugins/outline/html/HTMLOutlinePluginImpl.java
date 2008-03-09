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
package net.sf.xpontus.plugins.outline.html;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusMimeConstantsIF;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.OutlineViewDockable; 
import net.sf.xpontus.plugins.outline.OutlinePluginIF;

import java.io.ByteArrayInputStream;
import java.io.Reader;

import javax.swing.SwingUtilities;
import javax.swing.text.Document;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class HTMLOutlinePluginImpl implements OutlinePluginIF {
    /**
     * Returns the content type supported by this plugin implementation
     * @return
     */
    public String getContentType() {
        return XPontusMimeConstantsIF.TEXT_HTML;
    }

    public void updateOutline(Document doc) {
        try {
            byte[] b = doc.getText(0, doc.getLength()).getBytes();
            CharsetDetector detector = new CharsetDetector();
            detector.setText(new ByteArrayInputStream(b));

            Reader m_reader = detector.detect().getReader();

            final HtmlTreeBuilder m_treeBuilder = new HtmlTreeBuilder(); 
            m_treeBuilder.parse(m_reader);

            final OutlineViewDockable outline = DefaultXPontusWindowImpl.getInstance()
                                                                        .getOutline();

            final Document mDoc = doc;

            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        mDoc.putProperty(XPontusConstantsIF.OUTLINE_INFO,
                            m_treeBuilder.getRootNode());
                        outline.updateAll(m_treeBuilder.getRootNode());
                    }
                });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
