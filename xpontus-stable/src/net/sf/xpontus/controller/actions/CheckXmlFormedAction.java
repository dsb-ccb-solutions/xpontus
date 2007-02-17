/*
 * CheckXmlFormedAction.java
 *
 * Created on 2 octobre 2005, 16:59
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

import com.ibm.icu.text.CharsetDetector;
import net.sf.xpontus.core.controller.actions.ThreadedAction;
import net.sf.xpontus.utils.MsgUtils;
import net.sf.xpontus.view.XPontusWindow;
import org.apache.xerces.parsers.SAXParser;
import org.springframework.context.MessageSource;
import org.springframework.context.MessageSourceAware;
import org.xml.sax.InputSource;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Locale;
import net.sf.xpontus.utils.EncodingHelper;
import org.xml.sax.SAXParseException;


/**
 * Action to check if the xml is well-formed
 * @author Yves Zoundi
 */
public class CheckXmlFormedAction extends ThreadedAction
    implements MessageSourceAware {
    private MessageSource messageSource; 

    /** Creates a new instance of CheckXmlFormedAction */
    public CheckXmlFormedAction() {
    }
 

    /**
     * @see net.sf.xpontus.core.controller.actions#execute()
     */
    public void execute() {
        String message;

        try {
            message = messageSource.getMessage("msg.checkDocumentForm", null,
                    Locale.ENGLISH);

            XPontusWindow.getInstance().append("\n" + message);

            XPontusWindow.getInstance().getStatusBar()
                         .setOperationMessage(message);

            SAXParser parser = new SAXParser();
            parser.setFeature("http://xml.org/sax/features/validation", false);

            javax.swing.JEditorPane edit = XPontusWindow.getInstance()
                                                        .getCurrentEditor();
            byte[] bt = edit.getText().getBytes();
            java.io.InputStream is = new java.io.ByteArrayInputStream(bt);
            parser.parse(new InputSource(EncodingHelper.getReader(is)));

            message = messageSource.getMessage("msg.documentWellFormed", null,
                    Locale.ENGLISH);

            XPontusWindow.getInstance().append(message);
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage(message);
        } catch (Exception e) {
            XPontusWindow.getInstance().getStatusBar()
                         .setNotificationMessage("Error see messages window!");
            StringBuffer errorMsg = new StringBuffer();
            if(e instanceof SAXParseException){
                SAXParseException spe = (SAXParseException)e;
                errorMsg.append("Error, line:" + spe.getLineNumber() + ",column:" + spe.getColumnNumber() + "\n");
            }
            
            XPontusWindow.getInstance().append(errorMsg.toString() + e.getMessage());
        }
    }

    /**
     *
     * @param messageSource
     */
    public void setMessageSource(MessageSource messageSource) {
        this.messageSource = messageSource;
    }
}
