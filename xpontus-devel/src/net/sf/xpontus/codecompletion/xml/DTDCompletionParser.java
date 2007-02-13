/*
 * DTDCompletionParser.java
 *
 * Created on February 7, 2007, 7:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.codecompletion.xml;

import DTDDoc.ExtendedDTD;
import com.wutka.dtd.DTD;
import com.wutka.dtd.DTDAttribute;
import com.wutka.dtd.DTDChoice;
import com.wutka.dtd.DTDDecl;
import com.wutka.dtd.DTDElement;
import com.wutka.dtd.DTDEmpty;
import com.wutka.dtd.DTDEnumeration;
import com.wutka.dtd.DTDItem;
import com.wutka.dtd.DTDMixed;
import com.wutka.dtd.DTDName;
import com.wutka.dtd.DTDParseException;
import com.wutka.dtd.DTDParser;
import com.wutka.dtd.DTDSequence;
import java.io.File;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import java.io.Reader;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import net.sf.xpontus.view.XPontusWindow;
import net.sf.xpontus.view.editor.syntax.LexerInputStream;
import org.apache.commons.io.IOUtils;
import org.apache.xerces.parsers.SAXParser;
import org.exolab.castor.xml.dtd.ContentParticle;
import org.exolab.castor.xml.dtd.DTDdocument;
import org.exolab.castor.xml.dtd.Element;
import org.exolab.castor.xml.dtd.parser.CharStream;
import org.exolab.castor.xml.dtd.parser.InputCharStream;
import org.xml.sax.InputSource;
import org.xml.sax.ext.DeclHandler;


/**
 *
 * @author Yves Zoundi
 */
public class DTDCompletionParser implements ICompletionParser
{
    private Log logger = LogFactory.getLog(DTDCompletionParser.class);
    private List tagList = new ArrayList();
    private Map nsTagListMap = new HashMap();

    /** Creates a new instance of DTDCompletionParser */
    public DTDCompletionParser()
    {
        
    }
    
    public void init(List tagList, Map nsTagListMap){
         this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }


    // code completion parser 
    
    // il ne faut pas faire le zouavae et documetner le programme en tenant compte de la documentation 
    
     class CustomDeclHandler implements org.xml.sax.ext.DeclHandler {
        public void attributeDecl(java.lang.String elementName,
                java.lang.String attributeName, java.lang.String type,
                java.lang.String valueDefault, java.lang.String value) {
           
        }
        
        public void elementDecl(java.lang.String name, java.lang.String model) {
           TagInfo tagInfo = new TagInfo(name, true); 
           tagList.add(tagInfo);
        }
        
        public void externalEntityDecl(java.lang.String name,
                java.lang.String publicId, java.lang.String systemId) {
            System.out.println("EXTERNAL ENTITY: " + name + publicId +
                    systemId);
        }
        
        public void internalEntityDecl(java.lang.String name,
                java.lang.String value) {
            if(!name.startsWith("%")){
                TagInfo tagInfo = new TagInfo(name, true); 
           tagList.add(tagInfo);
            }
              
        }
    }
     
            
            
    public void updateCompletionInfo(String uri, Reader in)
    {
         
        // clear at fisrt
        tagList.clear();
         try
        {
            
             SAXParser parser = new SAXParser();
        DeclHandler handler = new CustomDeclHandler();
        parser.setProperty("http://xml.org/sax/properties/declaration-handler",  handler);
        parser.parse(new InputSource(new StringReader( XPontusWindow.getInstance().getCurrentEditor().getText())));

        //		root = null;
       
            
        }
        catch (Exception ex)
        {
            System.out.println("error parsing DTD");
            logger.error(ex.getMessage());
        }
    }

    private void setChildTagName(TagInfo tagInfo, DTDItem[] items)
    {
        for (int i = 0; i < items.length; i++)
        {
            if (items[i] instanceof DTDName)
            {
                DTDName dtdName = (DTDName) items[i];
                tagInfo.addChildTagName(dtdName.getValue());
            }
            else if (items[i] instanceof DTDChoice)
            {
                DTDChoice dtdChoise = (DTDChoice) items[i];
                setChildTagName(tagInfo, dtdChoise.getItem());
            }
        }
    }
}
