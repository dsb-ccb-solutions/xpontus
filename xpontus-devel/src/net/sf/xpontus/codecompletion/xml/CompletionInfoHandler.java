package net.sf.xpontus.codecompletion.xml;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class CompletionInfoHandler extends DefaultHandler
{
    //{{{ Private Members
    private CompletionInfo m_m_info;
    private Locator m_m_loc;
    private ElementDecl m_m_element;

    //{{{ CompletionInfoHandler constructor
    public CompletionInfoHandler()
    {
        m_m_info = new CompletionInfo();
        m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "lt", "<"));
        m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "gt", ">"));
        m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "amp", "&"));
        m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "quot", "\""));
        m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL, "apos", "'"));
    } //}}}

    //{{{ getCompletionInfo() method
    public CompletionInfo getCompletionInfo()
    {
        return m_m_info;
    } //}}}

    //{{{ setDocumentLocator() method
    public void setDocumentLocator(Locator loc)
    {
        m_m_loc = loc;
    } //}}}

    //{{{ resolveEntity() method
    /*
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException {
        try {
            return CatalogManager.resolve(loc.getSystemId(),publicId,systemId);
        } catch(Exception e) {
            throw new SAXException(e);
        }
    }*/

    //}}}

    //{{{ startElement() method
    public void startElement(String namespaceURI, String sName, // simple name
        String qName, // qualified name
        Attributes attrs) throws SAXException
    {
        if (sName.equals("dtd"))
        {
            /*String extend = attrs.getValue("extend");
            
            if (extend != null) {
                String infoURI = jsXe.getProperty("mode."+extend+".xml.completion-info");
                if (infoURI != null) {
                    CompletionInfo extendInfo = CompletionInfo.getCompletionInfoFromResource(infoURI);
                    if (extendInfo != null)
                        m_m_completionInfo = (CompletionInfo)extendInfo.clone();
                }
            }*/
        }
        else if (sName.equals("entity"))
        {
            m_m_info.addEntity(new EntityDecl(EntityDecl.INTERNAL,
                    attrs.getValue("name"), attrs.getValue("value")));
        }
        else if (sName.equals("element"))
        {
            m_m_element = new ElementDecl(m_m_info, attrs.getValue("name"),
                    attrs.getValue("content"));

            m_m_info.addElement(m_m_element);

            if ("true".equals(attrs.getValue("anywhere")))
            {
                m_m_info.elementsAllowedAnywhere.add(m_m_element);
            }
        }
        else if (sName.equals("attribute"))
        {
            String name = attrs.getValue("name");
            String value = attrs.getValue("value");
            String type = attrs.getValue("type");

            ArrayList values;

            if (type.startsWith("("))
            {
                values = new ArrayList();

                StringTokenizer st = new StringTokenizer(type.substring(1,
                            type.length() - 1), "|");

                while (st.hasMoreTokens())
                {
                    values.add(st.nextToken());
                }
            }
            else
            {
                values = null;
            }

            boolean required = "true".equals(attrs.getValue("required"));

            m_m_element.addAttribute(new AttributeDecl(name,
                    value, values, type, required));
        }
    } //}}}

    //}}}
}