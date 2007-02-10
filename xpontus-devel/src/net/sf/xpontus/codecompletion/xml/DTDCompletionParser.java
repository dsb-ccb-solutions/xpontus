/*
 * DTDCompletionParser.java
 *
 * Created on February 7, 2007, 7:41 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.codecompletion.xml;

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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Reader;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


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
    public DTDCompletionParser(List tagList, Map nsTagListMap)
    {
        
    }
    
    public void init(List tagList, Map nsTagListMap){
         this.tagList = tagList;
        this.nsTagListMap = nsTagListMap;
    }


    // code completion parser 
    
    // il ne faut pas faire le zouavae et documetner le programme en tenant compte de la documentation 
    
    
            
            
    public void updateCompletionInfo(String uri, Reader in)
    {
        // clear at fisrt
        tagList.clear();

        //		root = null;
        try
        {
            DTDParser parser = new DTDParser(in);
            DTD dtd = parser.parse();
            Object[] obj = dtd.getItems();

            for (int i = 0; i < obj.length; i++)
            {
                if (obj[i] instanceof DTDElement)
                {
                    DTDElement element = (DTDElement) obj[i];
                    String name = element.getName();
                    DTDItem item = element.getContent();
                    boolean hasBody = true;

                    if (item instanceof DTDEmpty)
                    {
                        hasBody = false;
                    }

                    TagInfo tagInfo = new TagInfo(name, hasBody);
                    Iterator ite = element.attributes.keySet().iterator();

                    // VERIFY set child tags
                    if (item instanceof DTDSequence)
                    {
                        DTDSequence seq = (DTDSequence) item;

                        setChildTagName(tagInfo, seq.getItem());
                    }
                    else if (item instanceof DTDMixed)
                    {
                        // #PCDATA
                    }

                    while (ite.hasNext())
                    {
                        String attrName = (String) ite.next();
                        DTDAttribute attr = element.getAttribute(attrName);

                        DTDDecl decl = attr.getDecl();
                        boolean required = false;

                        if (decl == DTDDecl.REQUIRED)
                        {
                            required = true;
                        }

                        AttributeInfo attrInfo = new AttributeInfo(attrName,
                                true, AttributeInfo.NONE, required);
                        tagInfo.addAttributeInfo(attrInfo);

                        Object attrType = attr.getType();

                        if (attrType instanceof DTDEnumeration)
                        {
                            DTDEnumeration dtdEnum = (DTDEnumeration) attrType;
                            String[] items = dtdEnum.getItems();

                            for (int j = 0; j < items.length; j++)
                            {
                                attrInfo.addValue(items[j]);
                            }
                        }
                    }

                    String tagString = tagInfo.toString(); 
                    
                    tagList.add(tagString);

                    // TODO root tag is an element that was found at first.
                }
            }
        }
        catch (DTDParseException ex)
        {
            logger.error(ex.getMessage());
        }
        catch (Exception ex)
        {
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
