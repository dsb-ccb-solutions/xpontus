/*
 * HtmlDocument.java -- classes to represent HTML documents as parse trees
 * Copyright (C) 1999 Quiotix Corporation.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License, version 2, as
 * published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License (http://www.gnu.org/copyleft/gpl.txt)
 * for more details.
 */
package net.sf.xpontus.plugins.lexer.html;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Represents an HTML document as a sequence of elements.  The defined
 * element types are: Tag, EndTag, TagBlock (matched tag..end tag, with the
 * intervening elements), Comment, Text, Newline, and Annotation.
 * <p/>
 * <P> The various element types are defined as nested classes within
 * HtmlDocument.
 *
 * @author Brian Goetz, Quiotix
 * @see com.quiotix.html.parser.HtmlVisitor
 */
public class HtmlDocument
{
    ElementSequence elements;

    /**
     *
     * @param s
     */
    public HtmlDocument(ElementSequence s)
    {
        elements = s;
    }

    /**
     *
     * @param v
     */
    public void accept(HtmlVisitor v)
    {
        v.visit(this);
    }

    private static String dequote(String s)
    {
        if (s == null)
        {
            return "";
        }

        if ((s.startsWith("\"") && s.endsWith("\"")) ||
                (s.startsWith("'") && s.endsWith("'")))
        {
            return s.substring(1, s.length() - 1);
        }
        else
        {
            return s;
        }
    }

    // The various elements of the HtmlDocument (Tag, EndTag, etc) are included
    // as nested subclasses largely for reasons of namespace control.
    // The following subclasses of HtmlElement exist: Tag, EndTag, Text, Comment,
    // Newline, Annotation, TagBlock.  Also, the additional classes
    // ElementSequence, Attribute, and AttributeList are defined here as well.

    // Each subclass of HtmlElement should have a visit() method in the
    // HtmlVisitor class.

    /**
     * Abstract class for HTML elements.  Enforces support for Visitors.
     */
    public static abstract class HtmlElement
    {
        public abstract void accept(HtmlVisitor v);
    }

    /**
     * HTML start tag.  Stores the tag name and a list of tag attributes.
     */
    public static class Tag extends HtmlElement
    {
        public String tagName;
        public AttributeList attributeList;
        public boolean emptyTag = false;

        /**
         *
         * @param t
         * @param a
         */
        public Tag(String t, AttributeList a)
        {
            tagName = t;
            attributeList = a;
        }

        /**
         *
         * @param b
         */
        public void setEmpty(boolean b)
        {
            emptyTag = b;
        }

        /**
         *
         * @param v
         */
        public void accept(HtmlVisitor v)
        {
            v.visit(this);
        }

        /**
         *
         * @param name
         * @return
         */
        public boolean hasAttribute(String name)
        {
            return attributeList.contains(name);
        }

        /**
         *
         * @param name
         * @return
         */
        public boolean hasAttributeValue(String name)
        {
            return attributeList.hasValue(name);
        }

        /**
         *
         * @param name
         * @return
         */
        public String getAttributeValue(String name)
        {
            return attributeList.getValue(name);
        }

        /**
         *
         * @return
         */
        public int getLength()
        {
            int length = 0;

            for (Iterator iterator = attributeList.attributes.iterator();
                    iterator.hasNext();)
            {
                Attribute attribute = (Attribute) iterator.next();
                length += (1 + (attribute.getLength()));
            }

            return length + tagName.length() + 2 + (emptyTag ? 1 : 0);
        }

        /**
         *
         * @return
         */
        public String toString()
        {
            StringBuffer s = new StringBuffer();
            s.append("<");
            s.append(tagName);

            for (Iterator iterator = attributeList.attributes.iterator();
                    iterator.hasNext();)
            {
                Attribute attribute = (Attribute) iterator.next();
                s.append(" ");
                s.append(attribute.toString());
            }

            if (emptyTag)
            {
                s.append("/");
            }

            s.append(">");

            return s.toString();
        }
    }

    /**
     * Html end tag.  Stores only the tag name.
     */
    public static class EndTag extends HtmlElement
    {
        public String tagName;

        /**
         *
         * @param t
         */
        public EndTag(String t)
        {
            tagName = t;
        }

        /**
         *
         * @param v
         */
        public void accept(HtmlVisitor v)
        {
            v.visit(this);
        }

        /**
         *
         * @return
         */
        public int getLength()
        {
            return 3 + tagName.length();
        }

        public String toString()
        {
            return "</" + tagName + ">";
        }
    }

    /**
     * A tag block is a composite structure consisting of a start tag
     * a sequence of HTML elements, and a matching end tag.
     */
    public static class TagBlock extends HtmlElement
    {
        public Tag startTag;
        public EndTag endTag;
        public ElementSequence body;

        /**
         *
         * @param name
         * @param aList
         * @param b
         */
        public TagBlock(String name, AttributeList aList, ElementSequence b)
        {
            startTag = new Tag(name, aList);
            endTag = new EndTag(name);
            body = b;
        }

        /**
         *
         * @param v
         */
        public void accept(HtmlVisitor v)
        {
            v.visit(this);
        }
    }

