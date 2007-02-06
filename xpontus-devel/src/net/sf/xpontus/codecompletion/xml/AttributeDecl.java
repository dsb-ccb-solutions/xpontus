package net.sf.xpontus.codecompletion.xml;

import java.util.ArrayList;

public class AttributeDecl
{
    public String name;
    public String value;
    public ArrayList values;
    public String type;
    public boolean required;

    public AttributeDecl(String name, String value, ArrayList values,
        String type, boolean required)
    {
        this.name = name;
        this.value = value;
        this.values = values;
        this.type = type;
        this.required = required;
    }

    public String toString()
    {
        StringBuffer buf = new StringBuffer("<attribute name=\"");
        buf.append(name);
        buf.append('"');

        if (value != null)
        {
            buf.append(" value=\"");
            buf.append(value);
            buf.append('"');
        }

        buf.append(" type=\"");
        buf.append(type);
        buf.append('"');

        if (required)
        {
            buf.append(" required=\"true\"");
        }

        buf.append(" />");

        return buf.toString();
    }
} //}}}