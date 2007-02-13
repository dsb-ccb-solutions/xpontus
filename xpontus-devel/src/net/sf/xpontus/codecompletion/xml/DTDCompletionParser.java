/*
 * DTDCompletionParser.java
 *
 * Created on February 7, 2007, 7:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.codecompletion.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import org.apache.xerces.impl.dtd.DTDGrammar;
import org.apache.xerces.impl.dtd.XMLDTDLoader;
import org.apache.xerces.xni.parser.XMLInputSource;

import java.io.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 *
 * @author Yves Zoundi
 */
public class DTDCompletionParser implements ICompletionParser {
    private Log logger = LogFactory.getLog(DTDCompletionParser.class);
    private List tagList = new ArrayList();
    private Map nsTagListMap = new HashMap();

    /** Creates a new instance of DTDCompletionParser */
    public DTDCompletionParser() {
    }

    public void init(List tagList, Map nsTagListMap) {
        this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }

    public void updateCompletionInfo(String pubid, String uri, Reader in) {
        try {
            XMLDTDLoader loader = new XMLDTDLoader() {
                    public void element(java.lang.String elementName,
                        org.apache.xerces.xni.Augmentations augs)
                        throws org.apache.xerces.xni.XNIException {
                        super.element(elementName, augs);
                        tagList.add(elementName);
                    }
                };

            FileObject fo = VFS.getManager().resolveFile(uri);

            DTDGrammar grammar = (DTDGrammar) loader.loadGrammar((new XMLInputSource(
                        pubid, uri, fo.getURL().toExternalForm(), in, null)));
        } catch (Exception ex) {
            ex.printStackTrace();
            ;
        }
    }
}
