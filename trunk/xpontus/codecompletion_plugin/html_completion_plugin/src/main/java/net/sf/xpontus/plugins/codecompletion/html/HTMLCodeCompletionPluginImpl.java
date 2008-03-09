/*
 *
 *
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
package net.sf.xpontus.plugins.codecompletion.html;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.parsers.TokenNode;
import net.sf.xpontus.plugins.codecompletion.xml.DTDCompletionParser;
import net.sf.xpontus.plugins.codecompletion.xml.ICompletionParser;
import net.sf.xpontus.plugins.codecompletion.xml.TagInfo;
import net.sf.xpontus.plugins.codecompletion.xml.XMLTokenWalker;
import net.sf.xpontus.plugins.codecompletion.xml.XSDCompletionParser;
import net.sf.xpontus.plugins.completion.CodeCompletionIF;
import net.sf.xpontus.syntax.SyntaxDocument;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Reader;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.Utilities;
import javax.swing.tree.DefaultMutableTreeNode;


/**
 * Code completion implementation for HTML files
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class HTMLCodeCompletionPluginImpl implements CodeCompletionIF {
    private Log logger = LogFactory.getLog(HTMLCodeCompletionPluginImpl.class);
    private List tagList = new Vector();
    private boolean isDTDCompletion = false;
    private Map nsTagListMap = new HashMap();
    private String completionInformation;
    private transient Document doc;
    private XMLTokenWalker walker = new XMLTokenWalker();

    // interface to parse DTD or completion and store the completion information
    private ICompletionParser completionParser = new DTDCompletionParser();
    private JTextComponent jtc;

    /**
     * The constructor without DTD / XSD.
     */
    public HTMLCodeCompletionPluginImpl() {
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

    public synchronized List getTagsCompletionList(String tagName) {
        System.out.println("Trying to get code completion for tag:" + tagName);

        TagInfo tagInfo = getTagInfo(tagName);

        final List emptyList = new ArrayList();

        if (tagInfo == null) {
            System.out.println("Cannot get tagInfo");

            return emptyList;
        }

        List completionListData = Arrays.asList(tagInfo.getChildTagNames());
        System.out.println("Got " + completionListData.size() +
            " completions for " + tagName);

        return completionListData;
    }

    public synchronized List getAttributesCompletionList(String tagName) {
        List completionList = tagList;

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

    public synchronized List getCompletionList(String trigger, int offset) {
        List completionList = tagList;

        SyntaxDocument syntaxDocument = (SyntaxDocument) jtc.getDocument();

        if (trigger.equals(">")) {
            completeEndTag(offset, trigger, null);

            return null;
        } else {
            // check for attributes completion
            if (trigger.equals(" ")) {
                try {
                    String t = syntaxDocument.getText(0, offset);

                    if (isInsideTag(t, offset)) {
                        int openAngle = t.lastIndexOf("<");
                        String insideTag = StringUtils.substringBefore(t.substring(openAngle +
                                    1, t.length()), " ");

                        return getAttributesCompletionList(insideTag);
                    }
                } catch (Exception err) {
                }
            }
            // check for elements completion
            else if (trigger.equals("<")) {
                int line = syntaxDocument.getDefaultRootElement()
                                         .getElementIndex(offset);
                Element element = Utilities.getParagraphElement(jtc, offset);
                int column = offset - element.getStartOffset();
                Object o = syntaxDocument.getProperty(XPontusConstantsIF.OUTLINE_INFO);

                if (o != null) {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
                    walker.setPositionInformation(syntaxDocument, offset, line,
                        column);
                    walker.walk(node);

                    TokenNode tn = walker.getNearestTokenNode();

                    if (tn != null) {
                        return getTagsCompletionList(tn.toString());
                    }
                }
            }
        }

        return null;
    }

    //{{{ isInsideTag() method
    public boolean isInsideTag(String text, int pos) {
        int start = text.lastIndexOf('<', pos);
        int end = text.lastIndexOf('>', pos);

        if (start > -1) {
            if (end == -1) {
                return true;
            } else if (end < start) {
                return true;
            } else {
                return false;
            }
        } else if (end > -1) {
            return true;
        } else {
            return false;
        }
    } //}}}

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
        if ((completionInformation == null) ||
                !(completionInformation.equals(uri))) {
            completionInformation = uri;

            Thread t = new Thread() {
                    public void run() {
                        tagList.clear();
                        completionParser.init(tagList, nsTagListMap);
                        completionParser.updateCompletionInfo(pubid, uri, r);
                    }
                };

            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        }
    }

    public boolean isTrigger(String str) {
        return str.equals("<") || str.equals(">") || str.equals(" ");
    }

    public String getMimeType() {
        return "text/html";
    }

    public String getFileMode() {
        return "HTML";
    }

    public void init(final javax.swing.text.Document doc) {
        System.out.println("Running code completion...");
        this.doc = doc;

        String dtdLocation = null;
        String schemaLocation = null;

        SyntaxDocument mDoc = (SyntaxDocument) doc;

        Object mDtd = mDoc.getProperty(XPontusConstantsIF.PARSER_DATA_DTD_COMPLETION_INFO);
        Object mXsd = mDoc.getProperty(XPontusConstantsIF.PARSER_DATA_SCHEMA_COMPLETION_INFO);

        if (mDtd != null) {
            System.out.println("Got dtd completion... from outline");
            dtdLocation = mDtd.toString();
        }

        if (mXsd != null) {
            schemaLocation = mXsd.toString();
        }

        Object o = doc.getProperty("BUILTIN_COMPLETION");

        if (o != null) {
            if (o.equals("HTML")) {
                dtdLocation = getClass().getResource("xhtml.dtd")
                                  .toExternalForm();
            }
        }

        try {
            if (dtdLocation != null) {
                logger.info("Using dtd location to build completion database");
                setCompletionParser(new DTDCompletionParser());

                java.net.URL url = new java.net.URL(dtdLocation);
                java.io.Reader dtdReader = new java.io.InputStreamReader(url.openStream());

                updateAssistInfo(null, dtdLocation, dtdReader);
            } else if (schemaLocation != null) {
                logger.info(
                    "Using schema location to build completion database");
                setCompletionParser(new XSDCompletionParser());

                java.net.URL url = new java.net.URL(schemaLocation);
                java.io.Reader dtdReader = new java.io.InputStreamReader(url.openStream());
                updateAssistInfo(null, schemaLocation, dtdReader);
            }
        } catch (Exception err) {
            if (err instanceof java.net.UnknownHostException) {
            } else {
                logger.fatal(err.getMessage());
            }
        }
    }

    public void completeEndTag(int off, String str, AttributeSet set) {
        final String insertString = new String(str);

        final Document doc = jtc.getDocument();

        int dot = jtc.getCaret().getDot();

        String endTag = new String(str);

        String text = null;

        try {
            text = doc.getText(0, off);
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }

        int startTag = text.lastIndexOf('<', off);
        int prefEndTag = text.lastIndexOf('>', off);

        // If there was a start tag and if the start tag is not empty
        // and
        // if the start-tag has not got an end-tag already.
        if ((startTag > 0) && (startTag > prefEndTag) &&
                (startTag < (text.length() - 1))) {
            String tag = text.substring(startTag, text.length());
            char first = tag.charAt(1);

            if ((first != '/') && (first != '!') && (first != '?') &&
                    !Character.isWhitespace(first)) {
                boolean finished = false;
                char previous = tag.charAt(tag.length() - 1);

                if ((previous != '/') && (previous != '-')) {
                    endTag += ("</");

                    for (int i = 1; (i < tag.length()) && !finished; i++) {
                        char ch = tag.charAt(i);

                        if (!Character.isWhitespace(ch)) {
                            endTag += (ch);
                        } else {
                            finished = true;
                        }
                    }

                    //                    endTag += (">");
                }
            }
        }

        str = endTag;

        try {
            if (str.equals(">")) {
                return;
            }

            doc.insertString(off, str, set);
            jtc.getCaret().setDot(dot);
        } catch (Exception ex) {
        }
    }

    // Tries to find out if the line finishes with an element start
    private boolean isStartElement(String line) {
        boolean result = false;

        int first = line.lastIndexOf("<");
        int last = line.lastIndexOf(">");

        if (last < first) { // In the Tag
            result = true;
        } else {
            int firstEnd = line.lastIndexOf("</");
            int lastEnd = line.lastIndexOf("/>");

            // Last Tag is not an End Tag
            if ((firstEnd != first) && ((lastEnd + 1) != last)) {
                result = true;
            }
        }

        return result;
    }

    public void setTextComponent(JTextComponent jtc) {
        this.jtc = jtc;
    }
}
