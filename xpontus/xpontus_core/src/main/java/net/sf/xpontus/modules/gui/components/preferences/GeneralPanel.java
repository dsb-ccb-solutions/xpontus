/*
 * GeneralPanel.java
 *
 * Created on 27 janvier 2008, 18:31
 */
package net.sf.xpontus.modules.gui.components.preferences;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.io.File;
import java.util.Properties;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;

/**
 *
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class GeneralPanel extends javax.swing.JPanel implements IPreferencesPanel, PreferencesPluginIF {

    /** Creates new form GeneralPanel */
    public GeneralPanel() {
        initComponents();
    }

    @Override
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

        toolbarIconsLabel.setText("  Toolbar icons");

        toolbarSettingsList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Text and icons" }));
        toolbarSettingsList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                toolbarSettingsListItemStateChanged(evt);
            }
        });

        menubarIconsLabel.setText("  Menubar icons");

        menubarStyleList.setModel(new javax.swing.DefaultComboBoxModel(new String[] { "Text and icons" }));
        menubarStyleList.addItemListener(new java.awt.event.ItemListener() {
            public void itemStateChanged(java.awt.event.ItemEvent evt) {
                menubarStyleListItemStateChanged(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(showConfirmDialogOnExitOption)
                    .addComponent(menubarIconsLabel)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(toolbarIconsLabel)
                        .addComponent(showSplashScreenOption, javax.swing.GroupLayout.PREFERRED_SIZE, 319, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(defaultThemeLabel)
                                .addComponent(defaultIconSetLabel))
                            .addGap(54, 54, 54)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(iconSetList, javax.swing.GroupLayout.Alignment.TRAILING, 0, 184, Short.MAX_VALUE)
                                .addComponent(toolbarSettingsList, javax.swing.GroupLayout.Alignment.TRAILING, 0, 184, Short.MAX_VALUE)
                                .addComponent(themeList, 0, 184, Short.MAX_VALUE)
                                .addComponent(menubarStyleList, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addContainerGap(49, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addComponent(showSplashScreenOption)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(defaultThemeLabel)
                    .addComponent(themeList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(26, 26, 26)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(defaultIconSetLabel)
                    .addComponent(iconSetList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(toolbarIconsLabel)
                    .addComponent(toolbarSettingsList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(37, 37, 37)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(menubarIconsLabel)
                    .addComponent(menubarStyleList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addComponent(showConfirmDialogOnExitOption)
                .addGap(18, 18, 18))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void showSplashScreenOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showSplashScreenOptionActionPerformed
        props.setProperty("showSplashScreenOnStartup", Boolean.valueOf(showSplashScreenOption.isEnabled()).toString());
    }//GEN-LAST:event_showSplashScreenOptionActionPerformed

    private void showConfirmDialogOnExitOptionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_showConfirmDialogOnExitOptionActionPerformed
        props.setProperty("showConfirmDialogOnExit", Boolean.valueOf(showSplashScreenOption.isEnabled()).toString());
    }//GEN-LAST:event_showConfirmDialogOnExitOptionActionPerformed

    private void themeListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_themeListItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            props.setProperty("defaultTheme", themeList.getSelectedItem().toString());
        }
    }//GEN-LAST:event_themeListItemStateChanged

    private void iconSetListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_iconSetListItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            props.setProperty("defaultIconSet", iconSetList.getSelectedItem().toString());
        }
    }//GEN-LAST:event_iconSetListItemStateChanged

    private void toolbarSettingsListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_toolbarSettingsListItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            props.setProperty("defaultIconSet", iconSetList.getSelectedItem().toString());
        }
    }//GEN-LAST:event_toolbarSettingsListItemStateChanged

    private void menubarStyleListItemStateChanged(java.awt.event.ItemEvent evt) {//GEN-FIRST:event_menubarStyleListItemStateChanged
        if (evt.getStateChange() == ItemEvent.SELECTED) {
            props.setProperty("MenuBarLookAndFeel", iconSetList.getSelectedItem().toString());
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
    }

    public void loadSettings() {
        if(true){
            return;
        }
        // load the properties file
        props = PropertiesConfigurationLoader.load(config);
        
        this.showSplashScreenOnStartup = Boolean.valueOf(props.get("showSplashScreenOnStartup").toString());
        this.showTipsOnStartup = Boolean.valueOf(props.get("showTipsOnStartup").toString());
        this.showConfirmDialogOnExit = Boolean.valueOf(props.get("showConfirmDialogOnExit").toString());
        this.defaultTheme = props.get("defaultTheme").toString();
        this.defaultIconSet = props.get("defaultIconSet").toString();
        this.toolbarConfig = props.get("ToolbarIcons").toString();
        this.menubarConfig = props.get("MenuBarLookAndFeel").toString();
        
        // update controls
        this.showSplashScreenOption.setSelected(showTipsOnStartup);
        this.showConfirmDialogOnExitOption.setSelected(showConfirmDialogOnExit);
        this.themeList.setSelectedItem(defaultTheme);
        this.iconSetList.setSelectedItem(defaultIconSet);
        this.toolbarSettingsList.setSelectedItem(toolbarConfig);
        this.menubarStyleList.setSelectedItem(menubarConfig);
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        return this;
    }
    
    private final File config = XPontusConfigurationConstantsIF.GENERAL_PREFERENCES_FILE;
    private Properties props;
    private String menubarConfig;
    private String toolbarConfig;
    private String defaultIconSet;
    private String defaultTheme;
    private boolean showConfirmDialogOnExit;
    private boolean showTipsOnStartup;
    private boolean showSplashScreenOnStartup;

    public Component getJComponent() {
        return this;
    }
}
