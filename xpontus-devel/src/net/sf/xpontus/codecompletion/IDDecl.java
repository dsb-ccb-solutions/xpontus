/*
IDDecl.java
:tabSize=4:indentSize=4:noTabs=true:
:folding=explicit:collapseFolds=1:

Copyright (C) 2001 Slava Pestov
Portions Copyright (C) 2002 Ian Lewis (IanLewis@member.fsf.org)

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
Optionally, you may find a copy of the GNU General Public License
from http://www.fsf.org/copyleft/gpl.txt
*/
package net.sf.xpontus.codecompletion;

import net.sf.xpontus.utils.MiscUtilities;

import java.util.Comparator;


//}}}
public class IDDecl
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

    //{{{ Compare class
    public static class Compare implements Comparator
    {
        public int compare(Object obj1, Object obj2)
        {
            IDDecl id1 = (IDDecl) obj1;
            IDDecl id2 = (IDDecl) obj2;

            return MiscUtilities.compareStrings(id1.id, id2.id, true);
        }
    } //}}}
}
