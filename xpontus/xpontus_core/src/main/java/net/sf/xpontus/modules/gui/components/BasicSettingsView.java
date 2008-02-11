/*
 * BasicSettingsView.java
 *
 * Created on 30 janvier 2008, 17:51
 */
package net.sf.xpontus.modules.gui.components;

import com.jidesoft.dialog.*;

import net.sf.xpontus.modules.gui.components.preferences.EditorPanel;
import net.sf.xpontus.modules.gui.components.preferences.GeneralPanel;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import java.awt.*;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Frame;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JPanel;


/**
 *
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class BasicSettingsView extends MultiplePageDialog {
    private JDialog dialog;
    private JButton saveButton;
    private JButton cancelButton;
    private JButton advancedButton;

    /**
     * Creates new form BasicSettingsView
     * @param parent
     * @param modal
     */
    public BasicSettingsView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        final Dimension dimension = new Dimension(500, 500);
            
            setMinimumSize(dimension);
            setPreferredSize(dimension);
            
        setTitle("Basic settings");
        setStyle(MultiplePageDialog.ICON_STYLE);

        PageList model = new PageList();

        String[] icons = {
                "/net/sf/xpontus/icons/gear.gif",
                "/net/sf/xpontus/icons/accessories-text-editor.gif"
            };

        AbstractDialogPage panel1 = new AbstractDialogPageBuilder("General",
                new ImageIcon(getClass().getResource(icons[0])),
                new GeneralPanel());
        AbstractDialogPage panel2 = new AbstractDialogPageBuilder("Editor",
                new ImageIcon(getClass().getResource(icons[1])),
                new EditorPanel());

        model.append(panel1);
        model.append(panel2);

        setPageList(model);

        pack();
    }

    /**
     *
     */
    public BasicSettingsView() {
        this((Frame) XPontusComponentsUtils.getTopComponent()
                                           .getDisplayComponent(), true);
    }

    @Override
    public JComponent getBannerPanel() {
        BannerPanel panel = new BannerPanel("Preferences");
        return panel;
    }

    
    /**
     * 
     * @return
     */
    @Override
    public ButtonPanel createButtonPanel() {
        ButtonPanel buttonPanel = new ButtonPanel();
        
        saveButton = new JButton("Save");
        advancedButton = new JButton("Advanced");
        cancelButton = new JButton("Cancel");
        
        buttonPanel.add(saveButton);
        buttonPanel.add(cancelButton);
        buttonPanel.add(advancedButton);

        advancedButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    if (dialog == null) {
                        JDialog parent = BasicSettingsView.this;
                        dialog = new AdvancedSettingsView(parent, true);
                    }

                    dialog.setLocationRelativeTo(BasicSettingsView.this);
                    dialog.setVisible(true);
                }
            });

        return buttonPanel;
    }

    class AbstractDialogPageBuilder extends AbstractDialogPage {
        private PreferencesPluginIF p;

        public AbstractDialogPageBuilder(String name, Icon icon,
            PreferencesPluginIF p) {
            super(name, icon);
            this.p = p;
        }
 
        @Override
        public void lazyInitialize() {
            setLayout(new BorderLayout());
            
            
            
            getContentPane()
                .add(new BannerPanel(p.getPreferencesPanelComponent().getTitle()),
                BorderLayout.NORTH);
            getContentPane()
                .add(p.getPreferencesPanelComponent().getJComponent(),
                BorderLayout.CENTER);
             
           
        }
    }
}
