/*
 * XMLOutlinePluginImpl.java
 *
 * Created on 2007-10-02, 18:17:22
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
package net.sf.xpontus.plugins.outline.xml;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.OutlineViewDockable;
import net.sf.xpontus.plugins.outline.OutlinePluginIF;

import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.ByteArrayInputStream;
import java.io.Reader;

import javax.swing.SwingUtilities;
import javax.swing.text.Document;


/**
 * XML Outline plugin implementation
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.2
 */
public class XMLOutlinePluginImpl implements OutlinePluginIF
{
    private static final Log LOG = LogFactory.getLog(XMLOutlinePluginImpl.class);
    private static final String CONTENT_TYPE = "text/xml";

    /**
     * Returns the content type supported by this plugin implementation
     * @return
     */
    public String getContentType()
    {
        return CONTENT_TYPE;
    }

    /**
     * Update the outline view of the document
     * @param doc The document being edited
     */
    public void updateOutline(Document doc)
    {
        Reader reader = null;

        try
        {
            byte[] b = doc.getText(0, doc.getLength()).getBytes();
            CharsetDetector detector = new CharsetDetector();
            detector.setText(new ByteArrayInputStream(b));

            reader = detector.detect().getReader();

            final XMLOutlineDocumentParser parser = new XMLOutlineDocumentParser();
            parser.parse(reader);

            final OutlineViewDockable outline = DefaultXPontusWindowImpl.getInstance()
                                                                        .getOutline();

            final Document mDoc = doc;

            String dtdLocation = parser.getDtdLocation();
            String schemaLocation = parser.getSchemaLocation();
            
          //  System.out.println("dtdLocation:" + dtdLocation);

            if (dtdLocation != null)
            {
                mDoc.putProperty(XPontusConstantsIF.PARSER_DATA_DTD_COMPLETION_INFO,
                    dtdLocation);
            }
            else if (schemaLocation != null)
            {
                mDoc.putProperty(XPontusConstantsIF.PARSER_DATA_SCHEMA_COMPLETION_INFO,
                    schemaLocation);
            }

            SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        mDoc.putProperty(XPontusConstantsIF.OUTLINE_INFO,
                            parser.getRootNode());
                        outline.updateAll(parser.getRootNode());
                    }
                });
        }
        catch (Exception e)
        {
            LOG.error(e.getMessage(), e);
        }
        finally
        {
            if (reader != null)
            {
                IOUtils.closeQuietly(reader);
            }
        }
    }
}
