/*
 * BasicSettingsView.java
 *
 * Created on 30 janvier 2008, 17:51
 */
package net.sf.xpontus.modules.gui.components;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.BannerPanel;
import com.jidesoft.dialog.ButtonPanel;
import com.jidesoft.dialog.MultiplePageDialog;
import com.jidesoft.dialog.PageList;

import net.sf.xpontus.modules.gui.components.preferences.EditorPanelDialog;
import net.sf.xpontus.modules.gui.components.preferences.GeneralPanelDialog;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;


/**
 *
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class BasicSettingsView extends MultiplePageDialog
{
    private JDialog dialog;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton advancedButton;

    /**
     * Creates new form BasicSettingsView
     * @param parent
     * @param modal
     */
    public BasicSettingsView(java.awt.Frame parent, boolean modal)
    {
        super(parent, modal);

        setTitle("Basic settings");
        setStyle(MultiplePageDialog.ICON_STYLE);

        PageList model = new PageList();

        String[] icons = 
            {
                "/net/sf/xpontus/icons/gear.gif",
                "/net/sf/xpontus/icons/accessories-text-editor.gif"
            };

        AbstractDialogPage panel1 = new GeneralPanelDialog("General",
                new ImageIcon(getClass().getResource(icons[0])));
        AbstractDialogPage panel2 = new EditorPanelDialog("Editor",
                new ImageIcon(getClass().getResource(icons[1])));

        model.append(panel1);
        model.append(panel2);

        setPageList(model);

        pack();
    }

    /**
     *
     */
    public BasicSettingsView()
    {
        this((Frame) XPontusComponentsUtils.getTopComponent()
                                           .getDisplayComponent(), true);
    }

    @Override
    public JComponent getBannerPanel()
    {
        BannerPanel panel = new BannerPanel("Preferences");

        return panel;
    }

    /**
     *
     * @return
     */
    @Override
    public ButtonPanel createButtonPanel()
    {
        ButtonPanel buttonPanel = new ButtonPanel();

        saveButton = new JButton("Save");
        advancedButton = new JButton("Advanced");
        cancelButton = new JButton("Cancel");

        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(advancedButton);

        advancedButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e)
                {
                    if (dialog == null)
                    {
                        JDialog parent = BasicSettingsView.this;
                        dialog = new AdvancedSettingsView(parent, true);
                    }

                    dialog.setLocationRelativeTo(BasicSettingsView.this);
                    dialog.setVisible(true);
                }
            });

        return buttonPanel;
    }
}
