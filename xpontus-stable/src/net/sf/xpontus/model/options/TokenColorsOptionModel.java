/*
 * TokenColorsOptionModel.java
 *
 * Created on February 16, 2007, 9:56 PM
 *
 *  Copyright (C) 2005-2007 Yves Zoundi
 *
 *  This library is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published
 *  by the Free Software Foundation; either version 2.1 of the License, or
 *  (at your option) any later version.
 *
 *  This library is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU Lesser General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 675 Mass Ave, Cambridge, MA 02139, USA.
 */
package net.sf.xpontus.model.options;

import com.sun.java.help.impl.SwingWorker;

import net.sf.xpontus.constants.XPontusConstants;
import net.sf.xpontus.core.model.ConfigurationModel;
import net.sf.xpontus.view.PaneForm;
import net.sf.xpontus.view.XPontusWindow;

import org.syntax.jedit.SyntaxDocument;
import org.syntax.jedit.tokenmarker.Token;

import java.awt.Color;

import java.io.File;

import javax.swing.text.Document;


/**
 * The tokens' colors model
 * @author Yves Zoundi
 */
public class TokenColorsOptionModel extends ConfigurationModel {
    private Color[] colors;

    /** Creates a new instance of TokenColorsOptionModel */
    public TokenColorsOptionModel() {
        colors = new Color[Token.ID_COUNT];
        colors[Token.COMMENT1] = new Color(0, 102, 102);
        colors[Token.COMMENT2] = new Color(Integer.parseInt("727675", 16));
        colors[Token.COMMENT3] = new Color(Integer.parseInt("dd25af", 16));
        colors[Token.KEYWORD1] = new Color(Integer.parseInt("1723aa", 16));
        colors[Token.KEYWORD2] = new Color(0, 124, 0);
        colors[Token.KEYWORD3] = Color.MAGENTA;
        colors[Token.PRIMITIVE] = Color.blue;
        colors[Token.LITERAL1] = new Color(153, 0, 107);
        colors[Token.LABEL] = new Color(0x990000);
        colors[Token.OPERATOR] = new Color(0xcc9900);
        colors[Token.INVALID] = new Color(0xff3300);
    }

    /**
     *
     * @return
     */
    public Color[] getColors() {
        return colors;
    }

    /**
     *
     * @param colors
     */
    public void setColors(Color[] colors) {
        this.colors = colors;

        final Color[] mColors = colors;
        final SwingWorker worker = new SwingWorker() {
                public Object construct() {
                    PaneForm paneform = XPontusWindow.getInstance().getPane();

                    for (int i = 0; i < paneform.getTabCount(); i++) {
                        javax.swing.JScrollPane sp = (javax.swing.JScrollPane) paneform.getComponentAt(i);
                        javax.swing.JEditorPane edit = paneform.getEditorAt(i);
                        Document doc = edit.getDocument();

                        if (doc instanceof SyntaxDocument) {
                            SyntaxDocument m_doc = (SyntaxDocument) doc;
                            m_doc.setColors(mColors);
                            edit.repaint(edit.getVisibleRect());
                        }
                    }

                    return null;
                }
            };

        worker.start();
    }

    public String getMappingURL() {
        return null;
    }

    public File getFileToSaveTo() {
        return XPontusConstants.COLOR_PREF;
    }
}
