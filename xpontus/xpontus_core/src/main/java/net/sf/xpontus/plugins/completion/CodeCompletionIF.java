/*
 * CodeCompletionIF.java
 *
 * Created on 2007-08-08, 14:06:27
 *
 * Copyright (C) 2005-2008 Yves Zoundi
 *
 * This library is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published
 * by the Free Software Foundation; either version 2.1 of the License, or
 * (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.plugins.completion;

import java.util.List;

import javax.swing.text.Document;
import javax.swing.text.JTextComponent;


/**
 * Code completion interface to implement
 * @version 0.0.1
 * @author Yves Zoundi
 */
public interface CodeCompletionIF {
    /**
     *
     * @param tagCompletionName
     * @return
     */
    public List getAttributesCompletionList(String tagCompletionName);

    /**
     *
     * @param str
     * @return
     */
    public boolean isTrigger(String str);

    /**
     * @param jtc
     */
    public void setTextComponent(JTextComponent jtc);

    /**
     *
     * @return
     */
    String getMimeType();

    /**
     *
     * @return
     */
    String getFileMode();

    /**
     * Get the completion list at a given position in the document
     * @param offset The position in the document
     * @return The completion list at the given position in the document
     */
    public List getCompletionList(String trigger, int offset);

    /**
     *
     * @param doc The document being edited
     */
    public void init(final Document doc);
}
