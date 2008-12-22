/*
 * EditorPanel.java
 *
 * Created on 28 January 2008, 04:55 
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
import java.awt.Font;
import java.io.File;
import java.nio.charset.Charset;
import java.util.Properties;

import javax.swing.DefaultComboBoxModel;
import javax.swing.SpinnerNumberModel;

import net.sf.xpontus.configuration.XPontusConfig;
import net.sf.xpontus.constants.XPontusConfigurationConstantsIF;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;
import net.sf.xpontus.utils.GUIUtils;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import com.l2fprod.common.swing.JFontChooser;

/**
 *
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class EditorPanel extends javax.swing.JPanel implements IPreferencesPanel, PreferencesPluginIF {
 
	private static final long serialVersionUID = 3030121601328024083L;
	/**
     * 
     * @return
     */
     public String toString() {
        return getTitle();
    }

    /** Creates new form EditorPanel */
    public EditorPanel() {
        encodingModel = new DefaultComboBoxModel(); 
        for (String charSet : Charset.availableCharsets().keySet()) {
            encodingModel.addElement(charSet);
        }
        initComponents();
    }

    public String getTitle() {
        return "Editor";
    }

    public Component getJComponent() {
        return this;
    }

    public String getId() {
        return getClass().getName();
    }

    public String getPluginCategory() {
       return "";
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        return this;
    }

    public void saveSettings() {
        properties.put("displayLineNumbers", Boolean.valueOf(displayLineNumbersOption.isSelected()) + "");
        XPontusConfig.put("displayLineNumbers", Boolean.valueOf(displayLineNumbersOption.isSelected()).toString());
        
        properties.put("EditorPane.Font", GUIUtils.fontToString(fontValueLabel.getFont()));
        XPontusConfig.put("EditorPane.Font", fontValueLabel.getFont()); 
        
        properties.put("editor.tabsize", tabSizeTF.getValue().toString());
        XPontusConfig.put("editor.tabsize", properties.getProperty("editor.tabsize")); 
        
        properties.put("cursorBlinkRate", cursorBlinkRateValues.getValue().toString());
        XPontusConfig.put("cursorBlinkRate", properties.getProperty("cursorBlinkRate"));
        
        properties.put("DefaultXMLEncoding", encodingList.getSelectedItem().toString());
        XPontusConfig.put("DefaultXMLEncoding", properties.getProperty("DefaultXMLEncoding")); 
        
        PropertiesConfigurationLoader.save(propertiesFile, properties);
    }

    public void loadSettings() {
       
        //load the properties
        properties = PropertiesConfigurationLoader.load(propertiesFile); 
        
        System.out.println("Editor props:" + properties.size());
        String[] f = properties.getProperty("EditorPane.Font").split(",");
        String family = f[0].trim();
        String style1 = f[1].trim();
        int style = Integer.parseInt(style1);
         
        int size = Integer.parseInt(f[2].trim());
        
        this.displayLineNumbers = Boolean.valueOf(properties.getProperty("displayLineNumbers"));
        this.editorFont = new Font(family, style, size);
        this.cursorBlinkRate = Integer.parseInt(properties.getProperty("cursorBlinkRate"));
        this.defaultXMLEncoding = properties.getProperty("DefaultXMLEncoding").toString();
        this.tabsize = Integer.valueOf(properties.getProperty("editor.tabsize"));
        
        this.displayLineNumbersOption.setSelected(displayLineNumbers);
        this.fontValueLabel.setText(getStringFont(editorFont) );
        this.fontValueLabel.setFont(editorFont);
        this.encodingList.setSelectedItem(defaultXMLEncoding);
        this.cursorBlinkRateValues.setValue(Integer.valueOf(cursorBlinkRate));        
        this.tabSizeTF.setValue(tabsize);
    }
    
    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        displayLineNumbersOption = new javax.swing.JCheckBox();
        chooseFontButton = new javax.swing.JButton();
        fontValueLabel = new javax.swing.JLabel();
        tabSizeLabel = new javax.swing.JLabel();
        tabSizeTF = new javax.swing.JFormattedTextField();
        cursorBlinkRateLabel = new javax.swing.JLabel();
        cursorBlinkRateValues = new javax.swing.JSpinner();
        encodingLabel = new javax.swing.JLabel();
        encodingList = new javax.swing.JComboBox();

        displayLineNumbersOption.setSelected(true);
        displayLineNumbersOption.setText("Display line numbers");

        chooseFontButton.setText("Font...");
        chooseFontButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chooseFontButtonActionPerformed(evt);
            }
        });

        fontValueLabel.setFont((Font)XPontusConfig.getValue("EditorPane.Font"));
        fontValueLabel.setText(getStringFont((Font)XPontusConfig.getValue("EditorPane.Font")));

        tabSizeLabel.setText("Tab size");

        tabSizeTF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(new javax.swing.text.NumberFormatter(new java.text.DecimalFormat("#"))));
        tabSizeTF.setValue(new Integer(4));

        cursorBlinkRateLabel.setText("Cursor blink rate");

        cursorBlinkRateValues.setModel(model);

        encodingLabel.setText("Default XML encoding");

        encodingList.setModel(encodingModel);

        org.jdesktop.layout.GroupLayout layout = new org.jdesktop.layout.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                    .add(layout.createSequentialGroup()
                        .add(22, 22, 22)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(chooseFontButton)
                            .add(tabSizeLabel)
                            .add(cursorBlinkRateLabel)
                            .add(encodingLabel))
                        .add(88, 88, 88)
                        .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                            .add(encodingList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 98, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                            .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
                                .add(layout.createSequentialGroup()
                                    .add(fontValueLabel, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 141, Short.MAX_VALUE)
                                    .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED))
                                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING, false)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, tabSizeTF)
                                    .add(org.jdesktop.layout.GroupLayout.LEADING, cursorBlinkRateValues, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)))))
                    .add(layout.createSequentialGroup()
                        .add(14, 14, 14)
                        .add(displayLineNumbersOption, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, 316, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)))
                .add(15, 15, 15))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(org.jdesktop.layout.GroupLayout.LEADING)
            .add(layout.createSequentialGroup()
                .addContainerGap()
                .add(displayLineNumbersOption)
                .add(28, 28, 28)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(layout.createSequentialGroup()
                        .add(cursorBlinkRateLabel)
                        .add(20, 20, 20))
                    .add(layout.createSequentialGroup()
                        .add(cursorBlinkRateValues, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE)
                        .add(18, 18, 18)))
                .addPreferredGap(org.jdesktop.layout.LayoutStyle.RELATED)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.TRAILING)
                    .add(tabSizeLabel)
                    .add(tabSizeTF, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .add(27, 27, 27)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(chooseFontButton)
                    .add(fontValueLabel))
                .add(25, 25, 25)
                .add(layout.createParallelGroup(org.jdesktop.layout.GroupLayout.BASELINE)
                    .add(encodingLabel)
                    .add(encodingList, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE, org.jdesktop.layout.GroupLayout.DEFAULT_SIZE, org.jdesktop.layout.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(28, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents
    private void chooseFontButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chooseFontButtonActionPerformed
        Component frame = XPontusComponentsUtils.getTopComponent().getDisplayComponent();
        Font selectedFont = (Font)XPontusConfig.getValue("EditorPane.Font");

        Font f = JFontChooser.showDialog(frame, "Select font", selectedFont);
        if (f != null) {
            fontValueLabel.setText(getStringFont(f));
            fontValueLabel.setFont(f);
            fontValueLabel.repaint();  
        }
}//GEN-LAST:event_chooseFontButtonActionPerformed

    public String getStringFont(Font f) {
        String style = "Bold Italic";

        switch (f.getStyle()) {
            case Font.ITALIC:
                style = "Italic";
                break;
            case Font.BOLD:
                style = "Bold";
                break;
            case 3:
                style = "Bold Italic";
                break;
            case Font.PLAIN:
                style = "Plain";
                break;
            default:
                style = "Plain";
                break;
        }

        return f.getFamily() + "," + style + "," + f.getSize();
    }
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton chooseFontButton;
    private javax.swing.JLabel cursorBlinkRateLabel;
    private javax.swing.JSpinner cursorBlinkRateValues;
    private javax.swing.JCheckBox displayLineNumbersOption;
    private javax.swing.JLabel encodingLabel;
    private javax.swing.JComboBox encodingList;
    private javax.swing.JLabel fontValueLabel;
    private javax.swing.JLabel tabSizeLabel;
    private javax.swing.JFormattedTextField tabSizeTF;
    // End of variables declaration//GEN-END:variables
    
    private Integer value = new Integer(700);
    private Integer min = new Integer(500);
    private Integer max = new Integer(1000);
    private Integer step = new Integer(100);
    private SpinnerNumberModel model = new SpinnerNumberModel(value, min, max, step);
    private DefaultComboBoxModel encodingModel;
 
    private File propertiesFile = XPontusConfigurationConstantsIF.EDITOR_PREFERENCES_FILE;
    private Properties properties;
    private int cursorBlinkRate;
    private boolean displayLineNumbers;
    private Font editorFont;
    private Integer tabsize;
    private String defaultXMLEncoding;
}
