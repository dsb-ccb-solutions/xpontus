/*
 * XMLAssistProcessor.java
 *
 * Created on February 7, 2007, 7:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.codecompletion.xml;

import net.sf.xpontus.view.XPontusWindow;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.net.URL;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;


/**
 *
 * @author Yves Zoundi
 */
public class XMLAssistProcessor implements AssistProcessor {
    private Log logger = LogFactory.getLog(XMLAssistProcessor.class);
    private List tagList = new ArrayList();
    private boolean isDTDCompletion = false;
    private boolean isSchemaCompletion = false;
    private Map nsTagListMap = new HashMap();
    private String completionInformation;
    private boolean parsingDone = false;

    // interface to parse DTD or completion and store the completion information
    private ICompletionParser completionParser = new DTDCompletionParser();

    /**
     * The constructor without DTD / XSD.
     */
    public XMLAssistProcessor() {
    }

    protected TagInfo getTagInfo(String name) {
        TagInfo info = null;

        for (int i = 0; i < tagList.size(); i++) {
            info = (TagInfo) tagList.get(i);

            if (info.getTagName().equals(name)) {
                break;
            }
        }

        return info;
    }

    public synchronized List getAttributesCompletionList(String tagName) {
        List completionList = getCompletionList();

        TagInfo tagInfo = getTagInfo(tagName);

        final List emptyList = new ArrayList();

        if (tagInfo == null) {
            return emptyList;
        }

        if (tagInfo.getAttributeInfo() != null) {
            return Arrays.asList(tagInfo.getAttributeInfo());
        } else {
            return emptyList;
        }
    }

    public synchronized List getCompletionList() {
        List completionList = tagList;

        if (!isDTDCompletion) {
            completionList = (List) nsTagListMap.get(nsTagListMap.keySet()
                                                                 .iterator());
        }

        return completionList;
    }

    /**
     *
     * @param completionParser
     */
    public void setCompletionParser(ICompletionParser completionParser) {
        if (completionParser == null) {
            this.completionParser = completionParser;
        } else {
            if (!(this.completionParser.getClass() == completionParser.getClass())) {
                this.completionParser = completionParser;
            }
        }

        this.isDTDCompletion = (completionParser.getClass() == DTDCompletionParser.class);
    }

    public void updateAssistInfo(final String pubid, final String uri,
        final Reader r) {
        System.out.println("updating pubid with:" + pubid);

        if ((completionInformation == null) ||
                !(completionInformation.equals(uri))) {
            completionInformation = uri;

            Thread t = new Thread() {
                    public void run() {
                        logger.info("parsing dtd/schema...");
                        parsingDone = false;
                        tagList.clear();
                        completionParser.init(tagList, nsTagListMap);
                        completionParser.updateCompletionInfo(pubid, uri, r);
                        parsingDone = true;
                        logger.info("parsing dtd/schema is done");
                    }
                };

            t.start();
        }
    }

    public boolean isTrigger(String str) {
        return str.equals("<") || str.equals(">");
    }
}
