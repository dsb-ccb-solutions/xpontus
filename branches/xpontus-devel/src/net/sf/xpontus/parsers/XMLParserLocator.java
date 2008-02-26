package net.sf.xpontus.parsers;

import org.xml.sax.Locator;


public class XMLParserLocator implements Locator
{
    private int colNumber;
    private int rowNumber;

    /**
     * @param colNumber The colNumber to set.
     */
    public void setColNumber(int colNumber)
    {
        this.colNumber = colNumber;
    }

    /**
     * @param rowNumber The rowNumber to set.
     */
    public void setRowNumber(int rowNumber)
    {
        this.rowNumber = rowNumber;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Locator#getColumnNumber()
     */
    public int getColumnNumber()
    {
        return this.colNumber;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Locator#getLineNumber()
     */
    public int getLineNumber()
    {
        return this.rowNumber;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Locator#getPublicId()
     */
    public String getPublicId()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* (non-Javadoc)
     * @see org.xml.sax.Locator#getSystemId()
     */
    public String getSystemId()
    {
        // TODO Auto-generated method stub
        return null;
    }
}
