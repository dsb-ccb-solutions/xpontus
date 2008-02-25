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
package net.sf.xpontus.plugins.lexer.html;

import net.sf.xpontus.configuration.XPontusConfig;
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

import java.io.File;
import java.util.Properties;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.table.TableModel;
import net.sf.xpontus.utils.ColorUtils;
import net.sf.xpontus.utils.PropertiesConfigurationLoader;


/**
 *
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class HtmlLexerPreferences implements PreferencesPluginIF {
    private IPreferencesPanel panel;
    private Color[] colors = new Color[6];
private JTable table;
    public HtmlLexerPreferences() {
    }

    public String getPluginCategory() {
        return "Lexer";
    }

    public IPreferencesPanel getPreferencesPanelComponent() {
        if (panel == null) {
            panel = new HtmlLexerPreferencesPanel();
        }

        return panel;
    }

    public void saveSettings() {
        String[] props = {
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.STRING_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.ATTRIBUTE_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.COMMENT_PROPERTY,
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.TAGS_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.DECLARATION_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.ATTRIBUTES_PROPERTY
            };
        Properties m_props = new Properties();
        TableModel m_model = table.getModel();
        for(int i=0;i<m_model.getRowCount();i++){
            Color c = (Color) m_model.getValueAt(i, 1);
            m_props.put(props[i], ColorUtils.colorToString(c));
            XPontusConfig.put(props[i], c);
        }
        File propertiesFile = HTMLLexerPlugin.configfile;
        PropertiesConfigurationLoader.save(propertiesFile, m_props);
        
    }

    public void loadSettings() {
        String[] props = {
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.STRING_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.ATTRIBUTE_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.COMMENT_PROPERTY,
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.TAGS_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.DECLARATION_PROPERTY,
                
                HMLLexerPreferencesConstantsIF.class.getName() + "$" +
                HMLLexerPreferencesConstantsIF.ATTRIBUTES_PROPERTY
            };

        for (int i = 0; i < props.length; i++) {
            colors[i] = (Color) XPontusConfig.getValue(props[i]);
        }
    }

    public class HtmlLexerPreferencesPanel extends JComponent
        implements IPreferencesPanel {
        
        private JScrollPane sp;
        private final String[] TABLE_COLUMNS;
        private final Object[][] TABLE_DATA;

        public HtmlLexerPreferencesPanel() {
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
                    { "STRING", colors[0] },
                    { "ATTRIBUTE", colors[1] },
                    { "COMMENT", colors[2] },
                    { "TAGS", colors[3] },
                    { "DECLARATION", colors[4] },
                    { "ATTRIBUTES", colors[5] }
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
            return "HTML syntax highlighting";
        }

        public Component getJComponent() {
            return this;
        }

        public String getId() {
            return getClass().getName();
        }
    }
}
