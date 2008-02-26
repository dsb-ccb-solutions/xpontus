/*
 * XPontusCaret.java
 *
 * Created on 10 septembre 2005, 15:04
 *
 *
 *  Copyright (C) 2005 Yves Zoundi
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
package net.sf.xpontus.view;

import net.sf.xpontus.model.options.EditorOptionModel;

import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultHighlighter.DefaultHighlightPainter;
import javax.swing.text.Highlighter.HighlightPainter;


/**
 * The XPontus XML Editor caret which has to blink all the time
 *
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class XPontusCaret extends DefaultCaret
  {
    /**
     *
     */
    private static final long serialVersionUID = 1280669888698357505L;
    private javax.swing.text.Highlighter.HighlightPainter painter;

    /** Creates a new instance of XPontusCaret */
    public XPontusCaret()
      {
        EditorOptionModel m = new EditorOptionModel();
        EditorOptionModel obj = (EditorOptionModel) m.load();
        this.setBlinkRate(obj.getCursorRate());
        painter = new CustomHighlightPainter(java.awt.Color.LIGHT_GRAY);
      }

    protected HighlightPainter getSelectionPainter()
      {
        return painter;
      }

    public void focusLost(java.awt.event.FocusEvent evt)
      {
      }

    class CustomHighlightPainter extends DefaultHighlightPainter
      {
        public CustomHighlightPainter(java.awt.Color color)
          {
            super(color);
          }
      }
  }
