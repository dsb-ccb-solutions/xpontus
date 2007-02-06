/*
 * EntityDecl.java
 *
 * Created on February 2, 2007, 8:58 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */

package net.sf.xpontus.codecompletion.xml;

import java.util.Comparator;

import net.sf.xpontus.utils.MiscUtilities;

/**
 *
 * @author Yves Zoundi
 */
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
		if(type == INTERNAL)
			return getClass().getName() + "[" + name + "," + value + "]";
		else if(type == EXTERNAL)
			return getClass().getName() + "[" + name
				+ "," + publicId + "," + systemId + "]";
		else
			return null;
	} //}}}

	//{{{ Compare class
	/*
	 * Not sure this class is even needed ...
	 */
	public static class Compare implements Comparator
	{
		public int compare(Object obj1, Object obj2)
		{
			EntityDecl entity1 = (EntityDecl)obj1;
			EntityDecl entity2 = (EntityDecl)obj2;

			if(entity1.type != entity2.type)
				return entity2.type - entity1.type;
			else
			{
				return MiscUtilities.compareStrings(
					entity1.name,
					entity2.name,true);
			}
		}
	} //}}}
}

