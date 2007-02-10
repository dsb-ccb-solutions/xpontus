/*
 * XMLAssistProcessor.java
 *
 * Created on February 7, 2007, 7:34 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.codecompletion.xml;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.SwingUtilities;


/**
 *
 * @author Yves Zoundi
 */
public class XMLAssistProcessor
{
    private Log logger = LogFactory.getLog(XMLAssistProcessor.class);
    private List tagList = new ArrayList();
    private boolean isDTDCompletion = false;
    private boolean isSchemaCompletion = false;
    private Map nsTagListMap = new HashMap();
    private boolean parsingDone = false;

    // interface to parse DTD or completion and store the completion information
    private ICompletionParser completionParser;

    /**
     * The constructor without DTD / XSD.
     */
    public XMLAssistProcessor()
    {
        try
        {
            URL url = new URL(
                    "http://www.springframework.org/dtd/spring-beans.dtd");
            InputStream in = url.openStream();
            this.completionParser = new DTDCompletionParser(this.tagList,
                    this.nsTagListMap);

            Reader r = new InputStreamReader(in);
            updateAssistInfo(null, r);
        }
        catch (Exception e)
        {
            logger.error(e.getMessage());
        }
    }

    public List getTagList()
    {
        return this.tagList;
    }

    /**
     *
     * @param completionParser
     */
    public void setCompletionParser(ICompletionParser completionParser)
    {
        this.completionParser = completionParser;
    }

    /**
     *
     * @param uri
     * @param r
     */
    public void updateAssistInfoRunnable(final String uri, final Reader r)
    {
        parsingDone = false;
        tagList.clear();
        completionParser.init(this.tagList, this.nsTagListMap);
        completionParser.updateCompletionInfo(uri, r);
        parsingDone = true;
    }

    public void updateAssistInfo(final String uri, final Reader r)
    {
         SwingUtilities.invokeLater(new Runnable()
            {
                public void run()
                {
                    updateAssistInfoRunnable(uri, r);
                }
            });
            
       
    }

    public boolean isTrigger(String str) {
        return str.equals("<") || str.equals(">");
    }
}
