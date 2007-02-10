/*
 * EnvironmentModel.java
 *
 * Created on 2 octobre 2005, 16:25
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
package net.sf.xpontus.core.utils;

import java.io.File;
import java.io.FileOutputStream;

import java.util.*;

import javax.xml.transform.*;
import javax.xml.transform.stream.StreamResult;
import net.sf.xpontus.view.XPontusWindow;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class XMLUtils {
    private String processor;
    private String encoding;
    
    public XMLUtils() {
    }
    
    /**
     *
     * @param in
     * @param out
     * @param xsl
     * @param params
     */
    public void transform(Source in, File out, Source xsl, Hashtable params) {
        TransformerFactory _factory = null;
        Transformer tf = null;
        Log logger = LogFactory.getLog(XMLUtils.class);
        try {
            _factory = TransformerFactory.newInstance();
            tf = _factory.newTransformer(xsl);
            
            if (params.size() > 0) {
                String cle;
                String val;
                
                for (Iterator it = params.keySet().iterator(); it.hasNext();
                tf.setParameter(cle, val)) {
                    cle = (String) it.next();
                    val = params.get(cle).toString();
                }
            }
            logger.info("XSL transformation");
            logger.info("Input:" + in.getSystemId());
            logger.info("Output:"+ out.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(out);
            javax.xml.transform.Result res = new StreamResult(fos);
            tf.transform(in, res);
            fos.close();
            String log = "XML/HTML Transformation finished";
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
        } catch (Exception e) {
            String log = "Error" + e.getMessage();
            XPontusWindow.getInstance().getStatusBar().setOperationMessage(log);
        }
    }
    
    /**
     *
     * @param processor
     */
    public void setProcessor(String processor) {
        this.processor = processor;
    }
    
    /**
     *
     * @param encoding
     */
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
}
