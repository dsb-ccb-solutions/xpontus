/*
 * DynamicIntArray.java
 *
 * Created on January 22, 2007, 6:33 PM
 *
 * To change this template, choose Tools | Template Manager
 * and open the template in the editor.
 */
package net.sf.xpontus.utils;


/**
 *
 * @author Yves Zoundi
 */
public class DynamicIntArray
{
    /**
     * The actual data.
     */
    private int[] data;

    /**
     * The number of values in the array.  Note that this is NOT the
     * capacity of the array; rather, <code>size &lt;= capacity</code>.
     */
    private int size;

    /**
     * Constructs a new array object with an initial capacity of 10.
     */
    public DynamicIntArray()
    {
        this(10);
    }

    /**
     * Constructs a new array object with a given initial capacity.
     *
     * @param initialCapacity The initial capacity.
     * @throws IllegalArgumentException If <code>initialCapacity</code> is
     *                                  negative.
     */
    public DynamicIntArray(int initialCapacity)
    {
        if (initialCapacity < 0)
        {
            throw new IllegalArgumentException("Illegal initialCapacity: " +
                initialCapacity);
        }

        data = new int[initialCapacity];
        size = 0;
    }

    /**
     * Constructs a new array object from the given int array.  The resulting
     * <code>DynamicIntArray</code> will have an initial capacity of 110%
     * the size of the array.
     *
     * @param intArray Initial data for the array object.
     * @throws NullPointerException If <code>intArray</code> is
     *                              <code>null</code>.
     */
    public DynamicIntArray(int[] intArray)
    {
        size = intArray.length;

        int capacity = (int) Math.min((size * 110L) / 100, Integer.MAX_VALUE);
        data = new int[capacity];
        System.arraycopy(intArray, 0, data, 0, size); // source, dest, length.
    }

    /**
     * Appends the specified <code>int</code> to the end of this array.
     *
     * @param value The <code>int</code> to be appended to this array.
     */
    public void add(int value)
    {
        ensureCapacity(size + 1);
        data[size++] = value;
    }

    /**
     * Inserts all <code>int</code>s in the specified array into this array
     * object at the specified location.  Shifts the <code>int</code>
     * currently at that position (if any) and any subsequent
     * <code>int</code>s to the right (adds one to their indices).
     *
     * @param index The index at which the specified integer is to be
     *              inserted.
     * @param intArray The array of <code>int</code>s to insert.
     * @throws IndexOutOfBoundsException If <code>index</code> is less than
     *         zero or greater than <code>getSize()</code>.
     * @throws NullPointerException If <code>intArray<code> is
     *         <code>null<code>.
     */
    public void add(int index, int[] intArray)
    {
        if (index > size)
        {
            throw new IndexOutOfBoundsException("Index " + index +
                ", not in range [0-" + size + "]");
        }

        int addCount = intArray.length;
        int moveCount = size - index;

        if (moveCount > 0)
        {
            System.arraycopy(data, index, data, index + addCount, moveCount);
        }

        System.arraycopy(data, index, intArray, 0, moveCount);
        size += addCount;
    }

    /**
     * Inserts the specified <code>int</code> at the specified position in
     * this array. Shifts the <code>int</code> currently at that position (if
     * any) and any subsequent <code>int</code>s to the right (adds one to
     * their indices).
     *
     * @param index The index at which the specified integer is to be
     *              inserted.
     * @param value The <code>int</code> to be inserted.
     * @throws IndexOutOfBoundsException If <code>index</code> is less than
     *         zero or greater than <code>getSize()</code>.
     */
    public void add(int index, int value)
    {
        if (index > size)
        {
            throw new IndexOutOfBoundsException("Index " + index +
                ", not in range [0-" + size + "]");
        }

        ensureCapacity(size + 1);
        System.arraycopy(data, index, data, index + 1, size - index);
        data[index] = value;
        size++;
    }

    /**
     * Removes all values from this array object.  Capacity will remain the
     * same.
     */
    public void clear()
    {
        size = 0;
    }

