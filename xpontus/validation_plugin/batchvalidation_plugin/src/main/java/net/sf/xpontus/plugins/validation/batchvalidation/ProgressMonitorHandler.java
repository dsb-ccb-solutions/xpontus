/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net.sf.xpontus.plugins.validation.batchvalidation;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.net.URL;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;
import net.sf.xpontus.modules.gui.components.ImageButton;


/**
 *
 * @author mrcheeks
 */
public class ProgressMonitorHandler extends JComponent {
    private JProgressBar progressBar;
    private JButton cancelButton;
    private boolean canceled = false;
    private int min, max, value;

    public ProgressMonitorHandler(int min, int max) {
        setLayout(new GridBagLayout());
        
        

        this.min = min;
        this.max= max;
        Dimension dim = new Dimension(70, 20);
        progressBar = new JProgressBar(min, max);
        progressBar.setMinimumSize(dim);
        progressBar.setPreferredSize(dim);

        URL iconURL = getClass().getResource("close-button.png");
        Icon sIcon = new ImageIcon(iconURL);
        cancelButton = new ImageButton(sIcon);
        cancelButton.setToolTipText("Stop batch validation");
        
        cancelButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent evt) {
                    canceled = true;

                    final Container c = getParent();
                    c.remove(ProgressMonitorHandler.this);
                    c.invalidate();
                    c.validate();
                    c.repaint();
                }
            });

              GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 0, 1,
                    GridBagConstraints.NORTHWEST, GridBagConstraints.NONE,
                    new Insets(2, 2, 2, 2), 0, 0);

            add(cancelButton, gbc);

            gbc = new GridBagConstraints(1, 0, 1, 2, 0.5, 1,
                    GridBagConstraints.NORTHWEST,
                    GridBagConstraints.HORIZONTAL, new Insets(2, 2, 2, 2), 0, 0);

            add(progressBar, gbc);
    }

    public void updateProgress(final int value2) {
        SwingUtilities.invokeLater(new Runnable() {
                public void run() {
                    value = value2;
                    progressBar.setValue(value2);
                }
            });
    }

    public boolean isCanceled() {
        return canceled;
    }
}
