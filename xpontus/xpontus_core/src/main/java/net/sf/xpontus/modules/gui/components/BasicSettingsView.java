/*
 * BasicSettingsView.java
 *
 * Created on 30 janvier 2008, 17:51
 */
package net.sf.xpontus.modules.gui.components;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToggleButton;
import javax.swing.UIManager;
import net.sf.xpontus.modules.gui.components.preferences.EditorPanel;
import net.sf.xpontus.modules.gui.components.preferences.GeneralPanel;
import net.sf.xpontus.plugins.preferences.PreferencesPluginIF;
import net.sf.xpontus.utils.XPontusComponentsUtils;
import com.jidesoft.dialog.*;
import com.jidesoft.plaf.LookAndFeelFactory;
import com.jidesoft.plaf.UIDefaultsLookup;
import com.jidesoft.swing.JideSwingUtilities;
import com.jidesoft.swing.MultilineLabel;
import com.jidesoft.swing.PartialLineBorder;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.border.CompoundBorder;
import java.awt.*;
import java.awt.event.*;

/**
 *
 * @author  Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class BasicSettingsView extends MultiplePageDialog {

    private Component currentComponent;
    private JPanel bar = new JPanel();
    private PreferencesPluginIF[] panels;
    private JDialog advancedSettingsDialog;
    private Component nullComponent;

    /** 
     * Creates new form BasicSettingsView
     * @param parent
     * @param modal 
     */
    public BasicSettingsView(java.awt.Frame parent, boolean modal) {
        super(parent, modal);

        setTitle("Basic settings");
        setStyle(MultiplePageDialog.ICON_STYLE);
        PageList model = new PageList();

        String icons[] = {"/net/sf/xpontus/icons/gear.gif", "/net/sf/xpontus/icons/accessories-text-editor.gif"};

        AbstractDialogPage panel1 = new GeneralPanel("General", new ImageIcon(getClass().getResource(icons[0])));
        AbstractDialogPage panel2 = new EditorPanel("Editor", new ImageIcon(getClass().getResource(icons[1])));

        model.append(panel1);
        model.append(panel2);

        setPageList(model);

        pack();


    }

    /**
     * 
     */
    public BasicSettingsView() {
        this((Frame) XPontusComponentsUtils.getTopComponent().getDisplayComponent(), true);
    }
}
