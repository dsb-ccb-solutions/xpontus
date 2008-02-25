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
package net.sf.xpontus.plugins.indentation.html;

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
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import java.nio.charset.Charset;

import java.util.Iterator;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class HTMLIndenterPreferencesPanelImpl implements PreferencesPluginIF {
    private HTMLIndenterPrefComponent panel;
    private String elementsP = null;
    private String attributesP = null;
    private String normalizeAttributesOptionP = null;
    private String fixWindowsEntitiesP = null;
    private String encodingP = null;

    public HTMLIndenterPreferencesPanelImpl() {
    }

    public String getPluginCategory() {
        return "Indentation";
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        if (panel == null) {
            panel = new HTMLIndenterPrefComponent();
        }

        return panel;
    }

    public void saveSettings() {
        Properties props = new Properties();
        props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
            HTMLIndenterPluginConstantsIF.ATTRIBUTES_PROPERTY, attributesP);

        props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
            HTMLIndenterPluginConstantsIF.ELEMENTS_PROPERTY, elementsP);

        props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
            HTMLIndenterPluginConstantsIF.FIX_WINDOW_ENTITIES_PROPERTY,
            fixWindowsEntitiesP);

        props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
            HTMLIndenterPluginConstantsIF.NORMALIZE_ATTRIBUTES_PROPERTY,
            normalizeAttributesOptionP);

        props.put(HTMLIndenterPluginConstantsIF.class.getName() + "$" +
            HTMLIndenterPluginConstantsIF.ENCODING_PROPERTY, encodingP);

        Iterator it = props.keySet().iterator();

        while (it.hasNext()) {
            String m_key = it.next().toString();
            String m_value = props.getProperty(m_key);
            XPontusConfig.put(m_key, m_value);
        }

        PropertiesConfigurationLoader.save(HTMLIndentationPlugin.configfile,
            props);
    }

    public void loadSettings() {
        attributesP = XPontusConfig.getValue(HTMLIndenterPluginConstantsIF.class.getName() +
                "$" + HTMLIndenterPluginConstantsIF.ATTRIBUTES_PROPERTY)
                                   .toString();

        elementsP = XPontusConfig.getValue(HTMLIndenterPluginConstantsIF.class.getName() +
                "$" + HTMLIndenterPluginConstantsIF.ELEMENTS_PROPERTY).toString();

        fixWindowsEntitiesP = XPontusConfig.getValue(HTMLIndenterPluginConstantsIF.class.getName() +
                "$" +
                HTMLIndenterPluginConstantsIF.FIX_WINDOW_ENTITIES_PROPERTY)
                                           .toString();

        normalizeAttributesOptionP = XPontusConfig.getValue(HTMLIndenterPluginConstantsIF.class.getName() +
                "$" +
                HTMLIndenterPluginConstantsIF.NORMALIZE_ATTRIBUTES_PROPERTY)
                                                  .toString();

        encodingP = XPontusConfig.getValue(HTMLIndenterPluginConstantsIF.class.getName() +
                "$" + HTMLIndenterPluginConstantsIF.ENCODING_PROPERTY).toString();
    }

    public class HTMLIndenterPrefComponent extends JComponent
        implements IPreferencesPanel {
        private JLabel elementsOptionLabel;
        private JComboBox elementsOptionList;
        private JLabel attributesOptionLabel;
        private JComboBox attributesOptionList;
        private JCheckBox normalizeAttributesOption;
        private JCheckBox fixWindowsEntitiesRef;
        private JLabel encodingLabel;
        private DefaultComboBoxModel encodingModel;
        private JComboBox encodingList;

        public HTMLIndenterPrefComponent() {
            setLayout(new BorderLayout());

            JLabel top = new JLabel(getTitle());
            top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            top.setFont(top.getFont().deriveFont(Font.BOLD));
            top.setOpaque(true);
            top.setBackground(Color.white);

            add("North", top);

            JPanel p = new JPanel(new GridBagLayout());

            GridBagConstraints c = null;

            encodingModel = new DefaultComboBoxModel();
            encodingModel.addElement("AUTODETECT");

            Iterator it = Charset.availableCharsets().keySet().iterator();

            while (it.hasNext()) {
                encodingModel.addElement(it.next());
            }

            elementsOptionList = new JComboBox(new String[] {
                        "upper", "lower", "match"
                    });

            elementsOptionList.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent arg0) {
                        if (arg0.getStateChange() == ItemEvent.SELECTED) {
                            elementsP = elementsOptionList.getSelectedItem()
                                                          .toString();
                        }
                    }
                });
            attributesOptionList = new JComboBox(new String[] {
                        "upper", "lower", "no-change"
                    });

            attributesOptionList.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent arg0) {
                        if (arg0.getStateChange() == ItemEvent.SELECTED) {
                            attributesP = attributesOptionList.getSelectedItem()
                                                              .toString();
                        }
                    }
                });
            fixWindowsEntitiesRef = new JCheckBox("Fix Windows entities", true);

            fixWindowsEntitiesRef.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        fixWindowsEntitiesP = Boolean.valueOf(fixWindowsEntitiesRef.isSelected())
                                                     .toString();
                    }
                });
            normalizeAttributesOption = new JCheckBox("Normalize attributes",
                    true);
            normalizeAttributesOption.addActionListener(new ActionListener() {
                    public void actionPerformed(ActionEvent arg0) {
                        normalizeAttributesOptionP = Boolean.valueOf(fixWindowsEntitiesRef.isSelected())
                                                            .toString();
                    }
                });

            elementsOptionLabel = new JLabel("Elements");
            attributesOptionLabel = new JLabel("Attributes");
            encodingLabel = new JLabel("Default encoding");
            encodingList = new JComboBox(encodingModel);

            encodingList.addItemListener(new ItemListener() {
                    public void itemStateChanged(ItemEvent arg0) {
                        if (arg0.getStateChange() == ItemEvent.SELECTED) {
                            encodingP = encodingList.getSelectedItem().toString();
                        }
                    }
                });

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 0;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(5, 5, 5, 5);

            p.add(elementsOptionLabel, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 0;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(5, 5, 5, 5);
            p.add(elementsOptionList, c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 1;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(5, 5, 5, 5);
            p.add(attributesOptionLabel, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 1;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(5, 5, 5, 5);
            p.add(attributesOptionList, c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 2;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(5, 5, 5, 5);
            p.add(encodingLabel, c);

            c = new GridBagConstraints();
            c.gridx = 1;
            c.gridy = 2;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(5, 5, 5, 5);
            p.add(encodingList, c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 3;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(5, 5, 5, 5);
            p.add(fixWindowsEntitiesRef, c);

            c = new GridBagConstraints();
            c.gridx = 0;
            c.gridy = 4;
            c.gridwidth = 2;
            c.fill = GridBagConstraints.BOTH;
            c.anchor = GridBagConstraints.NORTHWEST;
            c.insets = new Insets(5, 5, 5, 5);
            p.add(normalizeAttributesOption, c);

            add(p, BorderLayout.CENTER);

            refreshProperties();
        }

        public void refreshProperties() {
            normalizeAttributesOption.setSelected(Boolean.valueOf(
                    normalizeAttributesOptionP).booleanValue());
            elementsOptionList.setSelectedItem(elementsP);
            attributesOptionList.setSelectedItem(attributesP);
            encodingList.setSelectedItem(encodingP);
            fixWindowsEntitiesRef.setSelected(Boolean.valueOf(
                    fixWindowsEntitiesP).booleanValue());
        }

        public String getTitle() {
            return "HTML indentation";
        }

        public Component getJComponent() {
            return this;
        }

        public String getId() {
            return getClass().getName();
        }
    }
}
