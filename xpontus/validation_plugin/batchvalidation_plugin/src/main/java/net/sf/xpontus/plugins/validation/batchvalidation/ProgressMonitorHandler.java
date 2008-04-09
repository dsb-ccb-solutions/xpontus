/*
 * ProgressMonitorHandler.java
 *
 * Created on 2007-07-26, 14:12:27
 *
 *
 * Copyright (C) 2005-2007 Yves Zoundi
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

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JProgressBar;

import net.sf.xpontus.modules.gui.components.ImageButton;


/**
 *
 * @author mrcheeks
 */
public class ProgressMonitorHandler extends JComponent {
    private JProgressBar progressBar;
    private JButton cancelButton;
    private boolean canceled = false;
    private int min;
    private int max;
    private int value;

    public ProgressMonitorHandler(int min, int max) {
        setLayout(new GridBagLayout());

        this.min = min;
        this.max = max;

        progressBar = new JProgressBar(min, max);
        progressBar.setBorder(BorderFactory.createEmptyBorder());

        Dimension m_dimension = new Dimension(20, 5);
        progressBar.setMinimumSize(m_dimension);
        progressBar.setPreferredSize(m_dimension);

        progressBar.setBorderPainted(true);
        progressBar.setStringPainted(true);
        progressBar.setString("");

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

        gbc = new GridBagConstraints(1, 0, 1, 2, 0.8, 1,
                GridBagConstraints.NORTHWEST, GridBagConstraints.HORIZONTAL,
                new Insets(2, 2, 2, 2), 0, 0);

        add(progressBar, gbc);
    }

    public void updateProgress(final int value2) {
        Thread worker = new Thread() {
                public void run() {
                    value = value2;
                    progressBar.setValue(value2);
                }
            };

        worker.setPriority(Thread.MIN_PRIORITY);
        worker.start();
    }

    public boolean isCanceled() {
        return canceled;
    }
}
