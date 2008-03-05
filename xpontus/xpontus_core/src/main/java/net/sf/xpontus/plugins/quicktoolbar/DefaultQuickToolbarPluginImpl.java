/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.quicktoolbar;

import net.sf.xpontus.actions.impl.InsertCDataActionImpl;
import net.sf.xpontus.actions.impl.XMLCommentActionImpl;
import net.sf.xpontus.modules.gui.components.ImageButton;
import net.sf.xpontus.plugins.ioc.IOCPlugin;
import net.sf.xpontus.properties.PropertiesHolder;

import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.Action;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JToolBar;


/**
 *
 * @author Propriétaire
 */
public class DefaultQuickToolbarPluginImpl implements QuickToolBarPluginIF {
    private JComponent panel;

    public String getName() {
        return "Default quicktoolbar";
    }

    public String getDescription() {
        return "Default quicktoolbar";
    }

    public String getMimeType() {
        return "text/xml";
    }

    public Component getComponent() {
        if (panel == null) {
            panel = new JPanel(new FlowLayout(FlowLayout.LEFT));

            IOCPlugin pc = (IOCPlugin) PropertiesHolder.getPropertyValue(IOCPlugin.PLUGIN_IDENTIFIER);

            Action m_action = (Action) pc.getBean(XMLCommentActionImpl.BEAN_ALIAS);
            JButton m_button = new JButton(m_action);
            m_button.setBackground(panel.getBackground());
            m_button.setText(null);
            panel.add(m_button);

            m_action = (Action) pc.getBean(InsertCDataActionImpl.BEAN_ALIAS);
            m_button = new JButton(m_action);
            m_button.setText(null);
            m_button.setBackground(panel.getBackground());
            panel.add(m_button);
        }

        return panel;
    }

    public String getFileContextMode() {
        return "xml";
    }
}