    /**
     * Returns whether this array contains a given integer.  This method
     * performs a linear search, so it is not optimized for performance.
     *
     * @param integer The <code>int</code> for which to search.
     * @return Whether the given integer is contained in this array.
     */
    public boolean contains(int integer)
    {
        for (int i = 0; i < size; i++)
        {
            if (data[i] == integer)
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Makes sure that this <code>DynamicIntArray</code> instance can hold
     * at least the number of elements specified.  If it can't, then the
     * capacity is increased.
     *
     * @param minCapacity The desired minimum capacity.
     */
    public void ensureCapacity(int minCapacity)
    {
        int oldCapacity = data.length;

        if (minCapacity > oldCapacity)
        {
            int[] oldData = data;

            // Ensures we don't just keep increasing capacity by some small
            // number like 1...
            int newCapacity = ((oldCapacity * 3) / 2) + 1;

            if (newCapacity < minCapacity)
            {
                newCapacity = minCapacity;
            }

            data = new int[newCapacity];
            System.arraycopy(oldData, 0, data, 0, size);
        }
    }

    /**
     * Returns the <code>int</code> at the specified position in this array
     * object.
     *
     * @param index The index of the <code>int</code> to return.
     * @return The <code>int</code> at the specified position in this array.
     * @throws IndexOutOfBoundsException If <code>index</code> is less than
     *         zero or greater than or equal to <code>getSize()</code>.
     */
    public int get(int index)
    {
        if (index >= size)
        {
            throw new IndexOutOfBoundsException("Index " + index +
                " not in valid range [" + 0 + "-" + (size - 1) + "]");
        }

        return data[index];
    }

    /**
     * Returns the number of <code>int</code>s in this array object.
     *
     * @return The number of <code>int</code>s in this array object.
     */
    public int getSize()
    {
        return size;
    }

    /**
     * Returns whether or not this array object is empty.
     *
     * @return Whether or not this array object contains no elements.
     */
    public boolean isEmpty()
    {
        return size == 0;
    }

    /**
     * Removes the <code>int</code> at the specified location from this array
     * object.
     *
     * @param index The index of the <code>int</code> to remove.
     * @throws IndexOutOfBoundsException If <code>index</code> is less than
     *         zero or greater than or equal to <code>getSize()</code>.
     */
    public void remove(int index)
    {
        if (index >= size)
        {
            throw new IndexOutOfBoundsException("Index " + index +
                " not in valid range [0-" + (size - 1) + "]");
        }

        int toMove = size - index - 1;

        if (toMove > 0)
        {
            System.arraycopy(data, index + 1, data, index, toMove);
        }

        --size;
    }

    /**
     * Removes the <code>int</code>s in the specified range from this array
     * object.
     *
     * @param fromIndex The index of the first <code>int</code> to remove.
     * @param toIndex The index AFTER the last <code>int</code> to remove.
     * @throws IndexOutOfBoundsException If either of <code>fromIndex</code>
     *         or <code>toIndex</code> is less than zero or greater than or
     *         equal to <code>getSize()</code>.
     */
    public void removeRange(int fromIndex, int toIndex)
    {
        if ((fromIndex >= size) || (toIndex > size))
        {
            throw new IndexOutOfBoundsException("Index range [" + fromIndex +
                ", " + toIndex + "] not in valid range [0-" + (size - 1) + "]");
        }

        int moveCount = size - toIndex;
        System.arraycopy(data, toIndex, data, fromIndex, moveCount);
        size -= (toIndex - fromIndex);
    }

    /**
     * Sets the <code>int</code> value at the specified position in this
     * array object.
     *
     * @param index The index of the <code>int</code> to set
     * @param value The value to set it to.
     * @throws IndexOutOfBoundsException If <code>index</code> is less than
     *            zero or greater than or equal to <code>getSize()</code>.
     */
    public void set(int index, int value)
    {
        if (index >= size)
        {
            throw new IndexOutOfBoundsException("Index " + index +
                " not in valid range [0-" + (size - 1) + "]");
        }

        data[index] = value;
    }
}
