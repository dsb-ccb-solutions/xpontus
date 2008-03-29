/*
 * XMLIndentationPluginImpl.java
 *
 * Created on 8-Aug-2007, 6:01:57 PM
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
 */
package net.sf.xpontus.plugins.indentation.xml;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.plugins.indentation.IndentationPluginIF;

import java.io.StringWriter;

import java.util.Vector;

import javax.swing.text.JTextComponent;



/**
 * Class description
 * @author Yves Zoundi
 */
public class XMLIndentationPluginImpl implements IndentationPluginIF {
    private static final IndentingTransformerImpl TRANSFORMER = new IndentingTransformerImpl();

    public XMLIndentationPluginImpl() {
    }

    public String getName() {
        return "XML Indentation engine";
    }

    public String getMimeType() {
        return "text/xml";
    }

    public void run() throws Exception {
        JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                     .getDocumentTabContainer()
                                                     .getCurrentEditor();

        CharsetDetector chd = new CharsetDetector();

        //        byte[] buf = jtc.getText().getBytes();
        //        chd.setText(new ByteArrayInputStream(buf));
        //
        //        CharsetMatch match = chd.detect();
        //        Reader reader = match.getReader();
        String omitCommentsOption = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() +
                "$" +
                XMLIndentationPreferencesConstantsIF.OMIT_COMMENTS_OPTION);

        String omitDoctypeOption = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() +
                "$" + XMLIndentationPreferencesConstantsIF.OMIT_DOCTYPE_OPTION);

        String omitXmlDeclaration = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() +
                "$" +
                XMLIndentationPreferencesConstantsIF.OMIT_XML_DECLARATION_OPTION);

        String preserveSpaceOption = (String) XPontusConfig.getValue(XMLIndentationPreferencesConstantsIF.class.getName() +
                "$" +
                XMLIndentationPreferencesConstantsIF.PRESERVE_SPACE_OPTION);

        try {
            StringWriter m_writer = new StringWriter();
            TRANSFORMER.indentXml(jtc.getText(), m_writer, 4, false,
                new Vector());

            byte[] b = m_writer.toString().getBytes();

            if (b.length > 0) {
                jtc.getDocument().remove(0, jtc.getDocument().getLength());
                chd = new CharsetDetector();
                chd.setText(b);
                jtc.read(chd.detect().getReader(), null);
            }
        } catch (Exception e) {
            throw e;
        }
    }
}
