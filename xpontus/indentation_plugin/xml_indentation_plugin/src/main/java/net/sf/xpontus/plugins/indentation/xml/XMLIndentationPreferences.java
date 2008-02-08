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
package net.sf.xpontus.plugins.indentation.xml;

import net.sf.xpontus.modules.gui.components.preferences.IPreferencesPanel;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XMLIndentationPreferences implements PreferencesPluginIF {
    private IPreferencesPanel panel;

    public XMLIndentationPreferences() {
        panel = new XMLIndentationPreferencesPanel();
    }

    public String getPluginCategory() {
        return "Indentation";
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        return panel;
    }

    public void saveSettings() {
    }

    public void loadSettings() {
    }

    public class XMLIndentationPreferencesPanel extends JComponent
        implements IPreferencesPanel {
        private JCheckBox omitCommentsOption;
        private JCheckBox omitDocTypeOption;
        private JCheckBox omitXMLDeclarationOption;
        private JCheckBox preserveSpaceOption;

        public XMLIndentationPreferencesPanel() {
            setLayout(new BorderLayout());

            JLabel top = new JLabel(getTitle());
            top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            top.setFont(top.getFont().deriveFont(Font.BOLD));
            top.setOpaque(true);
            top.setBackground(Color.white);

            add("North", top);

            JPanel p = new JPanel(new GridLayout(4, 1, 4, 4));

            omitCommentsOption = new JCheckBox("Omit comments", true);
            omitDocTypeOption = new JCheckBox("Omit doctype", true);
            omitXMLDeclarationOption = new JCheckBox("Omit XML declaration",
                    true);
            preserveSpaceOption = new JCheckBox("Preserve space", true);

            p.add(omitCommentsOption);
            p.add(omitDocTypeOption);
            p.add(omitXMLDeclarationOption);
            p.add(preserveSpaceOption);

            add(p, BorderLayout.CENTER);
        }

        public String getTitle() {
            return "XML indentation";
        }

        public Component getJComponent() {
            return this;
        }

        public String getId() {
            return getClass().getName();
        }
    }
}
