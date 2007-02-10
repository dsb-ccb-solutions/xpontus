package test.completion;

import java.util.*;

import javax.swing.AbstractListModel;

/**
 * A list model for {@link CalltipWindow}.
 *
 * @author ddjohnson
 * @version 1.0
 */
public class StringListModel extends AbstractListModel
{
	protected List strings;

	/**
	 * Constructor
	 */
	public StringListModel()
	{
		this(new ArrayList());
	}

	/**
	 * Constructor
	 *
	 * @param strings
	 */
	public StringListModel(List strings)
	{
		this.strings = strings;
	}

	/**
	 * Adds a string
	 *
	 * @param s
	 */
	public void add(String s)
	{
		int index0 = strings.size();
		strings.add(s);
		super.fireIntervalAdded(this, index0, strings.size());
	}

	/**
	 * Adds a {@link Collection} of strings.
	 *
	 * @param c
	 */
	public void addAll(Collection c)
	{
		int index0 = strings.size();
		for (Iterator iter = c.iterator(); iter.hasNext();)
		{
			Object o = iter.next();
			if (o instanceof String)
			{
				strings.add((String)o);
			}
			else
			{
				//throw something i dunno
			}
		}
		super.fireIntervalAdded(this, index0, strings.size());
	}

	/**
	 * Removes a string.
	 *
	 * @param s
	 */
	public void remove(String s)
	{
		int index0 = strings.size();
		strings.remove(s);
		super.fireIntervalRemoved(this, index0, strings.size());
	}

	/**
	 * Removes the string at a given index.
	 *
	 * @param index index to remove
	 */
	public void remove(int index)
	{
		Object o = strings.get(index);
		if (o instanceof String)
			this.remove((String)o);
	}

	/**
	 * Clears the list.
	 */
	public void clear()
	{
		strings.clear();
		super.fireContentsChanged(this, 0, 0);
	}

	public Object getElementAt(int index)
	{
		return strings.get(index);
	}

	public int getSize()
	{
		return strings.size();
	}
}
