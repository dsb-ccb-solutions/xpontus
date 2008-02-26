/*
 * CheckXMLActionImpl.java
 *
 * Created on 19-Aug-2007, 9:34:47 AM
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
package net.sf.xpontus.actions.impl;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.modules.gui.components.ConsoleOutputWindow;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.DocumentContainer;
import net.sf.xpontus.modules.gui.components.MessagesWindowDockable;
import net.sf.xpontus.modules.gui.components.OutputDockable;
import net.sf.xpontus.utils.DocumentContainerChangeEvent;
import net.sf.xpontus.utils.NullEntityResolver;

import org.apache.commons.io.IOUtils;

import org.apache.xerces.parsers.SAXParser;

import org.xml.sax.InputSource;
import org.xml.sax.SAXParseException;

import java.awt.Toolkit;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Reader;

import javax.swing.text.JTextComponent;
import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.utils.XPontusComponentsUtils;


/**
 * Check if the XML document is well-formed
 * @author Yves Zoundi
 */
public class CheckXMLActionImpl extends XPontusDocumentAwareThreadedActionImpl {
    /** Namespaces feature id (http://xml.org/sax/features/namespaces). */
    protected static final String NAMESPACES_FEATURE_ID = "http://xml.org/sax/features/namespaces";

    /** Namespace prefixes feature id (http://xml.org/sax/features/namespace-prefixes). */
    protected static final String NAMESPACE_PREFIXES_FEATURE_ID = "http://xml.org/sax/features/namespace-prefixes";

    /** Validation feature id (http://xml.org/sax/features/validation). */
    protected static final String VALIDATION_FEATURE_ID = "http://xml.org/sax/features/validation";

    /** Schema validation feature id (http://apache.org/xml/features/validation/schema). */
    protected static final String SCHEMA_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/schema";

    /** Schema full checking feature id (http://apache.org/xml/features/validation/schema-full-checking). */
    protected static final String SCHEMA_FULL_CHECKING_FEATURE_ID = "http://apache.org/xml/features/validation/schema-full-checking";

    /** Honour all schema locations feature id (http://apache.org/xml/features/honour-all-schemaLocations). */
    protected static final String HONOUR_ALL_SCHEMA_LOCATIONS_ID = "http://apache.org/xml/features/honour-all-schemaLocations";

    /** Validate schema annotations feature id (http://apache.org/xml/features/validate-annotations) */
    protected static final String VALIDATE_ANNOTATIONS_ID = "http://apache.org/xml/features/validate-annotations";

    /** Dynamic validation feature id (http://apache.org/xml/features/validation/dynamic). */
    protected static final String DYNAMIC_VALIDATION_FEATURE_ID = "http://apache.org/xml/features/validation/dynamic";

    /** XInclude feature id (http://apache.org/xml/features/xinclude). */
    protected static final String XINCLUDE_FEATURE_ID = "http://apache.org/xml/features/xinclude";

    /** XInclude fixup base URIs feature id (http://apache.org/xml/features/xinclude/fixup-base-uris). */
    protected static final String XINCLUDE_FIXUP_BASE_URIS_FEATURE_ID = "http://apache.org/xml/features/xinclude/fixup-base-uris";

    /** XInclude fixup language feature id (http://apache.org/xml/features/xinclude/fixup-language). */
    protected static final String XINCLUDE_FIXUP_LANGUAGE_FEATURE_ID = "http://apache.org/xml/features/xinclude/fixup-language";
    public static final String BEAN_ALIAS = "action.checkxml";

    public CheckXMLActionImpl() {
    }

    public void run() {
        JTextComponent jtc = DefaultXPontusWindowImpl.getInstance()
                                                     .getDocumentTabContainer()
                                                     .getCurrentEditor();
        
        
        Object contentType = jtc.getClientProperty(XPontusConstantsIF.CONTENT_TYPE) ;
        
        if(contentType == null){
            return;
        }
        
        if(!contentType.equals("text/xml")){
            XPontusComponentsUtils.showInformationMessage("The document is not an XML document \nor is not registered as one");;
            return;
        }

        ConsoleOutputWindow console = DefaultXPontusWindowImpl.getInstance()
                                                              .getConsole();

        OutputDockable odk = (OutputDockable) console.getDockableById(MessagesWindowDockable.DOCKABLE_ID);

        DocumentContainer container = (DocumentContainer) DefaultXPontusWindowImpl.getInstance()
                                                                                  .getDocumentTabContainer()
                                                                                  .getCurrentDockable();

        try {
            // read the document
            CharsetDetector d = new CharsetDetector();

            InputStream is = new BufferedInputStream(new ByteArrayInputStream(
                        jtc.getText().getBytes()));
            d.setText(is);

            Reader m_reader = d.detect().getReader();

            SAXParser parser = new SAXParser();

            parser.setFeature(SCHEMA_FULL_CHECKING_FEATURE_ID, false);
            parser.setFeature(HONOUR_ALL_SCHEMA_LOCATIONS_ID, false);
            parser.setFeature(VALIDATION_FEATURE_ID, false);
            parser.setFeature(DYNAMIC_VALIDATION_FEATURE_ID, false);
            parser.setFeature(SCHEMA_VALIDATION_FEATURE_ID, false);
            parser.setFeature(VALIDATE_ANNOTATIONS_ID, false);
            parser.setEntityResolver(NullEntityResolver.getInstance());

            parser.parse(new InputSource(m_reader));

            // close the streams
            IOUtils.closeQuietly(m_reader);
            IOUtils.closeQuietly(is);

            container.getStatusBar().setMessage("The document is well formed");
            odk.println("The document is well formed");
        } catch (Exception e) {
            StringBuffer err = new StringBuffer();

            if (e instanceof SAXParseException) {
                SAXParseException spe = (SAXParseException) e;
                err.append("Error at line:" + spe.getLineNumber() + ",column:" +
                    spe.getColumnNumber() + "\n");
            }

            container.getStatusBar().setMessage("Document not well formed");
            odk.println(err.toString() + e.getMessage(),
                OutputDockable.RED_STYLE);
        } finally {
            Toolkit.getDefaultToolkit().beep();
            console.setFocus(MessagesWindowDockable.DOCKABLE_ID);
        }
    }

    public void onNotify(DocumentContainerChangeEvent evt) {
        setEnabled(evt != null);
    }
}
