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
package net.sf.xpontus.plugins.lexer.xml;

import net.sf.xpontus.modules.gui.components.preferences.ColorTableEditor;
import net.sf.xpontus.modules.gui.components.preferences.ColorTableModel;
import net.sf.xpontus.modules.gui.components.preferences.ColorTableRenderer;
import net.sf.xpontus.modules.gui.components.preferences.IPreferencesPanel;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;


/**
 * Settings for the XML lexer
 * @version 0.0.1
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class XMLLexerPreferences implements PreferencesPluginIF {
    private IPreferencesPanel panel;

    public XMLLexerPreferences() {
        panel = new XMLLexerPreferencesPanel();
    }

    public String getPluginCategory() {
        return "Lexer";
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        return panel;
    }

    public void saveSettings() {
    }

    public void loadSettings() {
    }

    public class XMLLexerPreferencesPanel extends JComponent
        implements IPreferencesPanel {
        private JTable table;
        private JScrollPane sp;
        private final String[] TABLE_COLUMNS;
        private final Object[][] TABLE_DATA;

        public XMLLexerPreferencesPanel() {
            setLayout(new BorderLayout());

            JLabel top = new JLabel(getTitle());
            top.setBorder(BorderFactory.createEmptyBorder(4, 4, 4, 4));
            top.setFont(top.getFont().deriveFont(Font.BOLD));
            top.setOpaque(true);
            top.setBackground(Color.white);

            add("North", top);

            JPanel p = new JPanel(new GridLayout());

            TABLE_COLUMNS = new String[] { "TokenID", "Color" };
            TABLE_DATA = new Object[][] {
                    { "STRING", Color.RED },
                    { "XML DECLARATIONS", Color.MAGENTA },
                    { "COMMENT", new Color(0, 100, 0) },
                    { "TAGS", Color.BLUE },
                    { "ATTRIBUTES", new Color(127, 0, 85) }
                };
            table = new JTable(new ColorTableModel(TABLE_DATA, TABLE_COLUMNS));

            //Set up renderer and editor for the Favorite Color column.
            table.setDefaultRenderer(Color.class, new ColorTableRenderer(true));
            table.setDefaultEditor(Color.class, new ColorTableEditor());

            //Add the scroll pane to this panel.
            sp = new JScrollPane(table);
            p.add(sp);

            add(p, BorderLayout.CENTER);
        }

        public String getTitle() {
            return "XML syntax highlighting";
        }

        public Component getJComponent() {
            return this;
        }

        public String getId() {
            return getClass().getName();
        }
    }
}
