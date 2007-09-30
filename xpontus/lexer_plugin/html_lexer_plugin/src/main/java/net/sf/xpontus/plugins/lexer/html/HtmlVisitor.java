/*
 * HtmlVisitor.java
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

import java.util.Iterator;


/**
 * Abstract class implementing Visitor pattern for HtmlDocument objects.
 *
 * @author Brian Goetz, Quiotix
 */
public abstract class HtmlVisitor
{
    /**
     *
     * @param t
     */
    public void visit(HtmlDocument.Tag t)
    {
    }

    /**
     *
     * @param t
     */
    public void visit(HtmlDocument.EndTag t)
    {
    }

    /**
     *
     * @param c
     */
    public void visit(HtmlDocument.Comment c)
    {
    }

    /**
     *
     * @param t
     */
    public void visit(HtmlDocument.Text t)
    {
    }

    /**
     *
     * @param n
     */
    public void visit(HtmlDocument.Newline n)
    {
    }

    /**
     *
     * @param a
     */
    public void visit(HtmlDocument.Annotation a)
    {
    }

    /**
     *
     * @param bl
     */
    public void visit(HtmlDocument.TagBlock bl)
    {
        bl.startTag.accept(this);
        visit(bl.body);
        bl.endTag.accept(this);
    }

    /**
     *
     * @param s
     */
    public void visit(HtmlDocument.ElementSequence s)
    {
        for (Iterator iterator = s.iterator(); iterator.hasNext();)
        {
            HtmlDocument.HtmlElement htmlElement = (HtmlDocument.HtmlElement) iterator.next();
            htmlElement.accept(this);
        }
    }

    /**
     *
     * @param d
     */
    public void visit(HtmlDocument d)
    {
        start();
        visit(d.elements);
        finish();
    }

    public void start()
    {
    }

    public void finish()
    {
    }
}
