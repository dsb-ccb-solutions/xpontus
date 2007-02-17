/*
 * SyntaxDocument.java - Document that can be tokenized
 * Copyright (C) 1999 Slava Pestov
 *
 * You may use and modify this package for any purpose. Redistribution is
 * permitted, in both source and binary form, provided that this notice
 * remains intact in all source distributions of this package.
 */
package org.syntax.jedit;

import org.syntax.jedit.tokenmarker.TokenMarker;
import java.awt.Color;
import javax.swing.JEditorPane;
import javax.swing.event.DocumentEvent;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.PlainDocument;
import javax.swing.text.Segment;
import javax.swing.undo.UndoableEdit;
import net.sf.xpontus.model.options.EditorOptionModel;
import net.sf.xpontus.model.options.TokenColorsOptionModel;
import net.sf.xpontus.view.XPontusWindow;
import org.syntax.jedit.tokenmarker.XMLTokenMarker;


/**
 * A document implementation that can be tokenized by the syntax highlighting
 * system.
 *
 * @author Slava Pestov
 * @version $Id: SyntaxDocument.java,v 1.1.1.1 2005/12/08 01:34:34 yveszoundi Exp $
 */
public class SyntaxDocument extends PlainDocument {
    // private members
    protected Color[] colors;
    
    // protected members
    protected TokenMarker tokenMarker = null; // new XMLTokenMarker();
    
    public SyntaxDocument() {
        colors = getUserColors();
        EditorOptionModel m = new EditorOptionModel();
        EditorOptionModel obj = (EditorOptionModel)m.load();
        String tbsize = obj.getTabSize() + "";
        putProperty( PlainDocument.tabSizeAttribute, new Integer(Integer.parseInt(tbsize)) );
    }
    
    /**
     * Creates a new <code>DefaultSyntaxDocument</code> instance.
     */
    public SyntaxDocument(Color[] colors) {
        this.colors = colors;
    }
    
    public void setColors(Color[] colors){
        this.colors = colors;
    }
    
    /**
     * Returns the token marker that is to be used to split lines
     * of this document up into tokens. May return null if this
     * document is not to be colorized.
     */
    public TokenMarker getTokenMarker() {
        return tokenMarker;
    }
    
    /**
     * Returns the color array that maps token identifiers to
     * <code>java.awt.Color</code> objects.
     */
    public Color[] getColors() {
        return colors;
    }
    
    private Color[] getUserColors() {
        if (colors == null) {
            TokenColorsOptionModel model1 = (TokenColorsOptionModel)new TokenColorsOptionModel().load();
            setColors(model1.getColors()); 
        }
        
        return colors;
    }
    
    /**
     * Sets the token marker that is to be used to split lines of
     * this document up into tokens. May throw an exception if
     * this is not supported for this type of document.
     * @param tm The new token marker
     */
    public void setTokenMarker(TokenMarker tm) {
        tokenMarker = tm;
        
        if (tm == null) {
            return;
        }
        
        tokenMarker.insertLines(0, getDefaultRootElement().getElementCount());
        tokenizeLines();
    }
    
    /**
     * Reparses the document, by passing all lines to the token
     * marker. This should be called after the document is first
     * loaded.
     */
    public void tokenizeLines() {
        tokenizeLines(0, getDefaultRootElement().getElementCount());
    }
    
    /**
     * Reparses the document, by passing the specified lines to the
     * token marker. This should be called after a large quantity of
     * text is first inserted.
     * @param start The first line to parse
     * @param len The number of lines, after the first one to parse
     */
    public void tokenizeLines(int start, int len) {
        if ((tokenMarker == null) || !tokenMarker.supportsMultilineTokens()) {
            return;
        }
        
        Segment lineSegment = new Segment();
        Element map = getDefaultRootElement();
        
        len += start;
        
        try {
            for (int i = start; i < len; i++) {
                Element lineElement = map.getElement(i);
                int lineStart = lineElement.getStartOffset();
                getText(lineStart, lineElement.getEndOffset() - lineStart - 1,
                        lineSegment);
                tokenMarker.markTokens(lineSegment, i);
            }
        } catch (BadLocationException bl) {
            bl.printStackTrace();
        }
    }
    
