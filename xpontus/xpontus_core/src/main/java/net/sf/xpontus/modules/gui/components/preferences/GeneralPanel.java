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
package net.sf.xpontus.modules.gui.components.preferences;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.BannerPanel;

import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;
import net.sf.xpontus.utils.SpringUtilities;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SpringLayout;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class GeneralPanel extends AbstractDialogPage
    implements IPreferencesPanel, PreferencesPluginIF {
    private javax.swing.JLabel defaultIconSetLabel;
    private javax.swing.JLabel defaultThemeLabel;
    private javax.swing.JComboBox iconSetList;
    private javax.swing.JLabel menubarIconsLabel;
    private javax.swing.JComboBox menubarStyleList;
    private javax.swing.JCheckBox showConfirmDialogOnExitOption;
    private javax.swing.JCheckBox showSplashScreenOption;
    private javax.swing.JCheckBox showTipsOption;
    private javax.swing.JComboBox themeList;
    private javax.swing.JLabel toolbarIconsLabel;
    private javax.swing.JComboBox toolbarSettingsList;
    private JPanel centerPanel;

    public GeneralPanel(String name, Icon icon) {
        super(name, icon);

        setLayout(new BorderLayout());

        final Dimension dimension = new Dimension(500, 400);

        setMinimumSize(dimension);
        setPreferredSize(dimension);

        showSplashScreenOption = new javax.swing.JCheckBox();
        showTipsOption = new javax.swing.JCheckBox();
        defaultThemeLabel = new javax.swing.JLabel();
        defaultIconSetLabel = new javax.swing.JLabel();
        themeList = new javax.swing.JComboBox();
        iconSetList = new javax.swing.JComboBox();
        showConfirmDialogOnExitOption = new javax.swing.JCheckBox();
        toolbarIconsLabel = new javax.swing.JLabel();
        toolbarSettingsList = new javax.swing.JComboBox();
        menubarIconsLabel = new javax.swing.JLabel();
        menubarStyleList = new javax.swing.JComboBox();

        showSplashScreenOption.setSelected(true);
        showSplashScreenOption.setText("Show splash screen on startup");

        showTipsOption.setSelected(true);
        showTipsOption.setText("Show tips on startup");

        defaultThemeLabel.setText("  Default theme");

        defaultIconSetLabel.setText("  Default icon set");

        themeList.setModel(new javax.swing.DefaultComboBoxModel(
                new String[] { "Default" }));

        iconSetList.setModel(new javax.swing.DefaultComboBoxModel(
                new String[] { "Default" }));

        showConfirmDialogOnExitOption.setSelected(true);
        showConfirmDialogOnExitOption.setText("Show confirm dialog on exit");

        toolbarIconsLabel.setText("  Toolbar icons");

        toolbarSettingsList.setModel(new javax.swing.DefaultComboBoxModel(
                new String[] { "Text and icons" }));

        menubarIconsLabel.setText("  Menubar icons");

        menubarStyleList.setModel(new javax.swing.DefaultComboBoxModel(
                new String[] { "Text and icons" }));

        add(new BannerPanel(name), BorderLayout.NORTH);

        centerPanel = new JPanel(new SpringLayout());

        centerPanel.add(defaultThemeLabel);
        centerPanel.add(themeList);

        centerPanel.add(defaultIconSetLabel);
        centerPanel.add(iconSetList);

        SpringUtilities.makeGrid(centerPanel, 2, 2, 5, 5, 5, 5);
        
        add(centerPanel, BorderLayout.CENTER);
    }

    public String getTitle() {
        return "General";
    }

    public Component getJComponent() {
        return this;
    }

    public String getId() {
        return "";
    }

    public String getPluginCategory() {
        return "";
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        return this;
    }

    public void saveSettings() {
    }

    public void loadSettings() {
    }

    @Override
    public void lazyInitialize() {
    }
}
