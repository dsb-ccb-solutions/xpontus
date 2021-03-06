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

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.modules.gui.components.preferences.IPreferencesPanel;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;

import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;


/**
 *
 * @author Yves Zoundi
 */
public class FOPConfigurationPanel implements PreferencesPluginIF {
    private final String PLUGIN_CATEGORY = "Transformation";
    private IPreferencesPanel panel;
    private JFileChooser chooser = new JFileChooser();
    private String configFile;
    JTextField fopSettingsTF = new JTextField();

    public String getPluginCategory() {
        return PLUGIN_CATEGORY;
    }

    public void saveSettings() {
        String pkg = "" + getClass().getPackage();
        File propertiesFile = FOPTransformationPlugin.configfile;
        Properties props = new Properties();
        String m_prop = FOPConfigurationPanel.class.getName() + "$" +
            FOPTransformationPluginImpl.FOP_CONFIG_FILE_PROPERTY;

        if (configFile == null) {             
			configFile="";
		}
	    props.put(m_prop, configFile);
        PropertiesConfigurationLoader.save(propertiesFile, props);
        XPontusConfig.put(m_prop, configFile);
    }

    public void loadSettings() {
        String m_prop = FOPConfigurationPanel.class.getName() + "$" +
            FOPTransformationPluginImpl.FOP_CONFIG_FILE_PROPERTY;
        Object o = XPontusConfig.getValue(m_prop);

        if (o == null) {
            configFile = null;
        } else {
            fopSettingsTF.setText("");
        }
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        if (panel == null) {
            panel = new FOPConfigurationComponent();
        }

        return panel;
    }

    public class FOPConfigurationComponent extends JComponent
        implements IPreferencesPanel {
        private JButton browseButton;
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

            chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            chooser.setDialogTitle("Select the FOP configuration file");
            fopSettingsTF.setEditable(false);

            browseButton.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        int rep = chooser.showOpenDialog(FOPConfigurationComponent.this);

                        if (rep == JFileChooser.APPROVE_OPTION) {
                            configFile = chooser.getSelectedFile()
                                                .getAbsolutePath();
                            fopSettingsTF.setText(configFile);
                        }
                    }
                });

            GridBagConstraints c = new GridBagConstraints();
            c.gridx = 0;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(2, 2, 2, 2);
            c.gridy = 0;
            p.add(browseButton, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.gridwidth = 3;
            c.ipadx = 200;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(2, 2, 2, 2);
            c.fill = GridBagConstraints.HORIZONTAL;
            p.add(fopSettingsTF, c);

            add(p, BorderLayout.CENTER);
        }

        public String getTitle() {
            return CONFIGURATION;
        }

        public Component getJComponent() {
            return this;
        }

        public String getId() {
            return getClass().getName();
        }
    }
}
