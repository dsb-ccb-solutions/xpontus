/*
 * SyntaxDocument.java
 *
 * Created on December 22, 2006, 1:14 AM
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
package net.sf.xpontus.syntax;

import net.sf.xpontus.constants.XPontusConstantsIF;
import net.sf.xpontus.constants.XPontusPropertiesConstantsIF;
import net.sf.xpontus.plugins.completion.CodeCompletionIF;
import net.sf.xpontus.plugins.completion.ContentAssistWindow;
import net.sf.xpontus.plugins.outline.OutlinePluginIF;
import net.sf.xpontus.properties.PropertiesHolder;
import net.sf.xpontus.utils.DynamicIntArray;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.swing.JList;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.JTextComponent;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.text.SimpleAttributeSet;


/**
 *
 * @author Yves Zoundi
 */
public class SyntaxDocument extends PlainDocument {
    // the lexer for the document
    private ILexer lexer;
    private boolean isCodeCompletion = false;
    private CodeCompletionIF plugin;
    private OutlinePluginIF outlinePlugin;

    //element that represents the previous line postion of the cursor
    private DynamicIntArray endTokens;
    private Segment seg;
    private boolean isLoading = false;
    private JPopupMenu completionMenu = new JPopupMenu();
    private JList completionList = new JList();
    private JScrollPane completionPane = new JScrollPane(completionList);

    //map of styles used to highlight text with
    private Map styleMap; //<TokenType, SimpleStyle>

    //last line postion of cursor - used to highlight - uses pkg access so SyntaxView can read it
    int selected = -1;

    //text component this document is for
    private JTextComponent editor;
    private final MutableAttributeSet DEFAULT_STYLE;
    private boolean xmlCompletion = false;

