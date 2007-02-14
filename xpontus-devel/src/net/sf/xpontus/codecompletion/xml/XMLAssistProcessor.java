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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.SwingUtilities;
import net.sf.xpontus.view.XPontusWindow;


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
        List completionList = tagList;

        if (!isDTDCompletion)
        {
            completionList = (List) nsTagListMap.get(nsTagListMap.keySet()
                                                                 .iterator());
        }
           
        Collections.sort(completionList);
        return completionList; 
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

        this.isDTDCompletion = (completionParser.getClass() == DTDCompletionParser.class);
    }

    public void updateAssistInfo(final String pubid, final String uri, final Reader r)
    {
        if ((completionInformation == null) ||
                !(completionInformation.equals(uri)))
        {
            completionInformation = uri;
 
            Thread t = new Thread(){ 
                    public void run()
                    {
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

    public boolean isTrigger(String str)
    {
        return str.equals("<") || str.equals(">");
    }
}
