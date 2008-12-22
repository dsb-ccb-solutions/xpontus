/**
 * SyntaxEditorkit.java
 *
 * Created on 4-Aug-2007, 10:18:31 AM
 *
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
 *
 */
package net.sf.xpontus.syntax;

import net.sf.xpontus.modules.gui.components.XPontusCaret;

import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;


/**
 * Class SyntaxEditorkit ...
 *
 * @author Yves Zoundi
 * Created on Apr 5, 2008
 */
public class SyntaxEditorkit extends DefaultEditorKit implements ViewFactory
{
    private static final long serialVersionUID = -8774638508566771288L;
    private Document doc;
    private SyntaxSupport syntaxSupport;

    /**
     *
     * @param editor
     * @param mode
     *
     */
    public SyntaxEditorkit(JTextComponent editor, String mode)
    {
        editor.setCaretPosition(0);
        editor.setCaret(new XPontusCaret());
        this.syntaxSupport = SyntaxSupportFactory.getSyntax(mode);
        doc = new SyntaxDocument(editor, syntaxSupport);
        ((SyntaxDocument) doc).setLoading(true);
        editor.setDocument(doc);
    }

    /**
     * Returns an instance of a view factory that can be used for creating
     * views from elements. This implementation returns the current
     * instance, because this class already implements
     * <code>ViewFactory</code>.
     * @return
     */
    public ViewFactory getViewFactory()
    {
        return this;
    }

    /**
     * Creates a view from an element that can be used for painting that
     * element. This implementation returns a new <code>SyntaxView</code>
     * instance.
     *
     * @param elem The element
     * @return a new SyntaxView for an element
     */
    public View create(Element elem)
    {
        return new SyntaxView(elem);
    }

    public Document createDefaultDocument()
    {
        return doc;
    }
}
