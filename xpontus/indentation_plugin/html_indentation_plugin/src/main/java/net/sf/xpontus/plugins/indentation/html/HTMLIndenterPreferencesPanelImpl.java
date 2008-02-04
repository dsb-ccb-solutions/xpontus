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

import net.sf.xpontus.modules.gui.components.preferences.IPreferencesPanel;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import java.nio.charset.Charset;

import java.util.Iterator;

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
    private IPreferencesPanel panel;

    public HTMLIndenterPreferencesPanelImpl() {
        panel = new HTMLIndenterPrefComponent();
    }

    public String getPluginCategory() {
        return "Indentation";
    }

    public IPreferencesPanel getComponent() {
        return panel;
    }

    public void saveSettings() {
    }

    public void loadSettings() {
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

            Iterator it = Charset.availableCharsets().keySet().iterator();

            while (it.hasNext()) {
                encodingModel.addElement(it.next());
            }

            elementsOptionList = new JComboBox(new String[] {
                        "upper", "lower", "match"
                    });
            attributesOptionList = new JComboBox(new String[] {
                        "upper", "lower", "no-change"
                    });
            fixWindowsEntitiesRef = new JCheckBox("Fix Windows entities", true);
            normalizeAttributesOption = new JCheckBox("Normalize attributes",
                    true);
            elementsOptionLabel = new JLabel("Elements");
            attributesOptionLabel = new JLabel("Attributes");
            encodingLabel = new JLabel("Default encoding");
            encodingList = new JComboBox(encodingModel);

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
        }

        public String getTitle() {
            return "HTML indentation";
        }

        public Component getComponent() {
            return this;
        }

        public String getId() {
            return getClass().getName();
        }
    }
}
