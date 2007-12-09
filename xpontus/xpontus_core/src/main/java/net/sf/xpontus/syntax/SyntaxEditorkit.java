/**
 * MoeSyntaxEditorKit.java - adapted from
 * SyntaxEditorKit.java - jEdit's own editor kit
 * to add Syntax highlighting to the BlueJ programming environment.
 * Copyright (C) 1998, 1999 Slava Pestov
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA.
 */
package net.sf.xpontus.syntax;

import java.awt.Font;
import javax.swing.text.DefaultEditorKit;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.View;
import javax.swing.text.ViewFactory;
import net.sf.xpontus.modules.gui.components.XPontusCaret;


/**
 * An implementation of <code>EditorKit</code> used for syntax coloring. This
 * is an adaptation of the SyntaxEditorKit class from JEdit for BlueJ.
 *
 * @author Bruce Quig
 * @author Michael K\u0161lling
 *
 * @see org.gjt.sp.jedit.syntax.SyntaxView
 */
public class SyntaxEditorkit extends DefaultEditorKit implements ViewFactory {
    private Document doc;
    private SyntaxSupport syntaxSupport;

    /**
     *
     * @param editor
     * @param mode
     *
     */
    public SyntaxEditorkit(JTextComponent editor, String mode) {
        editor.setFont(new Font("Monospaced", Font.PLAIN, 13) );        
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
    public ViewFactory getViewFactory() {
        return this;
    }

    /**
     * Creates a view from an element that can be used for painting that
     * element. This implementation returns a new <code>SyntaxView</code>
     * instance.
     *
     * @param elem
     *                The element
     * @return a new XPontusEditorView for an element
     * @see org.gjt.sp.jedit.syntax.SyntaxView
     */
    public View create(Element elem) {
        return new SyntaxView(elem);
    }

    /**
     * Creates a new instance of the default document for this editor kit.
     * This returns a new instance of <code>DefaultSyntaxDocument</code>.
     *
     * @return
     */
    public Document createDefaultDocument() {
        return doc;
    }
}
