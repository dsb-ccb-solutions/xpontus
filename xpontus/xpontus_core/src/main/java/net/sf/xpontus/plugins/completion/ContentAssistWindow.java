/*
 * ContentAssistWindow.java
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
package net.sf.xpontus.plugins.completion;

import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;


/**
 * A class which show the completion window (CompletionWindow class should never called directly)
 * @version 0.0.1
 * @author Yves Zoundi
 */
public class ContentAssistWindow
{
    private static final ContentAssistWindow INSTANCE = new ContentAssistWindow();
    private JTextComponent jtc;
    private int offset;

    public ContentAssistWindow()
    {
    }

    public static ContentAssistWindow getInstance()
    {
        return INSTANCE;
    }

    public void complete(final JTextComponent jtc,
        final CodeCompletionIF contentAssist, int off, final String str,
        final AttributeSet set)
    {
        this.jtc = jtc;
        this.offset = off;

        List completionData = contentAssist.getCompletionList(str, off);

        if (completionData == null)
        {
            return;
        }

        if (completionData.size() > 0)
        {
            CompletionWindow window = CompletionWindow.getInstance();
            window.getCompletionListModel().updateData(completionData);
            window.showWindow(jtc);
        }
    }
}
