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
package net.sf.xpontus.plugins.preview;

import com.ibm.icu.text.CharsetDetector;

import net.sf.xpontus.plugins.scenarios.DetachableScenarioModel;
import net.sf.xpontus.utils.XPontusComponentsUtils;

import org.apache.commons.vfs.FileObject;
import org.apache.commons.vfs.VFS;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.BufferedInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


/**
 * Default transformation previewer
 * @author Yves Zoundi <yveszoundi at users dot sf dot net>
 */
public class PreviewTextImpl implements PreviewPluginIF {
    public String getMimeType() {
        return "text/plain";
    }

    public String getName() {
        return "Plain text previewer";
    }

    public void preview(DetachableScenarioModel model) {
        Frame m_frame = (Frame) XPontusComponentsUtils.getTopComponent()
                                                      .getDisplayComponent();

        final JDialog dialog = new JDialog(m_frame, true);
        dialog.setTitle("Previewing transformation result");

        JTextArea infoLabel = new JTextArea(model.getAlias());
        infoLabel.setWrapStyleWord(true);
        infoLabel.setLineWrap(true);
        infoLabel.setEditable(false);

        JComponent c = new JPanel();
        Dimension dim = new Dimension(400, 350);
        c.setPreferredSize(dim);
        c.setMinimumSize(dim);
        c.setLayout(new BorderLayout());

        JTextArea jtc = new JTextArea();
        jtc.setEditable(false);
        jtc.setLineWrap(true);
        jtc.setWrapStyleWord(true);

        try {
            FileObject fo = VFS.getManager().resolveFile(model.getOutput());

            if (!fo.exists()) {
                throw new FileNotFoundException(model.getOutput() +
                    " does not exists!!");
            }

            InputStream is = fo.getContent().getInputStream();
            InputStream bis = new BufferedInputStream(is);
            CharsetDetector cdt = new CharsetDetector();
            cdt.setText(bis);
            jtc.read(cdt.detect().getReader(), null);
        } catch (Exception e) {
            jtc.setText(e.getLocalizedMessage());
        }

        JScrollPane scrollPane = new JScrollPane(jtc);

        JComponent buttonsPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent arg0) {
                    dialog.setVisible(false);
                }
            });

        buttonsPanel.add(closeButton);

        c.add(infoLabel, BorderLayout.NORTH);
        c.add(scrollPane, BorderLayout.CENTER);
        c.add(buttonsPanel, BorderLayout.SOUTH);

        c.setOpaque(true);

        dialog.setContentPane(c);

        dialog.pack();

        dialog.setLocationRelativeTo(m_frame);

        dialog.setVisible(true);
    }
}
