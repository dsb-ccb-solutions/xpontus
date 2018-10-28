/*
 * GeneralPanel.java
 *
 * Created on 27 janvier 2008, 18:31
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
 */
package net.sf.xpontus.modules.gui.components.preferences;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.util.Iterator;
import java.util.Properties;
import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.model.preferences.GeneralOptionModel;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;

/**
 *
 * @author Yves Zoundi
 */
public class GeneralPanel extends javax.swing.JPanel implements IPreferencesPanel, PreferencesPluginIF {
 
	private static final long serialVersionUID = 2077742480990262224L;
	/** Creates new form GeneralPanel */
    public GeneralPanel() {
        initComponents();
    }

    public String toString() {
        return getTitle();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        showSplashScreenOption = new javax.swing.JCheckBox();
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
        showSplashScreenOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showSplashScreenOptionActionPerformed(evt);
            }
        });

        defaultThemeLabel.setText("  Default theme");

        defaultIconSetLabel.setText("  Default icon set");

        themeList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default" }));
        themeList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                themeListItemStateChanged(evt);
            }
        });

        iconSetList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Default" }));
        iconSetList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                iconSetListItemStateChanged(evt);
            }
        });

        showConfirmDialogOnExitOption.setSelected(true);
        showConfirmDialogOnExitOption.setText("Show confirm dialog on exit");
        showConfirmDialogOnExitOption.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                showConfirmDialogOnExitOptionActionPerformed(evt);
            }
        });

        toolbarIconsLabel.setText("  Toolbar style");

        toolbarSettingsList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Text and icons", "Text only", "Icons only" }));
        toolbarSettingsList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                toolbarSettingsListItemStateChanged(evt);
            }
        });

        menubarIconsLabel.setText("  Menubar style");

        menubarStyleList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Text and icons", "Text only" }));
        menubarStyleList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                menubarStyleListItemStateChanged(evt);
            }
        });

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(showConfirmDialogOnExitOption)
                    .add(menubarIconsLabel)
                    .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING, false)
                        .add(toolbarIconsLabel)
                        .add(showSplashScreenOption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 319, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(layout.createSequentialGroup()
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(defaultThemeLabel)
                                .add(defaultIconSetLabel))
                            .add(54, 54, 54)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, iconSetList, 0, 184, Short.MAX_VALUE)
                                .add(org.jdesktop.layout.GroupLayout.TRAILING, toolbarSettingsList, 0, 184, Short.MAX_VALUE)
                                .add(themeList, 0, 184, Short.MAX_VALUE)
                                .add(menubarStyleList, 0, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(23, 23, 23)
                .add(showSplashScreenOption)
                .add(18, 18, 18)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(defaultThemeLabel)
                    .add(themeList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(26, 26, 26)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(defaultIconSetLabel)
                    .add(iconSetList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(28, 28, 28)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(toolbarIconsLabel)
                    .add(toolbarSettingsList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(37, 37, 37)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(menubarIconsLabel)
                    .add(menubarStyleList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(30, 30, 30)
                .add(showConfirmDialogOnExitOption)
                .add(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void showSplashScreenOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showSplashScreenOptionActionPerformed
        configModel.setShowSplash(showSplashScreenOption.isSelected());
    }//GEN-LAST:event_showSplashScreenOptionActionPerformed

    private void showConfirmDialogOnExitOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showConfirmDialogOnExitOptionActionPerformed
        configModel.setShowConfirmDialogOnExit(showSplashScreenOption.isSelected());
    }//GEN-LAST:event_showConfirmDialogOnExitOptionActionPerformed

    private void themeListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_themeListItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            configModel.setDefaultTheme(themeList.getSelectedItem().toString());
        }
    }//GEN-LAST:event_themeListItemStateChanged

    private void iconSetListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_iconSetListItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            configModel.setDefaultIconSet(iconSetList.getSelectedItem().toString());
        }
    }//GEN-LAST:event_iconSetListItemStateChanged

    private void toolbarSettingsListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_toolbarSettingsListItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
           configModel.setToolBarStyle(toolbarSettingsList.getSelectedItem().toString());
        }
    }//GEN-LAST:event_toolbarSettingsListItemStateChanged

    private void menubarStyleListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_menubarStyleListItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            configModel.setMenuBarStyle(menubarStyleList.getSelectedItem().toString());
        }
    }//GEN-LAST:event_menubarStyleListItemStateChanged

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel defaultIconSetLabel;
    private javax.swing.JLabel defaultThemeLabel;
    private javax.swing.JComboBox iconSetList;
    private javax.swing.JLabel menubarIconsLabel;
    private javax.swing.JComboBox menubarStyleList;
    private javax.swing.JCheckBox showConfirmDialogOnExitOption;
    private javax.swing.JCheckBox showSplashScreenOption;
    private javax.swing.JComboBox themeList;
    private javax.swing.JLabel toolbarIconsLabel;
    private javax.swing.JComboBox toolbarSettingsList;
    // End of variables declaration//GEN-END:variables
    public String getTitle() {
        return "General";
    }

    public String getId() {
        return getClass().getName();
    }

    public String getPluginCategory() {
        return "";
    }

    public void saveSettings() {
        PropertiesConfigurationLoader.save(config, props);
        Iterator m_iterator = props.keySet().iterator();
        while (m_iterator.hasNext()) {
            Object m_key = m_iterator.next();
            Object m_value = props.get(m_key);
            XPontusConfig.put(m_key, m_value);
        }
    }

    public void loadSettings() {
        props = PropertiesConfigurationLoader.load(config);

        configModel.setDefaultIconSet(XPontusConfig.getValue("xpontus.defaultIconSet").toString());
        configModel.setDefaultTheme(XPontusConfig.getValue("xpontus.defaultTheme").toString());
        configModel.setMenuBarStyle(XPontusConfig.getValue("xpontus.MenuBarLookAndFeel").toString());
        configModel.setShowConfirmDialogOnExit(Boolean.valueOf(XPontusConfig.getValue("xpontus.showConfirmDialogOnExit").toString()));
        configModel.setShowSplash(Boolean.valueOf(XPontusConfig.getValue("xpontus.showSplashScreenOnStartup").toString()));
        configModel.setToolBarStyle(XPontusConfig.getValue("xpontus.ToolbarIcons").toString());

        this.showSplashScreenOption.setSelected(configModel.isShowSplash());
        this.showConfirmDialogOnExitOption.setSelected(configModel.isShowConfirmDialogOnExit());
        this.themeList.setSelectedItem(configModel.getDefaultTheme());
        this.iconSetList.setSelectedItem(configModel.getDefaultIconSet());
        this.toolbarSettingsList.setSelectedItem(configModel.getToolBarStyle());
        this.menubarStyleList.setSelectedItem(configModel.getMenuBarStyle());


        configModel.addPropertyChangeListener("defaultIconSet",
                new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent pce) {
                        props.put("xpontus.defaultIconSet", configModel.getDefaultIconSet());

                    }
                });

        configModel.addPropertyChangeListener("showSplash",
                new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent pce) {
                        props.put("xpontus.showSplashScreenOnStartup", Boolean.valueOf(configModel.isShowSplash()).toString());

                    }
                });

        configModel.addPropertyChangeListener("showConfirmDialogOnExit",
                new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent pce) {
                        props.put("xpontus.showConfirmDialogOnExit", Boolean.valueOf(configModel.isShowConfirmDialogOnExit()).toString());

                    }
                });

        configModel.addPropertyChangeListener("defaultTheme",
                new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent pce) {
                        props.put("xpontus.defaultTheme", configModel.getDefaultTheme());

                    }
                });

        configModel.addPropertyChangeListener("toolBarStyle",
                new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent pce) {
                        props.put("xpontus.ToolbarIcons", configModel.getToolBarStyle());

                    }
                });


        configModel.addPropertyChangeListener("menuBarStyle",
                new PropertyChangeListener() {

                    public void propertyChange(PropertyChangeEvent pce) {
                        props.put("xpontus.MenuBarLookAndFeel", configModel.getMenuBarStyle());

                    }
                });

    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        return this;
    }
    private final File config = XPontusConfigurationConstantsIF.GENERAL_PREFERENCES_FILE;
    private Properties props;
    private GeneralOptionModel configModel = new GeneralOptionModel();

    public Component getJComponent() {
        return this;
    }
}
