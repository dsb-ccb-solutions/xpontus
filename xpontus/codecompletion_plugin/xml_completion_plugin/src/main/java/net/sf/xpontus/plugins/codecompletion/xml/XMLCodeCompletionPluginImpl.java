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
import net.sf.xpontus.parsers.TokenNode;
import net.sf.xpontus.plugins.completion.CodeCompletionIF;
import net.sf.xpontus.syntax.SyntaxDocument;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs.FileObject;

import java.io.File;
import java.io.Reader;

import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
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
 * Code completion plugin for XML files
 *
 * @author Yves Zoundi
 * @version 0.0.1
 */
public class XMLCodeCompletionPluginImpl implements CodeCompletionIF
{
    private Log logger = LogFactory.getLog(XMLCodeCompletionPluginImpl.class);
    private List tagList = new Vector();
    private boolean isDTDCompletion = false;
    private Map nsTagListMap = new HashMap();
    private String completionInformation;
    private transient Document doc;
    private XMLTokenWalker walker = new XMLTokenWalker();

    // interface to parse DTD or completion and store the completion information
    private ICompletionParser completionParser = new DTDCompletionParser();
    private JTextComponent editor;

    /**
     * The constructor without DTD / XSD.
     */
    public XMLCodeCompletionPluginImpl()
    {
    }

    protected TagInfo getTagInfo(String name)
    {
        TagInfo info = null;

        List li = null;

        boolean isSchemaCompletion = false;

        if (nsTagListMap.size() > 0)
        {
            isSchemaCompletion = true;

            Object defaultNS = nsTagListMap.keySet().iterator().next();

            // check for a namespace prefix
            if (name.indexOf(":") != -1)
            {
                String namespace = StringUtils.substringBefore(name, ":");
                Iterator it = nsTagListMap.keySet().iterator();

                // loop to find the completion list for the target prefix
                while (it.hasNext())
                {
                    Object o = it.next();

                    if (o != null)
                    {
                        if (o.toString().equals(namespace))
                        {
                            li = (List) nsTagListMap.get(o);
                        }
                    }
                }

                if (li == null)
                {
                    li = (List) nsTagListMap.get(defaultNS);
                }
            }
            else
            {
                li = (List) nsTagListMap.get(defaultNS);
            }
        }
        else
        {
            li = tagList;
        }

        if (isSchemaCompletion)
        {
            if (name.indexOf(":") != -1)
            {
                name = StringUtils.substringAfterLast(name, ":");
            }
        }

        if (li != null)
        {
            for (int i = 0; i < li.size(); i++)
            {
                info = (TagInfo) li.get(i);

                if (info.getTagName().equals(name))
                {
                    break;
                }
            }
        }

        return info;
    }

    public synchronized List getTagsCompletionList(String tagName)
    {
        TagInfo tagInfo = getTagInfo(tagName);

        if (tagInfo == null)
        {
            return Collections.EMPTY_LIST;
        }

        return tagInfo.getChildTagNames();
    }

    public synchronized List getAttributesCompletionList(String tagName)
    {
        TagInfo tagInfo = getTagInfo(tagName);

        if (tagInfo == null)
        {
            return Collections.EMPTY_LIST;
        }

        if (tagInfo.getAttributeInfo() != null)
        {
            return tagInfo.getAttributeInfo();
        }
        else
        {
            return Collections.EMPTY_LIST;
        }
    }

    public synchronized List getCompletionList(String trigger, int offset)
    {
        List completionList = tagList;

        SyntaxDocument syntaxDocument = (SyntaxDocument) editor.getDocument();

        if (trigger.equals(">"))
        {
            completeEndTag(offset, trigger, null);

            return null;
        }
        else
        {
            // check for attributes completion
            if (trigger.equals(" "))
            {
                try
                {
                    String t = syntaxDocument.getText(0, offset);

                    if (isInsideTag(t, offset))
                    {
                        int openAngle = t.lastIndexOf("<");
                        String insideTag = StringUtils.substringBefore(t.substring(openAngle +
                                    1, t.length()), " ");

                        return getAttributesCompletionList(insideTag);
                    }
                }
                catch (Exception err)
                {
                }
            } // check for elements completion
            else if (trigger.equals("<"))
            {
                int line = syntaxDocument.getDefaultRootElement()
                                         .getElementIndex(offset);
                Element element = Utilities.getParagraphElement(editor, offset);
                int column = offset - element.getStartOffset();
                Object o = syntaxDocument.getProperty(XPontusConstantsIF.OUTLINE_INFO);

                if (o != null)
                {
                    DefaultMutableTreeNode node = (DefaultMutableTreeNode) o;
                    walker.setPositionInformation(syntaxDocument, offset, line,
                        column);
                    walker.walk(node);

                    TokenNode tn = walker.getNearestTokenNode();

                    if (tn != null)
                    {
                        return getTagsCompletionList(tn.toString());
                    }
                    else
                    {
                        return tagList;
                    }
                }
            }
        }

        return null;
    }

    //{{{ isInsideTag() method
    public boolean isInsideTag(String text, int pos)
    {
        int start = text.lastIndexOf('<', pos);
        int end = text.lastIndexOf('>', pos);

        if (start > -1)
        {
            if (end == -1)
            {
                return true;
            }
            else if (end < start)
            {
                return true;
            }
            else
            {
                return false;
            }
        }
        else if (end > -1)
        {
            return true;
        }
        else
        {
            return false;
        }
    } //}}}