    /**
     * HTML comments.
     */
    public static class Comment extends HtmlElement
    {
        public String comment;

        /**
         *
         * @param c
         */
        public Comment(String c)
        {
            comment = c;
        }

        /**
         *
         * @param v
         */
        public void accept(HtmlVisitor v)
        {
            v.visit(this);
        }

        /**
         *
         * @return
         */
        public int getLength()
        {
            return 3 + comment.length();
        }

        /**
         *
         * @return
         */
        public String toString()
        {
            return "<!" + comment + ">";
        }
    }

    /**
     * Plain text
     */
    public static class Text extends HtmlElement
    {
        public String text;

        /**
         *
         * @param t
         */
        public Text(String t)
        {
            text = t;
        }

        /**
         *
         * @param v
         */
        public void accept(HtmlVisitor v)
        {
            v.visit(this);
        }

        /**
         *
         * @return
         */
        public int getLength()
        {
            return text.length();
        }

        /**
         *
         * @return
         */
        public String toString()
        {
            return text;
        }
    }

    /**
     * End of line indicator.
     */
    public static class Newline extends HtmlElement
    {
        public static final String NL = System.getProperty("line.separator");

        /**
         *
         * @param v
         */
        public void accept(HtmlVisitor v)
        {
            v.visit(this);
        }

        /**
         *
         * @return
         */
        public int getLength()
        {
            return NL.length();
        }

        /**
         *
         * @return
         */
        public String toString()
        {
            return NL;
        }
    }

    /**
     * A sequence of HTML elements.
     */
    public static class ElementSequence
    {
        private List elements;

        /**
         *
         * @param n
         */
        public ElementSequence(int n)
        {
            elements = new ArrayList(n);
        }

        public ElementSequence()
        {
            elements = new ArrayList();
        }

        /**
         *
         * @param o
         */
        public void addElement(HtmlElement o)
        {
            elements.add(o);
        }

        public int size()
        {
            return elements.size();
        }

        /**
         *
         * @return
         */
        public Iterator iterator()
        {
            return elements.iterator();
        }

        /**
         *
         * @param coll
         */
        public void setElements(List coll)
        {
            elements.clear();
            elements.addAll(coll);
        }
    }

    /**
     * Annotations.  These are not part of the HTML document, but
     * provide a way for HTML-processing applications to insert
     * annotations into the document.  These annotations can be used by
     * other programs or can be brought to the user's attention at a
     * later time.  For example, the HtmlCollector might insert an
     * annotation to indicate that there is no corresponding start tag
     * for an end tag.
     */
    public static class Annotation extends HtmlElement
    {
        String type;
        String text;

        /**
         *
         * @param type
         * @param text
         */
        public Annotation(String type, String text)
        {
            this.type = type;
            this.text = text;
        }

        /**
         *
         * @param v
         */
        public void accept(HtmlVisitor v)
        {
            v.visit(this);
        }

        /**
         *
         * @return
         */
        public int getLength()
        {
            return 14 + type.length() + text.length();
        }

        /**
         *
         * @return
         */
        public String toString()
        {
            return "<!--NOTE(" + type + ") " + text + "-->";
        }
    }

    /**
     * A Tag Attribute.
     */
    public static class Attribute
    {
        public String name;
        public String value;
        public boolean hasValue;

        /**
         *
         * @param n
         */
        public Attribute(String n)
        {
            name = n;
            hasValue = false;
        }

        /**
         *
         * @param n
         * @param v
         */
        public Attribute(String n, String v)
        {
            name = n;
            value = v;
            hasValue = true;
        }

        /**
         *
         * @return
         */
        public int getLength()
        {
            return (hasValue ? (name.length() + 1 + value.length())
                             : name.length());
        }

        /**
         *
         * @return
         */
        public String toString()
        {
            return (hasValue ? (name + "=" + value) : name);
        }
    }

    /**
     * A List of Attributes.
     */
    public static class AttributeList
    {
        public List attributes = new ArrayList();

        /**
         *
         * @param a
         */
        public void addAttribute(Attribute a)
        {
            attributes.add(a);
        }

        /**
         *
         * @param name
         * @return
         */
        public boolean contains(String name)
        {
            for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
            {
                Attribute attribute = (Attribute) iterator.next();

                if (attribute.name.equalsIgnoreCase(name))
                {
                    return true;
                }
            }

            return false;
        }

        /**
         *
         * @param name
         * @return
         */
        public boolean hasValue(String name)
        {
            for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
            {
                Attribute attribute = (Attribute) iterator.next();

                if (attribute.name.equalsIgnoreCase(name) &&
                        attribute.hasValue)
                {
                    return true;
                }
            }

            return false;
        }

        /**
         *
         * @param name
         * @return
         */
        public String getValue(String name)
        {
            for (Iterator iterator = attributes.iterator(); iterator.hasNext();)
            {
                Attribute attribute = (Attribute) iterator.next();

                if (attribute.name.equalsIgnoreCase(name) &&
                        attribute.hasValue)
                {
                    return dequote(attribute.value);
                }
            }

            return null;
        }
    }
}
