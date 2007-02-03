/*
EntityDecl.java
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


public class EntityDecl
{
    public static final int INTERNAL = 0;
    public static final int EXTERNAL = 1;
    public int type;
    public String name;
    public String value;
    public String publicId;
    public String systemId;

    //{{{ EntityDecl constructor
    public EntityDecl(int type, String name, String value)
    {
        this.type = type;
        this.name = name;
        this.value = value;
    } //}}}

    //{{{ EntityDecl constructor
    public EntityDecl(int type, String name, String publicId, String systemId)
    {
        this.type = type;
        this.name = name;
        this.publicId = publicId;
        this.systemId = systemId;
    } //}}}

    //{{{ toString() method
    public String toString()
    {
        if (type == INTERNAL)
        {
            return getClass().getName() + "[" + name + "," + value + "]";
        }
        else if (type == EXTERNAL)
        {
            return getClass().getName() + "[" + name + "," + publicId + "," +
            systemId + "]";
        }
        else
        {
            return null;
        }
    } //}}}

    //{{{ Compare class
    public static class Compare implements Comparator
    {
        public int compare(Object obj1, Object obj2)
        {
            EntityDecl entity1 = (EntityDecl) obj1;
            EntityDecl entity2 = (EntityDecl) obj2;

            if (entity1.type != entity2.type)
            {
                return entity2.type - entity1.type;
            }
            else
            {
                return MiscUtilities.compareStrings(entity1.name, entity2.name,
                    true);
            }
        }
    } //}}}
}
