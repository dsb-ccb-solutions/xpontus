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
public class XMLAssistProcessor implements AssistProcessor
{
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
    public XMLAssistProcessor()
    {
    }

    public synchronized List getCompletionList()
    {
        if (completionParser.getClass() == DTDCompletionParser.class)
        {
            return this.tagList;
        }
        else
        {
            String nm = nsTagListMap.keySet().iterator().next().toString();

            return (List) nsTagListMap.get(nm);
        }
    }

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
    }

    public void updateAssistInfo(final String uri, final Reader r)
    {
        if ((completionInformation == null) ||
                !(completionInformation.equals(uri)))
        {
            completionInformation = uri;
            SwingUtilities.invokeLater(new Runnable()
                {
                    public void run()
                    {
                        parsingDone = false;
                        tagList.clear();
                        completionParser.init(tagList, nsTagListMap);
                        completionParser.updateCompletionInfo(uri, r);
                        parsingDone = true;
                    }
                });
        }
    }

    public boolean isTrigger(String str)
    {
        return str.equals("<") || str.equals(">");
    }
}
