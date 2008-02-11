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

import java.awt.BorderLayout;

/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.nio.charset.Charset;

import java.util.Iterator;

import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.JPanel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SpringLayout;
import javax.swing.UIManager;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class EditorPanel extends AbstractDialogPage implements IPreferencesPanel,
    PreferencesPluginIF {
    private javax.swing.JButton chooseFontButton;
    private javax.swing.JLabel cursorBlinkRateLabel;
    private javax.swing.JSpinner cursorBlinkRateValues;
    private javax.swing.JCheckBox displayLineNumbersOption;
    private javax.swing.JLabel encodingLabel;
    private javax.swing.JComboBox encodingList;
    private javax.swing.JLabel fontValueLabel;
    private javax.swing.JLabel tabSizeLabel;
    private javax.swing.JFormattedTextField tabSizeTF;
    private Integer value = new Integer(700);
    private Integer min = new Integer(500);
    private Integer max = new Integer(1000);
    private Integer step = new Integer(100);
    private SpinnerNumberModel model = new SpinnerNumberModel(value, min, max,
            step);
    private DefaultComboBoxModel encodingModel;
    private int cursorBlinkRate;
    private boolean displayLineNumbers;
    private Font editorFont;
    private String defaultXMLEncoding;
    private JPanel centerPanel;

    public EditorPanel(String name, Icon icon) {
        super(name, icon);

        setLayout(new BorderLayout());

        final Dimension dimension = new Dimension(500, 400);

        setMinimumSize(dimension);
        setPreferredSize(dimension);

        encodingModel = new DefaultComboBoxModel();

        Iterator<String> it = Charset.availableCharsets().keySet().iterator();

        while (it.hasNext()) {
            encodingModel.addElement(it.next());
        }

        centerPanel = new JPanel(new SpringLayout());
 

        add(new BannerPanel(name), BorderLayout.NORTH);
        add(centerPanel, BorderLayout.CENTER);
    }

    public String getTitle() {
        return "Editor";
    }

    public Component getJComponent() {
        return this;
    }

    public String getId() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getPluginCategory() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        return this;
    }

    public void saveSettings() {
    }

    public void loadSettings() {
    }

    private void initComponents2() {
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

        fontValueLabel.setFont(UIManager.getFont("EditorPane.font"));

        tabSizeLabel.setText("Tab size");

        tabSizeTF.setFormatterFactory(new javax.swing.text.DefaultFormatterFactory(
                new javax.swing.text.NumberFormatter(
                    new java.text.DecimalFormat("#"))));
        tabSizeTF.setValue(new Integer(4));

        cursorBlinkRateLabel.setText("Cursor blink rate");

        cursorBlinkRateValues.setModel(model);

        encodingLabel.setText("Default XML encoding");

        encodingList.setModel(encodingModel);
    }

    @Override
    public void lazyInitialize() {
    }
}