    /**
     * Starts a compound edit that can be undone in one operation.
     * Subclasses that implement undo should override this method;
     * this class has no undo functionality so this method is
     * empty.
     */
    public void beginCompoundEdit() {
    }
    
    /**
     * Ends a compound edit that can be undone in one operation.
     * Subclasses that implement undo should override this method;
     * this class has no undo functionality so this method is
     * empty.
     */
    public void endCompoundEdit() {
    }
    
    /**
     * Adds an undoable edit to this document's undo list. The edit
     * should be ignored if something is currently being undone.
     * @param edit The undoable edit
     *
     * @since jEdit 2.2pre1
     */
    public void addUndoableEdit(UndoableEdit edit) {
    }
    
    /**
     * We overwrite this method to update the token marker
     * state immediately so that any event listeners get a
     * consistent token marker.
     */
    protected void fireInsertUpdate(DocumentEvent evt) {
        if (tokenMarker != null) {
            DocumentEvent.ElementChange ch = evt.getChange(getDefaultRootElement());
            
            if (ch != null) {
                tokenMarker.insertLines(ch.getIndex() + 1,
                        ch.getChildrenAdded().length -
                        ch.getChildrenRemoved().length);
            }
        }
        
        super.fireInsertUpdate(evt);
    }
    
    /**
     * We overwrite this method to update the token marker
     * state immediately so that any event listeners get a
     * consistent token marker.
     */
    protected void fireRemoveUpdate(DocumentEvent evt) {
        if (tokenMarker != null) {
            DocumentEvent.ElementChange ch = evt.getChange(getDefaultRootElement());
            
            if (ch != null) {
                tokenMarker.deleteLines(ch.getIndex() + 1,
                        ch.getChildrenRemoved().length -
                        ch.getChildrenAdded().length);
            }
        }
        
        super.fireRemoveUpdate(evt);
    }
    
    /**
     *
     * @param off
     * @param str
     * @param set
     * @throws javax.swing.text.BadLocationException
     */
  
    public void insertString( int off, String str, AttributeSet set) throws BadLocationException {
        if ( str.equals( ">") && tokenMarker!=null && tokenMarker.getClass() == XMLTokenMarker.class) {
            final JEditorPane editor = XPontusWindow.getInstance().getCurrentEditor();
            
            int dot = editor.getCaret().getDot();
            
            StringBuffer endTag = new StringBuffer( str);
    
            String text = getText( 0, off);
            int startTag = text.lastIndexOf( '<', off);
            int prefEndTag = text.lastIndexOf( '>', off);
    
            // If there was a start tag and if the start tag is not empty
            // and
            // if the start-tag has not got an end-tag already.
            if ( (startTag > 0) && (startTag > prefEndTag) && (startTag < text.length() - 1)) {
                String tag = text.substring( startTag, text.length());
                char first = tag.charAt( 1);
    
                if ( first != '/' && first != '!' && first != '?' && !Character.isWhitespace( first)) {
                    boolean finished = false;
                    char previous = tag.charAt( tag.length() - 1);
    
                    if ( previous != '/' && previous != '-') {
    
                        endTag.append( "</");
    
                        for ( int i = 1; (i < tag.length()) && !finished; i++) {
                            char ch = tag.charAt( i);
    
                            if ( !Character.isWhitespace( ch)) {
                                endTag.append( ch);
                            } else {
                                finished = true;
                            }
                        }
    
                        endTag.append( ">");
                    }
                }
            }
    
            str = endTag.toString();
    
            super.insertString( off, str, set);
    
            editor.getCaret().setDot( dot + 1);
        }  else {
            super.insertString( off, str, set);
        }
    }

    // Tries to find out if the line finishes with an element start
    private boolean isStartElement( String line) {
        boolean result = false;

        int first = line.lastIndexOf( "<");
        int last = line.lastIndexOf( ">");

        if ( last < first) { // In the Tag
            result = true;
        } else {
            int firstEnd = line.lastIndexOf( "</");
            int lastEnd = line.lastIndexOf( "/>");

            // Last Tag is not an End Tag
            if ( (firstEnd != first) && ((lastEnd + 1) != last)) {
                result = true;
            }
        }

        return result;
    }
    
    
    
}
