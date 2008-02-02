/*
 * XMLCodeCompletionPluginImpl.java
 *
 * Created on 2007-09-22, 20:46:05
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
 */
package net.sf.xpontus.plugins.codecompletion.xml;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.modules.gui.components.DefaultXPontusWindowImpl;
import net.sf.xpontus.modules.gui.components.OutlineViewDockable;
import net.sf.xpontus.parsers.*;
import net.sf.xpontus.plugins.completion.CodeCompletionIF;
import net.sf.xpontus.syntax.SyntaxDocument;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Reader;
import java.io.StringReader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.SwingUtilities;


/**
 * Code completion plugin for XML files
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 * @version 0.0.1
 */
public class XMLCodeCompletionPluginImpl implements CodeCompletionIF {
    private Log logger = LogFactory.getLog(XMLCodeCompletionPluginImpl.class);
    private List tagList = new Vector();
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
    public XMLCodeCompletionPluginImpl() {
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

        System.out.println("map size:" + nsTagListMap.size());

        if (!isDTDCompletion) {
            if ((completionList == null) || (completionList.size() == 0)) {
                int taille = nsTagListMap.keySet().size();

                if (taille > 0) {
                    //                    completionList = new Vector((List)nsTagListMap.values().iterator().next());
                    List m = (List) nsTagListMap.values().iterator().next();
                    completionList = m;
                }
            } else {
                completionList = (List) nsTagListMap.get(nsTagListMap.keySet()
                                                                     .iterator());
            }
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

            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        } else {
            System.out.println("The completion database is up to date");
        }
    }

    public boolean isTrigger(String str) {
        return str.equals("<") || str.equals(">");
    }

    public String getMimeType() {
        return "text/xml";
    }

    public String getFileMode() {
        return "xml";
    }

    public void init(final javax.swing.text.Document doc) {
        String dtdLocation = null;
        XMLLexer lexer = null;
        XMLParser parser = null;
        String schemaLocation = null;

        SyntaxDocument mDoc = (SyntaxDocument) doc;

        try {
            String mText = doc.getText(0, doc.getLength());
            Reader mReader = new StringReader(mText);
            lexer = new XMLLexer(mReader);
            parser = new XMLParser(lexer);
            parser.parse();

            mDoc.putProperty(XPontusConstantsIF.OUTLINE_INFO,
                parser.getRootNode());

            final XMLParser xp = parser;
            final OutlineViewDockable outline = DefaultXPontusWindowImpl.getInstance()
                                                                        .getOutline();
            SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        outline.updateAll(xp.getRootNode());
                    }
                });
        } catch (Exception err) {
        }

        if (lexer != null) {
            dtdLocation = lexer.getDTDLocation();
            schemaLocation = lexer.getSchemaLocation();

            System.out.println("dtd:" + dtdLocation + ",schemaLocation:" +
                schemaLocation);
        }

        try {
            if (dtdLocation != null) {
                dtdLocation = dtdLocation.substring(1, dtdLocation.length() -
                        1);
                setCompletionParser(new DTDCompletionParser());

                java.net.URL url = new java.net.URL(dtdLocation);
                java.io.Reader dtdReader = new java.io.InputStreamReader(url.openStream());

                updateAssistInfo(lexer.getdtdPublicId(), dtdLocation, dtdReader);
                System.out.println("parsing dtd");
            } else if (schemaLocation != null) {
                System.out.println("parsing schema");
                setCompletionParser(new XSDCompletionParser());

                java.net.URL url = new java.net.URL(schemaLocation);
                java.io.Reader dtdReader = new java.io.InputStreamReader(url.openStream());
                updateAssistInfo(lexer.getdtdPublicId(), schemaLocation,
                    dtdReader);
            }
        } catch (Exception err) {
            if (err instanceof java.net.UnknownHostException) {
                logger.warn("Unable to resolve remote DTD location");
            } else {
                logger.fatal(err.getMessage());
            }
        }
    }
}
