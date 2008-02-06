/*
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
 *
 */
package net.sf.xpontus.plugins.scenario.fop.fop_plugin;

import java.awt.BorderLayout;
import java.awt.Color;
import net.sf.xpontus.modules.gui.components.preferences.IPreferencesPanel;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;

import java.awt.Component;

import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class FOPConfigurationPanel implements PreferencesPluginIF {

    private final String PLUGIN_CATEGORY = "Transformation";
    private IPreferencesPanel panel;

    public String getPluginCategory() {
        return PLUGIN_CATEGORY;
    }

    public void saveSettings() {
    }

    public void loadSettings() {
    }

    public class FOPConfigurationComponent extends JComponent
            implements IPreferencesPanel {

        private JButton browseButton;
        private JTextField fopSettingsTF;
        private final String CONFIGURATION = "FOP users settings";

        public FOPConfigurationComponent() {
            setLayout(new BorderLayout());

            JLabel top = new JLabel(getTitle());
            top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            top.setFont(top.getFont().deriveFont(Font.BOLD));
            top.setOpaque(true);
            top.setBackground(Color.white);

            add("North", top);

            JPanel p = new JPanel(new GridBagLayout());



            browseButton = new JButton("Browse...");

            fopSettingsTF = new JTextField(35);

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(2, 2, 2, 2);
            c.gridy = 0;
            p.add(browseButton, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(2, 2, 2, 2);
            c.fill = GridBagConstraints.BOTH;
            p.add(fopSettingsTF, c);

            add(p, BorderLayout.CENTER);
        }

        public String getTitle() {
            return CONFIGURATION;
        }

        public Component getComponent() {
            return this;
        }

        public String getId() {
            return getClass().getName();
        }
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        if (panel == null) {
            panel = new FOPConfigurationComponent();
        }

        return panel;
    }
}
