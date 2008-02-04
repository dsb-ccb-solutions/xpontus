package net.sf.xpontus.parsers;

import org.xml.sax.Attributes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AttributesImpl implements Attributes {
    private List attributes = new ArrayList();
    private HashMap att2Vals = new HashMap();

    public void addAttribute(String name, String string) {
        this.attributes.add(name);
        this.att2Vals.put(name, string);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getIndex(java.lang.String, java.lang.String)
     */
    public int getIndex(String uri, String localName) {
        String att = uri + localName;

        for (int i = 0, ll = attributes.size(); i < ll; i++) {
            String latt = (String) attributes.get(i);

            if (latt.equals(att)) {
                return i;
            }
        }

        return -1;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getIndex(java.lang.String)
     */
    public int getIndex(String qName) {
        for (int i = 0, ll = attributes.size(); i < ll; i++) {
            String latt = (String) attributes.get(i);

            if (latt.equals(qName)) {
                return i;
            }
        }

        return -1;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getLength()
     */
    public int getLength() {
        return this.attributes.size();
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getLocalName(int)
     */
    public String getLocalName(int index) {
        return (String) this.attributes.get(index);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getQName(int)
     */
    public String getQName(int index) {
        return (String) this.attributes.get(index);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getType(int)
     */
    public String getType(int index) {
        return "CDATA";
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getType(java.lang.String, java.lang.String)
     */
    public String getType(String uri, String localName) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getType(java.lang.String)
     */
    public String getType(String qName) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getURI(int)
     */
    public String getURI(int index) {
        return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getValue(int)
     */
    public String getValue(int index) {
        String name = (String) this.attributes.get(index);

        return (String) this.att2Vals.get(name);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getValue(java.lang.String, java.lang.String)
     */
    public String getValue(String uri, String localName) {
        return (String) this.att2Vals.get(localName);
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Attributes#getValue(java.lang.String)
     */
    public String getValue(String qName) {
        return (String) this.att2Vals.get(qName);
    }

    public boolean hasAttribute(String qname) {
        return this.att2Vals.containsKey(qname);
    }
}
