/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.modules.gui.components.preferences;

import com.jidesoft.dialog.AbstractDialogPage;
import com.jidesoft.dialog.BannerPanel;

import java.awt.BorderLayout;

import javax.swing.Icon;


/**
 *
 * @author yvzou
 */
public class GeneralPanelDialog extends AbstractDialogPage {
    public GeneralPanelDialog(String name, Icon icon) {
        super(name, icon);
    }

    public void lazyInitialize() {
        setLayout(new BorderLayout());

        BannerPanel bannerPanel = new BannerPanel(getName());
        bannerPanel.setBackground(getBackground().brighter());
        add(bannerPanel, BorderLayout.NORTH);
        add(new GeneralPanel(), BorderLayout.CENTER);
    }
}