    /**
     *
     * @param completionParser
     */
    public void setCompletionParser(ICompletionParser completionParser)
    {
        if (completionParser == null)
        {
            this.completionParser = completionParser;
        }
        else
        {
            if (!(this.completionParser.getClass() == completionParser.getClass()))
            {
                this.completionParser = completionParser;
            }
        }

        this.isDTDCompletion = (completionParser.getClass() == DTDCompletionParser.class);
    }

    public void updateAssistInfo(final String pubid, final String uri,
        final Reader r)
    {
        if ((completionInformation == null) ||
                !(completionInformation.equals(uri)))
        {
            completionInformation = uri;

            Thread t = new Thread()
                {
                    public void run()
                    {
                        tagList.clear();
                        completionParser.init(tagList, nsTagListMap);
                        completionParser.updateCompletionInfo(pubid, uri, r);
                    }
                };

            t.setPriority(Thread.MIN_PRIORITY);
            t.start();
        }
    }

    public boolean isTrigger(String str)
    {
        return str.equals("<") || str.equals(">") || str.equals(" ");
    }

    public String getMimeType()
    {
        return "text/xml";
    }

    public String getFileMode()
    {
        return "xml";
    }

    public void init(final javax.swing.text.Document doc)
    {
        this.doc = doc;

        String dtdLocation = null;
        String schemaLocation = null;

        SyntaxDocument mDoc = (SyntaxDocument) doc;

        Object mDtd = mDoc.getProperty(XPontusConstantsIF.PARSER_DATA_DTD_COMPLETION_INFO);
        Object mXsd = mDoc.getProperty(XPontusConstantsIF.PARSER_DATA_SCHEMA_COMPLETION_INFO);
        Object fileObjectProperty = editor.getClientProperty(XPontusConstantsIF.FILE_OBJECT);
        File baseDir = null;

        if (fileObjectProperty != null)
        {
            FileObject fo = (FileObject) fileObjectProperty;

            try
            {
                String filePath = fo.getURL().getFile(); 

                File file = new File(filePath);

                if (file.exists())
                {
                    baseDir = file.getParentFile(); 
                }
            }
            catch (Exception ex)
            {
                ex.printStackTrace();
            }
        }

        if (mDtd != null)
        {
            dtdLocation = mDtd.toString();

            if (dtdLocation.startsWith("file:"))
            {
                File fDtdLocation = new File(dtdLocation);
                String fName = fDtdLocation.getName();

                if (baseDir != null)
                {
                    File tmpLocation = new File(baseDir, fName);

                    if (tmpLocation.exists())
                    {
                        dtdLocation = tmpLocation.getAbsolutePath();
                    }
                }
            }
        }

        if (mXsd != null)
        {
            schemaLocation = mXsd.toString();

            if (schemaLocation.startsWith("file:"))
            {
                File fSchemaLocation = new File(schemaLocation);
                String fName = fSchemaLocation.getName();

                if (baseDir != null)
                {
                    File tmpLocation = new File(baseDir, fName);

                    if (tmpLocation.exists())
                    {
                        schemaLocation = tmpLocation.getAbsolutePath();
                    }
                }
            }
        }

        Object o = doc.getProperty("BUILTIN_COMPLETION");

        if (o != null)
        {
            if (o.equals("XSL"))
            {
                dtdLocation = getClass().getResource("xslt.dtd").toExternalForm();
            }
            else if (o.equals("XSD"))
            {
                schemaLocation = getClass().getResource("xsd.xsd")
                                     .toExternalForm();
            }
            else if (o.equals("RELAXNG"))
            {
                dtdLocation = getClass().getResource("relaxng.dtd")
                                  .toExternalForm();
            }
        }

        try
        {
            if (dtdLocation != null)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("Using dtd to build completion database");
                }

                setCompletionParser(new DTDCompletionParser());

                updateAssistInfo(null, dtdLocation, null);
            }
            else if (schemaLocation != null)
            {
                if (logger.isDebugEnabled())
                {
                    logger.debug("Using schema to build completion database");
                }

                setCompletionParser(new XSDCompletionParser());
                updateAssistInfo(null, schemaLocation, null);
            }
        }
        catch (Exception err)
        {
            logger.fatal(err, err);
        }
    }

    public void completeEndTag(int off, String str, AttributeSet set)
    {
        final Document doc = editor.getDocument();

        int dot = editor.getCaret().getDot();

        String endTag = new String(str);

        String text = null;

        try
        {
            text = doc.getText(0, off);
        }
        catch (BadLocationException ex)
        {
            ex.printStackTrace();
        }

        int startTag = text.lastIndexOf('<', off);
        int prefEndTag = text.lastIndexOf('>', off);

        // If there was a start tag and if the start tag is not empty
        // and
        // if the start-tag has not got an end-tag already.
        if ((startTag > 0) && (startTag > prefEndTag) &&
                (startTag < (text.length() - 1)))
        {
            String tag = text.substring(startTag, text.length());
            char first = tag.charAt(1);

            if ((first != '/') && (first != '!') && (first != '?') &&
                    !Character.isWhitespace(first))
            {
                boolean finished = false;
                char previous = tag.charAt(tag.length() - 1);

                if ((previous != '/') && (previous != '-'))
                {
                    endTag += ("</");

                    for (int i = 1; (i < tag.length()) && !finished; i++)
                    {
                        char ch = tag.charAt(i);

                        if (!Character.isWhitespace(ch))
                        {
                            endTag += (ch);
                        }
                        else
                        {
                            finished = true;
                        }
                    }
                }
            }
        }

        str = endTag;

        try
        {
            if (str.equals(">"))
            {
                return;
            }

            doc.insertString(off, str, set);
            editor.getCaret().setDot(dot);
        }
        catch (Exception ex)
        {
        }
    }

    public void setTextComponent(JTextComponent editor)
    {
        this.editor = editor;
    }
}
