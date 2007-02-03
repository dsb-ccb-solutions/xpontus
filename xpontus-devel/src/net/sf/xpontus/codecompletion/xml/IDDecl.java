/*
 * IDDecl.java
 *
 * Created on February 2, 2007, 8:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.codecompletion.xml;

import net.sf.xpontus.utils.MiscUtilities;

import java.util.Comparator;


/**
 *
 * @author Yves Zoundi
 */
public class IDDecl implements Comparator
{
    public String uri;
    public String id;
    public String element;
    public int line;
    public int column;

    //{{{ IDDecl constructor
    public IDDecl(String uri, String id, String element, int line, int column)
    {
        this.uri = uri;
        this.id = id;
        this.element = element;
        this.line = line;
        this.column = column;
    } //}}}

    //{{{ toString() method
    public String toString()
    {
        return id + " [element: <" + element + ">]";
    } //}}}

    public int compare(Object obj1, Object obj2)
    {
        IDDecl id1 = (IDDecl) obj1;
        IDDecl id2 = (IDDecl) obj2;

        return MiscUtilities.compareStrings(id1.id, id2.id, true);
    }
}
