/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.completion;



import java.util.List;

import javax.swing.text.AttributeSet;
import javax.swing.text.JTextComponent;


/**
 *
 * @author mrcheeks
 */
public class ContentAssistWindow {
    private static ContentAssistWindow INSTANCE;
    private JTextComponent jtc;
    private int offset;

    public ContentAssistWindow() {
    }

    public static ContentAssistWindow getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new ContentAssistWindow();
        }

        return INSTANCE;
    }

    public void complete(final JTextComponent jtc,
        final CodeCompletionIF contentAssist, int off, final String str,
        final AttributeSet set) {
        this.jtc = jtc;
        this.offset = off;

        List completionData = contentAssist.getCompletionList(str, off);

        if (completionData == null) {
            return;
        }

        if (completionData.size() > 0) {
            CompletionWindow window = CompletionWindow.getInstance(); 
            window.getCompletionListModel().updateData(completionData);
            window.showWindow(jtc);
        }
    }
}