    /**
     *
     * @param editor
     * @param support
     */
    public SyntaxDocument(JTextComponent editor, SyntaxSupport support) {
        this.styleMap = support.getColorProvider().getStyles();

        this.editor = editor;
        seg = new Segment();
        DEFAULT_STYLE = new SimpleAttributeSet();
        this.lexer = support.getLexer();
        endTokens = new DynamicIntArray(500);

        Hashtable _map = (Hashtable) PropertiesHolder.getPropertyValue(XPontusPropertiesConstantsIF.XPONTUS_COMPLETION_ENGINES);

        if (_map.size() > 0) {
            String completion_key = _map.keySet().iterator().next().toString();
            Hashtable m_map = (Hashtable) _map.get(completion_key);

            try {
                String m_classname = m_map.get(XPontusConstantsIF.OBJECT_CLASSNAME)
                                          .toString();
                ClassLoader loader = (ClassLoader) m_map.get(XPontusConstantsIF.CLASS_LOADER);
                setCodeCompletion((CodeCompletionIF) Class.forName(
                        m_classname, true, loader).newInstance());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Hashtable outlineTable = (Hashtable) PropertiesHolder.getPropertyValue(XPontusPropertiesConstantsIF.XPONTUS_OUTLINE_ENGINES);
        System.out.println("Outliners:" + outlineTable.size());

        if (outlineTable.size() > 0) {
            System.out.println(outlineTable.keySet().iterator().next());

            Object mimeType = editor.getClientProperty(XPontusConstantsIF.CONTENT_TYPE);

            if (mimeType != null) {
                System.out.println("Mimetype not null:" + mimeType);

                if (outlineTable.containsKey(mimeType)) {
                    System.out.println("We got a match");

                    Hashtable v = (Hashtable) outlineTable.get(mimeType);
                    String m_classname = (String) v.get(XPontusConstantsIF.OBJECT_CLASSNAME);
                    ClassLoader m_loader = (ClassLoader) v.get(XPontusConstantsIF.CLASS_LOADER);

                    try {
                        OutlinePluginIF m_outline = (OutlinePluginIF) m_loader.loadClass(m_classname)
                                                                              .newInstance();
                        setOutlinePlugin(m_outline);
                    } catch (Exception exe) {
                        exe.printStackTrace();
                    }
                }
            }
        } else {
            System.out.println("no outline plugins");
        }
    }

    public OutlinePluginIF getOutlinePlugin() {
        return outlinePlugin;
    }

    public void setOutlinePlugin(OutlinePluginIF outlinePlugin) {
        System.out.println("Outline plugin set");
        this.outlinePlugin = outlinePlugin;
    }

    public void setCodeCompletion(CodeCompletionIF plugin) {
        this.plugin = plugin;
    }

    public CodeCompletionIF getCodeCompletion() {
        return plugin;
    }

    public void setLoading(boolean b) {
        isLoading = b;
    }

    /**
     *
     * @return
     */
    public ILexer getLexer() {
        return lexer;
    }

    /**
     *
     * @return
     */
    public boolean isCodeCompletion() {
        return xmlCompletion;
    }

    /**
     *
     * @return
     */
    public Map getStyleMap() {
        return styleMap;
    }

    /**
     *
     * @param e
     */
    protected void fireInsertUpdate(DocumentEvent e) {
        Element lineMap = getDefaultRootElement();
        DocumentEvent.ElementChange change = e.getChange(lineMap);
        Element[] added = (change == null) ? null : change.getChildrenAdded();

        int numLines = lineMap.getElementCount();
        int line = lineMap.getElementIndex(e.getOffset());
        int previousLine = line - 1;
        int previousTokenType = ((previousLine > -1)
            ? endTokens.get(previousLine) : 0);

        // If entire lines were added...
        if ((added != null) && (added.length > 0)) {
            Element[] removed = change.getChildrenRemoved();
            int numRemoved = (removed != null) ? removed.length : 0;

            int endBefore = (line + added.length) - numRemoved;

            for (int i = line; i < endBefore; i++) {
                setSharedSegment(i); // Loads line i's text into s.

                int tokenType = lexer.getLastTokenTypeOnLine(seg,
                        previousTokenType);
                endTokens.add(i, tokenType);

                previousTokenType = tokenType;
            }

            // Update last tokens for lines below until they stop changing.
            updateLastTokensBelow(endBefore, numLines, previousTokenType);
        }
        // Otherwise, text was inserted on a single line...
        else {
            // Update last tokens for lines below until they stop changing.
            updateLastTokensBelow(line, numLines, previousTokenType);
        }

        super.fireInsertUpdate(e);
    }

    protected void fireRemoveUpdate(DocumentEvent chng) {
        Element lineMap = getDefaultRootElement();
        int numLines = lineMap.getElementCount();

        DocumentEvent.ElementChange change = chng.getChange(lineMap);
        Element[] removed = (change == null) ? null : change.getChildrenRemoved();

        // If entire lines were removed...
        if ((removed != null) && (removed.length > 0)) {
            int line = change.getIndex(); // First line entirely removed.
            int previousLine = line - 1; // Line before that.
            int previousTokenType = ((previousLine > -1)
                ? endTokens.get(previousLine) : 0);

            Element[] added = change.getChildrenAdded();
            int numAdded = (added == null) ? 0 : added.length;

            // Remove the cached last-token values for the removed lines.
            int endBefore = (line + removed.length) - numAdded;

            endTokens.removeRange(line, endBefore); // Removing values for

            // lines
            // [line-(endBefore-1)].
            // Update last tokens for lines below until they've stopped changing.
            updateLastTokensBelow(line, numLines, previousTokenType);
        }
        // Otherwise, text was removed from just one line...
        else {
            int line = lineMap.getElementIndex(chng.getOffset());

            if (line >= endTokens.getSize()) {
                return; // If we're editing the

                // last line in a
                // document...
            }

            int previousLine = line - 1;
            int previousTokenType = ((previousLine > -1)
                ? endTokens.get(previousLine) : 0);

            // Update last tokens for lines below until they've stopped changing.
            updateLastTokensBelow(line, numLines, previousTokenType);
        }

        super.fireRemoveUpdate(chng);
    }

    /**
     * Makes our private <code>Segment s</code> point to the text in our
     * document referenced by the specified element. Note that <code>line</code>
     * MUST be a valid line number in the document.
     *
     * @param line
     *          The line number you want to get.
     */
    private final void setSharedSegment(int line) {
        Element map = getDefaultRootElement();
        int numLines = map.getElementCount();

        Element element = map.getElement(line);

        if (element == null) {
            throw new InternalError("Invalid line number: " + line);
        }

        int startOffset = element.getStartOffset();
        int endOffset = ((line == (numLines - 1)) ? (element.getEndOffset() -
            1) : (element.getEndOffset() - 1));

        try {
            getText(startOffset, endOffset - startOffset, seg);
        } catch (BadLocationException ble) {
            throw new InternalError("Text range not in document: " +
                startOffset + "-" + endOffset);
        }
    }

    /**
     *
     * @param line
     * @param numLines
     * @param previousTokenType
     * @return
     */
    private int updateLastTokensBelow(int line, int numLines,
        int previousTokenType) {
        int firstLine = line;
        int end = numLines - 1;

        while (line < end) {
            setSharedSegment(line);

            int oldTokenType = endTokens.get(line);
            int newTokenType = lexer.getLastTokenTypeOnLine(seg,
                    previousTokenType);

            // If this line's end-token value didn't change, stop here. Note
            // that we're saying this line needs repainting; this is because
            // the beginning of this line did indeed change color, but the
            // end didn't.
            if (oldTokenType == newTokenType) {
                damageRange(firstLine, line);

                return line;
            }

            // If the line's end-token value did change, update it and
            // keep going.
            endTokens.set(line, newTokenType);
            previousTokenType = newTokenType;
            line++;
        }

        if (line > firstLine) {
            damageRange(firstLine, line);
        }

        return line;
    }

    /**
     *
     * @param firstLine
     * @param lastLine
     */
    private void damageRange(int firstLine, int lastLine) {
        Element f = getDefaultRootElement().getElement(firstLine);
        Element e = getDefaultRootElement().getElement(lastLine);
        editor.getUI().damageRange(editor, f.getStartOffset(), e.getEndOffset());
    }

    /**
     *
     * @param line
     * @return
     */
    public final List getTokenListForLine(int line) {
        Element map = getDefaultRootElement();
        Element elem = map.getElement(line);
        int startOffset = elem.getStartOffset();
        int endOffset = elem.getEndOffset() - 1;

        try {
            getText(startOffset, endOffset - startOffset, seg);
        } catch (BadLocationException ble) {
            ble.printStackTrace();

            return null;
        }

        int initialTokenType = 0;

        if (line > 0) {
            // BUG HERE check if the value of line first...
            initialTokenType = endTokens.get(line - 1);
        }

        return lexer.getTokens(seg, initialTokenType, startOffset);
    }

    /**
     *
     * @param kind
     * @return
     */
    public MutableAttributeSet getStyleForType(int kind) {
        Integer tokenId = new Integer(kind);
        Object tokenStyle = styleMap.get(tokenId);

        return (tokenStyle != null) ? (MutableAttributeSet) tokenStyle
                                    : DEFAULT_STYLE;
    }

    /**
     *
     * @param off
     * @param str
     * @param set
     * @throws javax.swing.text.BadLocationException
     */
    public void insertString(int off, String str, AttributeSet set)
        throws BadLocationException {
        super.insertString(off, str, set);

        if ((plugin != null) && plugin.isTrigger(str)) {
            ContentAssistWindow.complete(editor, plugin, off, str, set);
        }
    }
}
